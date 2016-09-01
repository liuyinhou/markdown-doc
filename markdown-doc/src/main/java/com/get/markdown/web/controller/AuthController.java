package com.get.markdown.web.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.get.markdown.entity.enumeration.ResultCodeEnum;
import com.get.markdown.entity.vo.JsonResponse;
import com.get.markdown.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping("/login")
	public String toLogin() {
		return "login";
	}
	
	/**
	 * 用户登录
	 * @return
	 */
	@RequestMapping(value="/doLogin", method=RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse doLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam("userName") String userName,
			@RequestParam("pwd") String pwd) {
		JsonResponse jr = new JsonResponse();
    	try {
    		jr = authService.login(userName, pwd);
    		if (ResultCodeEnum.SUCCESS.getCode().equals(jr.getCode())) {
    			Map<String, Object> result = (Map<String, Object>)jr.getData();
        		Cookie token = new Cookie("token", result.get("token").toString());
                token.setPath("/");
                response.addCookie(token);
                Cookie userId = new Cookie("userId", result.get("userId").toString());
                userId.setPath("/");
                response.addCookie(userId);
                Cookie userNameCo = new Cookie("userName", result.get("userName").toString());
                userNameCo.setPath("/");
                response.addCookie(userNameCo);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("登录异常", e);
    		jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
    	}
    	return jr;
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie token = new Cookie("token", null);
        token.setPath("/");
        response.addCookie(token);
        Cookie userId = new Cookie("userId", null);
        userId.setPath("/");
        response.addCookie(userId);
        Cookie userName = new Cookie("userName", null);
        userName.setPath("/");
        response.addCookie(userName);
		return "login";
	}
	
	@RequestMapping(value="/init")
	public String init() {
		authService.init();
    	return "login";
	}
	
	
}
