package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoGradeVendas extends ConexaoServer {


    public ConexaoGradeVendas(Context ctx) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, MalformedURLException {
        super(ctx);
        msg = "Grade de Vendas" ;
        maps.put("class", "AfoodGradeVendas");
        maps.put("method", "consultar");
        maps.put("chave", UtilSet.getChave(ctx));
        maps.put("loja_id", UtilSet.getLojaId(ctx));
        maps.put("MAC", UtilSet.getMAC(ctx));
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fGradeVendas).getUrl();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("ConexaoGradeVendas", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                Dao.getGradeVendasADO(this.ctx).setJSON(j.getJSONArray("data"));
                oK() ;
            } else {
                erro(j.getString("data")) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }

    public abstract void oK() ;
    public abstract void erro(String msg) ;

}
