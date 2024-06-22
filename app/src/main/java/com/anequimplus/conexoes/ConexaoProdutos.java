package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoProdutos extends ConexaoServer{

    public ConexaoProdutos(Context ctx){
        super(ctx);
        try {
            msg = "Atualizando Produtos...";
            maps.put("class","AfoodProduto") ;
            maps.put("method","consultar") ;
            maps.put("MAC", UtilSet.getMAC(ctx)) ;
            url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaProduto).getUrl() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Produtos", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
               DaoDbTabela.getProdutoADO(ctx).setJSON(j.getJSONArray("data"));
                Ok() ;
            } else {
                erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage()) ;
            Toast.makeText(ctx, "JSON "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void Ok();
    public abstract void erro(String msg);
}
