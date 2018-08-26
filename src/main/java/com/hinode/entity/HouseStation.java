package com.hinode.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hinode.enums.TransportType;

import lombok.Data;

@Entity
@Data
@Table(name="houseStation")
public class HouseStation implements Serializable{
	
	/**
	 * UID
	 */
	private static final long serialVersionUID = -3933455409349603345L;

	@Id
	@Column(name="houseId")
	private int houseId;
	
	@Id
	@Column(name="stationId")
	private int stationId;
	
	@Column(name="transportType")
	private TransportType transportType;
	
	@Column(name="transportTime")
	private int transportTime;
}
