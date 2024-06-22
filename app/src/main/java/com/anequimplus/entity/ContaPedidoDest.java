package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ContaPedidoDest extends Entidade{
    private int id ;
    private String UUID ;
    private int contaPedido_id ;
    private String cpfcnpj ;
    private String nome ;
    private int status ;

    public ContaPedidoDest(int id, String UUID, int contaPedido_id, String cpfcnpj, String nome, int status) {
        this.id = id;
        this.UUID = UUID;
        this.contaPedido_id = contaPedido_id;
        this.cpfcnpj = cpfcnpj;
        this.nome = nome;
        this.status = status;
    }

    public ContaPedidoDest(JSONObject j) throws JSONException {
        id             = j.getInt("ID") ;
        UUID           = j.getString("UUID");
        contaPedido_id = j.getInt("CONTA_PEDIDO_ID");
        cpfcnpj        = j.getString("CPFCNPJ"); ;
        nome           = j.getString("NOME"); ;
        status         = j.getInt("STATUS") ;
    }

    public Object getExportacaoJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("UUID", UUID) ;
        j.put("CONTA_PEDIDO_ID", contaPedido_id) ;
        j.put("CPFCNPJ", cpfcnpj) ;
        j.put("NOME", nome) ;
        j.put("STATUS", status) ;
        return j;

    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID", id) ;
        j.put("UUID", UUID) ;
        j.put("CONTA_PEDIDO_ID", contaPedido_id) ;
        j.put("CPFCNPJ", cpfcnpj) ;
        j.put("NOME", nome) ;
        j.put("STATUS", status) ;
        return j;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCpfcnpj() {
        return cpfcnpj;
    }

    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
