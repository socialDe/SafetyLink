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

@Controller
public class TabletController {

	@Resource(name = "cbiz")
	Biz<Integer, String, CarVO> cbiz;
	@Resource(name = "sbiz")
	Biz<Integer, String, CarSensorVO> sbiz;

	// 현재 시간 계산
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
	Calendar time = Calendar.getInstance();
	String timenow = format.format(time.getTime());
		
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

		storeContents(cs, contents);

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
		int carid = cbiz.caridfromnumber(carnum).getCarid();

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
		
		System.out.println("---------test:"+ja.toJSONString());

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();
	}

}
