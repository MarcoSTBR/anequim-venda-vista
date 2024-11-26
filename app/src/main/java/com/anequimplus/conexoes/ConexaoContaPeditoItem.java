package com.anequimplus.conexoes;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerContaPedidoItem;
import com.anequimplus.tipos.TipoConexao;

import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPeditoItem {

    private Activity ctx ;
    private FilterTables filters ;
    private String order ;
    private ListenerContaPedidoItem listenerContaPedidoItem ;
    private ContaPedido contaPedido ;
    private ContaPedidoItem contaPedidoItem ;
    private TipoConexao tipoConexao ;

    public ConexaoContaPeditoItem(Activity ctx, FilterTables filters, String order, ListenerContaPedidoItem listenerContaPedidoItem) {
        this.ctx = ctx;
        this.filters = filters ;
        this.order = order ;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        tipoConexao = TipoConexao.cxConsultar;
    }

    public ConexaoContaPeditoItem(Activity ctx, ContaPedidoItem contaPedidoItem, ListenerContaPedidoItem listenerContaPedidoItem) {
        this.ctx = ctx;
        this.contaPedidoItem = contaPedidoItem ;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        tipoConexao = TipoConexao.cxAlterar;
    }

    public ConexaoContaPeditoItem(Activity ctx, ContaPedido contaPedido , ContaPedidoItem contaPedidoItem, ListenerContaPedidoItem listenerContaPedidoItem) {
        this.ctx = ctx;
        this.contaPedido = contaPedido ;
        this.contaPedidoItem = contaPedidoItem ;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        tipoConexao = TipoConexao.cxIncluir;
    }


    public void executar(){
        switch (tipoConexao){
            case cxAlterar: {
                DaoDbTabela.getContaPedidoItemInternoDAO(ctx).alterar(contaPedidoItem);
                List<ContaPedidoItem> l = new ArrayList<ContaPedidoItem>();
                l.add(contaPedidoItem) ;
                listenerContaPedidoItem.ok(l);
            }
            break;
            case cxIncluir: {
                DaoDbTabela.getContaPedidoItemInternoDAO(ctx).inserir(contaPedido, contaPedidoItem);
                List<ContaPedidoItem> l = new ArrayList<ContaPedidoItem>();
                l.add(contaPedidoItem) ;
                listenerContaPedidoItem.ok(l);
            }
            break;
            case cxConsultar: listenerContaPedidoItem.ok(DaoDbTabela.getContaPedidoItemInternoDAO(ctx).geList(filters, order));
            break;
        }

    }
}
