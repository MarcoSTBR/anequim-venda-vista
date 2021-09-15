package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Produto {

    private int id ;
    private Grupo grupo ;
    private String codBarra ;
    private String unidade ;
    private String descricao ;
    private Double preco ;
    private int status ;

    public Produto(int id, Grupo grupo, String codBarra, String descricao, String unidade, Double preco, int status) {
        this.id = id;
        this.grupo = grupo;
        this.codBarra = codBarra;
        this.unidade = unidade;
        this.descricao = descricao;
        this.preco = preco;
        this.status = status ;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("GRUPO_ID", getGrupo().getId()) ;
        j.put("CODBARRA", unidade) ;
        j.put("UNIDADE", unidade) ;
        j.put("DESCRICAO", descricao) ;
        j.put("PRECO", preco) ;
        j.put("STATUS", status) ;
        return j ;
    }

    public Produto(Grupo grupo, JSONObject j) throws JSONException {
        this.id = j.getInt("produto_id");
        this.grupo = grupo;
        this.codBarra = j.getString("codbarra");
        this.unidade = j.getString("unidade");
        this.descricao = j.getString("descricao");;
        this.preco = j.getDouble("preco");;;
        this.status = j.getInt("produto_status") ;
    }

    public Produto(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.grupo = new Grupo(0,"TEMP",1);
        this.codBarra = j.getString("CODBARRA");
        this.unidade = j.getString("UNIDADE");
        this.descricao = j.getString("DESCRICAO");
        this.preco = 0.00 ;
        this.status = 1;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
