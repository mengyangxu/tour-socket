
package com.xmy.socket.utils;




/**
 * 业务异常
 *
 * @author zhangbin
 * @since 1.0.0
 */
public class BusinessException extends RuntimeException {

    private int code;

    private String message;

    public BusinessException(ExceptionType exceptionType) {
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
