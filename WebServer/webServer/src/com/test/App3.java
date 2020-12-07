package com.test;

import java.util.ArrayList;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.frame.Biz;
import com.vo.CarSensorVO;
import com.vo.CarVO;

public class App3 {

	public static void main(String[] args) {
		System.out.println("App Start .......");
		AbstractApplicationContext factory = 
		new GenericXmlApplicationContext("myspring.xml");
		System.out.println("Spring Started .......");
		// IoC
		
		Biz<Integer,String,CarVO> cbiz = (Biz)factory.getBean("cbiz");
		Biz<Integer,String,CarSensorVO> sbiz = (Biz)factory.getBean("sbiz");
		

		
		
//		ArrayList<CarVO> clist;
//		
//		try {
//			clist = cbiz.get();
//			for(CarVO c:clist) {
//				System.out.println(c);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		ArrayList<CarSensorVO> slist;
//		
//		try {
//			slist = sbiz.get();
//			for(CarSensorVO s:slist) {
//				System.out.println(s);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		ArrayList<CarVO> clist;
//		
//		try {
//			clist = cbiz.getcarsfromuser("id01");
//			for(CarVO c:clist) {
//				System.out.println(c);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//		ArrayList<CarSensorVO> slist;
//		
//		try {
//			slist = sbiz.getcarsfromuser("id01");
//			for(CarSensorVO s:slist) {
//				System.out.println(s);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		
		
		CarSensorVO dbcarsensor = null;
		
		try {
			dbcarsensor = sbiz.get(1);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		dbcarsensor.setStarting("o");
		dbcarsensor.setDoor("o");
		dbcarsensor.setTemper(18);
		
		
		
		try {
			sbiz.modify(dbcarsensor);
			System.out.println("Modify OK..");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		factory.close();
		System.out.println("Spring End .......");
		System.out.println("App End .......");

	}

}


