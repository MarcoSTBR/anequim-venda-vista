package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.VendaVista;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoVendaVista extends ConexaoServer{


    public ConexaoVendaVista(Context ctx, Caixa caixa) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, MalformedURLException
    {
        super(ctx);
        msg = "Consultar Venda a Vista";
        maps.put("class","AfoodVendaVista") ;
        maps.put("method","consultar") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        maps.put("caixa_id", caixa.getId()) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fGetVendaVista).getUrl() ;
    }

    public ConexaoVendaVista(Context ctx, JSONArray list) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, MalformedURLException
    {
        super(ctx);
        msg = "Enviando Venda Vista" ;
        maps.put("class","AfoodVendaVista") ;
        maps.put("method","incluir") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        maps.put("data",list) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fSetVendaVista).getUrl() ;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExVendaVista",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                List<VendaVista> l =  new ArrayList<VendaVista>() ;
                JSONArray ja = j.getJSONArray("data") ;
                for (int i=0 ; i < ja.length() ; i++ ) {
                       l.add(new VendaVista(ctx, ja.getJSONObject(i)));
                }
                Dao.getVendaVistaADO(ctx).excluir();
                Dao.getVendaVistaItemADO(ctx).excluir();
                Dao.getVendaVistaPagamentoADO(ctx).excluir();
                ok(l);
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }

    public abstract void  ok(List<VendaVista> l) ;
    public abstract void  erro(String msg) ;

}
