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
	@Resource(name = "ubiz")
	Biz<String, String, UsersVO> ubiz;

	// 회원 로그인 (2020.12.01)
	@RequestMapping("/userloginimpl.mc")
	public void userloginimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		System.out.println(id + ", " + pwd);

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		try {
			UsersVO user = ubiz.get(id);
			System.out.println(user);
			// 비밀번호 확인 후 로그인
			if (pwd.equals(user.getUserpwd())) {
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
			} else {
				out.print("fail");
			}
		} catch (Exception e) {
			// 로그인 실패 정보 전송
			out.print("fail");
			throw e;
		} finally {
			out.close();
		}
	}

	// 회원가입 (2020.12.01)
	@RequestMapping("/userregisterimpl.mc")
	public void userregisterimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

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
		} finally {
			out.close();
		}
	}

	// 아이디 중복 확인 (2020.12.01)
	@RequestMapping("/useridcheckimpl.mc")
	public void useridcheckimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		String userid = request.getParameter("id");

		try {
			UsersVO user = ubiz.get(userid);
			if (user == null) {
				// 중복되는 id 없음
				out.print("available");
			} else {
				// 중복되는 id 있음, 사용 불가
				out.print("cannot");
			}
		} catch (Exception e) {
			// 조회 실패
			out.print("checkfail");
			throw e;
		} finally {
			out.close();
		}
	}

	// 아이디 찾기 (2020.12.02)
	@RequestMapping("/useridfindimpl.mc")
	public void useridfindimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		String username = request.getParameter("name");
		String userphone = request.getParameter("phone");

		try {
			UsersVO user = ubiz.getFromKeys(username, userphone);
			if (user == null) {
				// 해당하는 회원 정보 없음
				out.print("noun");
			} else {
				// userid 출력
				out.print(user.getUserid());
			}
		} catch (Exception e) {
			// 조회 실패
			out.print("findfail");
			throw e;
		} finally {
			out.close();
		}
	}

	// 비밀번호 찾기 (2020.12.02)
	@RequestMapping("/userpwdfindimpl.mc")
	public void userpwdfindimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		String userid = request.getParameter("id");
		String username = request.getParameter("name");
		String userphone = request.getParameter("phone");

		try {
			UsersVO user = ubiz.get(userid);
			System.out.println("안드로이드: " + username + ", " + username + ", " + userphone);
			System.out.println("DB: " + user.getUsername() + ", " + user.getUserphone());
			if (username.equals(user.getUsername()) && userphone.equals(user.getUserphone())) {
				// 해당하는 회원정보가 존재
				out.print("findsuccess");
			} else {
				// 해당하는 회원정보가 없음
				out.print("noun");
			}
		} catch (Exception e) {
			// 조회 실패
			out.print("findfail");
			throw e;
		} finally {
			out.close();
		}
	}

	// 비밀번호 변경 (2020.12.02)
	@RequestMapping("/userpwdchangeimpl.mc")
	public void userpwdchangeimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		String userid = request.getParameter("id");
		String userpwd = request.getParameter("pwd");
		String userpwdcon = request.getParameter("pwdcon");

		try {
			// 비밀번호와 비밀번호 확인 문자 비교 후,
			// 회원정보를 가져와 비밀번호 변경
			if (userpwd.equals(userpwdcon)) {
				UsersVO user = ubiz.get(userid);
				user.setUserpwd(userpwd);
				ubiz.modify(user);
				out.print("changesuccess");
			} else {
				out.print("changefail");
			}
		} catch (Exception e) {
			// 변경
			out.print("changefail");
			throw e;
		} finally {
			out.close();
		}

	}

	// 로그아웃 (2020.12.02)
	@RequestMapping("/userlogoutimpl.mc")
	public void userlogoutimpl(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		String userid = request.getParameter("id");

		try {
			UsersVO user = ubiz.get(userid);
			user.setUserstate("f");
			ubiz.modify(user);
			out.print("logoutsuccess");
		} catch (Exception e) {
			// 로그아웃 실패
			out.print("logoutfail");
			throw e;
		} finally {
			out.close();
		}
	}
}
