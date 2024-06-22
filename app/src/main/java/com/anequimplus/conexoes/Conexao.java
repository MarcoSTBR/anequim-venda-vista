package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import java.net.URL;
import java.util.Map;

public class Conexao {

    public Context ctx ;
    public String msg;
    public int codInt = 0;
    public Map<String, Object> maps;
    public URL url;
    public String token;
    public String method;
    public Conexao(Context ctx) {
        this.ctx = ctx;
    }

    public void execute(){


    }

    protected void onPostExecute(String s){
        Log.i("onPostExecute", s) ;
    }

}

