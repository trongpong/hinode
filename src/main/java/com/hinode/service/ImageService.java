package com.hinode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hinode.dao.IImageDAO;
import com.hinode.entity.Image;

@Service
public class ImageService {
	@Autowired
	private IImageDAO imageDAO;
	
	public List<Image> findSlider() {
		return imageDAO.findSliderImage();
	}
	
	public void delete(int id) {
		imageDAO.deleteById(id);
	}
}
