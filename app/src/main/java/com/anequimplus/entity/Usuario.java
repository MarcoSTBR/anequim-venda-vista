package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Usuario extends Entidade {
    private int id;
    private String login;
    private String nome;
    private String senha;
    private String autenticacao;
    private int status;

    public Usuario(JSONObject j) throws JSONException {
        if (j.isNull("id"))
            id = j.getInt("ID");
        else
            id = j.getInt("id");
        login = j.getString("LOGIN");
        nome = j.getString("NOME");
        senha = j.getString("SENHA");
        autenticacao = j.getString("AUTENTICACAO");
        status = j.getInt("STATUS");
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID", id);
        j.put("LOGIN", login);
        j.put("NOME", nome);
        j.put("SENHA", senha);
        j.put("AUTENTICACAO", autenticacao);
        j.put("STATUS", status);
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAutenticacao() {
        return autenticacao;
    }

    public void setAutenticacao(String autenticacao) {
        this.autenticacao = autenticacao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
