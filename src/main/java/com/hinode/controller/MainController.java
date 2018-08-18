package com.hinode.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping
	public String init(Map<Object, String> model) {
		
		return "index";
	}
}
