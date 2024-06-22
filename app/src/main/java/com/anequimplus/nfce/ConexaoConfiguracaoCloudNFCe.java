package com.anequimplus.nfce;

import android.content.Context;
import android.util.Log;

import com.anequimplus.conexoes.ConexaoServer;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoConfiguracaoCloudNFCe extends ConexaoServer {

    private FilterTables filters ;
    private ListenerConfiguracaoCloudNFCe listenerConfiguracaoCloudNFCe ;

    public ConexaoConfiguracaoCloudNFCe(Context ctx, FilterTables filters, ListenerConfiguracaoCloudNFCe listenerConfiguracaoCloudNFCe) throws MalformedURLException {
        super(ctx);
        this.listenerConfiguracaoCloudNFCe = listenerConfiguracaoCloudNFCe ;
        msg = "Recuperando Token" ;
        maps.put("class", "AfoodCloudNFCe" );
        maps.put("method", "loadAll" );
        maps.put("filters", filters);
        url =  new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public void getToken(){
        super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("clod", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerConfiguracaoCloudNFCe.ok(j.getJSONArray("data").getJSONObject(0).getString("TOKEN")) ;
            } else listenerConfiguracaoCloudNFCe.erro(codInt, j.getString("data")) ;
        } catch (JSONException e) {
            e.printStackTrace();
            listenerConfiguracaoCloudNFCe.erro(codInt, e.getMessage()) ;
        }
    }

}
