package com.get.markdown.doc.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Integer getOperatorId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
        String userId = null;
        if (cookies != null) {
        	for(Cookie cookie : cookies) {
            	if ("userId".equals(cookie.getName())) {
            		userId = cookie.getValue();
            	}
            }
        }
        if (userId != null) {
        	return Integer.valueOf(userId);
        } else {
        	return null;
        }
	}

}
