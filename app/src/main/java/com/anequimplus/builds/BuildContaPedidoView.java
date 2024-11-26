package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.conexoes.ConexaoContaPedidoView;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoView;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.listeners.ListenerContaPedidoView;
import com.anequimplus.utilitarios.Configuracao;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BuildContaPedidoView {

    private Activity ctx ;
    private List<FilterTable> filters ;
    private String order ;
    private ListenerContaPedidoView listenerContaPedidoView ;

    public BuildContaPedidoView(Activity ctx, List<FilterTable> filters, String order, ListenerContaPedidoView listenerContaPedidoView) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order;
        this.listenerContaPedidoView = listenerContaPedidoView;
    }

    public void executar(){

        try {
            if (Configuracao.getPedidoCompartilhado(ctx)) {
                    new ConexaoContaPedidoView(ctx, filters, order, listenerContaPedidoView).execute() ;
            } else {
                List<ContaPedido> l = DaoDbTabela.getContaPedidoInternoDAO(ctx).getList(filters, order) ;
                List<ContaPedidoView> list = new ArrayList<ContaPedidoView>() ;
                for (ContaPedido cp : l){
                    list.add(new ContaPedidoView(cp.getId(), cp.getPedido(), cp.getData(), cp.getStatus(), cp.getTotalQuantidadeItens() , cp.getTotalItens())) ;
                }
                listenerContaPedidoView.ok(list);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerContaPedidoView.erro(0, e.getMessage());
        }

    }
}
