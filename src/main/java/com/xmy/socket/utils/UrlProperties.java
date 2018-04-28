package com.xmy.socket.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by xmy on 2018/4/6.
 */
@ConfigurationProperties(prefix = "com.xmy.properties")
public class UrlProperties {
    private String indexUrl;
    private String picUrl;

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
