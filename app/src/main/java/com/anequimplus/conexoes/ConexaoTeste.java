package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoTeste extends ConexaoServer {

    public ConexaoTeste(Activity ctx) throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        token = "" ;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/teste") ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("teste", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                retorno(j.getString("data"));
            } else retorno(j.getString("data")); ;
        } catch (Exception e) {
            e.printStackTrace();
            retorno(s);
        }
    }

    public abstract void retorno(String msg) ;
}
