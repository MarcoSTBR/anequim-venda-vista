package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfiguracaoImpressora extends Entidade{
    private int id ;
    private String config ;

    public ConfiguracaoImpressora(int id, String config) {
        this.id = id;
        this.config = config;
    }

    public ConfiguracaoImpressora(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.config = j.getString("CONFIG");
    }




    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("CONFIG", config) ;
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
