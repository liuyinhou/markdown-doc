package com.get.markdown.doc.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.get.markdown.doc.entity.enumeration.ResultCodeEnum;
import com.get.markdown.doc.entity.vo.JsonResponse;
import com.get.markdown.doc.service.TopicService;
import com.get.markdown.doc.service.UserService;

@Controller
@RequestMapping(value="/userCenter")
public class UserCenterController extends BaseController {

	@Autowired
	private TopicService topicService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/user")
	public String topicList(Model model) {
//		model.addAttribute("topMenu", "userList");
		return "userCenter";
	}
	
	
	@RequestMapping(value = "/changePasswd", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse changePasswd(HttpServletRequest request,
			@RequestParam(required=false) String oldPasswd, 
			@RequestParam(required=false) String newPasswd, 
			@RequestParam(required=false) String repeatPasswd) {
		JsonResponse jr = new JsonResponse();
		try {
			Integer operatorId = getOperatorId(request);
			jr = userService.changePasswd(operatorId, oldPasswd, newPasswd, repeatPasswd);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}

}
