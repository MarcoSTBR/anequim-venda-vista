package com.anequimplus.entity;

public class PagamentoConta {

    private int id ;
    private Modalidade modalidade ;
    private double valor ;

    public PagamentoConta(int id, Modalidade modalidade, double valor) {
        this.id = id;
        this.modalidade = modalidade;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
