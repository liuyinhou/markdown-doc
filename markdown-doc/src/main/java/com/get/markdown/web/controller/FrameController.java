package com.get.markdown.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/frame")
public class FrameController {

	@RequestMapping("")
	public String toMain(Model model) {
		model.addAttribute("topMenu", "");
		return "main";
	}
	
	
}
