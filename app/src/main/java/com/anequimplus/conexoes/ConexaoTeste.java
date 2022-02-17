package com.anequimplus.conexoes;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ConexaoTeste extends ConexaoServer {

    public ConexaoTeste(Context ctx)
    {
        super(ctx);
      //  setParametros() ;
    }

/*
    private void setParametros() {
        JSONObject j = new JSONObject() ;
        try {
            UtilSet.getAutenticacao(ctx, j);
            j.put("TESTE","Teste Conexao") ;
            j.put("MAC", UtilSet.getMAC(ctx)) ;
            //nParm = j.toString() ;
            url = new URL(UtilSet.getServidor(ctx)+"/testeconexao/") ;
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }
*/

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            String ms = (String) j.get("MENSAGEM");
            if (j.getInt("CODRETORNO") == 1){
                oK(ms);
            } else {
                erro(ms);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public abstract void oK(String msg) ;
    public abstract void erro(String msg) ;
}
