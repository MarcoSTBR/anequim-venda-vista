package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class VendaVistaPagamento {
    private int id ;
    private VendaVista vendaVista ;
    private Modalidade modalidade ;
    private double valor ;
    private int status ;

    public VendaVistaPagamento(int id, VendaVista vendaVista, Modalidade modalidade, double valor, int status) {
        this.id = id;
        this.vendaVista = vendaVista;
        this.modalidade = modalidade;
        this.valor = valor;
        this.status = status ;
    }

    public VendaVistaPagamento(VendaVista vendaVista, Modalidade modalidade, JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.vendaVista = vendaVista;
        this.modalidade = modalidade;
        this.valor = j.getDouble("VALOR");
        this.status = j.getInt("STATUS") ;

    }

    public JSONObject toJson() throws JSONException {
        JSONObject j = new JSONObject() ;
      //  j.put("ID", id) ;
      //  j.put("VENDA_VISTA_ID", vendaVista.getId()) ;
        j.put("MODALIDADE_ID", modalidade.getId()) ;
        j.put("VALOR", valor) ;
        j.put("STATUS", status) ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VendaVista getVendaVista() {
        return vendaVista;
    }

    public void setVendaVista(VendaVista vendaVista) {
        this.vendaVista = vendaVista;
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
