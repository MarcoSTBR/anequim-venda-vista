package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class SetorImp {
    private int id ;
    private String descricao ;
    private int status ;

    public SetorImp(JSONObject j) throws JSONException {
        if (j.isNull("id"))
            id = j.getInt("ID");
         else id = j.getInt("id");
        descricao = j.getString("DESCRICAO");
        status = j.getInt("STATUS");
    }

    public JSONObject getJSON() throws JSONException {
       JSONObject j = new JSONObject() ;
       j.put("ID", id) ;
       j.put("DESCRICAO", descricao) ;
       j.put("STATUS", status) ;
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
