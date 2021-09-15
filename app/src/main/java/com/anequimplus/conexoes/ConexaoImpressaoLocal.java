package com.anequimplus.conexoes;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Impressora;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class
ConexaoImpressaoLocal extends ConexaoServer {


    public ConexaoImpressaoLocal(Context ctx, Impressora impressora, int caixa_id, int opcao_id) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Impressão Local" ;
        maps.put("class","AfoodOpcoesFechamento") ;
        maps.put("method","imprimirOpRelatorio") ;
        maps.put("chave", UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("impressora_id",impressora.getId()) ;
        maps.put("caixa_id",caixa_id) ;
        maps.put("opcao_id",opcao_id) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fExecutaOpFechamento).getUrl() ;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                ok(j.getJSONObject("data").getString("mensagem"));
            } else erroMensagem(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        }

    }


    public abstract void ok(String msg) ;
    public abstract void erroMensagem(String msg) ;
}
