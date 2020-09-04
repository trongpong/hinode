package com.hinode.service;

import com.hinode.dao.IUrlMasterDAO;
import com.hinode.entity.UrlMaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UrlMasterServcie {
	@Autowired
	private IUrlMasterDAO iUrlMasterDAO;

	public List<UrlMaster> getAllUrlMaster() {
		return iUrlMasterDAO.findAll();
	}
}
