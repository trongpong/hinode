package com.hinode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hinode.dao.IHouseDAO;

@Service
public class HouseService implements IHouseService{

	@Autowired
	private IHouseDAO houseDAO;
	
}
