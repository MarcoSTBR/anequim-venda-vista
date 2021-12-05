package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Terminal {

    private int id ;
    private String cnpj ;
    private String mac ;
    private String descricao ;
    private int tipo_emissao ;
    private int status ;

    public Terminal(JSONObject j) throws JSONException {
        id = j.getInt("id") ;
        cnpj = j.getString("CNPJ") ;
        mac = j.getString("MAC") ;
        tipo_emissao = j.getInt("TIPO_EMISSAO") ;
        descricao = j.getString("DESCRICAO") ;
        status = j.getInt("STATUS") ;
    }

    public Terminal(int id, String cnpj, String mac, String descricao, int tipo_emissao, int status) {
        this.id = id;
        this.cnpj = cnpj;
        this.mac = mac;
        this.descricao = descricao;
        this.tipo_emissao = tipo_emissao;
        this.status = status;
    }

    public JSONObject getJson() throws JSONException {
        JSONObject j = new JSONObject();
        if (id != 0)
           j.put("id", id) ;
        j.put("CNPJ",cnpj) ;
        j.put("MAC",mac) ;
        j.put("DESCRICAO",descricao) ;
        j.put("TIPO_EMISSAO",tipo_emissao) ;
        j.put("STATUS",status) ;
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTipo_emissao() {
        return tipo_emissao;
    }

    public void setTipo_emissao(int tipo_emissao) {
        this.tipo_emissao = tipo_emissao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
