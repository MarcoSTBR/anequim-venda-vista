package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Acompanhamento_Item;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerAcompanhamentoItem;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoAcompanhamentoItem extends ConexaoServer{

    private Activity ctx ;
    private ListenerAcompanhamentoItem listenerAcompanhamentoItem ;
    private TipoConexao tipoConexao ;

    public ConexaoAcompanhamentoItem(Activity ctx, FilterTables filters, String order, ListenerAcompanhamentoItem listenerAcompanhamentoItem) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "Itens de Acompanhamento..." ;
        this.ctx = ctx;
        this.listenerAcompanhamentoItem = listenerAcompanhamentoItem;
        maps.put("class","AfoodAcompanhamentoItem") ;
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
                List<Acompanhamento_Item> l = getJSON(j.getJSONArray("data")) ;
                DaoDbTabela.getAcompanhamento_ItemADO(ctx).setJSON(l) ;
                listenerAcompanhamentoItem.ok(l);
            } else listenerAcompanhamentoItem.erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerAcompanhamentoItem.erro(codInt,s+" "+e.getMessage());
        }
    }

    private List<Acompanhamento_Item> getJSON(JSONArray j) throws JSONException {
        List<Acompanhamento_Item> l = new ArrayList<Acompanhamento_Item>();
        for (int i = 0 ; i < j.length() ; i++) {
            l.add(new Acompanhamento_Item(j.getJSONObject(i))) ;
        }
        return l ;
    }
}
