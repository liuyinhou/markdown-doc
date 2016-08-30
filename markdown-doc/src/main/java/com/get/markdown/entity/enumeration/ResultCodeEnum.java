package com.get.markdown.entity.enumeration;

public enum ResultCodeEnum {
    
    SUCCESS(200, "成功"),
    //请求类异常
    PARAM_ERROR(400, "参数错误"),
    FORBIDDEN(403, "非法请求"),
    UNKNOWN_METHOD(404, "未知方法"),
    //业务处理类异常
    SYSTEM_ERROR(500,"未知异常"),
    BIZ_ERROR(506,"业务异常");
    //其他的具体业务异常code，建议使用510及其之后的值

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
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
