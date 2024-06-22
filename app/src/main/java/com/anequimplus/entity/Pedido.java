package com.anequimplus.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Pedido extends Entidade {

    private int id ;
    private String pedido ;
    private Date data  ;
    private List<PedidoItem> listPedidoItem ;
    private int status ; // somente para seleção

    public Pedido(int id, String pedido, Date data, List<PedidoItem> listPedidoItem) {
        this.id = id;
        this.pedido = pedido;
        this.data = data;
        this.listPedidoItem = listPedidoItem;
        status = 1 ;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        j.put("ID", id) ;
        j.put("PEDIDO",pedido) ;
        j.put("DATA",s.format(data)) ;
        return j ;
    }

    public JSONObject getJsonExportacao() throws JSONException {
        JSONObject j = new JSONObject();
        JSONArray itens = new JSONArray() ;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        j.put("PEDIDO",pedido) ;
        j.put("DATA",s.format(data)) ;
        for (PedidoItem it : listPedidoItem){
            itens.put(it.getExportacaoJSon()) ;
        }
        j.put("ITENS",itens) ;
        return j ;
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

    public List<PedidoItem> getListPedidoItem() {
        return listPedidoItem;
    }

    public void setListPedidoItem(List<PedidoItem> listPedidoItem) {
        this.listPedidoItem = listPedidoItem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getQuantidadeTotalItens(){
        double vl = 0 ;
        for (PedidoItem it : listPedidoItem){
            vl = vl + it.getItemSelect().getQuantidade() ;
        }
        return vl;
    }

    public double getValorTotalItens(){
        double vl = 0 ;
        for (PedidoItem it : listPedidoItem){
            vl = vl + it.getItemSelect().getValor() ;
        }
        return vl;
    }

}
