package com.hinode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Data
@Table(name="image")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Image{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="house_id")
	private int houseId;
	
	@Column(name="image_data")
	private byte[] imageData;
	
}
