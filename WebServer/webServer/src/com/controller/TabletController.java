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
	
	@RequestMapping("/fcmPhone.mc")
	public ModelAndView inputChat(ModelAndView mv, String fcmContents) throws IOException {

		System.out.println(fcmContents+"�씪�뒗 �궡�슜�쑝濡� FCM �쟾�넚!!!!");

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
		notification.put("title", "怨듭��븣由�");
		notification.put("body", "body1");
		message.put("notification", notification);
		
		JSONObject data = new JSONObject();
		data.put("control",fcmContents );
		data.put("data", "");
		message.put("data", data);

		try {
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			System.out.println("FCM �쟾�넚:"+message.toString());
			out.write(message.toString());
			out.flush();
			conn.getInputStream();
			System.out.println("OK...............");

		} catch (IOException e) {
			System.out.println("Error while writing outputstream to firebase sending to ManageApp | IOException");
			e.printStackTrace();
		}
		
		
		mv.setViewName("main");
		return mv;
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
