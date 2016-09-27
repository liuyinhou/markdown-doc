package com.get.markdown.core.vo;

import java.util.ArrayList;
import java.util.List;


public class TableVo {

	
	private Boolean isInTable = false;
	
	private List<String> alignList = new ArrayList<String>();
	
	private List<String> headerList = new ArrayList<String>();

	public Boolean getIsInTable() {
		return isInTable;
	}

	public void setIsInTable(Boolean isInTable) {
		this.isInTable = isInTable;
	}

	public List<String> getAlignList() {
		return alignList;
	}

	public void setAlignList(List<String> alignList) {
		this.alignList = alignList;
	}

	public List<String> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<String> headerList) {
		this.headerList = headerList;
	}
	
}
