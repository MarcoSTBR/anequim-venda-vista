package com.anequimplus.entity;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedido implements Serializable {
    private int id ;
    private String uuid ;
    private String pedido ;
    private Date data ;
    private double desconto ;
    private List<ContaPedidoItem> listContaPedidoItem;
    private List<ContaPedidoPagamento> listPagamento;
    private int status ;
    private int status_comissao ;
    private int num_impressao ;
    private Date data_fechamento ;
    private int num_pessoas ;
    private int system_user_id ;

    public ContaPedido(String uuid, String pedido, Date data, int system_user_id){
        this.id = 0;
        this.uuid = uuid;
        this.pedido = pedido;
        this.data = data;
        this.desconto = 0;
        this.listContaPedidoItem = null ;
        this.listPagamento = null;
        this.status = 1;
        this.status_comissao = 1;
        this.num_impressao = 0;
        this.data_fechamento = data;
        this.num_pessoas = 1;
        this.system_user_id = system_user_id ;
    }

    public ContaPedido(int id, String uuid, String pedido, Date data, double desconto, List<ContaPedidoItem> listContaPedidoItem, List<ContaPedidoPagamento> listPagamento, int status, int status_comissao, int num_impressao, Date data_fechamento, int num_pessoas, int system_user_id) {
        this.id = id;
        this.uuid = uuid;
        this.pedido = pedido;
        this.data = data;
        this.desconto = desconto;
        this.listContaPedidoItem = listContaPedidoItem;
        this.listPagamento = listPagamento;
        this.status = status;
        this.status_comissao = status_comissao;
        this.num_impressao = num_impressao;
        this.data_fechamento = data_fechamento;
        this.num_pessoas = num_pessoas;
        this.system_user_id = system_user_id ;
    }

    public JSONObject getJSON() throws JSONException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        j.put("id", id) ;
        j.put("UUID", uuid) ;
        j.put("PEDIDO", pedido) ;
        j.put("DATA", df.format(data)) ;
        j.put("DESCONTO", desconto) ;

        return j ;
    }

    public ContaPedido(JSONObject j, List<ContaPedidoItem> listContaPedidoItem, List<ContaPedidoPagamento> listPagamento) throws JSONException {
        this.id = j.getInt("id");
        this.uuid = j.getString("UUID");
        this.pedido = j.getString("PEDIDO");
        this.data =  UtilSet.getData(j.getString("DATA")) ;
        this.desconto = j.getDouble("DESCONTO");
        this.status = j.getInt("STATUS") ;
        this.status_comissao = j.getInt("STATUS_COMISSAO") ;
        this.num_impressao = j.getInt("NUM_IMPRESSAO") ;
        this.data_fechamento = UtilSet.getData(j.getString("DATA_FECHAMENTO")) ;
        this.num_pessoas = j.getInt("NUM_PESSOAS") ;
        this.system_user_id = j.getInt("SYSTEM_USER_ID") ; ;
        this.listContaPedidoItem = listContaPedidoItem ;
        this.listPagamento = listPagamento ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public List<ContaPedidoItem> getListContaPedidoItem() {
        return listContaPedidoItem;
    }

    public void setListContaPedidoItem(List<ContaPedidoItem> listContaPedidoItem) {
        this.listContaPedidoItem = listContaPedidoItem;
    }

    public List<ContaPedidoPagamento> getListPagamento() {
        return listPagamento;
    }

    public void setListPagamento(List<ContaPedidoPagamento> listPagamento) {
        this.listPagamento = listPagamento;
    }

    public int getStatus_comissao() {
        return status_comissao;
    }

    public void setStatus_comissao(int status_comissao) {
        this.status_comissao = status_comissao;
    }

    public int getNum_impressao() {
        return num_impressao;
    }

    public void setNum_impressao(int num_impressao) {
        this.num_impressao = num_impressao;
    }

    public Date getData_fechamento() {
        return data_fechamento;
    }

    public void setData_fechamento(Date data_fechamento) {
        this.data_fechamento = data_fechamento;
    }

    public int getNum_pessoas() {
        return num_pessoas;
    }

    public void setNum_pessoas(int num_pessoas) {
        this.num_pessoas = num_pessoas;
    }

    public int getSystem_user_id() {
        return system_user_id;
    }

    public void setSystem_user_id(int system_user_id) {
        this.system_user_id = system_user_id;
    }

    public double getTotalItens(){
        double tot = 0 ;
        for (ContaPedidoItem it : listContaPedidoItem){
            if (it.getStatus() == 1)
               tot = tot + it.getValor();
        }
        return tot ;
    }

    public double getTotalPagamentos(){
        double tot = 0 ;
        for (ContaPedidoPagamento it : listPagamento){
            if (it.getStatus() == 1)
              tot = tot + it.getValor();
        }
        return tot ;
    }

    public double getTotalComissao(){
        double tot = 0 ;
        for (ContaPedidoItem it : listContaPedidoItem){
            if (it.getStatus() == 1)
                tot = tot + it.getComissao();
        }
        return tot ;
    }

    public double getTotal(){
        return getTotalItens() - getDesconto() + getTotalComissao() ;
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
