package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Sangria;
import com.anequimplus.listeners.ListenerSangria;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoSangriaCompartilhada extends ConexaoServer{

    private FilterTables filters ;
    private Sangria sangria ;
    private TipoConexao tipoConexao ;
    private ListenerSangria listenerSangria ;

    public ConexaoSangriaCompartilhada(Context ctx, FilterTables filters, ListenerSangria listenerSangria) throws MalformedURLException {
        super(ctx);
        msg = "Sangria..." ;
        method = "GET" ;
        this.filters = filters;
        this.listenerSangria = listenerSangria;
        maps.put("filters", filters.getJSON()) ;
        tipoConexao = TipoConexao.cxConsultar ;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/sangria") ;
    }

    public ConexaoSangriaCompartilhada(Context ctx, Sangria sangria, ListenerSangria listenerSangria) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Sangria..." ;
        method = "POST" ;
        this.sangria = sangria;
        this.listenerSangria = listenerSangria;
        maps.put("data", sangria.getExportacaoJSON()) ;
        if (sangria.getId() == 0)
            url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/sangria") ;
        else url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/sangria/"+sangria.getId()) ;
    }

    public void executar(){
        super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("sangria", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerSangria.ok(getList(j.getJSONArray("data")));
            } else {
                listenerSangria.erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerSangria.erro(e.getMessage()) ;
        }
    }

    private List<Sangria> getList(JSONArray j) throws JSONException {
        List<Sangria> t = new ArrayList<Sangria>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new Sangria(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
