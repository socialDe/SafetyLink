package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.AdminVO;
import com.vo.CarVO;
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
		String token = request.getParameter("token");
		System.out.println(id + ", " + pwd + ", " + token);

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		try {
			UsersVO user = ubiz.get(id);
			System.out.println(user);
			// 비밀번호 확인 후 로그인
			if (user != null && pwd.equals(user.getUserpwd())) {
				
				System.out.println("dbtoken:"+user.getMobiletoken());
//				// 로그인 상태이며 토큰이 다를 경우
//				if(user.getUserstate().equals("t") || !user.getMobiletoken().equals(token)) {
//					out.print("login");
//					System.out.println("logined..........");
//				} else {
					// 로그인 상태 설정, DB 수정
					user.setUserstate("t");
					user.setMobiletoken(token);
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
					jo.put("sleeppushcheck", user.getSleeppushcheck());
					jo.put("droppushcheck", user.getDroppushcheck());
					jo.put("mobiletoken", user.getMobiletoken());
					out.print(jo.toJSONString());
//				}
			} else {
				out.print("cannot");
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
		String result = "";

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

		try {
			// 가입된 회원 확인
			ArrayList<UsersVO> users = ubiz.get();
			for(UsersVO u : users) {
				System.out.println(u.toString());
				if(user.getUsername().equals(u.getUsername()) && user.getUserphone().equals(u.getUserphone())) {
					result = "cannot user";
					throw new Exception();
				}
			}
			
			// 회원가입
			ubiz.register(user);
			result = "success";
		} catch (Exception e) {
			// 회원가입 실패
			if(result == "") {
				result = "fail";
			}
			throw e;
		} finally {
			out.println(result);
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
		System.out.println(userid);

		try {
			UsersVO user = ubiz.get(userid);
			if (user == null) {
				// 중복되는 id 없음
				out.print("available");
			} else {
				// 중복되는 id 있음, 사용 불가
				out.print("cannot id");
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

		try {System.out.println("try");
			UsersVO user = ubiz.get(userid);
			System.out.println("user get");
			if (username.equals(user.getUsername()) && userphone.equals(user.getUserphone())) {
				// 해당하는 회원정보가 존재
				System.out.println("findsuccess");
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

		String destroy = "";
		
		String userid = request.getParameter("id");
		destroy = request.getParameter("destroy");

		try {
			UsersVO user = ubiz.get(userid);
			user.setUserstate("f");
			ubiz.modify(user);
			
			if(destroy.equals("yes")) {
				out.print("destroy");
			}else {
				out.print("logoutsuccess");
			}
			
		} catch (Exception e) {
			// 로그아웃 실패
			out.print("logoutfail");
			throw e;
		} finally {
			out.close();
		}
	}
	
	// 사용자 정보 전송(2020.12.08) : CustomerMobile에서 유저안전기능 설정 화면에서 사용
	@RequestMapping("/getUserInfo.mc")
	public void sendUserInfo(HttpServletRequest request, HttpServletResponse res) throws Exception{
		String id = request.getParameter("id");
		
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		
		PrintWriter out;
		out = res.getWriter();

		try {
			UsersVO user = ubiz.get(id);
			System.out.println(user);
			JSONObject jo = new JSONObject();
			jo.put("userid", user.getUserid());
			jo.put("usersubject", user.getUsersubject());
			jo.put("babypushcheck", user.getBabypushcheck());
			jo.put("accpushcheck", user.getAccpushcheck());
			jo.put("sleeppushcheck", user.getSleeppushcheck());
			jo.put("droppushcheck", user.getDroppushcheck());
			out.print(jo.toJSONString());

		} catch (Exception e) {
			out.print("fail");
			throw e;
		} finally {
			out.close();
		}
		
	}
	
	// 사용자 안전 기능 변경(2020.12.08)
	@RequestMapping("/safetyFuncSet.mc")
	public void safetyFuncSet(HttpServletRequest request, HttpServletResponse res) throws Exception {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		
		String userid = request.getParameter("userid");
		String func = request.getParameter("func");
		String value = request.getParameter("value");
		
		System.out.println("사용자 안전 기능 변경:"+userid+"님의"+func+"기능"+value+" set 요청");
		
		try {
			UsersVO user = ubiz.get(userid);
			
			if(func.equals("fcm")) {
				user.setUsersubject(value);
			}else if(func.equals("acc")) {
				user.setAccpushcheck(value);
			}else if(func.equals("sleep")){
				user.setSleeppushcheck(value);
			}else if(func.equals("baby")){
				user.setBabypushcheck(value);
			}else if(func.equals("drop")){
				user.setBabypushcheck(value);
			}
			ubiz.modify(user);
		}catch(Exception e){
			System.out.println("사용자 안전 기능 변경 실패");
			throw e;
		}finally {
			System.out.println("사용자 안전 기능 변경:"+userid+"님의"+func+"기능"+value+" set Completed.");
			out.close();
		}
	}
	
	@RequestMapping("/usermodifyimpl.mc")
	public void carmodifyimpl(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String phone = request.getParameter("phone");
		String strbirth = request.getParameter("birth");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date userbirth = sdf.parse(strbirth);
		
		System.out.println(id+" "+pwd+" "+name+" "+sex+" "+phone+" "+userbirth);
		
		UsersVO dbuser = null;
		try {
			dbuser = ubiz.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbuser.setUserpwd(pwd);
		dbuser.setUsername(name);
		dbuser.setUsersex(sex);
		dbuser.setUserphone(phone);
		dbuser.setUserbirth(userbirth);
		
		System.out.println(dbuser);
		
		ubiz.modify(dbuser);
	}
	
	@RequestMapping("/userdata.mc")
	@ResponseBody
	public void cardata(HttpServletRequest request, HttpServletResponse res) throws Exception {
		String id = request.getParameter("id");

		UsersVO user = new UsersVO();
		user = ubiz.get(id);
		
		JSONArray ja = new JSONArray();

		JSONObject data = new JSONObject();
			
		data.put("pwd", user.getUserpwd());
		data.put("name", user.getUsername());
		data.put("sex", user.getUsersex());
		data.put("phone", user.getUserphone());
		
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            Date userbirth = sdf.parse(user.getUserbirth().toString());
            SimpleDateFormat newsdf = new SimpleDateFormat("yyyy/MM/dd");
            String newbirth = newsdf.format(userbirth);
            data.put("birth", newbirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
		
			
		ja.add(data);
		
		System.out.println("getstatus:"+ja.toJSONString());

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		System.out.println(ja);
		out.print(ja.toJSONString());
		out.close();
	}
	
	@RequestMapping("/userdeleteimpl.mc")
	public void cardeleteimpl(HttpServletRequest request) throws Exception {
		String userid = request.getParameter("id");
		
		System.out.println(userid);
		ubiz.remove(userid);
	}
	
}
