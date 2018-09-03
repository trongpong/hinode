package com.hinode.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hinode.entity.House;
import com.hinode.service.HouseService;

@Controller
public class MainController {

	@Autowired
	private HouseService houseService;

	@GetMapping({ "/", "/index" })
	public String init(Map<String, Object> model) {
		
		// Get top 6 new house
		model.put("houseList", houseService.findTopNewHouse());
		
		return "public/index";
	}

	@RequestMapping("/listings")
	public String list(Map<String, Object> model) {
		// Get top 6 new house
		model.put("houseList", houseService.findTopNewHouse());
		return "public/listings";
	}
	
	@RequestMapping("/single")
	public String single(Map<String, Object> model) {
		return "public/single-listings";
	}
	
	@GetMapping("/admin")
	public String admin(Map<String, Object> model) {
		// Get top 10 new house
		model.put("houseList", houseService.findTop10House());
		return "admin/index";
	}
	
	@GetMapping("/page")
	public String page(Map<String, Object> model) {
		return "admin/pages";
	}
	
	@GetMapping("/ainput")
	public String ainput(Map<String, Object> model) {
		model.put("house", new House());
		return "public/admin";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute House house) {

		houseService.add(house);
		return "redirect:/admin";
	}

}
