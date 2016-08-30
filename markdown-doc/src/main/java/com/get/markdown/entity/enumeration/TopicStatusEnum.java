package com.get.markdown.entity.enumeration;

public enum TopicStatusEnum {
    
    DEFAULT(0, "正常显示"),
    PROTECTED(8, "受保护的，不能删除"),
    DELETED(9, "已删除");

    private Integer code;
    private String message;

    private TopicStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
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

}
