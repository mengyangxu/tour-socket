package com.xmy.socket.socket;

/**
 * @Description:
 * @Author: xumengyang
 * @Date: Created in 10:09 2018/4/10
 */
public class MessageDto {
    private String messageType;
    private String data;
    private String fromId;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
