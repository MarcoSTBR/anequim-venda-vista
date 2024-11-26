package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.entity.ContaPedidoDest;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerContaPedidoDest;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPedidoDest extends ConexaoServer{

    private ListenerContaPedidoDest listenerContaPedidoDest ;
    private TipoConexao tipoConexao ;


    public ConexaoContaPedidoDest(Activity ctx, FilterTables filters, String order, ListenerContaPedidoDest listenerContaPedidoDest) throws MalformedURLException {
        super(ctx);
        msg = "Destinatário..." ;
        method = "GET" ;
        this.listenerContaPedidoDest = listenerContaPedidoDest;
        maps.put("filters", filters.getJSON()) ;
        maps.put("order", order) ;
        tipoConexao = TipoConexao.cxConsultar ;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido_dest") ;
    }

    public ConexaoContaPedidoDest(Activity ctx, ContaPedidoDest contaPedidoDest, ListenerContaPedidoDest listenerContaPedidoDest) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Destinatário..." ;
        method = "POST" ;
        this.listenerContaPedidoDest = listenerContaPedidoDest;
        maps.put("data", contaPedidoDest.getExportacaoJSON()) ;
        if (contaPedidoDest.getId() == 0)
            url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido_dest") ;
         else url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido_dest/"+contaPedidoDest.getId()) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("ContaPedidoDest", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerContaPedidoDest.ok(getList(j.getJSONArray("data")));
            } else {
                listenerContaPedidoDest.erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerContaPedidoDest.erro(e.getMessage()) ;
        }
    }

    private List<ContaPedidoDest> getList(JSONArray j) throws JSONException {
        List<ContaPedidoDest> t = new ArrayList<ContaPedidoDest>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new ContaPedidoDest(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
