package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImpItens;
import com.anequimplus.listeners.ListenerSetorImpItens;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoSetorImpItensCompartilhado extends ConexaoServer{

    private ListenerSetorImpItens listenerSetorImpItens ;
    private TipoConexao tipoConexao ;

    public ConexaoSetorImpItensCompartilhado(Context ctx, FilterTables filter, String order, ListenerSetorImpItens listenerSetorImpItens) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Setor Impressora...." ;
        method = "GET" ;
        tipoConexao = TipoConexao.cxConsultar ;
        maps.put("filters", filter.getJSON()) ;
        maps.put("order", order) ;
        this.listenerSetorImpItens = listenerSetorImpItens;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/setor_imp_itens") ;
    }

    public ConexaoSetorImpItensCompartilhado(Context ctx, List<SetorImpItens> setorImps, ListenerSetorImpItens listenerSetorImpItens) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Gravando Setores..." ;
        method= "POST" ;
        tipoConexao = TipoConexao.cxIncluir ;
        maps.put("data",getJSON(setorImps)) ;
        this.listenerSetorImpItens = listenerSetorImpItens;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/setor_imp_itens") ;
    }

    public ConexaoSetorImpItensCompartilhado(Context ctx, SetorImpItens setorImp, ListenerSetorImpItens listenerSetorImpItens) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Gravando Setores..." ;
        method= "POST" ;
        maps.put("data",setorImp.getJSON()) ;
        tipoConexao = TipoConexao.cxIncluir ;
        this.listenerSetorImpItens = listenerSetorImpItens;
        url = new URL(Configuracao.getLinkComandaRemota(ctx)+"/setor_imp_itens") ;
    }

    public ConexaoSetorImpItensCompartilhado(Context ctx, FilterTables filter, ListenerSetorImpItens listenerSetorImpItens) throws MalformedURLException {
        super(ctx);
        msg = "Excluindo Setores..." ;
        method = "DELETE" ;
        tipoConexao = TipoConexao.cxDeletar ;
        maps.put("filters",filter.getJSON()) ;
        this.listenerSetorImpItens = listenerSetorImpItens;
        url = new URL(Configuracao.getLinkComandaRemota(ctx)+"/setor_imp_itens") ;
    }


    private JSONArray getJSON(List<SetorImpItens> l) throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (SetorImpItens s : l){
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
        Log.i("setor", codInt+" "+s) ;
        try
        {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                listenerSetorImpItens.ok(getJSON(j.getJSONArray("data")));
            } else listenerSetorImpItens.erro(j.getString("data"));
        } catch (JSONException e){
            e.printStackTrace();
            listenerSetorImpItens.erro(s+" "+e.getMessage());
        }
    }

    private List<SetorImpItens> getJSON(JSONArray j) throws JSONException {
        List<SetorImpItens> t = new ArrayList<SetorImpItens>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new SetorImpItens(j.getJSONObject(i))) ;
        }
        return t ;
    }
}

