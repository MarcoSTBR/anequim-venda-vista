package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerContaPedidoNfce;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPedidoNFCeCompartilhado extends ConexaoServer{


    private ListenerContaPedidoNfce listenerEmissaoNfce ;
    private TipoConexao tipoConexao ;

    public ConexaoContaPedidoNFCeCompartilhado(Activity ctx, FilterTables filter, String order, ListenerContaPedidoNfce listenerEmissaoNfce) throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        msg = "Consultando NFCe.." ;
        this.listenerEmissaoNfce = listenerEmissaoNfce;
        maps.put("filters", filter.getJSON()) ;
        maps.put("order", order) ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido_nfce") ;
    }

    public ConexaoContaPedidoNFCeCompartilhado(Activity ctx, ContaPedidoNFCe contaPedidoNFCe, TipoConexao tipoConexao, ListenerContaPedidoNfce listenerEmissaoNfce) throws MalformedURLException, JSONException {
        super(ctx);
        this.method = "POST" ;
        this.listenerEmissaoNfce = listenerEmissaoNfce;
        maps.put("data", contaPedidoNFCe.getExportacaoJSON()) ;
        if (tipoConexao == TipoConexao.cxIncluir) {
            msg = "Incluindo NFCe.." ;
            url = new URL(Configuracao.getLinkContaCompartilhada(ctx) + "/conta_pedido_nfce");
        } else {
            msg = "Alterando NFCe.." ;
            url = new URL(Configuracao.getLinkContaCompartilhada(ctx) + "/conta_pedido_nfce/" + contaPedidoNFCe.getUUID());
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("ContaPedidoNFCe", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerEmissaoNfce.ok(getList(j.getJSONArray("data")));
            } else {
                listenerEmissaoNfce.erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEmissaoNfce.erro(e.getMessage()) ;
        }
    }

    private List<ContaPedidoNFCe> getList(JSONArray j) throws JSONException {
        List<ContaPedidoNFCe> t = new ArrayList<ContaPedidoNFCe>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new ContaPedidoNFCe(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
