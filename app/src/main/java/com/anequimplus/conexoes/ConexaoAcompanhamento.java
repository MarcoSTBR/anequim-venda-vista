package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Acompanhamento;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerAcompanhamento;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoAcompanhamento extends ConexaoServer{

    private Context ctx ;
    private ListenerAcompanhamento listenerAcompanhamento ;
    private TipoConexao tipoConexao ;

    public ConexaoAcompanhamento(Context ctx, FilterTables filters, String order,ListenerAcompanhamento listenerAcompanhamento) throws MalformedURLException {
        super(ctx);
        msg = "Acompanhamentos..." ;
        method = "POST" ;
        this.ctx = ctx;
        this.listenerAcompanhamento = listenerAcompanhamento;
        maps.put("class","AfoodAcompanhamento") ;
        maps.put("method","loadAll") ;
        maps.put("filters",filters.getJSON()) ;
        maps.put("order", order) ;
        tipoConexao = TipoConexao.cxConsultar ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Acompanhamento", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                List<Acompanhamento> l = getJSON(j.getJSONArray("data")) ;
                DaoDbTabela.getAcompanhamentoADO(ctx).setJSON(l) ;
                listenerAcompanhamento.ok(l);
            } else listenerAcompanhamento.erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerAcompanhamento.erro(codInt,s+" "+e.getMessage());
        }
    }

    private List<Acompanhamento> getJSON(JSONArray j) throws JSONException {
        List<Acompanhamento> l = new ArrayList<Acompanhamento>();
        for (int i = 0 ; i < j.length() ; i++) {
            l.add(new Acompanhamento(j.getJSONObject(i))) ;
        }
        return l ;
    }
}
