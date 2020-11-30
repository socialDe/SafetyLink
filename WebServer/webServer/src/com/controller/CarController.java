package com.controller;

import java.io.PrintWriter;

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
	Biz<Integer, CarVO> cbiz;
	@Resource(name="sbiz")
	Biz<Integer, CarSensorVO> sbiz;
	
	// 차량 데이터
	@RequestMapping("/cardata.mc")
	@ResponseBody
	public void cardata(HttpServletRequest request, HttpServletResponse res) throws Exception {

		String carid = request.getParameter("carid");
		
		CarVO dbcar = null;
		
		try {
			dbcar = cbiz.get(Integer.parseInt(carid));
		} catch (Exception e) {
			System.out.println("get cardata 실패");
			e.printStackTrace();
		}
		
		JSONArray ja = new JSONArray();

		
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

		String carid = request.getParameter("carid");
		
		CarSensorVO dbcarsensor = null;
		
		try {
			dbcarsensor = sbiz.get(Integer.parseInt(carid));
		} catch (Exception e) {
			System.out.println("get carsensordata 실패");
			e.printStackTrace();
		}
		
		JSONArray ja = new JSONArray();

		
		JSONObject data = new JSONObject();
		data.put("carid", dbcarsensor.getCarid());
		data.put("heartbeat", dbcarsensor.getHeartbeat());
		data.put("pirfront", dbcarsensor.getPirfront());
		data.put("pirrear", dbcarsensor.getPirrear());
		data.put("freight", dbcarsensor.getFreight());
		data.put("fuel", dbcarsensor.getFuel());
		data.put("fuelmax", dbcarsensor.getFuelmax());
		data.put("temper", dbcarsensor.getTemper());
		data.put("starting", dbcarsensor.getStarting());
		data.put("moving", dbcarsensor.getMoving());
		data.put("movingstarttime", dbcarsensor.getMovingstarttime());
		data.put("aircon", dbcarsensor.getAircon());
		data.put("crash", dbcarsensor.getCrash());
		data.put("door", dbcarsensor.getDoor());
		data.put("lat", dbcarsensor.getLat());
		data.put("lng", dbcarsensor.getLng());
		ja.add(data);
		
		

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();
		
	}

	
}
