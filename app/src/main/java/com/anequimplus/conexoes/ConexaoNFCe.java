package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.NFCe;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoNFCe extends ConexaoServer {

    public ConexaoNFCe(Context ctx, String chave, Impressora impressora) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Consultando NFCe" ;
        maps.put("class","AfoodNFCe") ;
        maps.put("method","consultarChave") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        maps.put("impressora_id",impressora.getId()) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fNFCeGetContaPedido).getUrl();
        //url = new URL(UtilSet.getServidor(ctx) + "/NFCeGetContaPedido");
    }

    public ConexaoNFCe(Context ctx, ContaPedido contaPedido) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Emitindo NFCe" ;
        maps.put("class","AfoodNFCe") ;
        maps.put("method","emitirPedido") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        maps.put("pedido_id",contaPedido.getId()) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fNFCeGetContaPedido).getUrl();
        //url = new URL(UtilSet.getServidor(ctx) + "/NFCeGetContaPedido");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExecuteNFce",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                setNFCE(new NFCe(j.getJSONObject("NFCE"))); ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }

    public abstract void setNFCE(NFCe nfce) ;
    public abstract void erro(String msg) ;


}
