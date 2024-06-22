package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.ProdutoNfce;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoProdutoNfce extends ConexaoServer {

    public ConexaoProdutoNfce(Context ctx, FilterTable filterTable) throws MalformedURLException {
        super(ctx);
        msg = "Consulta Produto NFce" ;
        maps.put("class", "AfoodProdutoNfce");
        maps.put("class", "loadAll");
        maps.put("filters", filterTable.getJSON()) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("ConexaoProdutoNfce", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                ok(getList(j.getJSONArray("data"))) ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<ProdutoNfce> getList(JSONArray jarr) throws JSONException {
        List<ProdutoNfce> jaa = new ArrayList<ProdutoNfce>() ;
        for (int i = 0 ; i < jarr.length() ; i++) {
            jaa.add(new ProdutoNfce(jarr.getJSONObject(i))) ;
        }
        return jaa ;
    }
    public abstract void ok(List<ProdutoNfce> l) ;
    public abstract void erro(String msg) ;

}
