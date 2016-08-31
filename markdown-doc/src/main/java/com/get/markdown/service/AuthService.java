package com.get.markdown.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.get.markdown.dao.UserDao;
import com.get.markdown.entity.enumeration.ResultCodeEnum;
import com.get.markdown.entity.enumeration.UserStatusEnum;
import com.get.markdown.entity.po.User;
import com.get.markdown.entity.vo.JsonResponse;
import com.get.markdown.utils.MD5Encrypt;

@Service
public class AuthService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDao userDao;
	
	public JsonResponse login(String name, String passwd) {
		JsonResponse jr = new JsonResponse();
		logger.debug("用户登录:name={}, passwd={}", name, passwd);
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("name", name);
		query.put("status !=", UserStatusEnum.DELETED.getCode());
		List<User> userList = userDao.find(query, null);
		if (userList == null
				|| userList.isEmpty()) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("用户名或密码错误!");
			return jr;
		}
		User user = userList.get(0);
		//加密3次
		String md5 = MD5Encrypt.encrypt(MD5Encrypt.encrypt(MD5Encrypt.encrypt(passwd)));
		if (!md5.equals(user.getPasswd())) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("用户名或密码错误!");
			return jr;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userId", user.getId());
		result.put("userName", user.getName());
		result.put("token", MD5Encrypt.encrypt(user.getId()+user.getName()));
		jr.setData(result);
		return jr;
	}
	
}
