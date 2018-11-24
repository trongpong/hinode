package com.hinode.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hinode.entity.Image;

@Repository
public interface IImageDAO extends JpaRepository<Image, Integer> {
	
	public List<Image> findAllByHouseId(int houseId);
	
	@Query(nativeQuery=true, value="SELECT * FROM image WHERE house_id = '0'" )
	public List<Image> findSliderImage();
	
}
