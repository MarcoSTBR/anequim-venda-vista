package com.anequimplus.entity;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Suprimento extends Entidade {

    private int id ;
    private String descricao ;
    private Date data ;
    private int caixa_id ;
    private int modalidade_id ;
    private Double valor ;
    private String UUID ;
    private int status ;

    public Suprimento(int id, String descricao, Date data, int caixa_id, int modalidade_id, Double valor, String UUID, int status) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.caixa_id = caixa_id;
        this.modalidade_id = modalidade_id;
        this.valor = valor;
        this.UUID = UUID;
        this.status = status;
    }

    public Suprimento(JSONObject j) throws JSONException {
        if (j.isNull("id"))
           this.id = j.getInt("ID");
         else this.id = j.getInt("id");
        this.descricao = j.getString("DESCRICAO");
        this.data = UtilSet.getData(j.getString("DATA"));
        this.caixa_id = j.getInt("CAIXA_ID");
        this.modalidade_id = j.getInt("MODALIDADE_ID") ;
        this.valor = j.getDouble("VALOR");
        this.UUID = j.getString("UUID");
        this.status = j.getInt("STATUS");

    }

    @Override
    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("ID",id) ;
        j.put("UUID",UUID) ;
        j.put("DESCRICAO",descricao) ;
        j.put("DATA",df.format(data)) ;
        j.put("MODALIDADE_ID",modalidade_id) ;
        j.put("VALOR", valor) ;
        j.put("CAIXA_ID", caixa_id) ;
        j.put("STATUS",status) ;
        return j ;
    }

    public JSONObject getExportacaoJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("UUID",UUID) ;
        j.put("DESCRICAO",descricao) ;
        j.put("DATA",df.format(data)) ;
        j.put("MODALIDADE_ID",modalidade_id) ;
        j.put("VALOR", valor) ;
        j.put("CAIXA_ID", caixa_id) ;
        j.put("STATUS",status) ;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getCaixa_id() {
        return caixa_id;
    }

    public void setCaixa_id(int caixa_id) {
        this.caixa_id = caixa_id;
    }

    public int getModalidade_id() {
        return modalidade_id;
    }

    public void setModalidade_id(int modalidade_id) {
        this.modalidade_id = modalidade_id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
