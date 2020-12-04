package com.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frame.Biz;
import com.vo.CarSensorVO;
import com.vo.CarVO;

@Controller
public class CarController {
	
	@Resource(name="cbiz")
	Biz<Integer,String,CarVO> cbiz;
	@Resource(name="sbiz")
	Biz<Integer,String,CarSensorVO> sbiz;
	
	// 차량 데이터
	@RequestMapping("/cardata.mc")
	@ResponseBody
	public void cardata(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String userid = request.getParameter("userid");
		
		ArrayList<CarVO> dbcarlist = new ArrayList<>();
		
		dbcarlist = cbiz.getcarsfromuser(userid);
		
		JSONArray ja = new JSONArray();

		
		for (CarVO dbcar : dbcarlist) {
			JSONObject data = new JSONObject();
			data.put("carid", dbcar.getCarid());
			data.put("userid", dbcar.getUserid());
			data.put("carnum", dbcar.getCarnum());
			data.put("carname", dbcar.getCarname());
			data.put("cartype", dbcar.getCartype());
			data.put("carmodel", dbcar.getCarmodel());
			data.put("caryear", dbcar.getCaryear());
			data.put("carimg", dbcar.getCarimg());
			data.put("caroiltype", dbcar.getCaroiltype());
			data.put("tablettoken", dbcar.getTablettoken());
			ja.add(data);
		}
		
		

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();
		
	}
	
	// 차량센서 데이터
	@RequestMapping("/carsensordata.mc")
	@ResponseBody
	public void carsensordata(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String userid = request.getParameter("userid");
		
		ArrayList<CarSensorVO> dbcarSensorlist = new ArrayList<>();
		
		dbcarSensorlist = sbiz.getcarsfromuser(userid);
		
		JSONArray ja = new JSONArray();

		for (CarSensorVO dbcarSensor : dbcarSensorlist) {
			JSONObject data = new JSONObject();
			data.put("carid", dbcarSensor.getCarid());
			data.put("heartbeat", dbcarSensor.getHeartbeat());
			data.put("pirfront", dbcarSensor.getPirfront());
			data.put("pirrear", dbcarSensor.getPirrear());
			data.put("freight", dbcarSensor.getFreight());
			data.put("fuel", dbcarSensor.getFuel());
			data.put("fuelmax", dbcarSensor.getFuelmax());
			data.put("temper", dbcarSensor.getTemper());
			data.put("starting", dbcarSensor.getStarting());
			data.put("moving", dbcarSensor.getMoving());
			
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String movingstarttime = format.format(dbcarSensor.getMovingstarttime());
			data.put("movingstarttime", movingstarttime);
			
			data.put("aircon", dbcarSensor.getAircon());
			data.put("crash", dbcarSensor.getCrash());
			data.put("door", dbcarSensor.getDoor());
			data.put("lat", dbcarSensor.getLat());
			data.put("lng", dbcarSensor.getLng());
			ja.add(data);
		}
		
		System.out.println("---------test:"+ja.toJSONString());

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();
		
	}
	
	
	// 차량 제어
	@RequestMapping("/control.mc")
	@ResponseBody
	public void control(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carid = request.getParameter("carid");
		String type = request.getParameter("type");
		String control = request.getParameter("control");
		
		System.out.println(carid+" "+type+" "+control);
		
		
		// DB에 control 변경값 저장
		CarSensorVO dbcarsensor = null;
		
		try {
			dbcarsensor = sbiz.get(Integer.parseInt(carid));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		if(type.equals("starting")) {
			
			dbcarsensor.setStarting(control);
			
		}else if(type.equals("door")) {
			
			dbcarsensor.setDoor(control);
			
		}else if(type.equals("temper")) {
			
			dbcarsensor.setTemper(Integer.parseInt(control));
			
		}
		
	
		try {
			sbiz.modify(dbcarsensor);
			System.out.println("Modify OK..");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			
				
		
		// FCM으로 차량 제어
		URL url = null;
		try {
			url = new URL("https://fcm.googleapis.com/fcm/send");
		} catch (MalformedURLException e) {
			System.out.println("Error while creating Firebase URL | MalformedURLException");
			e.printStackTrace();
		}
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			System.out.println("Error while createing connection with Firebase URL | IOException");
			e.printStackTrace();
		}
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");

		// set my firebase server key
		conn.setRequestProperty("Authorization", "key="
				+ "AAAAK89FyMY:APA91bGxNwkQC6S_QQAKbn3COepWgndhyyjynT8ZvIEarTaGpEfMA1SPFo-ReN8b9uO21R1OfSOpNhfYbQaeohKP_sKzsgVTxu7K5tmzcjEfHzlgXRFrB1r0uqhfxLp4p836lbKw_iaN");

		// create notification message into JSON format
		JSONObject message = new JSONObject();
		message.put("to", "/topics/car");
		message.put("priority", "high");
		
		JSONObject notification = new JSONObject();
		notification.put("title", "차 제어");
		notification.put("body", "test:"+carid+" "+type+" "+control);
		message.put("notification", notification);
		
		JSONObject data = new JSONObject();
		data.put("carid",carid);
		data.put("type",type);
		data.put("control", control);
		message.put("data", data);


		try {
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			System.out.println("FCM 전송:"+message.toString());
			out.write(message.toString());
			out.flush();
			conn.getInputStream();
			System.out.println(" FCM OK...............");

		} catch (IOException e) {
			System.out.println("Error while writing outputstream to firebase sending to ManageApp | IOException");
			e.printStackTrace();
		}
		
		
	}
	
	

	
}
