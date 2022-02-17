package com.anequimplus.conexoes;

import static com.anequimplus.tipos.Link.fConsultaPedido;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContaPedido extends ConexaoServer {

    private Link link ;
    private List<FilterTable> filterTables = null ;
    private ContaPedido contaPedido = null ;

    public ConexaoContaPedido(Context ctx, List<FilterTable> filterTables) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg  = "Conta Pedido" ;
        this.filterTables = filterTables;
        link = fConsultaPedido ;
        maps.put("class","AfoodContaPedido") ;
        maps.put("method","consultarAbertos") ;
        maps.put("filters",getFilters(filterTables)) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(fConsultaPedido).getUrl();
    }

    private JSONArray getFilters(List<FilterTable> filterTables){
        JSONArray jaa = new JSONArray() ;
        for (FilterTable f : filterTables) {
            jaa.put(f.getJSON()) ;
        }
        return jaa ;
    }

    public ConexaoContaPedido(Context ctx, ContaPedido contaPedido, Link link) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado, JSONException {
        super(ctx);
        msg  = "Conta Pedido" ;
        this.link = link ;
        this.contaPedido = contaPedido ;
        maps.put("class","AfoodContaPedido") ;
        maps.put("method","consultarAbertos") ;
        maps.put("dados",contaPedido.getJSON()) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(fConsultaPedido).getUrl();
    }

    public void execute(){
        if (Configuracao.getPedidoCompartilhado(ctx)) {
            super.execute() ;
        } else {
           switch (link) {
               case fConsultaPedido: oK(Dao.getContaPedidoInternoDAO(ctx).getList(filterTables, ""));
               break;
               case fAlterarPedido:
                        Dao.getContaPedidoInternoDAO(ctx).alterar(contaPedido);
                        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
                        l.add(contaPedido) ;
                        oK(l);
                   break;
               default: erro("Opção Inválida!");
           }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("contapedido", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                oK(Dao.getContaPedidoInternoDAO(ctx).getJSON(j.getJSONArray("data"))) ;
            } else {
                erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public abstract void oK(List<ContaPedido> l) ;
    public abstract void erro(String mgg) ;
}
