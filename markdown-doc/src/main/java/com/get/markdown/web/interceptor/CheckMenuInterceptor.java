package com.get.markdown.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.get.markdown.dao.UserDao;
import com.get.markdown.entity.enumeration.UserStatusEnum;
import com.get.markdown.entity.po.User;

public class CheckMenuInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		Cookie[] cookies = request.getCookies();
        String userId = null;
        if (cookies != null) {
        	for(Cookie cookie : cookies) {
            	if ("userId".equals(cookie.getName())) {
            		userId = cookie.getValue();
            	}
            }
        }
        if (StringUtils.isEmpty(userId)) {
        	logger.warn("未登录的请求！");
			request.getRequestDispatcher("/auth/login")
					.forward(request, response);
        }
        
        User user = userDao.findById(Integer.valueOf(userId));
        if (user == null
        		|| UserStatusEnum.DELETED.getCode().equals(user.getStatus())) {
        	logger.warn("登录用户异常！");
			request.getRequestDispatcher("/auth/login")
					.forward(request, response);
        }
        if (!StringUtils.isEmpty(user.getAuthMenu())) {
        	String[] menus = user.getAuthMenu().split(",");
            for(String menu : menus) {
            	modelAndView.addObject("mn_" + menu, true);
            }
        }
	}
	
	

}