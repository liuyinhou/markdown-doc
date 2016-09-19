package com.get.markdown.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.get.markdown.dao.UserDao;
import com.get.markdown.entity.enumeration.ResultCodeEnum;
import com.get.markdown.entity.enumeration.TopicContentStatusEnum;
import com.get.markdown.entity.enumeration.UserAuthMenuEnum;
import com.get.markdown.entity.enumeration.UserStatusEnum;
import com.get.markdown.entity.po.User;
import com.get.markdown.entity.vo.JsonResponse;
import com.get.markdown.entity.vo.Page;
import com.get.markdown.utils.MD5Encrypt;

@Service
public class UserService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm";
	private SimpleDateFormat dateformat = new SimpleDateFormat(DEFAULT_FORMAT);
	
	@Autowired
	private UserDao userDao;
	
	public JsonResponse findById(String name, String passwd) {
		JsonResponse jr = new JsonResponse();
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
	
	public JsonResponse getUserList(Integer pageNum, Integer pageSize) {
		JsonResponse jr = new JsonResponse();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status !=", TopicContentStatusEnum.DELETED.getCode());
		List<User> list = userDao.findPage(pageNum, pageSize, params, "id asc");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (User user : list) {
			Map<String, Object> oneMap = new HashMap<String, Object>();
			result.add(oneMap);
			oneMap.put("id", user.getId());
			oneMap.put("name", user.getName());
			if (StringUtils.isEmpty(user.getAuthMenu())) {
				oneMap.put("authMenu", "");
			} else {
				List<String> authList = new ArrayList<String>();
				for (String oneMenu : user.getAuthMenu().split(",")) {
					UserAuthMenuEnum thisEnum = UserAuthMenuEnum.getEnumByKey(oneMenu);
					if (thisEnum != null) {
						authList.add(thisEnum.getMessage());
					}
				}
				if (!authList.isEmpty()) {
					oneMap.put("authMenu", StringUtils.collectionToDelimitedString(authList, ","));
				} else {
					oneMap.put("authMenu", "");
				}
			}
			
			oneMap.put("createTime", dateformat.format(user.getCreateTime()));
			oneMap.put("updateTime", dateformat.format(user.getUpdateTime()));
			oneMap.put("status", user.getStatus());
			oneMap.put("statusMessage", UserStatusEnum.getMessageByCode(user.getStatus()));
		}
		jr.setData(result);
		Page page = new Page(pageNum, pageSize);
		Integer count = userDao.count(params);
		page.setTotal(count);
		jr.setPage(page);
		return jr;
	}
	
	public JsonResponse addUser(String name, String passwd, String menu, Integer status) {
		JsonResponse jr = new JsonResponse();
		logger.debug("创建用户:name={}, menu={}", name, menu);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("status !=", UserStatusEnum.DELETED.getCode());
		List<User> userList = userDao.find(params, null);
		if (!userList.isEmpty()) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("保存失败，用户名已经存在！");
			return jr;
		}
		User user = new User();
		user.setName(name);
		if (!StringUtils.isEmpty(menu)) {
			List<String> authList = new ArrayList<String>();
			for (String oneMenu : menu.split(",")) {
				if (UserAuthMenuEnum.getEnumByKey(oneMenu) != null) {
					authList.add(oneMenu);
				}
			}
			if (!authList.isEmpty()) {
				user.setAuthMenu(StringUtils.collectionToDelimitedString(authList, ","));
			}
		}
		String md5 = MD5Encrypt.encrypt(MD5Encrypt.encrypt(MD5Encrypt.encrypt(passwd)));
		user.setPasswd(md5);
		user.setStatus(status);
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		userDao.save(user);
		return jr;
	}
	
	public JsonResponse getUser(Integer id) {
		JsonResponse jr = new JsonResponse();
		User user = userDao.findById(id);
		if (user == null) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("未知的内容ID");
			return jr;
		}
		jr.setData(user);
		return jr;
	}
	
	public JsonResponse editUser(Integer id, String menu, Integer status) {
		JsonResponse jr = new JsonResponse();
		User user = userDao.findById(id);
		if (user == null) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("未知的内容ID");
			return jr;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		if (!StringUtils.isEmpty(menu)) {
			List<String> authList = new ArrayList<String>();
			for (String oneMenu : menu.split(",")) {
				if (UserAuthMenuEnum.getEnumByKey(oneMenu) != null) {
					authList.add(oneMenu);
				}
			}
			if (!authList.isEmpty()) {
				params.put("auth_menu", StringUtils.collectionToDelimitedString(authList, ","));
			}
		}
		params.put("update_time", new Date());
		userDao.update(id, params);
		return jr;
	}
	
	public JsonResponse deleteUser(Integer id) {
		JsonResponse jr = new JsonResponse();
		User user = userDao.findById(id);
		if (user == null) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("未知的内容ID");
			return jr;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", UserStatusEnum.DELETED.getCode());
		params.put("update_time", new Date());
		userDao.update(id, params);
		return jr;
	}
	
	public JsonResponse changePasswd(Integer operatorId, String oldPasswd, String newPasswd, String repeatPasswd) {
		JsonResponse jr = new JsonResponse();
		User user = userDao.findById(operatorId);
		if (user == null) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("未知的内容ID");
			return jr;
		}
		String oldPdMd5 = MD5Encrypt.encrypt(MD5Encrypt.encrypt(MD5Encrypt.encrypt(oldPasswd)));
		if (!oldPdMd5.equals(user.getPasswd())) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("用户密码错误");
			return jr;
		}
		if (!newPasswd.equals(repeatPasswd)) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("新密码输入不一致");
			return jr;
		}
		String newPdMd5 = MD5Encrypt.encrypt(MD5Encrypt.encrypt(MD5Encrypt.encrypt(newPasswd)));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("passwd", newPdMd5);
		params.put("update_time", new Date());
		userDao.update(user.getId(), params);
		return jr;
	}
	
}
