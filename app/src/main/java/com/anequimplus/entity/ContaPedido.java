package com.anequimplus.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ContaPedido implements Serializable {
    private int id ;
    private String pedido ;
    private Date data ;
    private double comissao ;
    private double desconto ;
    private List<ContaPedidoItem> listContaPedidoItem;
    private List<PagamentoConta> listPagamento;

    public ContaPedido(int id, String pedido, Date data, double comissao, double desconto, List<ContaPedidoItem> listContaPedidoItem, List<PagamentoConta> listPagamento) {
        this.id = id;
        this.pedido = pedido;
        this.data = data ;
        this.comissao = comissao;
        this.desconto = desconto;
        this.listContaPedidoItem = listContaPedidoItem;
        this.listPagamento = listPagamento ;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public List<ContaPedidoItem> getListContaPedidoItem() {
        return listContaPedidoItem;
    }

    public void setListContaPedidoItem(List<ContaPedidoItem> listContaPedidoItem) {
        this.listContaPedidoItem = listContaPedidoItem;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public List<PagamentoConta> getListPagamento() {
        return listPagamento;
    }

    public void setListPagamento(List<PagamentoConta> listPagamento) {
        this.listPagamento = listPagamento;
    }

    public double getTotalItens(){
        double tot = 0 ;
        for (ContaPedidoItem it : listContaPedidoItem){
            tot = tot + it.getValor();
        }
        return tot ;
    }

    public double getTotalPagamentos(){
        double tot = 0 ;
        for (PagamentoConta it : listPagamento){
            tot = tot + it.getValor();
        }
        return tot ;
    }

    public double getTotal(){
        return getTotalItens() - getDesconto() + getComissao() ;
    }

    public double getTotalaPagar(){
        return getTotal() - getTotalPagamentos() ;
    }

}
