package com.get.markdown.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.get.markdown.service.TopicService;
import com.get.markdown.utils.Constants;

@Controller
@RequestMapping
public class MarkdownController extends BaseController {

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
		
		if (result.isEmpty()) {
			return "init";
		} else {
			model.addAttribute("topMenu", "markdown");
			model.addAttribute("result", result);
			return "topic";
		}
	}
	
	@RequestMapping(value="/markdown/**")
	public String markdown(Model model, HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			String uri = request.getRequestURI();
			if (uri.endsWith("/")) {
				uri.substring(0, uri.length()-1);
			}
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
	
}
