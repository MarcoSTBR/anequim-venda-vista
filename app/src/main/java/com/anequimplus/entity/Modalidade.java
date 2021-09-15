package com.anequimplus.entity;

import com.anequimplus.tipos.TipoModalidade;

import org.json.JSONException;
import org.json.JSONObject;

public class Modalidade {

    private int id ;
    private String codigo ;
    private String descricao ;
    private TipoModalidade tipoModalidade;
    private int cod_recebimento ;
    private UrlParam foto ;
    private int status ;

    public Modalidade(JSONObject j) throws JSONException {
        this.id = j.getInt("id") ;
        this.codigo = j.getString("CODIGO");
        this.descricao = j.getString("DESCRICAO");
        this.tipoModalidade = TipoModalidade.valueOf(j.getString("TIPO")) ;
        this.cod_recebimento = j.getInt("COD_RECEBIMENTO");
        this.foto = new UrlParam(j.getString("FOTO"),j.getString("PARAM"));
        this.status =  j.getInt("STATUS") ;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject j = new JSONObject() ;
        JSONObject jFoto = new JSONObject() ;
        j.put("ID", id) ;
        j.put("CODIGO", codigo) ;
        j.put("DESCRICAO", descricao) ;
        j.put("TIPO", tipoModalidade.valor) ;
        jFoto.put("URL", foto.getUrl()) ;
        jFoto.put("PARAM", foto.getParam()) ;
        j.put("FOTO", jFoto) ;
        j.put("STATUS", status) ;
        return j ;
    }
    public Modalidade(int id, String codigo, String descricao, TipoModalidade tipoModalidade, int cod_recebimento, UrlParam foto, int status) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.tipoModalidade = tipoModalidade;
        this.cod_recebimento = cod_recebimento;
        this.foto = foto;
        this.status = status ;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoModalidade getTipoModalidade() {
        return tipoModalidade;
    }

    public void setTipoModalidade(TipoModalidade tipoModalidade) {
        this.tipoModalidade = tipoModalidade;
    }


    public int getCod_recebimento() {
        return cod_recebimento;
    }

    public void setCod_recebimento(int cod_recebimento) {
        this.cod_recebimento = cod_recebimento;
    }

    public UrlParam getFoto() {
        return foto;
    }

    public void setFoto(UrlParam foto) {
        this.foto = foto;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
