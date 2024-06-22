package com.anequimplus.entity;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContaPedidoNFCe extends Entidade {


    private String UUID ;
    private int contaPedido_id ;
    private Date data ;
    private String chave ;
    private String protocolo ;
    private String x_movito ;
    private int stact ;
    private int status_cancelamento ;
    private int status_contingencia ;
    private int status ;
    private int tipo ;

    public ContaPedidoNFCe(String UUID, int contaPedido_id, Date data, String chave, String protocolo,
                           String x_movito, int stact, int status_cancelamento, int status_contingencia, int status, int tipo) {
        this.UUID = UUID;
        this.contaPedido_id = contaPedido_id;
        this.data = data;
        this.chave = chave;
        this.protocolo = protocolo;
        this.x_movito = x_movito;
        this.stact = stact;
        this.status_cancelamento = status_cancelamento;
        this.status_contingencia = status_contingencia;
        this.status = status;
        this.tipo = tipo ;
    }

    public ContaPedidoNFCe(JSONObject j) throws JSONException {
        UUID           = j.getString("UUID") ;
        contaPedido_id = j.getInt("CONTA_PEDIDO_ID") ;
        data           = UtilSet.getData(j.getString("DATA")) ;
        chave          = j.getString("CHAVE") ;
        protocolo      = j.getString("PROTOCOLO") ;
        stact          = j.getInt("STACT") ;
        status         = j.getInt("STATUS") ;
        x_movito       = j.getString("XMOTIVO") ;
        status_cancelamento       = j.getInt("STATUS_CANCELAMENTO") ;
        status_contingencia       = j.getInt("STATUS_CONTINGENCIA") ;
        tipo           = j.getInt("TIPO") ;
    }

    public JSONObject getExportacaoJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("UUID", UUID) ;
        j.put("CONTA_PEDIDO_ID", contaPedido_id) ;
        j.put("DATA", df.format(data)) ;
        j.put("CHAVE", chave) ;
        j.put("PROTOCOLO", protocolo) ;
        j.put("STACT", stact) ;
        j.put("STATUS", status) ;
        j.put("XMOTIVO", x_movito) ;
        j.put("STATUS_CANCELAMENTO", status_cancelamento) ;
        j.put("STATUS_CONTINGENCIA", status_contingencia) ;
        j.put("TIPO", tipo) ;
        return j;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("UUID", UUID) ;
        j.put("CONTA_PEDIDO_ID", contaPedido_id) ;
        j.put("DATA", df.format(data)) ;
        j.put("CHAVE", chave) ;
        j.put("PROTOCOLO", protocolo) ;
        j.put("STACT", stact) ;
        j.put("STATUS", status) ;
        j.put("XMOTIVO", x_movito) ;
        j.put("STATUS_CANCELAMENTO", status_cancelamento) ;
        j.put("STATUS_CONTINGENCIA", status_contingencia) ;
        j.put("TIPO", tipo) ;
        return j;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getContaPedido_id() {
        return contaPedido_id;
    }

    public void setContaPedido_id(int contaPedido_id) {
        this.contaPedido_id = contaPedido_id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getX_movito() {
        return x_movito;
    }

    public void setX_movito(String x_movito) {
        this.x_movito = x_movito;
    }

    public int getStact() {
        return stact;
    }

    public void setStact(int stact) {
        this.stact = stact;
    }

    public int getStatus_cancelamento() {
        return status_cancelamento;
    }

    public void setStatus_cancelamento(int status_cancelamento) {
        this.status_cancelamento = status_cancelamento;
    }

    public int getStatus_contingencia() {
        return status_contingencia;
    }

    public void setStatus_contingencia(int status_contingencia) {
        this.status_contingencia = status_contingencia;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }


}
