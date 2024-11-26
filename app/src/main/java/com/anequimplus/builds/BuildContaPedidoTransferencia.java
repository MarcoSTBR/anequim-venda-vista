package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.conexoes.ConexaoTransferencia;
import com.anequimplus.conexoes.ConexaoTransferenciaCompartilhado;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.listeners.ListenerTransferencia;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import java.net.MalformedURLException;

public class BuildContaPedidoTransferencia {

    private Activity ctx ;
    private ListenerTransferencia listenerTransferencia ;
    private Transferencia transferencia ;
    private FilterTables filterTables ;
    private String order ;
    private TipoConexao tipoConexao ;
    private String pedido ;

    public BuildContaPedidoTransferencia(Activity ctx, FilterTables filterTables , String order, ListenerTransferencia listenerTransferencia) {
        this.ctx = ctx;
        this.listenerTransferencia = listenerTransferencia;
        this.filterTables = filterTables;
        this.order = order ;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildContaPedidoTransferencia(Activity ctx, String pedido, Transferencia transferencia, ListenerTransferencia listenerTransferencia) {
        this.ctx = ctx;
        this.pedido = pedido ;
        this.listenerTransferencia = listenerTransferencia;
        this.transferencia = transferencia;
        tipoConexao = TipoConexao.cxIncluir ;
    }

    public void executar(){
        try {
            if (!Configuracao.getPedidoCompartilhado(ctx)) {
             switch (tipoConexao) {
                 case cxConsultar: new ConexaoTransferencia(ctx, filterTables, order, listenerTransferencia).executar();
                 break;
                 case cxIncluir:  new ConexaoTransferencia(ctx, pedido, transferencia, listenerTransferencia).executar();
                 break;
                 default:
                     listenerTransferencia.erro("implementar tranferencia");
             }
           } else {
             switch (tipoConexao) {
                 case cxConsultar: new ConexaoTransferenciaCompartilhado(ctx, filterTables, order, listenerTransferencia).executar();
                 break;
                 case cxIncluir: new ConexaoTransferenciaCompartilhado(ctx, pedido, transferencia, listenerTransferencia).executar();
                 break;
                 default:
                     listenerTransferencia.erro("implementar tranferencia Compartinhado");
             }

        }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerTransferencia.erro(e.getMessage());
        }
    }
}
