package com.hinode.dao;

import com.hinode.entity.UrlMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUrlMasterDAO extends JpaRepository<UrlMaster, Integer> {}
