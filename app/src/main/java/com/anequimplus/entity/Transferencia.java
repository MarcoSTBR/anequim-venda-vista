package com.anequimplus.entity;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transferencia extends Entidade{

    private int id ;
    private String uuid ;
    private Date data ;
    private int usuario_id ;
    private int contaPedido_origem_id ;
    private int contaPedido_destino_id ;
    private int contaPedidoItem_id ;
    private double quantidade ;
    private int status ;
    private int conf_terminal_id ;


    public Transferencia(int id, String uuid, Date data, int contaPedido_origem_id, int contaPedido_destino_id, int contaPedidoItem_id, double quantidade, int status, int usuario, int caixa_id, int conf_terminal_id) {
        this.id = id;
        this.uuid = uuid ;
        this.data = data;
        this.contaPedido_origem_id = contaPedido_origem_id;
        this.contaPedido_destino_id = contaPedido_destino_id;
        this.contaPedidoItem_id = contaPedidoItem_id;
        this.quantidade = quantidade;
        this.status = status;
        this.usuario_id = usuario ;
        this.conf_terminal_id = conf_terminal_id ;
    }

    public Transferencia(JSONObject j) throws JSONException {
        if (j.isNull("id"))
            this.id = j.getInt("ID") ;
          else this.id = j.getInt("id") ;
        this.uuid = j.getString("UUID") ;
        this.data = UtilSet.getData(j.getString("DATA"));
        this.contaPedido_origem_id =  j.getInt("CONTA_PED_ORIGEM_ID") ;
        this.contaPedido_destino_id = j.getInt("CONTA_PED_DESTINO_ID") ;
        this.contaPedidoItem_id = j.getInt("CONTA_PED_ITEM_ID") ;
        this.quantidade = j.getDouble("QUANTIDADE");
        this.status =  j.getInt("STATUS");
        if (j.isNull("USUARIO_ID"))
           this.usuario_id = j.getInt("SYSTEM_USER_ID") ;
         else this.usuario_id = j.getInt("USUARIO_ID") ;
        this.conf_terminal_id = j.getInt("CONF_TERMINAL_ID") ;

    }

    @Override
    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        j.put("ID", id) ;
        j.put("UUID", uuid) ;
        j.put("CONTA_PED_ITEM_ID", contaPedidoItem_id) ;
        j.put("CONTA_PED_ORIGEM_ID", contaPedido_origem_id) ;
        j.put("CONTA_PED_DESTINO_ID", contaPedido_destino_id) ;
        j.put("DATA", fdate.format(data)) ;
        j.put("USUARIO_ID", usuario_id) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("STATUS", status) ;
        //j.put("CONF_TERMINAL_ID", conf_terminal_id) ;
        return j ;
    }

    public JSONObject getExportacaoJSON() throws JSONException {
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        j.put("UUID", uuid) ;
        j.put("CONTA_PED_ITEM_ID", contaPedidoItem_id) ;
        j.put("CONTA_PED_ORIGEM_ID", contaPedido_origem_id) ;
        j.put("CONTA_PED_DESTINO_ID", contaPedido_destino_id) ;
        j.put("DATA", fdate.format(data)) ;
        j.put("SYSTEM_USER_ID", usuario_id) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("STATUS", status) ;
        j.put("CONF_TERMINAL_ID", conf_terminal_id) ;
        return j ;
    }

    public JSONObject getEXPJSON() throws JSONException {
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        j.put("UUID", uuid) ;
/*
        j.put("CONTA_PED_ITEM_ID", contaPedidoItem_id) ;
        j.put("CONTA_PED_ORIGEM_ID", contaPedido_origem_id) ;
        j.put("CONTA_PED_DESTINO_ID", contaPedido_destino_id) ;
*/
        j.put("DATA", fdate.format(data)) ;
        j.put("SYSTEM_USER_ID", usuario_id) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("STATUS", status) ;
        j.put("CONF_TERMINAL_ID", conf_terminal_id) ;
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

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getContaPedido_origem_id() {
        return contaPedido_origem_id;
    }

    public void setContaPedido_origem_id(int contaPedido_origem_id) {
        this.contaPedido_origem_id = contaPedido_origem_id;
    }

    public int getContaPedido_destino_id() {
        return contaPedido_destino_id;
    }

    public void setContaPedido_destino_id(int contaPedido_destino_id) {
        this.contaPedido_destino_id = contaPedido_destino_id;
    }

    public int getContaPedidoItem_id() {
        return contaPedidoItem_id;
    }

    public void setContaPedidoItem_id(int contaPedidoItem_id) {
        this.contaPedidoItem_id = contaPedidoItem_id;
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

    public int getConf_terminal_id() {
        return conf_terminal_id;
    }

    public void setConf_terminal_id(int conf_terminal_id) {
        this.conf_terminal_id = conf_terminal_id;
    }


}
