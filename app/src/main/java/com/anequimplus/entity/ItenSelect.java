package com.anequimplus.entity;

import java.io.Serializable;

public class ItenSelect implements Serializable {

    private int id ;
    private Produto produto ;
    private double quantidade ;
    private double preco ;
    private double desconto ;
    private double valor ;
    private String obs ;

    public ItenSelect(int id, Produto produto, double quantidade, double preco, double desconto, double valor, String obs) {
        this.id = id ;
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
        this.desconto = desconto;
        this.valor = valor ;
        this.obs = obs ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObs() {
        if (obs == null) return " " ;
        else
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }


}
