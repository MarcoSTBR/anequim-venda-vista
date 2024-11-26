package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoGradeVendas extends ConexaoServer {


    public ConexaoGradeVendas(Activity ctx)  {
        super(ctx);
        try {
          msg = "Grade de Vendas..." ;
          maps.put("class", "AfoodGradeVendas");
          maps.put("method", "consultar");
          maps.put("MAC", UtilSet.getMAC(ctx));
          url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fGradeVendas).getUrl();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
       // super.onPostExecute(s);
        Log.i("ConexaoGradeVendas", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                DaoDbTabela.getGradeVendasADO(ctx).setGradeVendas(j.getJSONArray("data"));
              //  progressDialog.dismiss();
                oK() ;
            } else {
              //  progressDialog.dismiss();
                erro(j.getString("data")) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //progressDialog.dismiss();
            erro(s);
        }

    }

    public abstract void oK() ;
    public abstract void erro(String msg) ;

}
