package com.get.markdown.entity.enumeration;

public enum TopicContentStatusEnum {
    
    DEFAULT(0, "正常"),
    RECOVERABLE(1, "可被恢复的记录"),
    EXPIRE(2, "已过期"),
    DELETED(9, "已删除");

    private Integer code;
    private String message;

    private TopicContentStatusEnum(Integer code, String message) {
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
