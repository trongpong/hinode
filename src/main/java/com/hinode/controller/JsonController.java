package com.hinode.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hinode.entity.Client;
import com.hinode.entity.House;
import com.hinode.entity.Image;
import com.hinode.entity.Staff;
import com.hinode.service.ClientService;
import com.hinode.service.HouseService;
import com.hinode.service.StaffService;

@RestController
public class JsonController {
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping("/getImage")
	public List<Image> getImage(int houseId) {
		return houseService.findAllImageByHouseId(houseId);
	}
	
	@GetMapping("/findOne")
	public House findOne(int id) {
		return houseService.getById(id);
	}
	
	@GetMapping("/findOneStaff")
	public Staff findOneStaff(int id) {
		return staffService.getOne(id);
	}
	
	@GetMapping("/findOneClient")
	public Client findOneClient(int id) {
		return clientService.getOne(id);
	}
}
