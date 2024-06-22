package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImp;
import com.anequimplus.listeners.ListenerSetorImp;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoSetorImpCompartilhado extends ConexaoServer{

    private FilterTables filter ;
    private String order ;
    private SetorImp setorImp ;
    private List<SetorImp> setorImps ;
    private ListenerSetorImp listenerSetorImp ;
    private TipoConexao tipoConexao ;

    public ConexaoSetorImpCompartilhado(Context ctx, FilterTables filter, String order, ListenerSetorImp listenerSetorImp) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Setor Impressora...." ;
        method = "GET" ;
        maps.put("filters", filter.getJSON()) ;
        maps.put("order", order) ;
        this.listenerSetorImp = listenerSetorImp;
        tipoConexao = TipoConexao.cxConsultar ;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/setor_imp") ;
    }

    public ConexaoSetorImpCompartilhado(Context ctx, List<SetorImp> setorImps, ListenerSetorImp listenerSetorImp) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Gravando Setores..." ;
        method= "POST" ;
        this.setorImps = setorImps;
        this.listenerSetorImp = listenerSetorImp;
        tipoConexao = TipoConexao.cxIncluir ;
        maps.put("data",getJSON(setorImps)) ;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/setor_imp") ;
    }

    public ConexaoSetorImpCompartilhado(Context ctx, SetorImp setorImp, ListenerSetorImp listenerSetorImp) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Gravando Setores..." ;
        method= "POST" ;
        this.listenerSetorImp = listenerSetorImp;
        tipoConexao = TipoConexao.cxIncluir ;
        maps.put("data",setorImp.getJSON()) ;
        url = new URL(Configuracao.getLinkComandaRemota(ctx)+"/setor_imp") ;
    }

    public ConexaoSetorImpCompartilhado(Context ctx, FilterTables filter, ListenerSetorImp listenerSetorImp) throws MalformedURLException {
        super(ctx);
        msg = "Excluindo Setor Impressora...";
        method = "DELETE" ;
        this.filter = filter;
        this.listenerSetorImp = listenerSetorImp;
        tipoConexao = TipoConexao.cxDeletar ;
        maps.put("filters",filter.getJSON()) ;
        url = new URL(Configuracao.getLinkComandaRemota(ctx)+"/setor_imp") ;
    }


    private JSONArray getJSON(List<SetorImp> l) throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (SetorImp s : l){
            jaa.put(s.getJSON()) ;
        }
        return jaa ;
    }



    public void executar(){
        super.execute() ;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("setor", codInt + " " + s);
        try
        {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                  listenerSetorImp.ok(getJSON(j.getJSONArray("data")));
                } else listenerSetorImp.erro(j.getString("data"));
        } catch (JSONException e){
            e.printStackTrace();
            listenerSetorImp.erro(e.getMessage());
        }
    }

    private List<SetorImp> getJSON(JSONArray j) throws JSONException {
        List<SetorImp> t = new ArrayList<SetorImp>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new SetorImp(j.getJSONObject(i))) ;
        }
        return t ;
    }
}

