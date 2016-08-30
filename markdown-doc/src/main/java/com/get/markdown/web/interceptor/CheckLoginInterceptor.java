package com.get.markdown.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CheckLoginInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        boolean isLogin = false;
        logger.debug("in CheckLoginInterceptor");
        if (cookies != null) {
        	for(Cookie cookie : cookies) {
            	if ("token".equals(cookie.getName())) {
            		isLogin = checkLogin(cookie.getValue());
            		break;
            	}
            }
        }
        if (!isLogin) {
        	logger.warn("未登录的请求！");
			request.getRequestDispatcher("/login")
					.forward(request, response);
        }
		return isLogin;
	}

	private boolean checkLogin(String userToken) {
		
		return true;
	}
	
}