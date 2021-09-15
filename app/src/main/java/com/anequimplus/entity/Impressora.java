package com.anequimplus.entity;

import android.util.Log;

import com.anequimplus.tipos.TipoImpressora;

import org.json.JSONException;
import org.json.JSONObject;

public class Impressora {

    private int id ;
    private String descricao ;
    private int tamColuna;
    private TipoImpressora tipoImpressora ;
    private int status ;

    public Impressora(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.descricao = j.getString("DESCRICAO");
        this.tamColuna = j.getInt("TAMCOLUNA") ;
        this.tipoImpressora = TipoImpressora.valueOf(j.getString("TIPOIMPRESSORA")) ;
        this.status = j.getInt("STATUS") ;
        Log.i("tipoImpressora",this.tipoImpressora.name() );
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTamColuna() {
        return tamColuna;
    }

    public void setTamColuna(int tamColuna) {
        this.tamColuna = tamColuna;
    }

    public TipoImpressora getTipoImpressora() {
        return tipoImpressora;
    }

    public void setTipoImpressora(TipoImpressora tipoImpressora) {
        this.tipoImpressora = tipoImpressora;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
