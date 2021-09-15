package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Grupo {

    private int id ;
    private String descricao ;
    private int status ;

    public Grupo(int id, String descricao, int status)  {
        this.id = id ;
        this.descricao = descricao;
        this.status = status ;
    }

    public Grupo(JSONObject j) throws JSONException {
        id = j.getInt("grupo_id");
        descricao = j.getString("grupo_desc");
        status = j.getInt("grupo_status");
    }

    public JSONObject toJson() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("id",id) ;
        j.put("descricao",descricao) ;
        j.put("status",status) ;
        return j ;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
