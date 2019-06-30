package com.hinode.dao;

import com.hinode.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductDAO extends JpaRepository<Product, Integer> {

    @Query(value = "select * from product p where p.id > 0 Order By p.updated_at DESC", nativeQuery = true )
    List<Product> getProductUpdatedAt();
}
