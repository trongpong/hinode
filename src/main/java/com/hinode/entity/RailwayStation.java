package com.hinode.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="railwayStation")
public class RailwayStation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="line")
	private String line;
	
	@Column(name="nameAlpha")
	private String nameAlpha;
	
	@Column(name="nameKana", nullable=true)
	private String nameKana;
	
	@Column(name="nameKanji")
	private String nameKanji;
	
	@ManyToMany(mappedBy="stationList")
	private Set<House> houseList = new HashSet<>();
}
