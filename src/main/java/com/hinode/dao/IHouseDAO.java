package com.hinode.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hinode.entity.House;

@Repository
public interface IHouseDAO extends JpaRepository<House, String> {
	
	@Query(nativeQuery=true, value="SELECT TOP 6 FROM house WHERE delFlg" )
	public List<House> fetchTop6();
}
