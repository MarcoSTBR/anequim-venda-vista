package com.anequimplus.conexoes;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.listeners.ListenerTransferencia;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.UtilSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConexaoTransferencia {

    private Activity ctx ;
    private Transferencia transferencia;
    private FilterTables filterTables;
    private String order ;
    private ListenerTransferencia listenerTransferencia ;
    private TipoConexao tipoConexao ;
    private String pedido ;


    public ConexaoTransferencia(Activity ctx, FilterTables filterTables, String order, ListenerTransferencia listenerTransferencia) {
        this.ctx = ctx ;
        this.filterTables = filterTables ;
        this.order = order ;
        this.listenerTransferencia = listenerTransferencia ;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public ConexaoTransferencia(Activity ctx, String pedido, Transferencia transferencia, ListenerTransferencia listenerTransferencia)  {
        this.ctx = ctx ;
        this.pedido = pedido ;
        this.transferencia = transferencia ;
        this.listenerTransferencia = listenerTransferencia ;
        tipoConexao = TipoConexao.cxIncluir ;
    }

    public void executar(){
        switch (tipoConexao) {
            case cxConsultar: listenerTransferencia.ok(DaoDbTabela.getTransferenciaDAO(ctx).getList(filterTables, order));
            break;
            case cxIncluir : {
                   ContaPedido conta = null ;
                   FilterTables filter = new FilterTables() ;
                   filter.add(new FilterTable("PEDIDO", "=", pedido));
                   filter.add(new FilterTable("STATUS", "=", "1"));
                   List<ContaPedido> lcontas = DaoDbTabela.getContaPedidoInternoDAO(ctx).getList(filter.getList(), "") ;
                   if (lcontas.size()>0)
                       conta = lcontas.get(0);
                     else {
                       conta = new ContaPedido(0, UtilSet.getUUID(), pedido, new Date(), 0, new ArrayList<ContaPedidoItem>(), new ArrayList<ContaPedidoPagamento>(),
                               1,1,0,new Date(),1, UtilSet.getUsuarioId(ctx)) ;
                       DaoDbTabela.getContaPedidoInternoDAO(ctx).inserir(conta) ;
                   }
                    transferencia.setContaPedido_destino_id(conta.getId());
                    DaoDbTabela.getTransferenciaDAO(ctx).transferir(transferencia);
                    List<Transferencia> l = new ArrayList<Transferencia>();
                    l.add(transferencia);
                    listenerTransferencia.ok(l);
                }
             break;

        }
    }

}
