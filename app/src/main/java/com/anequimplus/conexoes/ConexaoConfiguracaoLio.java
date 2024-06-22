package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoConfiguracaoLio extends ConexaoServer {


    public ConexaoConfiguracaoLio(Context  ctx) throws MalformedURLException {
        super(ctx);
        msg = "Configuração LIO" ;
        maps.put("class","AfoodConfiguracaoLio") ;
        maps.put("method","consultar") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //;DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConfiguracaoLIO).getUrl() ;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExecute",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                oK(j.getJSONObject("data").getString("clientid"), j.getJSONObject("data").getString("accesstoken")) ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }

    public abstract void oK(String clientID, String accessToken) ;
    public abstract void erro(String msg) ;

}
