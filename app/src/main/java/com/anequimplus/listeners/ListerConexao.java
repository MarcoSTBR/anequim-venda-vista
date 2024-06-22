package com.anequimplus.listeners;

import org.json.JSONObject;

public interface ListerConexao {

    void resposta(JSONObject dados) ;
    void erroMsg(String msg) ;
}
