package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.listeners.ListenerEnvioComandaRemota;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoEnvioContaPedidoComandaRemota extends ConexaoServer{

    private JSONArray json ;
    private ListenerEnvioComandaRemota listenerEnvioComandaRemota ;

    public ConexaoEnvioContaPedidoComandaRemota(Context ctx, JSONArray json, ListenerEnvioComandaRemota listenerEnvioComandaRemota) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "Comanda Remota" ;
        this.json = json ;
        this.listenerEnvioComandaRemota = listenerEnvioComandaRemota ;
        maps.put("data", json) ;
        url = new URL(Configuracao.getLinkComandaRemota(ctx)+"/conta_pedido_remoto") ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("enviocomandaremota", codInt +" "+ s) ;
        try {
                JSONObject j = new JSONObject(s) ;
               if (j.getString("status").equals("success"))
                   listenerEnvioComandaRemota.ok(j.getString("data")) ;
                  else   listenerEnvioComandaRemota.erro(j.getString("data")) ;
            } catch (JSONException e) {
                e.printStackTrace();
                listenerEnvioComandaRemota.erro(s);
            }
    }
}
