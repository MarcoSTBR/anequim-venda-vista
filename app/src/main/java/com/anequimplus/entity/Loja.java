package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Loja {
    private int id ;
    private String nome ;
    private int status ;

    public Loja(int id, String nome, int status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
    }

    public Loja(JSONObject j) throws JSONException {
       this.id = j.getInt("id") ;
       this.nome = j.getString("LOJA") ;
       this.status = j.getInt("STATUS") ;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
