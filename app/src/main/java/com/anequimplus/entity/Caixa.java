package com.anequimplus.entity;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Caixa {
   private int id ;
   private String uuid ;
   private Date data ;
   private Date dataFechamento ;
   private int usuario_Id ;
   private int status ;
   private double valor ;


    public Caixa(JSONObject j) throws JSONException, ParseException {
        //{"conf_terminal_id":"2","system_user_id":"7","data_movimento":"2021-03-22 23:09:00","valor":"1.5","modalidade_id":"1","status":1,"loja_id":"1","id":1}}
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.id = j.getInt("CAIXA_ID");
        this.uuid = j.getString("UUID");
        this.data = fdate.parse(j.getString("DATA"));
        this.dataFechamento = fdate.parse(j.getString("DATA_FECHAMENTO"));
        this.usuario_Id = j.getInt("SYSTEM_USER_ID");
        this.status = j.getInt("STATUS");
        this.valor  = j.getDouble("VALOR");
    }

    public Caixa(int id, String uuid, Date data, Date dataFechamento, int usuario_Id, int status, double valor) {
        this.id = id;
        this.uuid = uuid;
        this.data = data;
        this.dataFechamento = dataFechamento;
        this.usuario_Id = usuario_Id;
        this.status = status;
        this.valor = valor;
    }

    public JSONObject getJson()  {
        JSONObject j = new JSONObject() ;
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            j.put("CAIXA_ID", id) ;
            j.put("UUID", uuid) ;
            j.put("DATA", fdate.format(data)) ;
            j.put("DATA_FECHAMENTO", fdate.format(dataFechamento)) ;
            j.put("SYSTEM_USER_ID", usuario_Id) ;
            j.put("STATUS", status) ;
            j.put("VALOR", valor) ;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("caixa_json", "except "+j.toString()) ;

        }
      //  Log.i("caixa_json", j.toString()) ;
        return j;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getUsuario_Id() {
        return usuario_Id;
    }

    public void setUsuario_Id(int usuario_Id) {
        this.usuario_Id = usuario_Id;
    }


}
