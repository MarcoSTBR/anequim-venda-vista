package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class GradeVendasItem extends Entidade{
    private int id ;
    private int gradeVendasId ;
    private int produto_id ;
    private int status ;
    private Produto produto ;

    public GradeVendasItem(int id, int gradeVendasId, int produto_id, int status, Produto produto) {
        this.id = id;
        this.gradeVendasId = gradeVendasId;
        this.produto_id = produto_id;
        this.status = status;
        this.produto = produto ;
    }

    public GradeVendasItem(JSONObject j) throws JSONException {
        id  = j.getInt("id") ;
        gradeVendasId = j.getInt("GRADE_VENDAS_ID") ;
        produto_id = j.getInt("PRODUTO_ID");
        status = j.getInt("STATUS") ;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("GRADE_VENDAS_ID", gradeVendasId) ;
        j.put("PRODUTO_ID", produto_id) ;
        j.put("STATUS", status) ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGradeVendasId() {
        return gradeVendasId;
    }

    public void setGradeVendasId(int gradeVendasId) {
        this.gradeVendasId = gradeVendasId;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

}
