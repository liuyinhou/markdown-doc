package com.get.markdown.doc.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 当前页码，从1开始
	 */
	private Integer pageNum = 1;

	/**
	 * 每页记录条数
	 */
	private Integer pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 总共有多少条记录
	 */
	private Integer total = 0;
	
	/**
	 * 总共有多少页
	 */
	private Integer pageCount = 0;
	
	/**
	 * 是否存在下一页
	 */
	private Boolean next;
	
	/**
	 * 用于页面上页码栏的展示
	 * <p>
	 * 最多5页，取当前页的前后5页
	 */
	private List<Integer> pageShow;

	/**
	 * 空构造函数
	 */
	public Page() {

	}
	
	public Page(Integer currPage, Integer pageSize) {
		this(currPage, pageSize, null);
	}

	public Page(Integer pageNum, Integer pageSize, Integer total) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.total = total;
	}

	public Integer getTotal() {
		if (total == null) {
			total = 0;
		}
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPageNum() {
		if (pageNum == null) {
			// 如果pageNum为null,默认为第一页
			pageNum = 1;
		}
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		if (pageSize == null) {
			pageSize = DEFAULT_PAGE_SIZE; // 默认值
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Integer getPageCount() {
		pageCount = getTotal()/getPageSize();
		if (getTotal()%getPageSize()>0) {
			pageCount ++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Boolean getNext() {
		if (next == null) {
			if (getPageNum() * getPageSize() < getTotal()) {
				next = Boolean.TRUE;
			} else {
				next = Boolean.FALSE;
			}
		}
		return next;
	}

	public void setNext(Boolean next) {
		this.next = next;
	}

	public List<Integer> getPageShow() {
		//前4后5
		pageShow = new ArrayList<Integer>();
		if (getPageNum() > (getPageCount()-5)) {
			//当后面不够5页 9 10
			int index = getPageCount()-9;
			for (int i=0;i<10;i++) {
				//不要负值和0
				if (index>0) {
					pageShow.add(index);
				}
				index++;
			}
		} else {
			int index = getPageNum()-4;
			while (pageShow.size() < 10) {
				//不要负值和0
				if (index<=0) {
					index++;
					continue;
				}
				//不能超过总页数
				if (index>getPageCount()) {
					break;
				}
				pageShow.add(index);
				index++;
			}
		}
		return pageShow;
	}

	public void setPageShow(List<Integer> pageShow) {
		this.pageShow = pageShow;
	}
	
}
