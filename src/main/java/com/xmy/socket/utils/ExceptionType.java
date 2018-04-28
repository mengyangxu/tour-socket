package com.xmy.socket.utils;

/**
 * Enum 响应类型
 *
 * @date 16/08/01
 * @auther hua xu
 */
public enum ExceptionType {
    SUCCESS(10000, "success"),
    SYSTEM_ERROR(300001, "系统错误"),
    RULE_IS_NULL(300002, "系统错误"),
    RULE_IS_ERROR(300003, "规则语句错误");

    private int code;
    private String message;

    ExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}


