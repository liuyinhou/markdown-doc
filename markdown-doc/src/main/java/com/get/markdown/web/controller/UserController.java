package com.get.markdown.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.get.markdown.entity.enumeration.ResultCodeEnum;
import com.get.markdown.entity.vo.JsonResponse;
import com.get.markdown.service.TopicService;
import com.get.markdown.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TopicService topicService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/userList")
	public String topicList(Model model) {
		model.addAttribute("topMenu", "userList");
		return "userList";
	}
	
	@RequestMapping(value = "/getUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse getUserList(@RequestParam(required=false) Integer pageNum, 
			@RequestParam(required=false) Integer pageSize) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = userService.getUserList(pageNum, pageSize);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse addUser(@RequestParam(required=false) String name, 
			@RequestParam(required=false) String passwd, 
			@RequestParam(required=false) String menu, 
			@RequestParam(required=false) Integer status) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = userService.addUser(name, passwd, menu, status);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/getUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse getTopic(@RequestParam(required=false) Integer id) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = userService.getUser(id);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteUser(@RequestParam(required=false) Integer id) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = userService.deleteUser(id);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/editUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse editUser(@RequestParam(required=false) Integer id, 
			@RequestParam(required=false) String menu, @RequestParam(required=false) Integer status) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = userService.editUser(id, menu, status);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}

}
