package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerItemCancelamento;
import com.anequimplus.tipos.TipoConexao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPedidoItemCancelar  {

    private Activity ctx ;
    private ContaPedidoItemCancelamento contaPedidoItemCancelamento ;
    private FilterTables filters ;
    private String order ;
    private ListenerItemCancelamento listenerItemCancelamento ;
    private TipoConexao tipoConexao ;

    public ConexaoContaPedidoItemCancelar(Activity ctx, FilterTables filters, String order, ListenerItemCancelamento listenerItemCancelamento) {
        this.ctx = ctx ;
        this.filters = filters ;
        this.order = order ;
        this.listenerItemCancelamento = listenerItemCancelamento ;
        tipoConexao = TipoConexao.cxConsultar ;

    }

    public ConexaoContaPedidoItemCancelar(Activity ctx, ContaPedidoItemCancelamento contaPedidoItemCancelamento, ListenerItemCancelamento listenerItemCancelamento ) throws MalformedURLException, JSONException {
        this.ctx = ctx ;
        this.contaPedidoItemCancelamento = contaPedidoItemCancelamento ;
        this.listenerItemCancelamento = listenerItemCancelamento ;
        tipoConexao = TipoConexao.cxIncluir ;
    }


    public void executar(){
        switch (tipoConexao) {
            case cxConsultar: listenerItemCancelamento.ok(DaoDbTabela.getContaPedidoItemCancelamentoDAO(ctx).getList(filters, order));
            break;
            case cxIncluir: {
                DaoDbTabela.getContaPedidoItemCancelamentoDAO(ctx).incluir(contaPedidoItemCancelamento);
                Log.i("execute", contaPedidoItemCancelamento.toString());
                ContaPedidoItem contaItem = DaoDbTabela.getContaPedidoItemInternoDAO(ctx).get(contaPedidoItemCancelamento.getContaPedidoItem_id());
                if (contaItem.getQuantidade() == contaPedidoItemCancelamento.getQuantidade()) {
                    contaItem.setStatus(0);
                } else {
                    Double quantRestante = contaItem.getQuantidade() - contaPedidoItemCancelamento.getQuantidade();
                    contaItem.setQuantidade(quantRestante);
                    contaItem.setValor(quantRestante * contaItem.getPreco());
                    contaItem.setComissao(contaItem.getProduto().getComissao() / 100 * contaItem.getValor());
                }
                DaoDbTabela.getContaPedidoItemInternoDAO(ctx).alterar(contaItem);
                List<ContaPedidoItemCancelamento> j = new ArrayList<ContaPedidoItemCancelamento>();
                j.add(contaPedidoItemCancelamento);
                listenerItemCancelamento.ok(j);
            }
            break;
        }
    }
}
