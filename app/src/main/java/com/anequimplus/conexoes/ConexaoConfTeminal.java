package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import static com.anequimplus.tipos.Link.fGetTerminal;
import static com.anequimplus.tipos.Link.fSetTerminal;

public abstract class ConexaoConfTeminal extends ConexaoServer {

    public ConexaoConfTeminal(Context ctx, String descricao) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Incluir Terminal" ;
        maps.put("class","AfoodConf_Terminal") ;
        maps.put("method","incluir") ;
        maps.put("chave", UtilSet.getChave(ctx)) ;
     //   maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("cnpj",UtilSet.getCnpj(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("descricao",descricao) ;
        maps.put("tipo_emissao",3) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(fSetTerminal).getUrl();

    }

    public ConexaoConfTeminal(Context ctx) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Consultar Terminal" ;
        maps.put("class","AfoodConf_Terminal") ;
        maps.put("method","consultar") ;
        maps.put("chave", UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(fGetTerminal).getUrl();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("onPostExTerminal", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                ok(j.getJSONObject("data").getInt("id"), j.getJSONObject("data").getString("DESCRICAO"));
            } else {
                erroMsg(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erroMsg(e.getMessage()) ;
        }

    }

   public abstract void ok(int id, String descricao) ;
   public abstract void erroMsg(String msg) ;
}
