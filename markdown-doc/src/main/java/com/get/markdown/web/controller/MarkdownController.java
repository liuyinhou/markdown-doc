package com.get.markdown.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.get.markdown.service.TopicService;
import com.get.markdown.utils.Constants;

@Controller
@RequestMapping
public class MarkdownController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TopicService topicService;
	
	@RequestMapping(value="/markdown")
	public String markdownIndex(Model model) {
		Map<String, Object> result = null;
		try {
			result = topicService.getTopicContentByUri(Constants.MARKDOWN_INDEX_URI);
		} catch (Exception e) {
			logger.error("", e);
			result = new HashMap<String, Object>();
			result.put("contentHtml", Constants.MARKDOWN_SYSTEM_ERROR);
		}
		model.addAttribute("topMenu", "markdown");
		model.addAttribute("result", result);
		return "topic";
	}
	
	@RequestMapping(value="/markdown/**")
	public String markdown(Model model, HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			String uri = request.getRequestURI();
			result = topicService.getTopicContentByUri(uri);
		} catch (Exception e) {
			logger.error("", e);
			result = new HashMap<String, Object>();
			result.put("contentHtml", Constants.MARKDOWN_SYSTEM_ERROR);
		}
		model.addAttribute("topMenu", "markdown");
		model.addAttribute("result", result);
		return "topic";
	}
	
//	@RequestMapping(value="/markdown")
//	public String markdownIndex(Model model, HttpServletRequest request) throws Exception {
//		String path = request.getRequestURI();
//		logger.debug("request.getRequestURI()={}", path);
////		String result = markdownService.markdown(path);
//		String result = path;
//		model.addAttribute("markdown", result);
//		return "/markdown";
//	}
//	
//	@RequestMapping(value="/markdown/*")
//	public String markdown(Model model, HttpServletRequest request) throws Exception {
//		String path = request.getRequestURI();
//		logger.debug("request.getRequestURI()={}", path);
////		String result = markdownService.markdown(path);
//		String result = path;
//		model.addAttribute("markdown", result);
//		return "/markdown";
//	}
	
}
