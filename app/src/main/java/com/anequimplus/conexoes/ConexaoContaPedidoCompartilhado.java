package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.builds.BuildTranformacaoVersao;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ConexaoContaPedidoCompartilhado extends ConexaoServer {

    private List<FilterTable> filterTables = null ;
    private ContaPedido contaPedido = null ;
    private List<ContaPedido> listContaPedido = null ;
    private String orders = "" ;
    private ListenerContaPedido listenerContaPedido ;
    private TipoConexao tipoConexao ;

    public ConexaoContaPedidoCompartilhado(Context ctx, List<FilterTable> filterTables, String orders, ListenerContaPedido listenerContaPedido) throws MalformedURLException {
        super(ctx);
        msg  = "Consultando Contas" ;
        method = "GET" ;
        this.orders = orders;//"DATA_FECHAMENTO DESC" ;
        this.filterTables = filterTables;
        this.listenerContaPedido = listenerContaPedido ;
        tipoConexao = TipoConexao.cxConsultar ;
        maps.put("filters",getFilters(filterTables)) ;
        if (orders.length() > 0)
          maps.put("order",orders) ;
        String nurl = Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido" ;
        url =  new URL(nurl) ;
    }

    public ConexaoContaPedidoCompartilhado(Context ctx, ContaPedido contaPedido, ListenerContaPedido listenerContaPedido) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg  = "Alterar Contas" ;
        this.listenerContaPedido = listenerContaPedido ;
        this.contaPedido = contaPedido ;
        maps.put("data", getAlterarJSON()) ;
      //  setParamentros() ;
        if (contaPedido.getId() == 0) {
            tipoConexao = TipoConexao.cxIncluir ;
            url = new URL(Configuracao.getLinkContaCompartilhada(ctx) + "/conta_pedido");
        } else {
            tipoConexao = TipoConexao.cxAlterar ;
            url = new URL(Configuracao.getLinkContaCompartilhada(ctx) + "/conta_pedido/" + contaPedido.getId());
        }

    }

    private JSONObject getAlterarJSON() throws JSONException {
        JSONObject j = contaPedido.getAlterarJSON() ;
        j.put("SYSTEM_USER_ID", UtilSet.getUsuarioId(ctx)) ;
        j.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
        j.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
        return j ;
    }

    private JSONArray getFilters(List<FilterTable> filterTables){
        JSONArray jaa = new JSONArray() ;
        for (FilterTable f : filterTables) {
            jaa.put(f.getJSON()) ;
        }
        Log.i("getFilters", jaa.toString()) ;
        return jaa ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("contapedido", "tam "+s.length()+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
               listenerContaPedido.ok(new BuildTranformacaoVersao(ctx).getContas(j.getJSONArray("data"))) ;
                //listenerContaPedido.ok(DaoDbTabela.getContaPedidoInternoDAO(ctx).getJSON(j.getJSONArray("data")));
                //oK(DaoDbTabela.getContaPedidoInternoDAO(ctx).getJSON(j.getJSONArray("data"))) ;
            } else {
                // erro(j.getString("data"));
                listenerContaPedido.erro(j.getString("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            //listenerContaPedido.erro(e.getMessage()+" "+s);
            listenerContaPedido.erro(e.getMessage());
        }
    }

}
