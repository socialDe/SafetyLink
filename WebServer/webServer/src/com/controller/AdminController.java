package com.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.AdminVO;

@Controller
public class AdminController {
	@Resource(name="abiz")
	Biz<String, AdminVO> abiz;
	
	// 로그인 (2020.11.27)
	@RequestMapping("/loginimpl.mc")
	public ModelAndView loginimpl(ModelAndView mv, HttpServletRequest request, HttpSession session) {
		String id = request.getParameter("id");
		String pwd = request.getParameter("password");
		System.out.println(id + ", " + pwd);
		
		try {
			AdminVO dbadmin = abiz.get(id);
			
			// 비밀번호 확인 후 로그인
			if(pwd.equals(dbadmin.getAdminpwd())) {
				session.setAttribute("admin", dbadmin);
				
				// 로그인 상태 설정, DB 수정
				dbadmin.setAdminstate("t");
				abiz.modify(dbadmin);
				
				// 로그인 완료 후 메인페이지로 대쉬보드 설정
				mv.addObject("centerpage", "dashboard");
			}else {
				// 로그인 실패 정보 전송
				mv.addObject("loginfail", "loginfail");
			}
		} catch (Exception e) {
			// 로그인 실패 정보 전송
			mv.addObject("loginfail", "loginfail");
			e.printStackTrace();
		}
		
		mv.setViewName("main");
		return mv;
	}
	
	// 로그아웃 (2020.11.27)
	@RequestMapping("/logout.mc")
	public ModelAndView logout(ModelAndView mv, HttpServletRequest request) {
		HttpSession session = request.getSession();
		AdminVO admin = (AdminVO) session.getAttribute("admin");
		
		if (session != null) {
			// 세선 정보 삭제
			session.invalidate();
			
			// 로그아웃 상태 설정
			admin.setAdminstate("f");
		}
		
		// 로그아웃 상태 DB 수정
		try {
			abiz.modify(admin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.setViewName("main");
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/chartSearch.mc")
	public void chartSearch() {
		
	}
	
	
}
