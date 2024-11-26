package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Usuario;
import com.anequimplus.listeners.ListenerUsuario;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.TokenSet;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoUsuario extends ConexaoServer{

    private ListenerUsuario listenerUsuario ;
    private TipoConexao tipoConexao ;
    private String t ;

    public ConexaoUsuario(Activity ctx, FilterTables filters, String order, ListenerUsuario listenerUsuario)  {
        super(ctx);
        t = TokenSet.getToken(ctx) ;
        method = "GET" ;
        msg = "Consultando Usuários" ;
        maps.put("class", "AfoodUsuario") ;
        maps.put("method", "loadAll") ;
        maps.put("filters", filters.getJSON()) ;
        maps.put("order", order) ;
        this.listenerUsuario = listenerUsuario;
        this.tipoConexao = TipoConexao.cxConsultar;
        try {
            url = new URL(UtilSet.getServidorMaster(ctx));
        } catch (MalformedURLException e){
            e.printStackTrace();
            listenerUsuario.erro(0, e.getMessage());
        }
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("usuario", "tk "+t) ;
        Log.i("usuario", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerUsuario.ok(getJSON(j.getJSONArray("data")));
            } else listenerUsuario.erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerUsuario.erro(codInt, s+" "+e.getMessage());
        }
    }

    private List<Usuario> getJSON(JSONArray j) throws JSONException {
        List<Usuario> l = new ArrayList<Usuario>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            l.add(new Usuario(j.getJSONObject(i))) ;
        }
        return l;
    }
}
