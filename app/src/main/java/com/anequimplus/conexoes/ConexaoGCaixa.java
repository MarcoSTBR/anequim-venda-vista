package com.anequimplus.conexoes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoGCaixa extends ConexaoServer{

    private Context ctx ;
    private TipoConexao tipoConexao ;
    private JSONArray list ;

    public ConexaoGCaixa(Activity ctx, JSONArray list) throws MalformedURLException {
        super(ctx);
        msg = "Enviando Caixa..." ;
        method = "POST" ;
        this.ctx = ctx;
        this.list = list ;
        maps.put("class","AfoodGCaixa") ;
        maps.put("method","atualizar") ;
        maps.put("caixas",list) ;
        tipoConexao = TipoConexao.cxIncluir ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("CaixaG", list.toString()) ;
        Log.i("CaixaG", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                ok();
            } else erro("COD: " + codInt + " "+ j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro("COD: " + codInt + " " + s);
        }
    }

    public abstract void ok() ;
    public abstract void erro(String msg) ;

}
