package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class GradeVendas extends Entidade{

    private int id ;
    private String descricao ;
    private int status ;
    private String imagem ;

    public GradeVendas(JSONObject j) throws JSONException {
        id = j.getInt("id") ;
        descricao = j.getString("DESCRICAO") ;
        status = j.getInt("STATUS");
        imagem = j.getString("IMAGEM");
    }

    public GradeVendas(int id, String descricao, int status, String imagem) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.imagem = imagem ;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("DESCRICAO", descricao) ;
        j.put("IMAGEM", imagem) ;
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }


}
