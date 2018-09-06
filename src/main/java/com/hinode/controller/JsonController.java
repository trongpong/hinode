package com.hinode.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hinode.entity.House;
import com.hinode.entity.Image;
import com.hinode.service.HouseService;

@RestController
public class JsonController {
	
	@Autowired
	private HouseService houseService;
	
	@GetMapping("/getImage")
	public List<Image> getImage(int houseId) {
		return houseService.findAllImageByHouseId(houseId);
	}
	
	@GetMapping("/findOne")
	public House findOne(int id) {
		return houseService.getById(id);
	}
}
