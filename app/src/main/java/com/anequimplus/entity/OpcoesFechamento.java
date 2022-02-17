package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class OpcoesFechamento  {

    private int id ;
    private String descricao ;

    public OpcoesFechamento(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.descricao = j.getString("descricao");
    }

    public OpcoesFechamento(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
