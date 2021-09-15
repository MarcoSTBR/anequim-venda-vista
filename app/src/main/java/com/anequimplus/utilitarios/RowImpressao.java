package com.anequimplus.utilitarios;

import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.tipos.TipoEstiloFont;

import org.json.JSONException;
import org.json.JSONObject;

public class RowImpressao {

    private String linha ;
    private TipoAlinhamento tipoAlinhamento ;
    private int tamFont ;
    private TipoEstiloFont tipoEstilo ;

    public RowImpressao(String linha, TipoAlinhamento tipoAlinhamento, int tamFont) {
        this.linha = linha;
        this.tipoAlinhamento = tipoAlinhamento;
        this.tamFont = tamFont;
        this.tipoEstilo = TipoEstiloFont.ptsNormal ;
    }

    public  RowImpressao(JSONObject j) throws JSONException {
        this.linha = j.getString("LINHA");
        this.tamFont = j.getInt("TAMFONT");
        this.tipoAlinhamento = TipoAlinhamento.valueOf(j.getString("ALINHAMENTO")) ;
        this.tipoEstilo = TipoEstiloFont.valueOf(j.getString("ESTILOFONT")) ;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public TipoAlinhamento getTipoAlinhamento() {
        return tipoAlinhamento;
    }

    public void setTipoAlinhamento(TipoAlinhamento tipoAlinhamento) {
        this.tipoAlinhamento = tipoAlinhamento;
    }

    public int getTamFont() {
        return tamFont;
    }

    public void setTamFont(int tamFont) {
        this.tamFont = tamFont;
    }

    public TipoEstiloFont getTipoEstilo() {
        return tipoEstilo;
    }

    public void setTipoEstilo(TipoEstiloFont tipoEstilo) {
        this.tipoEstilo = tipoEstilo;
    }
}
