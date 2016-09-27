package com.get.markdown.doc.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.get.markdown.doc.utils.MD5Encrypt;

public class CheckLoginInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        boolean isLogin = false;
        String token = null;
        String userId = null;
        String userName = null;
        if (cookies != null) {
        	for(Cookie cookie : cookies) {
            	if ("token".equals(cookie.getName())) {
            		token = cookie.getValue();
            	} else if ("userId".equals(cookie.getName())) {
            		userId = cookie.getValue();
            	} else if ("userName".equals(cookie.getName())) {
            		userName = cookie.getValue();
            	}
            }
        }
        isLogin = checkLogin(token, userId, userName);
        if (!isLogin) {
        	logger.warn("未登录的请求！");
			request.getRequestDispatcher("/auth/login")
					.forward(request, response);
        }
		return isLogin;
	}

	private boolean checkLogin(String userToken, String userId, String userName) {
		if (StringUtils.isEmpty(userToken)
				|| StringUtils.isEmpty(userId)
				|| StringUtils.isEmpty(userName)) {
			return false;
		}
		String md5 = MD5Encrypt.encrypt(userId+userName);
		if (md5.equals(userToken)) {
			return true;
		}
		return false;
	}
	
}