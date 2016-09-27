package com.get.markdown.core.vo;

import java.util.HashMap;
import java.util.Map;


/**
 * 单行处理
 * @author liuyinhou
 *
 */
public class LineHolder {

	/**
	 * current line
	 */
	private String line;
	
	/**
	 * previous line
	 */
	private String preLine;
	
	private UlListVo ulListVo;
	
	private Boolean isInPline;
	
	private Boolean isInOlList = false;
	
	private Boolean isInCode = false;
	
	private BlockQuoteVo blockQuoteVo;
	
	private TableVo tableVo;
	
	private Map<String, String> linksReference = new HashMap<String, String>();
	
	public LineHolder() {
		
	}
	
	public LineHolder(String line) {
		this.line = line;
	}
	
	public LineHolder(String line, LineHolder preLineHolder) {
		this.line = line;
		if (preLineHolder != null) {
			this.preLine = preLineHolder.getLine();
			this.ulListVo = preLineHolder.getUlListVo();
			this.isInOlList = preLineHolder.getIsInOlList();
			this.isInCode = preLineHolder.getIsInCode();
			this.blockQuoteVo = preLineHolder.getBlockQuoteVo();
			this.tableVo = preLineHolder.getTableVo();
			this.linksReference = preLineHolder.getLinksReference();
			this.isInPline = preLineHolder.getIsInPline();
		}
	}
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public UlListVo getUlListVo() {
		return ulListVo;
	}

	public void setUlListVo(UlListVo ulListVo) {
		this.ulListVo = ulListVo;
	}

	public Boolean getIsInCode() {
		return isInCode;
	}

	public void setIsInCode(Boolean isInCode) {
		this.isInCode = isInCode;
	}

	public BlockQuoteVo getBlockQuoteVo() {
		return blockQuoteVo;
	}

	public void setBlockQuoteVo(BlockQuoteVo blockQuoteVo) {
		this.blockQuoteVo = blockQuoteVo;
	}

	public TableVo getTableVo() {
		return tableVo;
	}

	public void setTableVo(TableVo tableVo) {
		this.tableVo = tableVo;
	}

	public String getPreLine() {
		return preLine;
	}

	public void setPreLine(String preLine) {
		this.preLine = preLine;
	}

	public Boolean getIsInOlList() {
		return isInOlList;
	}

	public void setIsInOlList(Boolean isInOlList) {
		this.isInOlList = isInOlList;
	}

	public Map<String, String> getLinksReference() {
		return linksReference;
	}

	public void setLinksReference(Map<String, String> linksReference) {
		this.linksReference = linksReference;
	}

	public Boolean getIsInPline() {
		return isInPline;
	}

	public void setIsInPline(Boolean isInPline) {
		this.isInPline = isInPline;
	}
	
}
