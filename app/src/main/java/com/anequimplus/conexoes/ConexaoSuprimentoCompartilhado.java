package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Suprimento;
import com.anequimplus.listeners.ListenerSuprimento;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoSuprimentoCompartilhado extends ConexaoServer{

    private FilterTables filters ;
    private Suprimento suprimento ;
    private TipoConexao tipoConexao ;
    private ListenerSuprimento listenerSuprimento ;

    public ConexaoSuprimentoCompartilhado(Context ctx, FilterTables filters, ListenerSuprimento listenerSuprimento) throws MalformedURLException {
        super(ctx);
        msg = "Suprimento..." ;
        method = "GET" ;
        this.filters = filters;
        this.listenerSuprimento = listenerSuprimento ;
        maps.put("filters", filters.getJSON()) ;
        tipoConexao = TipoConexao.cxConsultar ;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/suprimento") ;
    }

    public ConexaoSuprimentoCompartilhado(Context ctx, Suprimento suprimento, ListenerSuprimento listenerSuprimento) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Suprimento..." ;
        method = "POST" ;
        this.suprimento = suprimento;
        this.listenerSuprimento = listenerSuprimento;
        maps.put("data",suprimento.getExportacaoJSON()) ;
        tipoConexao = TipoConexao.cxIncluir ;
        if (suprimento.getId() == 0)
           url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/suprimento") ;
        else url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/suprimento/"+suprimento.getId()) ;
    }

    public void executar(){
        super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("suprimento", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerSuprimento.ok(getList(j.getJSONArray("data")));
            } else {
                listenerSuprimento.erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerSuprimento.erro(e.getMessage()) ;
        }
    }

    private List<Suprimento> getList(JSONArray j) throws JSONException {
        List<Suprimento> t = new ArrayList<Suprimento>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new Suprimento(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
