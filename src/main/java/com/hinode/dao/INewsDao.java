package com.hinode.dao;

import com.hinode.entity.New;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INewsDao extends JpaRepository<New,Integer> {

}
