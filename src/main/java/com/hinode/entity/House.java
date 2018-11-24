package com.hinode.entity;

import java.time.LocalDate;

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
@Table(name="house")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name", length=50, nullable=true)
	private String name;
	
	@Column(name="address", length=100, nullable=true)
	private String address;
	
	@Column(name="room_type", nullable=true)
	private String roomType;
	
	@Column(name="rent_fee", nullable=true)
	private int rentFee;
	
	@Column(name="deposite_fee", nullable=true)
	private int depositeFee;
	
	@Column(name="guarantee_fee", nullable=true)
	private int guaranteeFee;
	
	@Column(name="manage_fee", nullable=true)
	private int manageFee = 0;
	
	@Column(name="area", nullable=true)
	private double area;
	
	@Column(name="build_from", nullable=true)
	private LocalDate buildFrom;
	
	@Column(name="movable_date", nullable=true)
	private LocalDate movableDate;
	
	@Column(name="status", nullable=true, length=20)
	private String status;
	
	@Column(name="other", nullable=true)
	private String other;
	
	@Column(name="station", nullable=true)
	private String station;
	
	@Column(name="person_in_charge", nullable=true)
	private String personInCharge;
	
	@Column(name="staff_contact", nullable=true)
	private String staffContact;
	
	@Column(name="del_flg", length = 1)
	private String delFlg;

	public House() {
		buildFrom = LocalDate.now();
	}
	
	
}
