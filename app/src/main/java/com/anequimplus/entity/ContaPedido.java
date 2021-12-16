package com.anequimplus.entity;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedido implements Serializable {
    private int id ;
    private String uuid ;
    private String pedido ;
    private Date data ;
    private double comissao ;
    private double desconto ;
    private List<ContaPedidoItem> listContaPedidoItem;
    private List<ContaPedidoPagamento> listPagamento;
    private int status ;

    public ContaPedido(int id, String uuid, String pedido, Date data, double comissao, double desconto, List<ContaPedidoItem> listContaPedidoItem, List<ContaPedidoPagamento> listPagamento, int status) {
        this.id = id;
        this.uuid = uuid ;
        this.pedido = pedido;
        this.data = data ;
        this.comissao = comissao;
        this.desconto = desconto;
        this.listContaPedidoItem = listContaPedidoItem;
        this.listPagamento = listPagamento ;
        this.status = status ;
    }

    public ContaPedido(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.uuid = j.getString("UUID");
        this.pedido = j.getString("PEDIDO");
        this.data =  UtilSet.getData(j.getString("DATA")) ;
        this.comissao = j.getDouble("COMISSAO");
        this.desconto = j.getDouble("DESCONTO");
        this.status = j.getInt("STATUS") ;
        listContaPedidoItem = new ArrayList<ContaPedidoItem>() ;
        listPagamento = new ArrayList<ContaPedidoPagamento>();
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public List<ContaPedidoPagamento> getListPagamento() {
        return listPagamento;
    }

    public void setListPagamento(List<ContaPedidoPagamento> listPagamento) {
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
        for (ContaPedidoPagamento it : listPagamento){
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ContaPedidoItem> getListContaPedidoItemAtivos(){
        List<ContaPedidoItem> l = new ArrayList<ContaPedidoItem>() ;
        for (ContaPedidoItem it : listContaPedidoItem){
            if (it.getStatus() == 1)
                l.add(it) ;
        }
        return  l ;
    }
}
