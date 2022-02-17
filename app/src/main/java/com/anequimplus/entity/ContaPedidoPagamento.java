package com.anequimplus.entity;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ContaPedidoPagamento {

    private int id ;
    private int contaPedido_id ;
    private String uuid ;
    private Date data ;
    private int caixa_id ;
    private Modalidade modalidade ;
    private double valor ;
    private int status ;

    public ContaPedidoPagamento(int id, int contaPedido_id, String uuid, Date data, int caixa_id, Modalidade modalidade, double valor, int status) {
        this.id = id;
        this.contaPedido_id = contaPedido_id ;
        this.uuid = uuid;
        this.data = data;
        this.caixa_id = caixa_id;
        this.modalidade = modalidade;
        this.valor = valor;
        this.status = status;

    }

    public ContaPedidoPagamento(int id, int contaPedido_id, Modalidade modalidade, double valor) {
        this.id = id ;
        this.contaPedido_id = contaPedido_id ;
        this.modalidade = modalidade ;
        this.valor = valor ;
    }

    public ContaPedidoPagamento(JSONObject j, Modalidade modalidade) throws JSONException {
        this.id = j.getInt("id") ;
        this.contaPedido_id = j.getInt("CONTA_PEDIDO_ID") ;
        this.uuid = j.getString("UUID");
        this.data = UtilSet.getData(j.getString("DATA"));
        this.caixa_id = j.getInt("CAIXA_ID");
        this.modalidade = modalidade;
        this.valor = j.getDouble("VALOR");
        this.status = j.getInt("STATUS");

    }


    public JSONObject getJSON() {
        JSONObject j = new JSONObject() ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContaPedido_id() {
        return contaPedido_id;
    }

    public void setContaPedido_id(int contaPedido_id) {
        this.contaPedido_id = contaPedido_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
