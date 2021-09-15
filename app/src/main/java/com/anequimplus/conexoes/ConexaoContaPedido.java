package com.anequimplus.conexoes;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import static com.anequimplus.tipos.Link.fConsultaPedido;

public abstract class ConexaoContaPedido extends ConexaoServer {

    public ConexaoContaPedido(Context ctx) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, MalformedURLException {
        super(ctx);
        msg = "Conta Pedido" ;
        maps.put("class","AfoodContaPedido") ;
        maps.put("method","consultarAbertos") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(fConsultaPedido).getUrl();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                  Dao.getContaPedidoADO(ctx).contaPedidoAdd(j.getJSONArray("data"));
                  oK() ;
            } else {
                erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public abstract void oK() ;
    public abstract void erro(String mgg) ;
}
