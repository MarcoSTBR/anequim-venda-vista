package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ProdutoImposto;
import com.anequimplus.listeners.ListenerProdutoImposto;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoProdutoImposto extends ConexaoServer{

    private FilterTables filters ;
    private String order ;
    private ProdutoImposto produtoImposto;
    private ListenerProdutoImposto listenerProdutoImposto ;
    private TipoConexao tipoConexao ;

    public ConexaoProdutoImposto(Context ctx, FilterTables filters, String order, ListenerProdutoImposto listenerProdutoImposto) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        this.filters = filters;
        this.order = order;
        this.listenerProdutoImposto = listenerProdutoImposto;
        tipoConexao = TipoConexao.cxConsultar ;
        maps.put("class", "AfoodProdutoNfce") ;
        maps.put("method", "loadAll") ;
        maps.put("filters", filters.getJSON()) ;
        maps.put("order", order) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("produto_importo", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerProdutoImposto.ok(getList(j.getJSONArray("data")));
            } else {
                listenerProdutoImposto.erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerProdutoImposto.erro(e.getMessage()) ;
        }
    }

    private List<ProdutoImposto> getList(JSONArray j) throws JSONException {
        List<ProdutoImposto> t = new ArrayList<ProdutoImposto>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new ProdutoImposto(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
