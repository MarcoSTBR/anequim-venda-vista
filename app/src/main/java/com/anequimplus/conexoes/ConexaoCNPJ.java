package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoCNPJ extends ConexaoServer{
    private String cnpj ;
    public ConexaoCNPJ(Activity ctx, String cnpj) {
        super(ctx);
        this.cnpj = cnpj;
        token = "" ;
        maps.put("class", "AfoodCNPJ");
        maps.put("method", "autenticacao");
        maps.put("cnpj", cnpj);
        try {
           // url =  new URL(UtilSet.getServidorMaster(ctx)) ;
            url =  new URL("https://gileade.com.br/anequimfood/rest.php") ;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }

/*

        try
        {
            JSONArray jarr = new JSONArray() ;
            JSONObject j = new JSONObject() ;
            j.put("id",1) ;
            j.put("unidade",this.cnpj) ;
            j.put("empresa","") ;
            j.put("imei", "") ;
            j.put("matricula","");
            j.put("MAC",UtilSet.getMAC(ctx)) ;
            jarr.put(j) ;
            msg = "Pesquisa CNPJ" ;
            maps.put("nameClass", "AdmClienteCNPJService");
            maps.put("data", jarr.toString());
            url = new URL("https://www.gerezim.com.br/strap/rest_client.php?nameClass=AdmClienteCNPJService") ;

        } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
                erro(0, e.getMessage());
        }
*/
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("ConexaoCNPJ", Integer.toString(codInt)+" "+s);
        try {
            Log.i("LOGADO", s) ;
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success"))
            {
                if (j.getJSONArray("data").length() > 0) {
                    JSONObject obj = (JSONObject) j.getJSONArray("data").get(0);
                    UtilSet.setServidorMaster(ctx, obj.getString("URL_MOBILE"));
                   // DaoDbTabela.getLinkAcessoADO(ctx).iniciarPadrao() ;
                    oK(cnpj, obj.getString("URL_MOBILE"));
                } else erro(codInt,"CNPJ "+cnpj+" Inexistente!");
            } else {
                erro(codInt, j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (codInt == 0) {
                erro(codInt, "Sem conexão com "+url.toString());

            } else{
                erro(codInt, e.getMessage());
            }
        }

    }

    public abstract void erro(int cod, String msg) ;
    public abstract void oK(String cnpj, String url) ;
}
