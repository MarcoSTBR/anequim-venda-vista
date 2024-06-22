package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImpTerminal;
import com.anequimplus.listeners.ListenerSetorImpTerminal;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoSetorImpTerminalCompartilhado extends ConexaoServer{

    private ListenerSetorImpTerminal listenerSetorImpTerminal ;
    private TipoConexao tipoConexao ;

    public ConexaoSetorImpTerminalCompartilhado(Context ctx, FilterTables filter, String order, ListenerSetorImpTerminal listenerSetorImpTerminal) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Setor Terminal...." ;
        method = "GET" ;
        maps.put("filters", filter.getJSON()) ;
        maps.put("order", order) ;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        tipoConexao = TipoConexao.cxConsultar ;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/setor_imp_terminal") ;
    }

    public ConexaoSetorImpTerminalCompartilhado(Context ctx, List<SetorImpTerminal> listSetorimpTermials, ListenerSetorImpTerminal listenerSetorImpTerminal) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Gravando Setores Terminais..." ;
        method= "POST" ;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        tipoConexao = TipoConexao.cxIncluir ;
        maps.put("data",getJSON(listSetorimpTermials)) ;
        url = new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/setor_imp_terminal") ;
    }

    public ConexaoSetorImpTerminalCompartilhado(Context ctx, SetorImpTerminal setorImpTerminal, ListenerSetorImpTerminal listenerSetorImpTerminal) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Gravando Setores Terminais..." ;
        method= "POST" ;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        tipoConexao = TipoConexao.cxIncluir ;
        maps.put("data",setorImpTerminal.getJSON()) ;
        url = new URL(Configuracao.getLinkComandaRemota(ctx)+"/setor_imp_terminal") ;
    }

    public ConexaoSetorImpTerminalCompartilhado(Context ctx, FilterTables filter, ListenerSetorImpTerminal listenerSetorImpTerminal) throws MalformedURLException {
        super(ctx);
        msg = "Excluindo Setor Terminais...";
        method = "DELETE" ;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        tipoConexao = TipoConexao.cxDeletar ;
        maps.put("filters",filter.getJSON()) ;
        url = new URL(Configuracao.getLinkComandaRemota(ctx)+"/setor_imp_terminal") ;
    }

    private JSONArray getJSON(List<SetorImpTerminal> l) throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (SetorImpTerminal s : l){
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
                  listenerSetorImpTerminal.ok(getJSON(j.getJSONArray("data")));
                } else listenerSetorImpTerminal.erro(j.getString("data"));
        } catch (JSONException e){
            e.printStackTrace();
            listenerSetorImpTerminal.erro(e.getMessage());
        }
    }

    private List<SetorImpTerminal> getJSON(JSONArray j) throws JSONException {
        List<SetorImpTerminal> t = new ArrayList<SetorImpTerminal>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new SetorImpTerminal(j.getJSONObject(i))) ;
        }
        return t ;
    }
}

