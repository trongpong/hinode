package com.hinode.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="house")
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="meisho", length=50, nullable=true)
	private String meisho;
	
	@Column(name="jusho", length=100, nullable=true)
	private String jusho;
	
	@Column(name="type", nullable=true)
	private String type;
	
	@Column(name="chinryo", nullable=true)
	private int chinryo;
	
	@Column(name="henkoryo", nullable=true)
	private int henkoryo;
	
	@Column(name="shikikin", nullable=true)
	private int shikikin;
	
	@Column(name="reikin", nullable=true)
	private int reikin;
	
	@Column(name="menseki", nullable=true)
	private double menseki;
	
	@Column(name="delFlg", length = 1)
	private String delFlg;
	
	@ManyToMany(cascade= {CascadeType.ALL})
	@JoinTable(
		name = "houseStation",
		joinColumns = {@JoinColumn(name="houseId")},
		inverseJoinColumns = {@JoinColumn(name="stationId")}
	)
	Set<RailwayStation> stationList = new HashSet<>();
}
