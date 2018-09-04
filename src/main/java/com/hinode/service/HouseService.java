package com.hinode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hinode.dao.IHouseDAO;
import com.hinode.dao.IImageDAO;
import com.hinode.dto.HouseSearchCondition;
import com.hinode.entity.House;
import com.hinode.entity.Image;

@Service
public class HouseService{

	@Autowired
	private IHouseDAO houseDAO;
	
	@Autowired
	private IImageDAO imageDAO;
	
	public void add(House house) {
		houseDAO.save(house);
	}
	
	public List<House> findByCondition(HouseSearchCondition condition) {
		
		return houseDAO.findByCondition(condition.getStation(), condition.getName(), condition.getAddress(), condition.getRoomType()
				, condition.getRentFeeFrom(), condition.getRentFeeTo(), condition.getDepositeFeeFrom()
				, condition.getDepositeFeeTo(), condition.getGuaranteeFeeFrom(), condition.getGuaranteeFeeTo()
				, condition.getAreaFrom(), condition.getAreaTo());
	}
	
	public List<House> findTopNewHouse() {
		return houseDAO.fetchTop6();
	}
	
	public List<House> findLast10House() {
		return houseDAO.fetchLast10();
	}
	
	public House getById(int id) {
		return houseDAO.getOne(id);
	}
	
	public List<Image> findAllImageByHouseId(int houseId) {
		return imageDAO.findAllByHouseId(houseId);
	}
	
	public List<House> findAll() {
		return houseDAO.findAll();
	}
	
	public void delete(int id) {
		houseDAO.delete(this.getById(id));
	}
}
