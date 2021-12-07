package com.anequimplus.conexoes;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.tipos.Link;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

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
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fImpressoras).getUrl() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        }

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                Dao.getImpressoraADO(ctx).ImpressoraADD(j.getJSONArray("data"));
                Ok() ;
            } else erroMensagem(j.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        }

    }
    public abstract void Ok() ;
    public abstract void erroMensagem(String msg) ;
}
