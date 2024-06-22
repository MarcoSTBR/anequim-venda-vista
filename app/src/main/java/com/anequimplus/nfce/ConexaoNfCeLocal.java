package com.anequimplus.nfce;

import android.content.Context;
import android.util.Log;

import com.anequimplus.conexoes.ConexaoServer;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoNfCeLocal extends ConexaoServer {

    private ListenerNFCeLocal listenerNFCeLocal ;
    public ConexaoNfCeLocal(Context ctx, JSONObject j, ListenerNFCeLocal listenerNFCeLocal) throws MalformedURLException {
        super(ctx);
        this.listenerNFCeLocal = listenerNFCeLocal ;
        msg = "Emitindo NFce...";
        maps.put("data", j) ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/emitir_nfce") ;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("NfCeLocal",codInt+" "+s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                if (j.getJSONObject("data").getString ("RESP").equals("OK")) {
                    listenerNFCeLocal.ok(j.getJSONObject("data"));
                } else listenerNFCeLocal.erro(j.getJSONObject("data").getString("MENSAGEM"));
            } else listenerNFCeLocal.erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerNFCeLocal.erro("NfCeLocal"+s);
        }

    }
}
