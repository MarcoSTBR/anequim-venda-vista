package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ContaPedidoItem {
    private int id ;
    private int contaPedido_id ;
    private String UUID ;
    private Produto produto ;
    private double quantidade ;
    private double preco ;
    private double desconto ;
    private double comissao ;
    private double valor ;
    private String obs ;
    private int status ;

    public ContaPedidoItem(int id, int contaPedido_id, String UUID, Produto produto, double quantidade, double preco, double desconto, double comissao, double valor, String obs, int status) {
        this.id = id;
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
    }

    public ContaPedidoItem(JSONObject j, Produto p) throws JSONException {
        id = j.getInt("id") ;
        UUID = j.getString("UUID") ;
        produto = p ;
        quantidade = j.getDouble("QUANTIDADE");
        preco = j.getDouble("PRECO");
        desconto = j.getDouble("DESCONTO");
        comissao = j.getDouble("COMISSAO");
        valor = j.getDouble("VALOR");
        obs = j.getString("OBS") ;
        status = j.getInt("STATUS") ;
    }

    public ContaPedidoItem(int i){
        this.id = id;
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

    public int getContaPedido_id() {
        return contaPedido_id;
    }

    public void setContaPedido_id(int contaPedido_id) {
        this.contaPedido_id = contaPedido_id;
    }

    @Override
    public String toString() {
        return "ContaPedidoItem{" +
                "id=" + id +
                ", UUID='" + UUID + '\'' +
                ", produto=" + produto +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", desconto=" + desconto +
                ", comissao=" + comissao +
                ", valor=" + valor +
                ", obs='" + obs + '\'' +
                ", status=" + status +
                '}';
    }
}
