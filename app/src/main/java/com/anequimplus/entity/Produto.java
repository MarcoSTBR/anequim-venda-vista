package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Produto extends Entidade{

    private int id ;
    private String codBarra ;
    private String unidade ;
    private String descricao ;
    private String imagem ;
    private int status ;
    private double preco ;
    private double comissao ;

    public Produto(int id, String codBarra, String unidade, String descricao, String imagem, int status, double preco, double comissao) {
        this.id = id;
        this.codBarra = codBarra;
        this.unidade = unidade;
        this.descricao = descricao;
        this.imagem = imagem;
        this.status = status;
        this.preco = preco ;
        this.comissao = comissao ;
    }

    public Produto(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.codBarra = j.getString("CODBARRA");
        this.unidade = j.getString("UNIDADE");
        this.descricao = j.getString("DESCRICAO");
        this.imagem = j.getString("IMAGEM") ;
        this.status = j.getInt("STATUS")  ;
        this.preco = j.getDouble("PRECO") ;
        this.comissao = j.getDouble("COMISSAO") ;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        if (j.isNull("CODBARRA"))
            j.put("CODIGO", codBarra) ;
         else j.put("CODBARRA", codBarra) ;
        j.put("UNIDADE", unidade) ;
        j.put("DESCRICAO", descricao) ;
        j.put("IMAGEM", imagem) ;
        j.put("STATUS", status) ;
        j.put("PRECO", preco) ;
        j.put("COMISSAO", comissao) ;
        return j ;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("CODBARRA", codBarra) ;
        j.put("UNIDADE", unidade) ;
        j.put("DESCRICAO", descricao) ;
        j.put("IMAGEM", imagem) ;
        j.put("STATUS", status) ;
        j.put("PRECO", preco) ;
        j.put("COMISSAO", comissao) ;
        return j ;
    }

    public JSONObject getJSONBase() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("CODBARRA", codBarra) ;
        j.put("UNIDADE", unidade) ;
        j.put("DESCRICAO", descricao) ;
        j.put("PRECO", preco) ;
        j.put("COMISSAO", comissao) ;
        return j ;
    }

    public JSONObject getJSONRemoto() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("CODIGO", codBarra) ;
        j.put("UNIDADE", unidade) ;
        j.put("DESCRICAO", descricao) ;
        return j ;
    }


    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codBarra='" + codBarra + '\'' +
                ", unidade='" + unidade + '\'' +
                ", descricao='" + descricao + '\'' +
                ", imagem='" + imagem + '\'' +
                ", status=" + status +
                ", preco=" + preco +
                ", comissao=" + comissao +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

}
