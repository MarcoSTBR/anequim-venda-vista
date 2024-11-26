package com.anequimplus.conexoes;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.listeners.ListenerContaPedidoPagamento;
import com.anequimplus.tipos.TipoConexao;

import java.util.ArrayList;
import java.util.List;

public class ConexaoContaPedidoPagamento {

    private Activity ctx ;
    private ContaPedidoPagamento contaPedidoPagamento ;
    private FilterTables filters ;
    private String order ;
    private ListenerContaPedidoPagamento listenerContaPedidoPagamento ;
    private TipoConexao tipoConexao ;

    public ConexaoContaPedidoPagamento(Activity ctx, FilterTables filters, String order, ListenerContaPedidoPagamento listenerContaPedidoPagamento) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order;
        this.listenerContaPedidoPagamento = listenerContaPedidoPagamento;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public ConexaoContaPedidoPagamento(Activity ctx, ContaPedidoPagamento contaPedidoPagamento, ListenerContaPedidoPagamento listenerContaPedidoPagamento)  {
        this.ctx = ctx ;
        this.contaPedidoPagamento = contaPedidoPagamento ;
        this.listenerContaPedidoPagamento = listenerContaPedidoPagamento ;
        this.tipoConexao = tipoConexao ;
        if (contaPedidoPagamento.getId() == 0)
            tipoConexao = TipoConexao.cxIncluir ;
          else tipoConexao = TipoConexao.cxAlterar ;
    }

    public void executar() {
        switch (tipoConexao) {
            case cxConsultar: listenerContaPedidoPagamento.ok(DaoDbTabela.getContaPedidoPagamentoADO(ctx).getList(filters, order));
            break;
            case cxIncluir: {
                List<ContaPedidoPagamento> l = new ArrayList<ContaPedidoPagamento>();
                Modalidade modtroco = DaoDbTabela.getModalidadeADO(ctx).getModalidadeTroco();
                try {
                    if (modtroco == null) throw new Exception("Modalidade Troco Não Encontrada!");
                    DaoDbTabela.getContaPedidoPagamentoADO(ctx).incluir(contaPedidoPagamento);
                    l.add(contaPedidoPagamento);
                    listenerContaPedidoPagamento.ok(l);

/*
                    ContaPedido c = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(contaPedidoPagamento.getContaPedido_id());
                    if (c.getTotalPagamentos() >= c.getTotal()) {
                        Double vrtoco = c.getTotalPagamentos() - c.getTotal();
                        if (vrtoco > 0) {
                            ContaPedidoPagamento troco = new ContaPedidoPagamento(0, contaPedidoPagamento.getContaPedido_id(), UtilSet.getUUID(), new Date(), contaPedidoPagamento.getCaixa_id(), modtroco, vrtoco, 1);
                            DaoDbTabela.getContaPedidoPagamentoADO(ctx).incluir(troco);
                        }
                        c.setStatus(2);
                        c.setData_fechamento(new Date());
                        DaoDbTabela.getContaPedidoInternoDAO(ctx).store(c);
                    }
*/

                    } catch (Exception e) {
                    e.printStackTrace();
                    listenerContaPedidoPagamento.erro(e.getMessage());
                    }
            }
            break ;
            default:listenerContaPedidoPagamento.erro("Conexao Pagamento opção não encontrada!");
        }

      }


}
