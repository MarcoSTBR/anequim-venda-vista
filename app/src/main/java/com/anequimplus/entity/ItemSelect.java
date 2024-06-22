package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ItemSelect implements Serializable {

    private int id;
    private Produto produto;
    private double quantidade;
    private double preco;
    private double desconto;
    private double comissao;
    private double valor;
    private String obs;
    private int status;

    public ItemSelect(int id, Produto produto, double quantidade, double preco, double desconto, double comissao, double valor, String obs, int status) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
        this.desconto = desconto;
        this.comissao = comissao;
        this.valor = valor;
        this.obs = obs;
        this.status = status;
    }
    /*
    public ItemSelect(JSONObject j) throws JSONException {
        id = j.getInt("id");
        produto = new Produto(j.getJSONObject("produto"));
        quantidade = j.getDouble("quantidade");
        preco = j.getDouble("preco");
        desconto = j.getDouble("desconto");
        comissao = j.getDouble("comissao");
        valor = j.getDouble("valor");
        obs = j.getString("obs");
        status = j.getInt("status");
    }
*/
    public ItemSelect(JSONObject j) throws JSONException {
        id = j.getInt("id");
        produto = new Produto(j.getJSONObject("PRODUTO"));
        quantidade = j.getDouble("QUANTIDADE");
        preco = j.getDouble("PRECO");
        desconto = j.getDouble("DESCONTO");
        comissao = j.getDouble("COMISSAO");
        valor = j.getDouble("STATUS");
        obs = j.getString("OBS");
        status = j.getInt("STATUS");

        //j.put("PRODUTO_ID", itemSelect.getProduto().getId()) ;
        //j.put("PRODUTO", itemSelect.getProduto().getJSONBase()) ;
        //j.put("QUANTIDADE", itemSelect.getQuantidade()) ;
        //j.put("PRECO", itemSelect.getPreco()) ;
        //j.put("DESCONTO", itemSelect.getDesconto()) ;
        //j.put("COMISSAO", itemSelect.getComissao()) ;
        //j.put("OBS", itemSelect.getObs()) ;
        //j.put("VALOR", itemSelect.getValor()) ;
        //j.put("STATUS", itemSelect.getStatus()) ;

    }

    public JSONObject getJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("PRODUTO", produto.getJSONBase()) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("PRECO", preco) ;
        j.put("DESCONTO", desconto) ;
        j.put("COMISSAO", comissao) ;
        j.put("OBS", obs) ;
        j.put("VALOR", valor) ;
        j.put("STATUS", status) ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}