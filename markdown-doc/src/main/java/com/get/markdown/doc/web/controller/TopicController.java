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

@Controller
@RequestMapping(value="/topic")
public class TopicController extends BaseController {

	@Autowired
	private TopicService topicService;
	
	@RequestMapping(value = "/topicList")
	public String topicList(Model model) {
		model.addAttribute("topMenu", "topicList");
		return "topicList";
	}
	
	@RequestMapping(value = "/getTopicList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse getTopicList(@RequestParam(required=false) Integer pageNum, 
			@RequestParam(required=false) Integer pageSize) {
		logger.debug("topicList");
		JsonResponse jr = new JsonResponse();
		try {
			jr = topicService.getTopicList(pageNum, pageSize);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/addTopic", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse addTopic(HttpServletRequest request,
			@RequestParam(required=false) String name, 
			@RequestParam(required=false) String uri) {
		JsonResponse jr = new JsonResponse();
		try {
			Integer operatorId = getOperatorId(request);
			jr = topicService.addTopic(name, uri, operatorId);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/getTopicContentById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse getTopicContentById(@RequestParam(required=false) String action, 
			@RequestParam(required=false) Integer id) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = topicService.getTopicContentById(id, action);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/getTopic", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse getTopic(@RequestParam(required=false) Integer id) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = topicService.getTopic(id);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/addTopicContent", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse addTopicContent(HttpServletRequest request,
			@RequestParam(required=false) String contentMarkdown, 
			@RequestParam(required=false) String remark, 
			@RequestParam(required=false) Integer preId) {
		JsonResponse jr = new JsonResponse();
		try {
			Integer operatorId = getOperatorId(request);
			jr = topicService.addTopicContent(contentMarkdown, remark, preId, operatorId);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/editTopic", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse editTopic(HttpServletRequest request,
			@RequestParam(required=false) Integer id, 
			@RequestParam(required=false) String name, @RequestParam(required=false) String uri) {
		JsonResponse jr = new JsonResponse();
		try {
			Integer operatorId = getOperatorId(request);
			jr = topicService.editTopic(id, name, uri, operatorId);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/deleteTopic", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteTopic(@RequestParam(required=false) Integer id) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = topicService.deleteTopic(id);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}

}
