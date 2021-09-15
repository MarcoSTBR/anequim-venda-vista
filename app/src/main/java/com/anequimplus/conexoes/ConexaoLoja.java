package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoLoja extends ConexaoServer {

    public ConexaoLoja(Context ctx) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Lojas" ;
        maps.put("class","AfoodLojas");
        maps.put("method","loadAll");
        maps.put("chave", UtilSet.getChave(ctx));
        maps.put("MAC",UtilSet.getMAC(ctx));
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fLojas).getUrl() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExecute",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                Dao.getLojaADO(ctx).add(j.getJSONArray("data"));
                oK();
            } else erro(j.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }

    public abstract void oK() ;
    public abstract void erro(String msg) ;

}
