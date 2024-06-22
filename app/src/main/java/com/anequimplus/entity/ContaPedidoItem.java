package com.anequimplus.entity;

import android.util.Log;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContaPedidoItem extends Entidade {
    private int id ;
    private int contaPedido_id ;
    private Date data ;
    private String UUID ;
    private Produto produto ;
    private double quantidade ;
    private double preco ;
    private double desconto ;
    private double comissao ;
    private double valor ;
    private String obs ;
    private int status ;
    private int usuario_id ;

    public ContaPedidoItem(int id, Date data, int contaPedido_id, String UUID, Produto produto, double quantidade, double preco, double desconto, double comissao, double valor, String obs, int status, int usuario_id) {
        this.id = id;
        this.data = data ;
        this.contaPedido_id = contaPedido_id ;
        this.UUID = UUID;
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
        this.desconto = desconto;
        this.comissao = comissao;
        this.valor = valor;
        this.obs = obs;
        this.status = status;
        this.usuario_id = usuario_id ;
    }

    public ContaPedidoItem(JSONObject j, Produto produto) throws JSONException {
        Log.i("jsonid", "id join " +j.toString()) ;
        if (j.isNull("id")){
            Log.i("jsonid", "id join " +j.getInt("ID")) ;
            this.id = j.getInt("ID");
        } else this.id = j.getInt("id");
        this.UUID = j.getString("UUID");
        this.data = UtilSet.getData(j.getString("DATA"));
        this.contaPedido_id = j.getInt("CONTA_PEDIDO_ID");
        this.produto = produto;
        this.quantidade = j.getDouble("QUANTIDADE");
        this.preco = j.getDouble("PRECO");
        this.desconto = j.getDouble("DESCONTO");
        this.comissao = j.getDouble("COMISSAO");
        this.valor = j.getDouble("VALOR");
        this.obs = j.getString("OBS");
        this.status = j.getInt("STATUS");
        this.usuario_id = j.getInt("SYSTEM_USER_ID");
    }



    public JSONObject getExportacaoJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("UUID", UUID) ;
        j.put("DATA", df.format(data)) ;
        j.put("CONTA_PEDIDO_ID", contaPedido_id) ;
        j.put("PRODUTO_ID", produto.getId()) ;
        j.put("QUANTIDADE",quantidade) ;
        j.put("PRECO",preco) ;
        j.put("DESCONTO",desconto) ;
        j.put("COMISSAO",comissao) ;
        j.put("VALOR",valor) ;
        j.put("OBS",obs) ;
        j.put("STATUS",status) ;
        j.put("SYSTEM_USER_ID", usuario_id) ;
        return j ;
    }


    @Override
    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("UUID", UUID) ;
        j.put("DATA", df.format(data)) ;
        j.put("CONTA_PEDIDO_ID", contaPedido_id) ;
        j.put("PRODUTO_ID", produto.getId()) ;
        j.put("QUANTIDADE",quantidade) ;
        j.put("PRECO",preco) ;
        j.put("DESCONTO",desconto) ;
        j.put("COMISSAO",comissao) ;
        j.put("VALOR",valor) ;
        j.put("OBS",obs) ;
        j.put("STATUS",status) ;
        j.put("SYSTEM_USER_ID", usuario_id) ;
        return j ;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
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
