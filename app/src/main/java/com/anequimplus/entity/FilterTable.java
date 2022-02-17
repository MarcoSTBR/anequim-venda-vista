package com.anequimplus.entity;

import org.json.JSONArray;

public class FilterTable {
    private String campo ;
    private String operador ;
    private String variavel;

    public FilterTable(String campo, String operador, String variavel) {
        this.campo = campo;
        this.operador = operador;
        this.variavel = variavel;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getVariavel() {
        return variavel;
    }

    public void setVariavel(String variavel) {
        this.variavel = variavel;
    }

    public JSONArray getJSON() {
        JSONArray j = new JSONArray();
        j.put(campo) ;
        j.put(operador) ;
        j.put(variavel) ;
        return j;

    }
}
