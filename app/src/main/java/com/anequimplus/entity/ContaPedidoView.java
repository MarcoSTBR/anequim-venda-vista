package com.anequimplus.entity;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ContaPedidoView {
    private int id ;
    private String pedido ;
    private Date data ;
    private int Status ;
    private Double quantidade ;
    private Double valor ;

    public ContaPedidoView(JSONObject j) throws JSONException {
        id         = j.getInt("ID");
        pedido     = j.getString("PEDIDO");
        data       = UtilSet.getData(j.getString("DATA")) ;
        Status     = j.getInt("STATUS");
        quantidade = j.getDouble("QUANTIDADE");
        valor      = j.getDouble("VALOR");
    }

    public ContaPedidoView(int id, String pedido, Date data, int status, Double quantidade, Double valor) {
        this.id = id;
        this.pedido = pedido;
        this.data = data;
        Status = status;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
