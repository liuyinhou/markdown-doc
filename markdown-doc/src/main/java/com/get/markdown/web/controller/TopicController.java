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
import com.get.markdown.utils.Constants;

@Controller
@RequestMapping(value="/topic")
public class TopicController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TopicService topicService;
	
//	@RequestMapping(value = "/topicIndex")
//	public String topicIndex(Model model) {
//		String result = null;
//		try {
//			result = topicService.topicIndex();
//		} catch (Exception e) {
//			logger.error("", e);
//			result = Constants.MARKDOWN_SYSTEM_ERROR;
//		}
//		model.addAttribute("topMenu", "topicIndex");
//		model.addAttribute("result", result);
//		return "topic";
//	}
	
	@RequestMapping(value = "/topicList")
	public String topicList(Model model) {
		model.addAttribute("topMenu", "topicList");
		return "topicList";
	}
	
	@RequestMapping(value = "/getTopicList", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "/addTopic", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse addTopic(@RequestParam(required=false) String name, 
			@RequestParam(required=false) String uri) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = topicService.addTopic(name, uri);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/getTopicContentById", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "/getTopic", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "/addTopicContent", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse addTopicContent(@RequestParam(required=false) String contentMarkdown, 
			@RequestParam(required=false) String remark, 
			@RequestParam(required=false) Integer preId) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = topicService.addTopicContent(contentMarkdown, remark, preId);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	@RequestMapping(value = "/editTopic", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse editTopic(@RequestParam(required=false) Integer id, 
			@RequestParam(required=false) String name, @RequestParam(required=false) String uri) {
		JsonResponse jr = new JsonResponse();
		try {
			jr = topicService.editTopic(id, name, uri);
		} catch (Exception e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}

}
