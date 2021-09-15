package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Caixa {
   private int id ;
   private Date data ;
   private Date dataFechamento ;
   private int usuario_Id ;
   private int status ;

    public Caixa(JSONObject j) throws JSONException, ParseException {
        //{"conf_terminal_id":"2","system_user_id":"7","data_movimento":"2021-03-22 23:09:00","valor":"1.5","modalidade_id":"1","status":1,"loja_id":"1","id":1}}
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.id = j.getInt("id");
        this.data = fdate.parse(j.getString("data_movimento"));
        this.dataFechamento = fdate.parse(j.getString("data_movimento"));
        this.usuario_Id = j.getInt("system_user_id");
        this.status = j.getInt("status");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public int getUsuario_Id() {
        return usuario_Id;
    }

    public void setUsuario_Id(int usuario_Id) {
        this.usuario_Id = usuario_Id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
