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

public abstract class ConexaoLogin extends ConexaoServer {


    public ConexaoLogin(Context ctx, String nUsuario, String nSenha) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, MalformedURLException {
        super(ctx);
        msg = "Logando" ;
        maps.put("class","AfoodLogar") ;
        maps.put("method","logar") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("login",nUsuario) ;
        maps.put("password",nSenha) ;
        maps.put("mac",UtilSet.getMAC(ctx)) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fLogar).getUrl() ;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.i("LOGADO", s) ;
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success"))
            {
                JSONObject obj = j.getJSONArray("data").getJSONObject(0);
                String aut = obj.getString("id") ;
                String nome_usu = obj.getString("name") ;
                Log.i("LOGADO", s) ;
                Log.i("LOGADO", nome_usu +  " " + aut) ;
                UtilSet.setAutenticacao(ctx, aut, nome_usu);
                Ok();
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }

   public abstract void Ok() ;
   public abstract void erro(String msg) ;

}
