package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Grupo {

    private int id ;
    private String descricao ;
    private int status ;
    private List<Produto> produtos ;

    public Grupo(int id, String descricao, int status, List<Produto> produtos)  {
        this.id = id ;
        this.descricao = descricao;
        this.status = status ;
        this.produtos = produtos ;
    }

    public Grupo(JSONObject j) throws JSONException {
        id = j.getInt("grupo_id");
        descricao = j.getString("grupo_desc");
        status = j.getInt("grupo_status");
        produtos = new ArrayList<Produto>() ;
        for (int i = 0 ; i < j.getJSONArray("produtos").length() ; i++) {
            produtos.add(new Produto(j.getJSONArray("produtos").getJSONObject(i))) ;
        }
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

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
