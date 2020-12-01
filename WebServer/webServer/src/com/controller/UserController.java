package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.AdminVO;
import com.vo.UsersVO;

@Controller
public class UserController {
	@Resource(name="ubiz")
	Biz<String, UsersVO> ubiz;
	

	// 회원 로그인 (2020.12.01)
	@RequestMapping("/userloginimpl.mc")
	public void userloginimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		System.out.println(id + ", " + pwd);
		
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out =  res.getWriter();
		
		try {
			UsersVO user = ubiz.get(id);
			System.out.println(user);
			// 비밀번호 확인 후 로그인
			if(pwd.equals(user.getUserpwd())) {
				// 로그인 상태 설정, DB 수정
				user.setUserstate("t");
				ubiz.modify(user);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String userbirth = sdf.format(user.getUserbirth());
				String userregdate = sdf.format(user.getUserregdate());
				

				JSONObject jo = new JSONObject();
				jo.put("userid", user.getUserid());
				jo.put("userpwd", user.getUserpwd());
				jo.put("username", user.getUsername());
				jo.put("userphone", user.getUserphone());
				jo.put("userbirth", userbirth);
				jo.put("usersex", user.getUsersex());
				jo.put("userregdate", userregdate);
				jo.put("userstate", user.getUserstate());
				jo.put("usersubject", user.getUsersubject());
				jo.put("babypushcheck", user.getBabypushcheck());
				jo.put("accpushcheck", user.getAccpushcheck());
				jo.put("mobiletoken", user.getMobiletoken());
				out.print(jo.toJSONString());
			}else {
				out.print("fail");
			}
		} catch (Exception e) {
			// 로그인 실패 정보 전송
			out.print("fail");
			throw e;
		}

		out.close();
	}

	@RequestMapping("/userregisterimpl.mc")
	public void userregisterimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out =  res.getWriter();
		
		
		String userid = request.getParameter("id");
		String userpwd = request.getParameter("pwd");
		String username = request.getParameter("name");
		String usersex = request.getParameter("sex");
		String userphone = request.getParameter("phone");
		String mobiletoken = request.getParameter("token");
		String strbirth = request.getParameter("birth");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date userbirth = sdf.parse(strbirth);
		
		UsersVO user = new UsersVO(userid, userpwd, username, userphone, userbirth, usersex, mobiletoken);
		System.out.println(user);
		
		try {
			ubiz.register(user);
			out.print("success");
		} catch (Exception e) {
			out.print("fail");
			throw e;
		}
		
		out.close();
	}
	
	@RequestMapping("/useridcheckimpl.mc")
	public void useridcheckimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out =  res.getWriter();
		
		String userid = request.getParameter("id");
		
		try {
			UsersVO user = ubiz.get(userid);
			if(user == null) {
				// 중복되는 id 없음
				out.print("available");
			}else {
				// 중복되는 id 있음, 사용 불가
				out.print("cannot");
			}
		} catch (Exception e) {
			// 조회 실패
			out.print("checkfail");
			throw e;
		}
		
		out.close();
	}
}
