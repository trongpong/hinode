package com.hinode.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hinode.entity.House;

@Repository
public interface IHouseDAO extends JpaRepository<House, Integer> {
	
	@Query(nativeQuery=true, value="SELECT * FROM house WHERE del_Flg = '0' ORDER BY id DESC LIMIT 6" )
	public List<House> fetchTop6();
	
	@Query(nativeQuery=true, value="SELECT * FROM house WHERE del_Flg = '0' ORDER BY id DESC LIMIT 10" )
	public List<House> fetchLast10();
	
	@Query(nativeQuery=true, value="SELECT * FROM house WHERE del_Flg = '0' "
			+ "AND station LIKE CONCAT('%',:station,'%') "
			+ "AND name LIKE CONCAT('%',:name,'%') "
			+ "AND address LIKE CONCAT('%',:address,'%') "
			+ "AND room_type LIKE CONCAT('%',:roomType,'%') "
			+ "AND rent_fee BETWEEN :rentFeeFrom AND :rentFeeTo "
			+ "AND deposite_fee BETWEEN :depositeFeeFrom AND :depositeFeeTo "
			+ "AND guarantee_fee BETWEEN :guaranteeFeeFrom AND :guaranteeFeeTo "
			+ "AND area BETWEEN :areaFrom AND :areaTo ")
	public List<House> findByCondition(@Param("station") String station, @Param("name") String name, @Param("address") String address
			, @Param("roomType") String roomType, @Param("rentFeeFrom") int rentFeeFrom, @Param("rentFeeTo") int rentFeeTo
			, @Param("depositeFeeFrom") int depositeFeeFrom, @Param("depositeFeeTo") int depositeFeeTo
			, @Param("guaranteeFeeFrom") int guaranteeFeeFrom, @Param("guaranteeFeeTo") int guaranteeFeeTo
			, @Param("areaFrom") double areaFrom, @Param("areaTo") double areaTo);
}
