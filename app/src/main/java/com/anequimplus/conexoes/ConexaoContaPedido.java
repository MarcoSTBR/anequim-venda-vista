package com.anequimplus.conexoes;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.tipos.TipoConexao;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPedido  {

    private Activity ctx ;
    private List<FilterTable> filterTables = null ;
    private ContaPedido contaPedido = null ;
    private List<ContaPedido> listContaPedido = null ;
    private String orders = "" ;
    private ListenerContaPedido listenerContaPedido ;
    private TipoConexao tipoConexao ;

    public ConexaoContaPedido(Activity ctx, List<FilterTable> filterTables, String orders, ListenerContaPedido listenerContaPedido){
       // super(ctx);
//        msg  = "Consultat Contas" ;
        this.ctx = ctx ;
        this.orders = orders;//"DATA_FECHAMENTO DESC" ;
        this.filterTables = filterTables;
        this.listenerContaPedido = listenerContaPedido ;
        tipoConexao = TipoConexao.cxConsultar ;
//        url =  new URL(UtilSet.getServidorMaster(ctx)) ;//DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(fConsultaPedido).getUrl();
    }

    private JSONArray getFilters(List<FilterTable> filterTables){
        JSONArray jaa = new JSONArray() ;
        for (FilterTable f : filterTables) {
            jaa.put(f.getJSON()) ;
        }
        return jaa ;
    }
    private JSONArray getContas(List<ContaPedido> list) throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (ContaPedido c : list) {
            jaa.put(c.getExportarJSON()) ;
        }
        return jaa ;
    }
    public ConexaoContaPedido(Activity ctx, ContaPedido contaPedido, ListenerContaPedido listenerContaPedido) {
        this.ctx = ctx ;
        this.contaPedido = contaPedido ;
        this.listenerContaPedido = listenerContaPedido ;
        tipoConexao = TipoConexao.cxAlterar ;
    }

/*

    public ConexaoContaPedido(Context ctx, ContaPedido contaPedido, Link link, ListenerContaPedido listenerContaPedido) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado, JSONException {
        super(ctx);
        msg  = "Conta Pedido" ;
        this.link = link ;
        this.contaPedido = contaPedido ;
        this.listenerContaPedido = listenerContaPedido ;
        maps.put("class","AfoodContaPedido") ;
        maps.put("method","atualizar") ;
        maps.put("dados",contaPedido.getJSON()) ;
        url = DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(fAlterarPedido).getUrl();
    }

    public ConexaoContaPedido(Context ctx, List<ContaPedido> listContaPedido) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado, JSONException {
        super(ctx);
        msg  = "Conta Pedido" ;
        this.link = fAtualizarContas ;
        this.listContaPedido = listContaPedido ;
        maps.put("class","AfoodContaPedido") ;
        maps.put("method","atualizar") ;
        maps.put("dados",getContas(listContaPedido)) ;
        url = DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(fAtualizarContas).getUrl();
    }
*/

    public void executar(){
           switch (tipoConexao) {
               case cxConsultar: listenerContaPedido.ok(DaoDbTabela.getContaPedidoInternoDAO(ctx).getList(filterTables, orders));
               break;
               case cxAlterar:
                        DaoDbTabela.getContaPedidoInternoDAO(ctx).alterar(contaPedido);
                        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
                        l.add(contaPedido) ;
                        listenerContaPedido.ok(l);
                   break;
              // case fAtualizarContas: listenerContaPedido.ok(listContaPedido) ;
              // break;
               default: listenerContaPedido.erro("Opção Inválida!");
           }
    }
/*

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("contapedido", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
               listenerContaPedido.ok(DaoDbTabela.getContaPedidoInternoDAO(ctx).getJSON(j.getJSONArray("data")));
                //oK(DaoDbTabela.getContaPedidoInternoDAO(ctx).getJSON(j.getJSONArray("data"))) ;
            } else {
               // erro(j.getString("data"));
                listenerContaPedido.erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerContaPedido.erro(e.getMessage());
        }
    }
*/


}
