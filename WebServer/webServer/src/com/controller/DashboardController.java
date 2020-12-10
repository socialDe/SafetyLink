package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.CarSensorVO;
import com.vo.UsersVO;

@Controller
public class DashboardController {

	@Resource(name = "ubiz")
    Biz<String, String, UsersVO> ubiz;
	

	@Resource(name = "sbiz")
    Biz<Integer, String, CarSensorVO> sbiz;

    // 네비게이션 바에서 dashboard탭 클릭하여 dashboard 페이지로 이동합니다.
    @RequestMapping("/dashboard.mc")
    public ModelAndView dashboard() {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("dashboard");  

    		// For Test
    		Random r = new Random();
    		
    		int sleep = r.nextInt(1000); // 졸음 경보 임시데이터
    		int freight = r.nextInt(100); // 적재물 경보 임시데이터
    		int accident = r.nextInt(100); // 일간 교통사고 임시데이터
    		int baby = r.nextInt(100); // 영유아 경보 임시데이터

            ArrayList<UsersVO> users = new ArrayList<>();
            ArrayList<CarSensorVO> drivingcars = new ArrayList<>();
            try {
				users = ubiz.get();
				drivingcars = sbiz.getdrivingcars("o");
			} catch (Exception e) {
				e.printStackTrace();
			}

            mv.addObject("drive", drivingcars.size()); // 현재 운행중인 차량
            mv.addObject("sleep", sleep);
            mv.addObject("freight", freight);
            mv.addObject("accident", accident);

            // 일간 주행 유저 = 주행 중 차량 수 / 전체 유저 * 100
            int driving = (int) (1.0 * drivingcars.size() / users.size() * 100);
            mv.addObject("driver", driving);
            
            // 일간 경보비율 = 전체 경보 횟수 / 전체 유저(임시데이터 사용) * 100
            int alarmRate = (int)(1.0 * (sleep + freight + accident + baby) / r.nextInt(1000)/*users.size()*/ * 100);
            mv.addObject("alarmRate", alarmRate);
            
            // 전월대비 경보 증감률 = (지난달 대비 이번달 경보 증감수) / 지난달 경보 횟수 * 100
            int lastMonthAlarm = r.nextInt(2000); // 지난달 경보 횟수 임시데이터
            int monthAlarm = r.nextInt(2000); // 이번달 누적 경보 횟수 임시데이터
            int monthAlarmRate = (int)(1.0 * (monthAlarm - lastMonthAlarm) / lastMonthAlarm * 100);
            mv.addObject("monthAlarmRate", monthAlarmRate);

            // 전일대비 경보 증감률 = (전일 대비 오늘 경보 증감수) / 전일 경보 횟수 * 100
            int lastDayAlarm = r.nextInt(2000); // 지난달 경보 횟수 임시데이터
            int dayAlarm = r.nextInt(2000); // 이번달 누적 경보 횟수 임시데이터
            int dayAlarmRate = (int)(1.0 * (dayAlarm - lastDayAlarm) / lastDayAlarm * 100);
            mv.addObject("dayAlarmRate", dayAlarmRate);
            
            return mv;
    }
    
	@RequestMapping("/donutchartimpl.mc")
	public void dashboardimpl(ModelAndView mv, HttpServletResponse res) throws IOException {
		ArrayList<Donutdata> result = new ArrayList<>();
		
		// For Test
		Random r = new Random();
		Donutdata do1 = new Donutdata("서울", r.nextInt(100));
		Donutdata do2 = new Donutdata("경기", r.nextInt(100));
		Donutdata do3 = new Donutdata("기타", r.nextInt(100));
		result.add(do1);
		result.add(do2);
		result.add(do3);

		// 객체의 Value를 기준으로 내림차순 정렬하기
		ValueAscending valueAscending = new ValueAscending();
		Collections.sort(result, valueAscending);
		
		JSONArray ja = new JSONArray();
		for (Donutdata d : result) {
			JSONObject jo = new JSONObject();
			jo.put("label", d.getLabel());
			jo.put("value", d.getValue());
			ja.add(jo);
		}

		// System.out.println(ja);
	    res.setContentType("text/xml;charset=utf-8");
		PrintWriter out = res.getWriter();
	    out.print(ja);
	    out.close();
	}
	
	// 객체의 Value를 기준으로 내림차순 정렬하기
	class ValueAscending implements Comparator<Donutdata> {
	 
	    @Override
	    public int compare(Donutdata a, Donutdata b) {
	        Integer temp1 = a.getValue();
	        Integer temp2 = b.getValue();
	        
	        return temp2.compareTo(temp1);
	    }
	}

	public class Donutdata {
		private String label;
		private int value;

		public Donutdata() {
		}

		public Donutdata(String label, int value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
}
