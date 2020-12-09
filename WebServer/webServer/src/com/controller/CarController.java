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
import com.google.cloud.Date;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.vo.CarSensorVO;
import com.vo.CarVO;
import com.vo.UsersVO;

@Controller
public class CarController {
	
	// @Resource(name="ubiz")
	// Biz<String,String,UsersVO> ubiz;
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

//			이 부분은 나중에 다시 손보자
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//			String movingstarttime = format.format(dbcarSensor.getMovingstarttime());
//			data.put("movingstarttime", movingstarttime);
			
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
	
	
	// FCM전송 함수
	@RequestMapping("/sendfcm.mc")
	@ResponseBody
	public void sendfcm(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carid = request.getParameter("carid");
		String contents = request.getParameter("contents");
		
		System.out.println(carid+" "+contents);
		
		
		// DB에 control 변경값 저장
		CarSensorVO dbcarsensor = null;

		try {
			dbcarsensor = sbiz.get(Integer.parseInt(carid));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//String token = cbiz.get(Integer.parseInt(carid)).getTablettoken();
		String token = "eHnFzYYCfFY:APA91bFIvkdWxZbiyG_MbUi7kwv1mLVeVLRam-0VD4HKCm4WBVuy2aGsYRr-WzS1Ji7GlVxi7ThepII1G3DjUY40sCYfTHDHfUfGXWudsNMprTZxFQe8mGv7LRLqQeFXyKeGIy3wh1NG";
		
		
		if(contents.equals("on")) {
			
			dbcarsensor.setStarting("o");
			
		}else if(contents.equals("off")) {
			
			dbcarsensor.setStarting("f");
			
		}else if(contents.equals("open")) {
			
			dbcarsensor.setDoor("o");
			
		}else if(contents.equals("close")) {
			
			dbcarsensor.setDoor("f");
			
		}else if(contents.charAt(0) =='T') {
			
			dbcarsensor.setTemper(Integer.parseInt(contents.substring(1)));
			
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
				+ "AAAAeDPCqVw:APA91bH08TNojrp8rdBiVAsIcwTeK5k6ITDZ4q8k5t-FRdEEQiRbFb5I46TAt-0NDg7xQsf9MxTZ7muyKtEeK__IygsotH3G4c4_e--VdDXRub-6H_mL9qetJu7fA-1XR9ip0xG-Q-4i");

		// create notification message into JSON format
		JSONObject message = new JSONObject();
		
		System.out.println(token);
		
		//message.put("to", token);
		message.put("to", "/topics/car");
		message.put("priority", "high");
		
		JSONObject notification = new JSONObject();
		notification.put("title", "차 제어");
		notification.put("body", "test:"+carid+" "+contents);
		message.put("notification", notification);
		
		JSONObject data = new JSONObject();
		data.put("carid",carid);
		data.put("contents",contents);
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


	// 차량과 센서(기본값) 등록
	@RequestMapping("/carregisterimpl.mc")
	public void carregisterimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		String userid = request.getParameter("userid");
		String carnum = request.getParameter("num");
		String cartype = request.getParameter("cartype");
		String carmodel = request.getParameter("model");
		int caryear = Integer.parseInt(request.getParameter("year"));
		String carimg = request.getParameter("img");
		String caroiltype = request.getParameter("oilType");
		String tablettoken = request.getParameter("token");
		// String carnum, String cartype, String carmodel, int caryear, String
		// caroiltype, String tablettoken
		CarVO car = new CarVO(userid, carnum, cartype, carmodel, caryear, carimg, caroiltype, tablettoken);
		System.out.println(car);

		try {
			cbiz.register(car);
			out.print("success");
		} catch (Exception e) {
			out.print("fail");
			throw e;
		}
		int carid = cbiz.caridfromnumber(carnum).getCarid();
		CarSensorVO carsensor = new CarSensorVO(carid,0,"0","0",0,0,50,0,"0","0","0","0","0",0,0);
		sbiz.register(carsensor);
		out.close();
	}

	// 토큰이 바꼈을 때 car number을 통해 token 업데이트
	@RequestMapping("/tokenupdateimpl.mc")
	public void tokenupdateimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		String carnum = request.getParameter("num");
		String tablettoken = request.getParameter("token");
		
		int carid = cbiz.caridfromnumber(carnum).getCarid();
		
		CarVO car = new CarVO(carid, tablettoken);
		System.out.println(car);

		try {
			cbiz.modify(car);
			out.print("success");
		} catch (Exception e) {
			out.print("fail");
			throw e;
		}

		out.close();
	}
	
	
	@RequestMapping("/carnumcheckimpl")
	@ResponseBody
	public void carnumcheck(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carnum = request.getParameter("carnum");
		int carid = cbiz.caridfromnumber(carnum).getCarid();

		CarVO car = new CarVO();
		car = cbiz.get(carid);
		
		JSONArray ja = new JSONArray();

		JSONObject data = new JSONObject();
			
		data.put("carmodel", car.getCarmodel());
		data.put("cartype", car.getCartype());
		data.put("caryear", car.getCaryear());
		data.put("fueltype", car.getCaroiltype());
			
		ja.add(data);
		
		System.out.println("---------test:"+ja.toJSONString());

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();
	}
	
	@RequestMapping("/sendnumberfcm.mc")
	@ResponseBody
	public void sendnumberfcm(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carnum = request.getParameter("carnum");
		String number = request.getParameter("number");
		
		System.out.println(carnum+" "+number);
		
		int carid = cbiz.caridfromnumber(carnum).getCarid();
		String token = cbiz.get(carid).getTablettoken();

		// FCM으로 모바일 제어
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
				+ "AAAAeDPCqVw:APA91bH08TNojrp8rdBiVAsIcwTeK5k6ITDZ4q8k5t-FRdEEQiRbFb5I46TAt-0NDg7xQsf9MxTZ7muyKtEeK__IygsotH3G4c4_e--VdDXRub-6H_mL9qetJu7fA-1XR9ip0xG-Q-4i");

		// create notification message into JSON format
		JSONObject message = new JSONObject();
		
		System.out.println(token);
		
		//message.put("to", token);
		message.put("to", "/topics/car");
		message.put("priority", "high");
		
		JSONObject notification = new JSONObject();
		notification.put("title", "인증번호");
		notification.put("body", number);
		message.put("notification", notification);
		
		JSONObject data = new JSONObject();
		data.put("carid","verify");
		data.put("contents",number);
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
	
	@RequestMapping("/usercarregisterimpl.mc")
	public void getTabletSensor(HttpServletRequest request) throws Exception {
		String userid = request.getParameter("userid");
		String carnum = request.getParameter("carnum");
		String carmodel = request.getParameter("carmodel");
		String cartype = request.getParameter("cartype");
		String caryear = request.getParameter("caryear");
		String fueltype = request.getParameter("fueltype");
		System.out.println(userid+" "+carnum+" "+carmodel+" "+cartype+" "+caryear+" "+fueltype);
		
		int carid = cbiz.caridfromnumber(carnum).getCarid();
		
		CarVO dbcar = null;
		try {
			dbcar = cbiz.get(carid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbcar.setUserid(userid);
		System.out.println(dbcar);
		
		cbiz.modify(dbcar);
	}
		
}
