package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Loja {

    private int id ;
    private String codigo ;
    private String cnpj ;
    private String loja ;
    private String alias ;
    private String crt ;
    private String uf ;
    private String insc_estadual ;
    private String municipio;
    private String logradouro ;
    private String numero ;
    private String complemento ;
    private String bairro ;
    private String cep ;
    private String telefone ;
    private String municipio_ibge ;
    private int status ;

    public Loja(JSONObject j) throws JSONException {
        id       = j.getInt("id");
        codigo   = j.getString("CODIGO");
        cnpj     = j.getString("CNPJ");
        loja     = j.getString("LOJA");
        alias    = j.getString("ALIAS");
        crt      = j.getString("CRT");
        uf       = j.getString("UF");
        insc_estadual = j.getString("INSC_ESTADUAL");
        municipio = j.getString("MUNICIPIO");
        logradouro  = j.getString("LOGRADOURO");
        numero      = j.getString("NUMERO");
        complemento = j.getString("COMPLEMENTO");
        bairro      = j.getString("BAIRRO");
        cep         = j.getString("CEP");
        telefone    = j.getString("TELEFONE");
        municipio_ibge = j.getString("MUNICIPIO_IBGE");
        status         = j.getInt("STATUS");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCrt() {
        return crt;
    }

    public void setCrt(String crt) {
        this.crt = crt;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getInsc_estadual() {
        return insc_estadual;
    }

    public void setInsc_estadual(String insc_estadual) {
        this.insc_estadual = insc_estadual;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMunicipio_ibge() {
        return municipio_ibge;
    }

    public void setMunicipio_ibge(String municipio_ibge) {
        this.municipio_ibge = municipio_ibge;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
