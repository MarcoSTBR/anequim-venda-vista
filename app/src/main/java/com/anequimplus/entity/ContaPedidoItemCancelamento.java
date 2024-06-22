package com.anequimplus.entity;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContaPedidoItemCancelamento extends Entidade {
    private int id ;
    private String uuid ;
    private int contaPedidoItem_id ;
    private Date data ;
    private int usuario_id ;
    private double quantidade ;
    private int status ;
    private int caixa_id ;
    private int conf_terminal_id ;

    public ContaPedidoItemCancelamento(int id, String uuid, int contaPedidoItem_id, Date data, int usuario_id, double quantidade, int status, int caixa_id, int conf_terminal_id) {
        this.id = id;
        this.uuid = uuid;
        this.contaPedidoItem_id = contaPedidoItem_id;
        this.data = data;
        this.usuario_id = usuario_id;
        this.quantidade = quantidade;
        this.status = status;
        this.caixa_id = caixa_id ;
        this.conf_terminal_id = conf_terminal_id ;
    }


    public ContaPedidoItemCancelamento(JSONObject j) throws JSONException {
        this.id = j.getInt("ID");
        this.uuid = j.getString("UUID");
        this.contaPedidoItem_id = j.getInt("CONTA_PEDIDO_ITEM_ID");
        this.data =  UtilSet.getData(j.getString("DATA"));
        this.usuario_id = j.getInt("SYSTEM_USER_ID");
        this.quantidade = j.getDouble("QUANTIDADE");;
        this.status = j.getInt("STATUS");
        this.caixa_id = 0 ;
        this.conf_terminal_id = j.getInt("CONF_TERMINAL_ID");
    }

    public JSONObject getExpJSON() throws JSONException {
        JSONObject j = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        j.put("CONTA_PEDIDO_ITEM_ID", contaPedidoItem_id) ;
        j.put("UUID", uuid) ;
        j.put("SYSTEM_USER_ID", usuario_id) ;
        j.put("DATA", df.format(data)) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("STATUS", status) ;
        j.put("CONF_TERMINAL_ID", conf_terminal_id) ;
        return j ;
    }

    public JSONObject getExportacaoJSON() throws JSONException {
        JSONObject j = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        j.put("CONTA_PEDIDO_ITEM_ID", contaPedidoItem_id) ;
        j.put("UUID", uuid) ;
        j.put("SYSTEM_USER_ID", usuario_id) ;
        j.put("DATA", df.format(data)) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("STATUS", status) ;
        j.put("CONF_TERMINAL_ID", conf_terminal_id) ;
        return j ;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        j.put("ID", id) ;
        j.put("UUID", uuid) ;
        j.put("CONTA_PEDIDO_ITEM_ID", contaPedidoItem_id) ;
        j.put("DATA", df.format(data)) ;
        j.put("USUARIO_ID", usuario_id) ;
        j.put("CAIXA_ID", caixa_id) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("STATUS", status) ;
       // j.put("CONF_TERMINAL_ID", conf_terminal_id) ;
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

    public int getContaPedidoItem_id() {
        return contaPedidoItem_id;
    }

    public void setContaPedidoItem_id(int contaPedidoItem_id) {
        this.contaPedidoItem_id = contaPedidoItem_id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
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

    public int getCaixa_id() {
        return caixa_id;
    }

    public void setCaixa_id(int caixa_id) {
        this.caixa_id = caixa_id;
    }

    public int getConf_terminal_id() {
        return conf_terminal_id;
    }

    public void setConf_terminal_id(int conf_terminal_id) {
        this.conf_terminal_id = conf_terminal_id;
    }

    @Override
    public String toString() {
        return "ContaPedidoItemCancelamento{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", contaPedidoItem_id=" + contaPedidoItem_id +
                ", data=" + data +
                ", usuario_id=" + usuario_id +
                ", quantidade=" + quantidade +
                ", status=" + status +
                '}';
    }
}
