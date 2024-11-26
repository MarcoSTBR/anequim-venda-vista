package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Produto;
import com.anequimplus.listeners.ListenerContaPedidoItem;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPeditoItemCompartilhado extends ConexaoServer{

    private ListenerContaPedidoItem listenerContaPedidoItem ;
    private ContaPedidoItem contaPedidoItem ;
    private TipoConexao tipoConexao ;

    public ConexaoContaPeditoItemCompartilhado(Activity ctx, FilterTables filters, String order, ListenerContaPedidoItem listenerContaPedidoItem) throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        msg = "Consultando Item da Conta" ;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        tipoConexao = TipoConexao.cxConsultar;
        maps.put("filters", filters.getJSON()) ;
        maps.put("order", order) ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedidoitem") ;
    }

    public ConexaoContaPeditoItemCompartilhado(Activity ctx, ContaPedidoItem contaPedidoItem, ListenerContaPedidoItem listenerContaPedidoItem) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "Alteração do Item da Conta" ;
        this.contaPedidoItem = contaPedidoItem ;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        tipoConexao = TipoConexao.cxAlterar;
        maps.put("data",contaPedidoItem.getExportacaoJSON()) ;
        if (contaPedidoItem.getId() == 0)
            url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedidoitem") ;
         else
           url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedidoitem/"+contaPedidoItem.getId()) ;
    }

    public ConexaoContaPeditoItemCompartilhado(Activity ctx, int id_delecao, ListenerContaPedidoItem listenerContaPedidoItem) throws MalformedURLException, JSONException {
        super(ctx);
        method = "DELETE" ;
        msg = "Alteração do Item da Conta" ;
        this.contaPedidoItem = contaPedidoItem ;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        tipoConexao = TipoConexao.cxDeletar;
       // maps.put("data",contaPedidoItem.getJSON()) ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedidoitem/"+String.valueOf(id_delecao)) ;
    }

    public void executar(){
        super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("conexaoContaItem",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                if (tipoConexao == TipoConexao.cxDeletar)
                    listenerContaPedidoItem.ok(new ArrayList<ContaPedidoItem>());
                    else
                      listenerContaPedidoItem.ok(getJSON(j.getJSONArray("data")));
            } else listenerContaPedidoItem.erro(j.getString("data")); ;
        } catch (Exception e) {
            e.printStackTrace();
            listenerContaPedidoItem.erro("trans " + e.getMessage()+" "+codInt+" "+s ) ;
        }
    }

    private List<ContaPedidoItem> getJSON(JSONArray jarr) throws JSONException {
        List<ContaPedidoItem> jaa = new ArrayList<ContaPedidoItem>() ;
        for (int i = 0 ; i < jarr.length() ; i++) {
            JSONObject it = jarr.getJSONObject(i) ;
            Produto prd =  null ;
            if (Configuracao.getApiVersao(ctx).equals("V13")) {
                prd = DaoDbTabela.getProdutoADO(ctx).getCodigo(it.getString("PRODUTO_ID"));
            } else {
                prd = DaoDbTabela.getProdutoADO(ctx).getProdutoId(it.getInt("PRODUTO_ID"));
            }
           // Log.i("getJSON", "json "+it) ;
            ContaPedidoItem ci = new ContaPedidoItem(it, prd) ;
            jaa.add(ci) ;
           // Log.i("getJSON", "json "+ci.getId()) ;
        }
        return jaa ;
    }
}
