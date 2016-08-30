package com.get.markdown.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/frame")
public class FrameController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@RequestMapping("")
	public String toMain(Model model) {
		model.addAttribute("topMenu", "");
		return "main";
	}
	
	
}
