package com.get.markdown.core.vo;

import java.util.HashMap;
import java.util.Map;

public class UlListVo {

	/**
	 * 存储历史上空格对应级别
	 */
	private Map<Integer, Integer> spaceLevel;
	
	/**
	 * 存储上一行的级别
	 */
	private Integer preLevel;
	
	/**
	 * 存储上一行空格数
	 */
	private Integer preSpace;

	public UlListVo() {
		spaceLevel = new HashMap<Integer, Integer>();
		preLevel = -1;
		preSpace = -1;
	}
	
	public Map<Integer, Integer> getSpaceLevel() {
		return spaceLevel;
	}

	public void setSpaceLevel(Map<Integer, Integer> spaceLevel) {
		this.spaceLevel = spaceLevel;
	}

	public Integer getPreLevel() {
		return preLevel;
	}

	public void setPreLevel(Integer preLevel) {
		this.preLevel = preLevel;
	}

	public Integer getPreSpace() {
		return preSpace;
	}

	public void setPreSpace(Integer preSpace) {
		this.preSpace = preSpace;
	}
	
}
