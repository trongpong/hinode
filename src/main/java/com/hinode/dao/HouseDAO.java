package com.hinode.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class HouseDAO implements IHouseDAO{
	
	@PersistenceContext
	EntityManager entityManager;
	
}
