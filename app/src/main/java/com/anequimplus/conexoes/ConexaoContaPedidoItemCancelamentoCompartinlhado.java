package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.builds.BuildContaPedidoItem;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerContaPedidoItem;
import com.anequimplus.listeners.ListenerItemCancelamento;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPedidoItemCancelamentoCompartinlhado extends ConexaoServer{

    private ContaPedidoItemCancelamento contaPedidoItemCancelamento = null  ;
    private ListenerItemCancelamento listenerItemCancelamento ;
    private ContaPedidoItem contaPedidoItem ;
    private TipoConexao tipoConexao ;
    // Versão 13
    private ContaPedidoItem item ;
    private ContaPedido conta ;



    public ConexaoContaPedidoItemCancelamentoCompartinlhado(Context ctx, FilterTables filters, String order, ListenerItemCancelamento listenerItemCancelamento) throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        msg = "Consulta de Cancelamentos.." ;
        this.listenerItemCancelamento = listenerItemCancelamento ;
        maps.put("filters", filters.getJSON());
        maps.put("order", order);
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido_item_cancelamento") ;
        tipoConexao = TipoConexao.cxConsultar  ;

    }

    public ConexaoContaPedidoItemCancelamentoCompartinlhado(Context ctx, ContaPedidoItemCancelamento contaPedidoItemCancelamento, ContaPedido conta, ContaPedidoItem item, ListenerItemCancelamento listenerItemCancelamento) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "Cancelamento de Item..." ;
        this.contaPedidoItemCancelamento = contaPedidoItemCancelamento ;
        this.listenerItemCancelamento = listenerItemCancelamento ;
        this.conta = conta ;
        this.item = item ;
        maps.put("data", getJSONCancel(contaPedidoItemCancelamento));
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido_item_cancelamento") ;
        tipoConexao = TipoConexao.cxIncluir  ;
    }

    private JSONObject getJSONCancel(ContaPedidoItemCancelamento contaPedidoItemCancelamento) throws JSONException {
        JSONObject j = contaPedidoItemCancelamento.getExportacaoJSON() ;
        j.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
        j.put("SYSTEM_USER_ID", UtilSet.getUsuarioId(ctx)) ;
        j.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
        if (Configuracao.getApiVersao(ctx).equals("V13")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            j.put("PEDIDO", conta.getPedido()) ;
            j.put("DATASEQUENCIAL", df.format(conta.getData())) ;
            j.put("USUARIO", UtilSet.getLogin(ctx) +" "+ UtilSet.getUsuarioNome(ctx)) ;
            j.put("PRODUTO", item.getProduto().getCodBarra()) ;
            j.put("DESPRODUTO", item.getProduto().getDescricao()) ;
            j.put("PRECO", item.getPreco()) ;

        }
        Log.i("cancelamento", "getJSONCancel"+j) ;
        return j ;
    }


    private JSONArray getJSON(List<FilterTable> filters){
        JSONArray jaa = new JSONArray();
        for (int i = 0 ; i < filters.size() ; i++) {
            jaa.put(filters.get(i).getJSON()) ;
        }
        return jaa ;
    }

    public void executar() {
        if (tipoConexao == TipoConexao.cxIncluir)
            recuperarItem() ;
        else super.execute();
    }

    private void recuperarItem(){
        FilterTables filters = new FilterTables();
        String idItem = String.valueOf(contaPedidoItemCancelamento.getContaPedidoItem_id()) ;
        filters.add(new FilterTable("ID", "=", idItem)) ;
        new BuildContaPedidoItem(ctx, filters, "ID",  new ListenerContaPedidoItem() {
            @Override
            public void ok(List<ContaPedidoItem> l) {
                //Log.i("recuperarItem", "json"+ l.) ;
                if (l.size()>0) {
                    contaPedidoItem = l.get(0) ;
                    iniciarCancelamento() ;
                } else listenerItemCancelamento.erro("Identificador "+idItem+" Não Encontrado!");
            }

            @Override
            public void erro(String msg) {
                listenerItemCancelamento.erro(msg);
            }
        }).executar();
    }


    private void incluirCancelamento(){
        super.execute();
    }


    private void alterarItem(){
        new BuildContaPedidoItem(ctx, contaPedidoItem, new ListenerContaPedidoItem() {
            @Override
            public void ok(List<ContaPedidoItem> l) {
                incluirCancelamento();
            }

            @Override
            public void erro(String msg) {
                listenerItemCancelamento.erro(msg);
            }
        }).executar();
    }


    private void iniciarCancelamento(){
        if (contaPedidoItemCancelamento.getQuantidade() == contaPedidoItem.getQuantidade()){
            contaPedidoItem.setStatus(0);
            Log.i("cancelamento", Configuracao.getApiVersao(ctx)) ;
            if ((Configuracao.getPedidoCompartilhado(ctx)) && (Configuracao.getApiVersao(ctx).equals("V13"))) {
                  Log.i("cancelamento", "excluir") ;
                 excluirItem();
                } else {
                 Log.i("cancelamento", "alterar") ;
                 alterarItem();
                }
            Log.i("contaPedidoItem", "= "+contaPedidoItem.getStatus()) ;
        } else {
            if ((Configuracao.getPedidoCompartilhado(ctx)) && (Configuracao.getApiVersao(ctx) == "V13")) {
                listenerItemCancelamento.erro("Opção não permitida na versão 13");
            }  else  cancelamentoparcial() ;
        }
    }

    private void excluirItem(){
        Log.i("excluirItem", "json "+contaPedidoItem.getId())  ;
        int id = contaPedidoItem.getId() ;
        new BuildContaPedidoItem(ctx, id, new ListenerContaPedidoItem() {
            @Override
            public void ok(List<ContaPedidoItem> l) {
                incluirCancelamento();
            }

            @Override
            public void erro(String msg) {
                listenerItemCancelamento.erro(msg);
            }
        }).executar();
    }

    private void cancelamentoparcial(){
        Double quantRestante = contaPedidoItem.getQuantidade() - contaPedidoItemCancelamento.getQuantidade() ;
        contaPedidoItem.setQuantidade(quantRestante);
        contaPedidoItem.setValor(quantRestante * contaPedidoItem.getPreco());
        contaPedidoItem.setComissao(contaPedidoItem.getProduto().getComissao() / 100 * contaPedidoItem.getValor());
        Log.i("contaPedidoItem", "<>"+contaPedidoItem.getStatus()) ;
        alterarItem();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("cancelamento", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerItemCancelamento.ok(getList(j.getJSONArray("data")));
            } else {
                listenerItemCancelamento.erro(codInt+" "+j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerItemCancelamento.erro (codInt+" "+s); ;
        }
    }

    private List<ContaPedidoItemCancelamento> getList(JSONArray data) throws JSONException {
        List<ContaPedidoItemCancelamento> jaa = new ArrayList<ContaPedidoItemCancelamento>() ;
        for (int i = 0 ; i < data.length() ; i++) {
            jaa.add(new ContaPedidoItemCancelamento(data.getJSONObject(i))) ;
        }

        return jaa ;

    }
}
