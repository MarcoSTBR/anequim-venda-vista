package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Acompanhamento_produto;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerAcompanhamentoProduto;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoAcompanhamentoProduto extends ConexaoServer{

    private Context ctx ;
    private ListenerAcompanhamentoProduto listenerAcompanhamentoProduto ;
    private TipoConexao tipoConexao ;


    public ConexaoAcompanhamentoProduto(Context ctx, FilterTables filters, String order, ListenerAcompanhamentoProduto listenerAcompanhamentoProduto) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "Acompanhamentos de Produtos..." ;
        this.listenerAcompanhamentoProduto = listenerAcompanhamentoProduto;
        maps.put("class","AfoodAcompanhamentoProduto") ;
        maps.put("method","loadAll") ;
        maps.put("filters",filters.getJSON()) ;
        maps.put("order", order) ;
        tipoConexao = TipoConexao.cxConsultar ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
        Log.i("Acompanhamento", url.toString()) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Acompanhamento", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                List<Acompanhamento_produto> l = getJSON(j.getJSONArray("data")) ;
                DaoDbTabela.getAcompanhamanto_ProdutoADO(ctx).setJSON(l) ;
                listenerAcompanhamentoProduto.ok(l);
            } else listenerAcompanhamentoProduto.erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerAcompanhamentoProduto.erro(codInt,s+" "+e.getMessage());
        }
    }

    private List<Acompanhamento_produto> getJSON(JSONArray j) throws JSONException {
        List<Acompanhamento_produto> l = new ArrayList<Acompanhamento_produto>();
        for (int i = 0 ; i < j.length() ; i++) {
            l.add(new Acompanhamento_produto(j.getJSONObject(i))) ;
        }
        return l ;
    }

}
