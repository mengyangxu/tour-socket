package com.xmy.socket.utils;


/**
 * 业务异常类json
 *
 * @author zhangbin
 * @since 1.0.0
 */
public class JsonResponse {
    private int code;
    private String message;
    private Object data;

    public JsonResponse(Exception exception) {
        if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            this.code = businessException.getCode();
            this.message = businessException.getMessage();
        } else {
            this.code = 9999;
            this.message = "系统异常";
        }
    }

    public JsonResponse(Object data) {
        this.code = 200;
        this.message = "success";
        this.data = data;
    }

    public JsonResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

}
