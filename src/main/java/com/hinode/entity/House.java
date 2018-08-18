package com.hinode.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(schema="dbo", name="house")
public class House {
	@Id
	private String id;
}
