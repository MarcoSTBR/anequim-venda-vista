package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImpItens;
import com.anequimplus.listeners.ListenerSetorImpItens;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoServerSetorImpItens extends ConexaoServer{

    private FilterTables filter ;
    private String order ;
    private ListenerSetorImpItens listenerSetorImpItens ;

    public ConexaoServerSetorImpItens(Context ctx, FilterTables filter, String order, ListenerSetorImpItens listenerSetorImpItens) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Setores Itens..." ;
        this.filter = filter;
        this.order = order;
        this.listenerSetorImpItens = listenerSetorImpItens;
        maps.put("class", "AfoodSetorImpItens") ;
        maps.put("method", "loadAll") ;
        maps.put("filters", filter.getJSON()) ;
        maps.put("order", order) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("setor", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerSetorImpItens.ok(getJSON(j.getJSONArray("data")));
            } else listenerSetorImpItens.erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerSetorImpItens.erro(s+" "+e.getMessage());
        }
    }

    private List<SetorImpItens> getJSON(JSONArray j) throws JSONException {
        List<SetorImpItens> t = new ArrayList<SetorImpItens>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new SetorImpItens(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
