package com.hinode.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hinode.entity.House;

@Repository
public interface IHouseDAO extends JpaRepository<House, String> {
	
	@Query(nativeQuery=true, value="SELECT * FROM house WHERE del_Flg = '0' ORDER BY id DESC LIMIT 6" )
	public List<House> fetchTop6();
	
	@Query(nativeQuery=true, value="SELECT * FROM house WHERE del_Flg = '0' ORDER BY id DESC LIMIT 10" )
	public List<House> fetchLast10();
	
	@Query(nativeQuery=true, value="SELECT * FROM house WHERE id =  ?1")
	public House getById(int id);
}
