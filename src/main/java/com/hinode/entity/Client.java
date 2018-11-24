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
@Table(name="client")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cid;
	
	@Column(name="cname")
	private String cName;
	
	@Column(name="caddress")
	private String cAddress;
	
	@Column(name="ccomment")
	private String cComment;
}
