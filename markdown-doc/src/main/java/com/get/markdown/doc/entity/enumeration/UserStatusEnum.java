package com.get.markdown.doc.entity.enumeration;

public enum UserStatusEnum {
    
    DEFAULT(0, "正常"),
    DISABLED(4, "禁用"),
    PROTECTED(8, "受保护"),
    DELETED(9, "已删除");

    private Integer code;
    private String message;

    private UserStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {
		for (UserStatusEnum e : UserStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e.getMessage();
			}
		}
		return null;
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
