package com.anequimplus.conexoes;

import org.json.JSONObject;

public interface ListerConexao {

    void resposta(JSONObject dados) ;
    void erroMsg(String msg) ;
}
