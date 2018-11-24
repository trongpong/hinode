package com.hinode.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hinode.entity.Client;

public interface IClient extends JpaRepository<Client, Integer>{
	
}
