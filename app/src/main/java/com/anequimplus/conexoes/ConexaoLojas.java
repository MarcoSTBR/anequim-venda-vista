package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Loja;
import com.anequimplus.listeners.ListenerLoja;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoLojas extends ConexaoServer {

    private FilterTables filterTables;
    private String order;
    private ListenerLoja listenerLoja;
    private int modo = 0;

    public ConexaoLojas(Context ctx, int id, ListenerLoja listenerLoja) throws MalformedURLException {
        super(ctx);
        modo = 1;
        method = "GET";
        msg = "Consultando Dados da Loja";
        this.filterTables = filterTables;
        this.order = order;
        this.listenerLoja = listenerLoja;
        maps.put("class", "AfoodLojas");
        maps.put("method", "load");
        maps.put("id", id);
        url = new URL(UtilSet.getServidorMaster(ctx));
    }

    public ConexaoLojas(Context ctx, FilterTables filterTables, String order, ListenerLoja listenerLoja) throws MalformedURLException {
        super(ctx);
        method = "GET";
        msg = "Consultando Dados da Loja";
        this.filterTables = filterTables;
        this.order = order;
        this.listenerLoja = listenerLoja;
        maps.put("class", "AfoodLojas");
        maps.put("method", "loadAll");
        maps.put("filters", filterTables.getJSON());
        url = new URL(UtilSet.getServidorMaster(ctx));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("lojas", codInt + " " + s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                if (modo == 0) {
                    listenerLoja.ok(getList(j.getJSONArray("data")));
                } else {
                    JSONArray l = new JSONArray();
                    l.put(j.getJSONObject("data"));
                    listenerLoja.ok(getList(l));
                }
            } else {
                listenerLoja.erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerLoja.erro(e.getMessage());
        }
    }

    private List<Loja> getList(JSONArray j) throws JSONException {
        List<Loja> t = new ArrayList<Loja>();
        for (int i = 0; i < j.length(); i++) {
            t.add(new Loja(j.getJSONObject(i)));
        }
        return t;
    }
}
