package com.get.markdown.doc.entity.vo;

import java.io.Serializable;

import com.get.markdown.doc.entity.enumeration.ResultCodeEnum;

public class JsonResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer code;

	private String message;

	private Object data;

	private Page page;

	public JsonResponse() {
		code = ResultCodeEnum.SUCCESS.getCode();
		message = ResultCodeEnum.SUCCESS.getMessage();
	}

	public JsonResponse(ResultCodeEnum resultCodeEnum) {
		code = resultCodeEnum.getCode();
		message = resultCodeEnum.getMessage();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
