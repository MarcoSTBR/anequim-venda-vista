package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transferencia {

    private int id ;
    private String uuid ;
    private Date data ;
    private int usuario_id ;
    private ContaPedido contaPedido_origem ;
    private ContaPedido contaPedido_destino ;
    private ContaPedidoItem contaPedidoItem ;
    private double quantidade ;
    private int status ;

    public Transferencia(int id, String uuid, Date data, ContaPedido contaPedido_origem, ContaPedido contaPedido_destino, ContaPedidoItem contaPedidoItem, double quantidade, int status, int usuario) {
        this.id = id;
        this.uuid = uuid ;
        this.data = data;
        this.contaPedido_origem = contaPedido_origem;
        this.contaPedido_destino = contaPedido_destino;
        this.contaPedidoItem = contaPedidoItem;
        this.quantidade = quantidade;
        this.status = status;
        this.usuario_id = usuario ;
    }


    public JSONObject getJSON() throws JSONException {
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        JSONObject j = new JSONObject();
        j.put("UUID", uuid) ;
        j.put("CONTA_PEDIDO_ITEM_ID", contaPedidoItem.getId()) ;
        j.put("CONTA_PEDIDO_ITEM_UUID", contaPedidoItem.getUUID()) ;
        j.put("CONTA_PEDIDO_ORIGEM_ID", contaPedido_origem.getId()) ;
        j.put("CONTA_PEDIDO_DESTINO_ID", contaPedido_destino.getId()) ;
        j.put("DATA", fdate.format(data)) ;
        j.put("SYSTEM_USER_ID", usuario_id) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("STATUS", status) ;
        return j ;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public ContaPedido getContaPedido_origem() {
        return contaPedido_origem;
    }

    public void setContaPedido_origem(ContaPedido contaPedido_origem) {
        this.contaPedido_origem = contaPedido_origem;
    }

    public ContaPedido getContaPedido_destino() {
        return contaPedido_destino;
    }

    public void setContaPedido_destino(ContaPedido contaPedido_destino) {
        this.contaPedido_destino = contaPedido_destino;
    }

    public ContaPedidoItem getContaPedidoItem() {
        return contaPedidoItem;
    }

    public void setContaPedidoItem(ContaPedidoItem contaPedidoItem) {
        this.contaPedidoItem = contaPedidoItem;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }
}
