package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoProdutos extends ConexaoServer{

    public ConexaoProdutos(Context ctx){
        super(ctx);
        try {
            msg = "Atualizando Produtos...";
            maps.put("class","AfoodProduto") ;
            maps.put("method","consultar") ;
            maps.put("MAC", UtilSet.getMAC(ctx)) ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaProduto).getUrl() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erro(e.getMessage());
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
               Dao.getProdutoADO(ctx).setJSON(j.getJSONArray("data"));
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
