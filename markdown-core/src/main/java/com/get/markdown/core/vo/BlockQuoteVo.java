package com.get.markdown.core.vo;


public class BlockQuoteVo {

	
	private Boolean isInBlockQuote = false;
	
	/**
	 * 存储上面的空白行数
	 */
	private Integer preBlank = 0;
	
	public BlockQuoteVo() {
		
	}

	public Boolean getIsInBlockQuote() {
		return isInBlockQuote;
	}

	public void setIsInBlockQuote(Boolean isInBlockQuote) {
		this.isInBlockQuote = isInBlockQuote;
	}

	public Integer getPreBlank() {
		return preBlank;
	}

	public void setPreBlank(Integer preBlank) {
		this.preBlank = preBlank;
	}
	
}
