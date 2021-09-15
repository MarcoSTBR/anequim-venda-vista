package com.anequimplus.utilitarios;

import org.json.JSONException;
import org.json.JSONObject;

public class LinksParaAcesso {
    private String url ;
    private String param ;

    public LinksParaAcesso(JSONObject j) throws JSONException {
        url   = j.getString("URL") ;
        param = j.getString("PARAM") ;
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
