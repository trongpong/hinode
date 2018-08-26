package com.hinode.dto;

import lombok.Data;

@Data
public class HouseSearchCondition {
	
	private String meisho;
	
	private String jusho;
	
	private String type;
	
	private int chinryoFrom;
	
	private int chinryoTo;
	
	private int henkoryoFrom;
	
	private int henkoryoTo;
	
	private int shikikinFrom;
	
	private int shikikinTo;
}
