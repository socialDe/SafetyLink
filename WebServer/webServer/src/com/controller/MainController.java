package com.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.UsersVO;

@Controller
public class MainController {

	@Resource(name = "ubiz")
    Biz<String, String, UsersVO> ubiz;

	
	@RequestMapping("/main.mc")
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("main");
		return mv;
	}
	

	@RequestMapping("/login.mc")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}
	
    // 네비게이션 바에서 Chart탭 클릭하여 Chart 페이지로 이동합니다.
    @RequestMapping("/chart.mc")
    public ModelAndView chart() {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("chart");  
            return mv;
    }
    
    // 네비게이션 바에서 ui
    @RequestMapping("/ui-elements.mc")
    public ModelAndView uielements() {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("ui-elements");  
            return mv;
    }
    
    // 네비게이션 바에서 Chart탭 클릭하여 Chart 페이지로 이동합니다.
    @RequestMapping("/table.mc")
    public ModelAndView table() {
            ModelAndView mv = new ModelAndView();
            
            ArrayList<UsersVO> usersInfo = new ArrayList<>();
            try {
				usersInfo = ubiz.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
            System.out.println(usersInfo);
            mv.addObject("usersInfo",usersInfo);
            mv.setViewName("table");  
            return mv;
    }
}
