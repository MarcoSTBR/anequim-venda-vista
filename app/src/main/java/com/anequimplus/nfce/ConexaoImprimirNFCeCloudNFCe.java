package com.anequimplus.nfce;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.utilitarios.ConfiguracaoCloudNFceNFCe;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConexaoImprimirNFCeCloudNFCe extends ConexaoCloudNFCe{

    private ContaPedidoNFCe contaPedidoNFCe ;
    private ListenerImprimirNFCe listenerImprimirNFCe ;

    public ConexaoImprimirNFCeCloudNFCe(Activity ctx, ContaPedidoNFCe contaPedidoNFCe, ListenerImprimirNFCe listenerImprimirNFCe)  throws MalformedURLException, JSONException {
        super(ctx);
        method = "GET" ;
        this.contaPedidoNFCe = contaPedidoNFCe;
        this.listenerImprimirNFCe = listenerImprimirNFCe;
        url =  new URL(ConfiguracaoCloudNFceNFCe.getLinkCloudNFce(ctx)+"nfce/"+contaPedidoNFCe.getChave()) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("imprimirNFce", s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getBoolean("sucesso")) {
                String jxml = j.getString("xml") ;
                byte[] bt = Base64.decode(jxml.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
                String xml = new String(bt);
                Log.i("imprimirNFce", xml) ;
                listenerImprimirNFCe.ok(xml);
            } else listenerImprimirNFCe.erro(j.getString("codigo")+" "+j.getString("mensagem"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerImprimirNFCe.erro(s);
        }

    }
}
