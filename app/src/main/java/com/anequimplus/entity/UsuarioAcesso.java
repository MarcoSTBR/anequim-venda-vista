package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioAcesso extends Entidade{
    private int id ;
    private int usuario_id ;
    private int acesso_id ;
    private String descricao ;


    public UsuarioAcesso(JSONObject j) throws JSONException {
        if (j.isNull("id"))
           id = j.getInt("ID");
        else id = j.getInt("id");
        usuario_id = j.getInt("ACESSO_USUARIO_ID");
        acesso_id = j.getInt("ACESSO_DESCRICAO_ID");
        descricao = j.getString("DESCRICAO");
    }

    public UsuarioAcesso(int id, int usuario_id, int acesso_id, String descricao) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.acesso_id = acesso_id;
        this.descricao = descricao;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID", id) ;
        j.put("USUARIO_ID", usuario_id) ;
        j.put("ACESSO_ID", acesso_id) ;
        j.put("DESCRICAO", descricao) ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getAcesso_id() {
        return acesso_id;
    }

    public void setAcesso_id(int acesso_id) {
        this.acesso_id = acesso_id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
