package com.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FcmController {
	
	@RequestMapping("/fcm.mc")
	public ModelAndView fcm(ModelAndView mv) {
		mv.setViewName("view/fcm");
		return mv;
	}
	
	//FCM 보냈을 때 일어나는 일을 모두 처리
	@RequestMapping("/fcmsendall.mc")
	public String fcmsendall(String title, String contents, MultipartFile mf) {
		System.out.println("phone Send Start...");
			
		// 이미지를 가져오는 과정
		// Window -> preferences -> General -> Workspace -> Refresh using native hooks or polling 체크
		// 체크해야 들어오는 img 파일이 바로 새로고침된다
		Util.saveFile(mf);
		String img = mf.getOriginalFilename();
		// 기본 이미지는 logo.png로 설정
		if(img == "") {
			img = "logo.png";
		}
		
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
			notification.put("title", title);
			notification.put("body", contents);
			message.put("notification", notification);
			
			JSONObject data = new JSONObject();
			data.put("img", img);
			data.put("control", "control1");
			data.put("data", 100);
			message.put("data", data);


			try {
				OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				System.out.println("FCM 전송:"+message.toString());
				out.write(message.toString());
				out.flush();
				conn.getInputStream();
				System.out.println("OK...............");

			} catch (IOException e) {
				System.out.println("Error while writing outputstream to firebase sending to ManageApp | IOException");
				e.printStackTrace();
			}	
			
			System.out.println("phone Send End...");
			
			// 눌렀을 때 다시 fcm 페이지를 보여주도록
			return "redirect:view/fcm.mc";
	}
		
	}
