package com.get.markdown.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/auth")
public class AuthController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/login")
	public String toLogin() {
		return "login";
	}
	
	/**
	 * 用户登录
	 * @return
	 */
	@RequestMapping(value="/doLogin", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam("userName") String userName,
			@RequestParam("pwd") String pwd) {
		Map<String, Object> jr = new HashMap<String, Object>();
    	try {
    		if("getadmin".equals(userName)
    				&& "getadmin2015".equals(pwd)){
    			Map<String, Object> result = new HashMap<String, Object>();
    			result.put("userId", "1");
    			result.put("token", "2");
    			jr.put("data", result);
    			jr.put("code", 200);
    		} else {
    			jr.put("code", 403);
    			jr.put("message", "用户名或密码错误!");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("登录异常", e);
    		jr.put("code", 500);
			jr.put("message", "系统异常!");
    	}
    	return jr;
	}
	
	
	
}
