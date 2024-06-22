package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Acompanhamento_Item extends Entidade{

    private int id ;
    private int acomp_id ;
    private int produto_id ;
    private String obs ;
    private int min ;
    private int max ;
    private Double preco ;
    private Double quantidade ;

    public Acompanhamento_Item(JSONObject j) throws JSONException {
        if (j.isNull("id"))
            id = j.getInt("ID") ;
        else id = j.getInt("id") ;
        acomp_id = j.getInt("AFOOD_ACOMP_ID") ;
        produto_id = j.getInt("AFOOD_PRODUTO_ID") ;
        obs = j.getString("OBS") ;
        min = j.getInt("MIN") ;
        max = j.getInt("MAX") ;
        preco = j.getDouble("PRECO") ;
    }

    public Acompanhamento_Item(int id, int acomp_id, int produto_id, String obs, int min, int max, Double preco) {
        this.id = id;
        this.acomp_id = acomp_id;
        this.produto_id = produto_id;
        this.obs = obs;
        this.min = min;
        this.max = max;
        this.preco = preco;
        this.quantidade = 0.0 ;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("AFOOD_ACOMP_ID", acomp_id) ;
        j.put("AFOOD_PRODUTO_ID", produto_id) ;
        j.put("OBS", obs) ;
        j.put("MIN", min) ;
        j.put("MAX", max) ;
        j.put("PRECO", preco) ;
       // j.put("QUANTIDADE", preco) ;
        return j;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAcomp_id() {
        return acomp_id;
    }

    public void setAcomp_id(int acomp_id) {
        this.acomp_id = acomp_id;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

}
