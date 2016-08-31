package com.get.markdown.entity.enumeration;


public enum UserAuthMenuEnum {
    
    EDIT_MARKDOWN("markdown", "编辑页面"),
    TOPIC_LIST("topicList", "页面管理"),
    USER_LIST("userList", "用户管理");

    private String key;
    private String message;

    private UserAuthMenuEnum(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public static String getMessageByKey(String key) {
		for (UserAuthMenuEnum e : UserAuthMenuEnum.values()) {
			if (e.getKey().equals(key)) {
				return e.getMessage();
			}
		}
		return null;
    }
    
    public static UserAuthMenuEnum getEnumByKey(String key) {
		for (UserAuthMenuEnum e : UserAuthMenuEnum.values()) {
			if (e.getKey().equals(key)) {
				return e;
			}
		}
		return null;
    }
    
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
