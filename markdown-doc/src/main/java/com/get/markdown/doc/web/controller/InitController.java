package com.get.markdown.doc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.get.markdown.doc.entity.enumeration.ResultCodeEnum;
import com.get.markdown.doc.entity.vo.JsonResponse;
import com.get.markdown.doc.service.InitService;

@Controller
@RequestMapping("init")
public class InitController extends BaseController {

	@Autowired
	private InitService initService;
	
	@RequestMapping(value="")
	public String initPage(Model model) {
		if (initService.checkNeedInit()) {
			return "init";
		} else {
			return "login";
		}
	}
	
	@RequestMapping(value="/init")
	@ResponseBody
	public JsonResponse init(@RequestParam(required=false) String adminName, 
			@RequestParam(required=false) String passwd) {
		JsonResponse jr = null;
		try {
			jr = initService.initDoc(adminName, passwd);
		} catch (Exception e) {
			logger.error("", e);
		}
		return jr;
	}
	
}
