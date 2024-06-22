package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.UsuarioAcesso;
import com.anequimplus.listeners.ListenerUsuarioAcesso;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoUsuarioAcesso extends ConexaoServer{

    private ListenerUsuarioAcesso listenerUsuarioAcesso ;
    private TipoConexao tipoConexao ;

    public ConexaoUsuarioAcesso(Context ctx, FilterTables filters, String order, ListenerUsuarioAcesso listenerUsuarioAcesso) throws MalformedURLException {
        super(ctx);
        msg = "Consulta Acessos" ;
        this.listenerUsuarioAcesso = listenerUsuarioAcesso;
        maps.put("class","AfoodUsuarioAcesso") ;
        maps.put("method","loadAll") ;
        maps.put("filters",filters.getJSON()) ;
        maps.put("order", order) ;
        tipoConexao = TipoConexao.cxConsultar ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }



    public void executar(){
        super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("usuarioAcesso", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerUsuarioAcesso.ok(getJSON(j.getJSONArray("data")));
            } else listenerUsuarioAcesso.erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerUsuarioAcesso.erro(s+" "+e.getMessage());
        }
    }

    private List<UsuarioAcesso> getJSON(JSONArray j) throws JSONException {
        List<UsuarioAcesso> l = new ArrayList<UsuarioAcesso>();
        for (int i = 0 ; i < j.length() ; i++) {
            l.add(new UsuarioAcesso(j.getJSONObject(i))) ;
        }
        return l ;
    }
}
