package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.ContaPedidoView;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.listeners.ListenerContaPedidoView;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPedidoView extends ConexaoServer{
    private List<FilterTable> filters ;
    private String order ;
    private ListenerContaPedidoView listenerContaPedidoView ;

    public ConexaoContaPedidoView(Context ctx, List<FilterTable> filters, String order, ListenerContaPedidoView listenerContaPedidoView) throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        msg = "Contas Abertas..." ;
        maps.put("filters", getJSON(filters)) ;
        maps.put("order", order) ;
        this.listenerContaPedidoView = listenerContaPedidoView;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx) + "/conta_pedido_view");
    }

    private JSONArray getJSON(List<FilterTable> filters){
        JSONArray j = new JSONArray() ;
        for (FilterTable f : filters){
            j.put(f.getJSON()) ;
        }
        return j ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("ConexaoContaPedidoView", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                listenerContaPedidoView.ok(getJSON(j.getJSONArray("data"))) ;
            } else {
                listenerContaPedidoView.erro(codInt, j.getString("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            listenerContaPedidoView.erro(codInt, s);
        }

    }

    private List<ContaPedidoView> getJSON(JSONArray data) throws JSONException {
        List<ContaPedidoView> l = new ArrayList<>() ;
        for (int i = 0 ; i < data.length() ; i++){
            l.add(new ContaPedidoView(data.getJSONObject(i))) ;
        }
      //  Collections.sort(l) ;
        return l ;
    }


}
