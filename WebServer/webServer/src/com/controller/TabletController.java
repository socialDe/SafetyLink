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

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.CarSensorVO;
import com.vo.CarVO;

@Controller
public class TabletController {
	
	@Resource(name="cbiz")
	Biz<Integer,String,CarVO> cbiz;
	@Resource(name="sbiz")
	Biz<Integer,String,CarSensorVO> sbiz;
	
	// 현재 시간 계산
	SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd/HH:mm:ss");
	Calendar time = Calendar.getInstance();
	String timenow = format.format(time.getTime());
	
	
	// FCM전송 함수
	@RequestMapping("/tabletsendfcm.mc")
	@ResponseBody
	public void sendfcm(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carnum = request.getParameter("carnum");
		String contents = request.getParameter("contents");
		
		System.out.println(carnum+" "+contents);
		
		int carid = cbiz.caridfromnumber(carnum).getCarid();
		// DB에 control 변경값 저장
		CarSensorVO dbcarsensor = null;

		try {
			dbcarsensor = sbiz.get(carid);
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
		
	
	@RequestMapping("/getTabletSensor.mc")
	public void androidWithRequest(HttpServletRequest request, HttpServletResponse res) throws Exception {
		String carnum = request.getParameter("carnum");
		String contents = request.getParameter("contents");

		int carid = cbiz.caridfromnumber(carnum).getCarid();
		
		CarSensorVO cs = null;
		try {
			cs = sbiz.get(carid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//storeContents(cs,contents);	      
	      
		sbiz.modify(cs);
	}
	
	
    public void storeContents(CarSensorVO cs,String contents) {
    	
        String contentsSensor = contents.substring(0,3);
        int contentsData = Integer.parseInt(contents.substring(4));

    	
        if(contentsSensor.equals("0001")) {
           cs.setTemper(contentsData/100);
        }else if(contentsSensor.equals("0002")) {
           cs.setCrash(String.valueOf(contentsData));
        }else if(contentsSensor.equals("0003")) {
           cs.setCrash(String.valueOf(contentsData));
        }else if(contentsSensor.equals("0004")) {
           cs.setPirfront(String.valueOf(contentsData));
           cs.setPirrear(String.valueOf(contentsData));
        }else if(contentsSensor.equals("0005")) {
           cs.setFreight(contentsData);
        }else if(contentsSensor.equals("0006")) {
           cs.setHeartbeat(contentsData);
        }else if(contentsSensor.equals("0007")) {
           cs.setFuel(contentsData);
        }else if(contentsSensor.equals("0021")) {
           cs.setAircon(String.valueOf(contentsData));
        }else if(contentsSensor.equals("0031")) {
           cs.setMoving(String.valueOf(contentsData));
           cs.setMovingstarttime(time.getTime());
        }else if(contentsSensor.equals("0032")) {
           cs.setStarting(String.valueOf(contentsData));
        }
        
        System.out.println(cs);
        
     }
    
    
	
}
