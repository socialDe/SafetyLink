package com.controller;

import java.awt.Window;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.UsersVO;

@Controller
public class FcmController {
	
	@Resource(name="ubiz")
	Biz<String, String, UsersVO> usersbiz;
	
	@RequestMapping("/fcm.mc")
	public ModelAndView fcm(ModelAndView mv) {
		mv.setViewName("fcm");
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
					+ "AAAAeDPCqVw:APA91bH08TNojrp8rdBiVAsIcwTeK5k6ITDZ4q8k5t-FRdEEQiRbFb5I46TAt-0NDg7xQsf9MxTZ7muyKtEeK__IygsotH3G4c4_e--VdDXRub-6H_mL9qetJu7fA-1XR9ip0xG-Q-4i");
			// create notification message into JSON format
			JSONObject message = new JSONObject();
			message.put("to", "/topics/car");
			// message.put("to", "token"); 토큰으로 하는 경우 String token = users.getToken()
			message.put("priority", "high");
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("body", "전체 FCM 메시지");
			message.put("notification", notification);
			
			JSONObject data = new JSONObject();
			data.put("carid", img);
			data.put("contents", contents);
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
			return "redirect:table.mc";
	}
		
	@RequestMapping("/fcmpopupsend.mc")
	public String fcmpopupsend(String token, String title, String contents, MultipartFile mf) {
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
					+ "AAAAeDPCqVw:APA91bH08TNojrp8rdBiVAsIcwTeK5k6ITDZ4q8k5t-FRdEEQiRbFb5I46TAt-0NDg7xQsf9MxTZ7muyKtEeK__IygsotH3G4c4_e--VdDXRub-6H_mL9qetJu7fA-1XR9ip0xG-Q-4i");
			// create notification message into JSON format
			JSONObject message = new JSONObject();
			message.put("to", token);
			// message.put("to", "token"); 토큰으로 하는 경우 String token = users.getToken()
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
			return "redirect:table.mc";
	}
}
