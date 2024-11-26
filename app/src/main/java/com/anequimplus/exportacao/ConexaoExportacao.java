package com.anequimplus.exportacao;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.conexoes.ConexaoServer;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoExportacao extends ConexaoServer {

    private ListenerExportacao listenerExportacao ;
    private JSONArray l ;
    protected RegExport regExport ;

    public ConexaoExportacao(Activity ctx, RegExport regExport, ListenerExportacao listenerExportacao) {
        super(ctx);
        msg = "Exportando...." ;
        this.listenerExportacao = listenerExportacao ;
        this.regExport = regExport ;
        l = getDados() ;
        maps.put("class", getClasse()) ;
        maps.put("method", getMethod()) ;
        maps.put(getNomeParam(), l) ;
        try {
            url = new URL(UtilSet.getServidorMaster(ctx)) ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerExportacao.erro(regExport, 0, e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("ConexaoExportacaoE", l.toString()) ;
        Log.i("ConexaoExportacaoR", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerExportacao.ok(regExport);
            } else listenerExportacao.erro(regExport, codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerExportacao.erro(regExport, codInt, getClasse()+" "+s);
        }
    }

    public RegExport getRegExport(){
        return regExport ;
    }

    public abstract String getClasse() ;
    public abstract String getMethod() ;
    public abstract String getNomeParam() ;
    public abstract JSONArray getDados() ;

}
