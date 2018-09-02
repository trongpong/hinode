package com.hinode.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="house")
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name", length=50, nullable=true)
	private String name;
	
	@Column(name="address", length=100, nullable=true)
	private String address;
	
	@Column(name="house_type", nullable=true)
	private String houseType;
	
	@Column(name="room_type", nullable=true)
	private String roomType;
	
	@Column(name="rent_fee", nullable=true)
	private int rentFee;
	
	@Column(name="deposite_fee", nullable=true)
	private int depositeFee;
	
	@Column(name="guarantee_fee", nullable=true)
	private int guaranteeFee;
	
	@Column(name="area", nullable=true)
	private double area;
	
	@Column(name="contract_duration", nullable=true)
	private int contractDuration;
	
	@Column(name="build_from", nullable=true)
	private Date buildFrom;
	
	@Column(name="movable_date", nullable=true)
	private Date movableDate;
	
	@Column(name="status", nullable=true, length=20)
	private String status;
	
	@Column(name="other", nullable=true)
	private String other;
	
	@Column(name="del_flg", length = 1)
	private String delFlg;
}
