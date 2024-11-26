package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImpTerminal;
import com.anequimplus.listeners.ListenerSetorImpTerminal;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoServerSetorImpTerminal extends ConexaoServer{

    private FilterTables filter ;
    private String order ;
    private ListenerSetorImpTerminal listenerSetorImpTerminal ;

    public ConexaoServerSetorImpTerminal(Activity ctx, FilterTables filter, String order, ListenerSetorImpTerminal listenerSetorImpTerminal) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Setores..." ;
        this.filter = filter;
        this.order = order;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        maps.put("class", "AfoodSetorImpTerminal") ;
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
                listenerSetorImpTerminal.ok(getJSON(j.getJSONArray("data")));
            } else listenerSetorImpTerminal.erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerSetorImpTerminal.erro(s+" "+e.getMessage());
        }
    }

    private List<SetorImpTerminal> getJSON(JSONArray j) throws JSONException {
        List<SetorImpTerminal> t = new ArrayList<SetorImpTerminal>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new SetorImpTerminal(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
