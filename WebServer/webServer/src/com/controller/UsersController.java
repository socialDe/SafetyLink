package com.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.UsersVO;

@Controller
public class UsersController {
	
	@Resource(name = "ubiz")
    Biz<String, UsersVO> ubiz;

}
