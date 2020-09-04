package com.hinode.entity;

import lombok.Data;

@Data
public class TabArea {
	String tabName;
	String idTab;

	public TabArea(String tabName, String idTab) {
		super();
		this.tabName = tabName;
		this.idTab = idTab;
	}
}
