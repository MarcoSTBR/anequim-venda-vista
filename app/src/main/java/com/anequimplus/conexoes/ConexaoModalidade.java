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

public abstract class ConexaoModalidade extends ConexaoServer {

    public ConexaoModalidade(Context ctx) {
        super(ctx);
        msg = "Modalidades" ;
        try {
            maps.put("class","AfoodModalidade") ;
            maps.put("method","consultar") ;
            maps.put("chave",UtilSet.getChave(ctx)) ;
            maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
            maps.put("MAC",UtilSet.getMAC(ctx)) ;
            maps.put("system_user_id",UtilSet.getUsuarioId(ctx)) ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaModalidade).getUrl() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erro(e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExeModalidades",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                Dao.getModalidadeADO(ctx).modalidadeADD(j.getJSONArray("data"));
                oK();
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }
    public abstract void oK() ;
    public abstract void erro(String msg) ;
}
