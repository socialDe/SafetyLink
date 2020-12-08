package com.test;

import java.util.ArrayList;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.frame.Biz;
import com.vo.AdminVO;
import com.vo.UsersVO;

public class App2 {

	public static void main(String[] args) {
		System.out.println("App Start .......");
		AbstractApplicationContext factory = 
		new GenericXmlApplicationContext("myspring.xml");
		System.out.println("Spring Started .......");
		// IoC
		
		Biz<String,Integer,AdminVO> biz = (Biz)factory.getBean("abiz");
		
		//MemberVO u = new MemberVO("id73", "pwd73","�Ӹ���");
		ArrayList<AdminVO> list = null;
		try {
			list = biz.get();
			for(AdminVO us:list) {
				System.out.println(us);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		factory.close();
		System.out.println("Spring End .......");
		System.out.println("App End .......");

	}

}


