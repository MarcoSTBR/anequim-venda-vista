package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ConexaoCaixaCompartilhado extends ConexaoServer {

    private FilterTables filters ;
    private Caixa caixa ;
    private ListenerCaixa listenercaixa ;
    private TipoConexao tipoConexao ;

    public ConexaoCaixaCompartilhado(Activity ctx, FilterTables filters, ListenerCaixa listenercaixa)  throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        msg = "Consultando Caixa..." ;
        this.filters = filters;
        this.listenercaixa = listenercaixa;
        maps.put("filters", filters.getJSON()) ;
        tipoConexao = TipoConexao.cxConsultar;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/caixa") ;
    }

    public ConexaoCaixaCompartilhado(Activity ctx, Caixa caixa, ListenerCaixa listenercaixa)  throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "Caixa..." ;
        this.caixa = caixa;
        this.listenercaixa = listenercaixa;
        maps.put("data", getJSON()) ;
        if (caixa.getId() == 0)
            url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/caixa") ;
         else url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/caixa/"+caixa.getId()) ;
        tipoConexao = TipoConexao.cxConsultar;
    }

    private JSONObject getJSON(){
        JSONObject j = caixa.getExportacaoJson() ;
        try {
            j.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;

    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("caixa", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenercaixa.ok(getList(j.getJSONArray("data")));
            } else {
                listenercaixa.erro(codInt+" "+j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenercaixa.erro (codInt+" "+s); ;
        }
         catch (ParseException e) {
            e.printStackTrace();
            listenercaixa.erro (e.getMessage()); ;
        }
    }

    private List<Caixa> getList(JSONArray data) throws JSONException, ParseException {
        List<Caixa> jaa = new ArrayList<Caixa>() ;
        for (int i = 0 ; i < data.length() ; i++) {
            jaa.add(new Caixa(data.getJSONObject(i))) ;
        }
        return jaa ;
    }

}
