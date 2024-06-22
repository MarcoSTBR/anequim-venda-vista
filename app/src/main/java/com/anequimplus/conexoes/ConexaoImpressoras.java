package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoImpressoras extends ConexaoServer {


    public ConexaoImpressoras(Context ctx) {
        super(ctx);
        msg = "Impressoras" ;
        try {
            maps.put("class", "AfoodImpressoras") ;
            maps.put("method", "consultar") ;
    //        maps.put("chave", UtilSet.getChave(ctx)) ;
//            maps.put("loja_id", UtilSet.getLojaId(ctx)) ;
//            maps.put("MAC", UtilSet.getMAC(ctx)) ;
            url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fImpressoras).getUrl() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        }

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("conexaoImpressora", "Cod "+codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                DaoDbTabela.getImpressoraADO(ctx).ImpressoraADD(j.getJSONArray("data"));
                Ok() ;
            } else erroMensagem(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        }

    }
    public abstract void Ok() ;
    public abstract void erroMensagem(String msg) ;
}
