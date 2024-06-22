package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImp;
import com.anequimplus.listeners.ListenerSetorImp;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoServerSetorImp extends ConexaoServer{

    private FilterTables filter ;
    private String order ;
    private ListenerSetorImp listenerSetorImp ;

    public ConexaoServerSetorImp(Context ctx, FilterTables filter, String order, ListenerSetorImp listenerSetorImp) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Setores..." ;
        this.filter = filter;
        this.order = order;
        this.listenerSetorImp = listenerSetorImp;
        maps.put("class", "AfoodSetorImp") ;
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
                listenerSetorImp.ok(getJSON(j.getJSONArray("data")));
            } else listenerSetorImp.erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerSetorImp.erro(s+" "+e.getMessage());
        }
    }

    private List<SetorImp> getJSON(JSONArray j) throws JSONException {
        List<SetorImp> t = new ArrayList<SetorImp>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new SetorImp(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
