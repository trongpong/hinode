package com.hinode.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@GetMapping({"/", "/index"})
	public String init(Map<Object, String> model) {
				return "public/index";
	}
	
	@RequestMapping("/listings")
	public String list(Map<Object, String> model) {
				return "public/listings";
	}
}
