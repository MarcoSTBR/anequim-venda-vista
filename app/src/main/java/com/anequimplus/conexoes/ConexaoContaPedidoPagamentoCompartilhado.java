package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.listeners.ListenerContaPedidoPagamento;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPedidoPagamentoCompartilhado extends ConexaoServer{

    private ContaPedidoPagamento contaPedidoPagamento ;
    private FilterTables filters ;
    private String order ;
    private ListenerContaPedidoPagamento listenerContaPedidoPagamento ;

    public ConexaoContaPedidoPagamentoCompartilhado(Context ctx, FilterTables filters, String order, ListenerContaPedidoPagamento listenerContaPedidoPagamento) throws MalformedURLException {
        super(ctx);
        msg = "Pagamentos..." ;
        method = "GET" ;
        this.ctx = ctx;
        this.filters = filters;
        this.order = order;
        this.listenerContaPedidoPagamento = listenerContaPedidoPagamento;
        maps.put("filters", filters.getJSON()) ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedidopagamento") ;
    }

    public ConexaoContaPedidoPagamentoCompartilhado(Context ctx, ContaPedidoPagamento contaPedidoPagamento, ListenerContaPedidoPagamento listenerContaPedidoPagamento) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Pagamentos..." ;
        method = "POST" ;
        this.ctx = ctx ;
        this.contaPedidoPagamento = contaPedidoPagamento ;
        this.listenerContaPedidoPagamento = listenerContaPedidoPagamento ;
        maps.put("data", getJSON()) ;
        if (contaPedidoPagamento.getId() == 0)
            url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedidopagamento") ;
         else  url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedidopagamento/"+contaPedidoPagamento.getId()) ;
    }

    private JSONObject getJSON() throws JSONException {
        JSONObject j = contaPedidoPagamento.getExportacaoJSON() ;
        j.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
        return j ;

    }

    public void executar() {
        super.execute();

      }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("pagamento",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                listenerContaPedidoPagamento.ok(getJSON(j.getJSONArray("data")));
            } else listenerContaPedidoPagamento.erro(j.getString("data")); ;
        } catch (Exception e) {
            e.printStackTrace();
            listenerContaPedidoPagamento.erro(e.getMessage()+" "+codInt+" "+s ) ;
        }
    }

    private List<ContaPedidoPagamento> getJSON(JSONArray jarr) throws JSONException {
        List<ContaPedidoPagamento> jaa = new ArrayList<ContaPedidoPagamento>() ;
        for (int i = 0 ; i < jarr.length() ; i++) {
            JSONObject it = jarr.getJSONObject(i) ;
            Modalidade modalidade= DaoDbTabela.getModalidadeADO(ctx).getModalidade(it.getInt("MODALIDADE_ID"));
            jaa.add(new ContaPedidoPagamento(it, modalidade)) ;
        }
        return jaa ;
    }}
