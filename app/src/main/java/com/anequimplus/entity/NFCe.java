package com.anequimplus.entity;

import com.anequimplus.utilitarios.LinksParaAcesso;
import com.anequimplus.utilitarios.RowImpressao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NFCe {

    private int id ;
    private String chave ;
    private LinksParaAcesso logoTipo ;
    private List<RowImpressao> corpo ;
    private List<RowImpressao> rodape ;


    public NFCe(JSONObject j) throws JSONException {
      //  id = j.getInt("ID") ;
        JSONObject jXml = j.getJSONObject("XML") ;
        id  = jXml.getInt("ID") ;
        logoTipo = new LinksParaAcesso(jXml.getJSONObject("LOGOTIPO")) ;
        chave = jXml.getString("CHAVE") ;
        corpo = new ArrayList<RowImpressao>() ;
        rodape = new ArrayList<RowImpressao>() ;
        for (int i = 0 ; i < jXml.getJSONArray("CORPO").length() ; i++) {
            corpo.add(new RowImpressao(jXml.getJSONArray("CORPO").getJSONObject(i)));
        }
        for (int i = 0 ; i < jXml.getJSONArray("RODAPE").length() ; i++) {
            rodape.add(new RowImpressao(jXml.getJSONArray("RODAPE").getJSONObject(i)));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public LinksParaAcesso getLogoTipo() {
        return logoTipo;
    }

    public void setLogoTipo(LinksParaAcesso logoTipo) {
        this.logoTipo = logoTipo;
    }

    public List<RowImpressao> getCorpo() {
        return corpo;
    }

    public void setCorpo(List<RowImpressao> corpo) {
        this.corpo = corpo;
    }

    public List<RowImpressao> getRodape() {
        return rodape;
    }

    public void setRodape(List<RowImpressao> rodape) {
        this.rodape = rodape;
    }
}
