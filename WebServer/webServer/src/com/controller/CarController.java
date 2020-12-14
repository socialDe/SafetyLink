package com.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

	 @Resource(name="ubiz")
	 Biz<String,String,UsersVO> ubiz;
	@Resource(name = "cbiz")
	Biz<Integer, String, CarVO> cbiz;
	@Resource(name = "sbiz")
	Biz<Integer, String, CarSensorVO> sbiz;

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
			//String movingstarttime = format.format(dbcarSensor.getMovingstarttime());
			String movingstarttime = null;
			data.put("movingstarttime", movingstarttime);

			data.put("aircon", dbcarSensor.getAircon());
			data.put("crash", dbcarSensor.getCrash());
			data.put("door", dbcarSensor.getDoor());
			data.put("lat", dbcarSensor.getLat());
			data.put("lng", dbcarSensor.getLng());
			ja.add(data);
		}

		//System.out.println("---------test:" + ja.toJSONString());

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

		String carnum = request.getParameter("carnum");
		String contents = request.getParameter("contents");
		
		String contentsSensor = contents.substring(4,8);
		int contentsData = Integer.parseInt(contents.substring(8));

		System.out.println("carnum:"+carnum + " " + "contents:"+contents);

		// DB에 control 변경값 저장
		CarSensorVO dbcarsensor = null;
		
		int carid = cbiz.caridfromnumber(carnum).getCarid();
		String userid = cbiz.get(carid).getUserid();

		
		try {
			dbcarsensor = sbiz.get(carid);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		
		String token = "";
		
		// 차량에서 모바일로 보내는 푸쉬(영유아)
        if(contentsSensor.equals("0004")) {
        	// 영유아 푸쉬 받기로 설정
        	if(ubiz.get(userid).getBabypushcheck().equals("o")) {
        		token = ubiz.get(userid).getMobiletoken();
        	}
        	// 안받기로 설정
        	else if(ubiz.get(userid).getBabypushcheck().equals("f")) {
        		return;
        	}      	
        }
        // 차량에서 모바일로 보내는 푸쉬(충돌)
        else if(contentsSensor.equals("0002") || contentsSensor.equals("0003")){
        	if(ubiz.get(userid).getAccpushcheck().equals("o")) {
        		token = ubiz.get(userid).getMobiletoken();
        	}
        	// 안받기로 설정
        	else if(ubiz.get(userid).getAccpushcheck().equals("f")) {
        		return;
        	}  
        }
        // 모바일에서 차량제어
        else{
        	token = cbiz.get(carid).getTablettoken();
        }
		


        
		 // 온도
        if(contentsSensor.equals("0001")) {
        	//contentsData/100  온도값 ex)15
        }
        // 충돌
        else if(contentsSensor.equals("0002")) {
            //String.valueOf(contentsData) 충돌여부 ex)1,0
        }
        // 진동
        else if(contentsSensor.equals("0003")) {
            //String.valueOf(contentsData) 충돌세기 ex)2(small),3(big)
        }
        // pir
        else if(contentsSensor.equals("0004")) {
        	dbcarsensor.setPirfront(String.valueOf(contentsData));
        	dbcarsensor.setPirrear(String.valueOf(contentsData));
           //String.valueOf(contentsData) 영유아감지여부 ex)1,0
        }
        // 무게
        else if(contentsSensor.equals("0005")) {
            //contentsData 무게 ex)30
        }
        // 심박수
        else if(contentsSensor.equals("0006")) {
            //contentsData 심박수 ex) 80
        }
        // 연료
        else if(contentsSensor.equals("0007")) {
            //contentsData 현재연료량 ex) 40
        }
        // 에어컨
        else if(contentsSensor.equals("0021")) {
        	System.out.println("Aircon contentsData:"+contentsData);
        	dbcarsensor.setAircon(String.valueOf(Math.round(contentsData/100)));
            //String.valueOf(contentsData) 에어컨목표온도값 ex) 25
        }
        // 시동
        else if(contentsSensor.equals("0031")) {
        	dbcarsensor.setStarting(String.valueOf(contentsData));
            //String.valueOf(contentsData) 시동여부 ex)1,0
        }
        // 주행
        else if(contentsSensor.equals("0032")) {
            //String.valueOf(contentsData) 주행여부 ex)1,0
            //time.getTime() 주행시작시간 ex) 시간값형태로 나올듯
        }
        // 문
        else if(contentsSensor.equals("0033")) {
        	dbcarsensor.setDoor(String.valueOf(contentsData));
            //String.valueOf(contentsData) 문  ex)1,0
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

		System.out.println("token:"+token);

		message.put("to", token);
		//message.put("to", "/topics/car");
		message.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "SaftyLink 알람");
		notification.put("body", "");
		message.put("notification", notification);

		JSONObject data = new JSONObject();
		data.put("carid", carid);
		data.put("contents", contents);
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

		
		CarVO car = new CarVO(userid, carnum, cartype, carmodel, caryear, "car1.jpg", caroiltype, tablettoken);
		System.out.println(car);

		
		try {
			cbiz.register(car);
			out.print("success");
			
			int carid = cbiz.caridfromnumber(carnum).getCarid();
			
			CarSensorVO carsensor = new CarSensorVO(carid, 0, "0", "0", 0, 50, 50, 0, "0", "0", "25", "0", "0", 0, 0);
			sbiz.register(carsensor);
		} catch (Exception e) {
			out.print("fail");
			throw e;
		}
			
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
	
	// 인증번호 입력 후 '확인' 버튼 누르면 Car 정보를 띄워줌
	@RequestMapping("/carnumcheckimpl.mc")
	@ResponseBody
	public void carnumcheck(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carnum = request.getParameter("carnum");
		System.out.println("carnum:"+carnum);
		int carid = cbiz.caridfromnumber(carnum).getCarid();
		System.out.println("00000000");

		CarVO car = new CarVO();
		car = cbiz.get(carid);
		
		JSONArray ja = new JSONArray();

		JSONObject data = new JSONObject();
			
		data.put("carmodel", car.getCarmodel());
		data.put("cartype", car.getCartype());
		data.put("caryear", car.getCaryear());
		data.put("fueltype", car.getCaroiltype());
			
		ja.add(data);
		
		System.out.println("out:"+ja.toJSONString());

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();
	}
	
	// '등록 확인' 버튼 누르면 Tablet로 인증번호 전송
	@RequestMapping("/sendnumberfcm.mc")
	@ResponseBody
	public void sendnumberfcm(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carnum = request.getParameter("carnum");
		String number = request.getParameter("number");
		
		String token = "";
		
		System.out.println(carnum+" "+number);
		
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter outt = res.getWriter();
		
		try {
			int carid = cbiz.caridfromnumber(carnum).getCarid();
			if(!cbiz.get(carid).getUserid().equals("unassigned")) {
				outt.print("alreadyExist");
				outt.close();
				return;
			}
			token = cbiz.get(carid).getTablettoken();
			System.out.println("carid:"+carid+"\n"+"token:"+token);
		}catch(Exception e) {
			outt.print("notExist");
			outt.close();
			return;
		}



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
		
		message.put("to", token);
		//message.put("to", "/topics/car");
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
	
	// 차량 검색 후 user와 연동하는 확인 버튼
	@RequestMapping("/usercarregisterimpl.mc")
	public void usercarregisterimpl(HttpServletRequest request) throws Exception {
		String userid = request.getParameter("userid");
		String carnum = request.getParameter("carnum");
		String carname = request.getParameter("carname");
		System.out.println(userid+" "+carnum+" "+carname);
		
		int carid = cbiz.caridfromnumber(carnum).getCarid();
		
		CarVO dbcar = null;
		try {
			dbcar = cbiz.get(carid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbcar.setCarname(carname);
		dbcar.setUserid(userid);
		dbcar.setCarname(carname);
		System.out.println(dbcar);
		
		cbiz.modify(dbcar);
	}
	
	// 영유아 확인 일정시간 누르지 않았을 때 실행 됨
	@RequestMapping("/alarmbaby.mc")
	public void alarmbaby(HttpServletRequest request) throws Exception {
		String userid = request.getParameter("userid");

		System.out.println("userid:"+userid+"의 차량에서 영유아가 확인되었습니다!!");
		

	}
	
		
}
