package com.hinode.dao;

import com.hinode.entity.New;
import com.hinode.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INewsDao extends JpaRepository<New,Integer> {

    @Query(value = "select * from news n where n.id > 0 Order By n.updated_at DESC", nativeQuery = true )
    List<New> getNewtUpdatedAt();
}
