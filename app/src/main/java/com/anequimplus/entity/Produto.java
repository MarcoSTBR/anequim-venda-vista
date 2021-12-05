package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Produto {

    private int id ;
    private String codBarra ;
    private String unidade ;
    private String descricao ;
    private String imagem ;
    private int status ;

    public Produto(int id, String codBarra, String unidade, String descricao, String imagem, int status) {
        this.id = id;
        this.codBarra = codBarra;
        this.unidade = unidade;
        this.descricao = descricao;
        this.imagem = imagem;
        this.status = status;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("CODBARRA", unidade) ;
        j.put("UNIDADE", unidade) ;
        j.put("DESCRICAO", descricao) ;
        j.put("IMAGEM", imagem) ;
        j.put("STATUS", status) ;
        return j ;
    }


    public Produto(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.codBarra = j.getString("CODBARRA");
        this.unidade = j.getString("UNIDADE");
        this.descricao = j.getString("DESCRICAO");
        this.imagem = j.getString("IMAGEM") ;
        this.status = j.getInt("STATUS")  ;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codBarra='" + codBarra + '\'' +
                ", unidade='" + unidade + '\'' +
                ", descricao='" + descricao + '\'' +
                ", imagem='" + imagem + '\'' +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
