package com.anequimplus.nfce;

import android.content.Context;
import android.util.Log;

import com.anequimplus.utilitarios.ConfiguracaoCloudNFceNFCe;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoCloudNFceCertificado extends ConexaoCloudNFCe{

    private ListenerCloudNFCeCertificado listenerCloudNFCeCertificado ;

    public ConexaoCloudNFceCertificado(Context ctx, ListenerCloudNFCeCertificado listenerCloudNFCeCertificado) throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        this.listenerCloudNFCeCertificado = listenerCloudNFCeCertificado ;
        url = new URL(ConfiguracaoCloudNFceNFCe.getLinkCloudNFce(ctx)+"certificado") ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("cluodnfce", method+" codigo "+codigoStatus+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getBoolean("sucesso")) {
                listenerCloudNFCeCertificado.ok(j.getString("mensagem"), UtilSet.getData( j.getString("validade")));
            } else listenerCloudNFCeCertificado.erro(j.getInt("codigo"), j.getString("mensagem"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerCloudNFCeCertificado.erro(codigoStatus, s);
        }
    }
}
