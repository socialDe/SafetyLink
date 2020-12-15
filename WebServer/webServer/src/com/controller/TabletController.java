package com.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.CarSensorVO;
import com.vo.CarVO;
import com.vo.UsersVO;

@Controller
public class TabletController {

	@Resource(name = "cbiz")
	Biz<Integer, String, CarVO> cbiz;
	@Resource(name = "sbiz")
	Biz<Integer, String, CarSensorVO> sbiz;
	@Resource(name = "ubiz")
	Biz<String, String, UsersVO> ubiz;

	// 현재 시간 계산
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
	Calendar time = Calendar.getInstance();
	String timenow = format.format(time.getTime());
		
	@RequestMapping("/getTabletSensor.mc")
	public void androidWithRequest(HttpServletRequest request, HttpServletResponse res) throws Exception {
		String carnum = request.getParameter("carnum");
		String contents = request.getParameter("contents");

		int carid = cbiz.carfromnumber(carnum).getCarid();

		CarSensorVO cs = null;
		try {
			cs = sbiz.get(carid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		storeContents(cs, contents);
		
		System.out.println("modify:"+cs);

		sbiz.modify(cs);
	}

	@RequestMapping("/getTabletSensors.mc")
	public void androidWithRequests(HttpServletRequest request, HttpServletResponse res) throws Exception {
		String carnum = request.getParameter("carnum");
		String contents = request.getParameter("contents");
		String fuel = request.getParameter("fuel");

		int carid = cbiz.carfromnumber(carnum).getCarid();

		CarSensorVO cs = null;
		try {
			cs = sbiz.get(carid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		storeContents(cs, contents);
		storeContents(cs, fuel);

		sbiz.modify(cs);
	}
	
	public void storeContents(CarSensorVO cs, String contents) {

		String contentsSensor = contents.substring(4, 8);
		int contentsData = Integer.parseInt(contents.substring(8));

		if (contentsSensor.equals("0001")) {
			cs.setTemper(contentsData/100);
		} else if (contentsSensor.equals("0002")) {
			cs.setCrash(String.valueOf(contentsData));
		} else if (contentsSensor.equals("0003")) {
			cs.setCrash(String.valueOf(contentsData));
		} else if (contentsSensor.equals("0004")) {
			cs.setPirfront(String.valueOf(contentsData));
			cs.setPirrear(String.valueOf(contentsData));
		} else if (contentsSensor.equals("0005")) {
			cs.setFreight(contentsData);
		} else if (contentsSensor.equals("0006")) {
			cs.setHeartbeat(contentsData);
		} else if (contentsSensor.equals("0007")) {
			cs.setFuel(contentsData);
		} else if (contentsSensor.equals("0021")) {
			cs.setAircon(String.valueOf(contentsData/100));
		} else if (contentsSensor.equals("0031")) {
			cs.setStarting(String.valueOf(contentsData));
		} else if (contentsSensor.equals("0032")) {
			cs.setMoving(String.valueOf(contentsData));
			cs.setMovingstarttime(time.getTime());
		} else if (contentsSensor.equals("0033")) {
			cs.setDoor(String.valueOf(contentsData));
		}

		System.out.println(cs);

	}
	
	// Tablet 을 켰을 때 상태를 가져와줌(Tablet을 늦게 켰을 경우, 오류 방지)
	@RequestMapping("/getstatus.mc")
	@ResponseBody
	public void carnumcheck(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carnum = request.getParameter("carnum");
		int carid = cbiz.carfromnumber(carnum).getCarid();

		CarSensorVO car = new CarSensorVO();
		car = sbiz.get(carid);
		
		JSONArray ja = new JSONArray();

		JSONObject data = new JSONObject();
			
		data.put("starting", car.getStarting());
		data.put("door", car.getDoor());
		data.put("moving", car.getMoving());
		data.put("fuel", car.getFuel());
		data.put("fuelmax", car.getFuelmax());
		data.put("temper", car.getTemper());
		data.put("aircon", car.getAircon());
		data.put("freight", car.getFreight());
			
		ja.add(data);
		
		System.out.println("getstatus:"+ja.toJSONString());

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();
	}
	
	
	// Tablet 을 켰을 때 push상태를 가져와줌
	@RequestMapping("/getpush.mc")
	@ResponseBody
	public void getpush(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carnum = request.getParameter("carnum");
		int carid = cbiz.carfromnumber(carnum).getCarid();
		
		String userid = cbiz.get(carid).getUserid();

		UsersVO user = new UsersVO();
		user = ubiz.get(userid);
		
		JSONArray ja = new JSONArray();

		JSONObject data = new JSONObject();
		
		data.put("accpushcheck", user.getAccpushcheck());	
		data.put("droppushcheck", user.getDroppushcheck());
		data.put("sleeppushcheck", user.getSleeppushcheck());

			
		ja.add(data);
		
		System.out.println("getpush:"+ja.toJSONString());

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();
	}
	
	

	@RequestMapping("/getMovingcar.mc")
	public void getMovingcar(HttpServletRequest request, HttpServletResponse res){
		String carnum = request.getParameter("carnum");
		System.out.println(carnum);
		
		CarSensorVO carinfo = null;
		CarVO car = null;
		String userid = "";
		UsersVO user = null;
		try {
			carinfo = sbiz.movingcarfromnumber(carnum);
			car = cbiz.carfromnumber(carnum);
			userid = car.getUserid();
			user = ubiz.get(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 이 부분에 수정할 코드를 넣는다
		if(carinfo != null) {
			PrintWriter out = null;
			try {
				out = res.getWriter();
				out.print("crush");
			} catch (IOException e) {
				// e.printStackTrace();
			} finally {
				out.close();
			}
		}else {

			String token = user.getMobiletoken();
//			String token = "eHnFzYYCfFY:APA91bFIvkdWxZbiyG_MbUi7kwv1mLVeVLRam-0VD4HKCm4WBVuy2aGsYRr-WzS1Ji7GlVxi7ThepII1G3DjUY40sCYfTHDHfUfGXWudsNMprTZxFQe8mGv7LRLqQeFXyKeGIy3wh1NG";
			
			
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

			System.out.println("token:"+token);

			message.put("to", token);
			//message.put("to", "/topics/car");
			message.put("priority", "high");

			JSONObject notification = new JSONObject();
			notification.put("title", "SaftyLink 알람");
			notification.put("body", "");
			message.put("notification", notification);

			JSONObject data = new JSONObject();
			data.put("carid", car.getCarid());
			data.put("contents", "CA02000300000002");
			message.put("data", data);

			try {
				OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				System.out.println("FCM 전송:" + message.toString());
				out.write(message.toString());
				out.flush();
				conn.getInputStream();
				System.out.println(" FCM OK...............");

			} catch (IOException e) {
				System.out.println("Error while writing outputstream to firebase sending to ManageApp | IOException");
				e.printStackTrace();
			}
			
			
			
////////////////////////////////////////////////////////////////
//			// FCM으로 모바일 제어
//			URL url = null;
//			try {
//				url = new URL("https://fcm.googleapis.com/fcm/send");
//			} catch (MalformedURLException e) {
//				System.out.println("Error while creating Firebase URL | MalformedURLException");
//				e.printStackTrace();
//			}
//			HttpURLConnection conn = null;
//			try {
//				conn = (HttpURLConnection) url.openConnection();
//			} catch (IOException e) {
//				System.out.println("Error while createing connection with Firebase URL | IOException");
//				e.printStackTrace();
//			}
//			conn.setUseCaches(false);
//			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			conn.setRequestProperty("Content-Type", "application/json");
//
//			// set my firebase server key
//			conn.setRequestProperty("Authorization", "key="
//					+ "AAAAeDPCqVw:APA91bH08TNojrp8rdBiVAsIcwTeK5k6ITDZ4q8k5t-FRdEEQiRbFb5I46TAt-0NDg7xQsf9MxTZ7muyKtEeK__IygsotH3G4c4_e--VdDXRub-6H_mL9qetJu7fA-1XR9ip0xG-Q-4i");
//
//			// create notification message into JSON format
//			JSONObject message = new JSONObject();
//			
//			System.out.println(token);
//			
//			message.put("to", token);
////			message.put("to", "/topics/car");
//			message.put("priority", "high");
//			
//			JSONObject notification = new JSONObject();
//			notification.put("title", "충돌 사고");
//			notification.put("body", "test:"+car.getCarid()+" "+"충돌 사고");
//			message.put("notification", notification);
//			
//			JSONObject data = new JSONObject();
//			data.put("carid",car.getCarid());
//			data.put("contents","충돌 사고");
//			message.put("data", data);
//
//
//			try {
//				OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//				System.out.println("FCM 전송:"+message.toString());
//				out.write(message.toString());
//				out.flush();
//				conn.getInputStream();
//				System.out.println(" FCM OK...............");
//
//			} catch (IOException e) {
//				System.out.println("Error while writing outputstream to firebase sending to ManageApp | IOException");
//				e.printStackTrace();
//			}
//			
		}
	}
}
