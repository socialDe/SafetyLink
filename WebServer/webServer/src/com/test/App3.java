package com.test;

import java.util.ArrayList;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.frame.Biz;
import com.vo.CarVO;

public class App3 {

	public static void main(String[] args) {
		System.out.println("App Start .......");
		AbstractApplicationContext factory = 
		new GenericXmlApplicationContext("myspring.xml");
		System.out.println("Spring Started .......");
		// IoC
		
		Biz<Integer, CarVO> biz = (Biz)factory.getBean("cbiz");
		
//		ShopVO s = new ShopVO("����", 10000,"pants.jpg");
//		try {
//			biz.register(s);
//			System.out.println("OK");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		ArrayList<CarVO> list;
		
		try {
			list = biz.get();
			for(CarVO sv:list) {
				System.out.println(sv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		factory.close();
		System.out.println("Spring End .......");
		System.out.println("App End .......");

	}

}


