package com.anequimplus.entity;

public class UrlParam {

    private String url ;
    private String param ;

    public UrlParam(String url, String param) {
        this.url = url;
        this.param = param;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}

