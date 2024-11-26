package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.conexoes.ConexaoContaPedidoPagamento;
import com.anequimplus.conexoes.ConexaoContaPedidoPagamentoCompartilhado;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerContaPedidoPagamento;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;

public class BuildContaPedidoPagamento {

    private Activity ctx ;
    private FilterTables filters ;
    private String order ;
    private ContaPedidoPagamento contaPedidoPagamento ;
    private ListenerContaPedidoPagamento listenerContaPedidoPagamento ;
    private TipoConexao tipoConexao ;

    public BuildContaPedidoPagamento(Activity ctx, FilterTables filters, String order, ListenerContaPedidoPagamento listenerContaPedidoPagamento) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order ;
        this.listenerContaPedidoPagamento = listenerContaPedidoPagamento;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildContaPedidoPagamento(Activity ctx, ContaPedidoPagamento contaPedidoPagamento, ListenerContaPedidoPagamento listenerContaPedidoPagamento) {
        this.ctx = ctx;
        this.contaPedidoPagamento = contaPedidoPagamento;
        this.listenerContaPedidoPagamento = listenerContaPedidoPagamento;
        if (contaPedidoPagamento.getId() == 0)
          tipoConexao = TipoConexao.cxIncluir ;
        else tipoConexao = TipoConexao.cxAlterar ;
    }

    public void executar(){
        if (!Configuracao.getPedidoCompartilhado(ctx)){
            switch (tipoConexao){
                case cxConsultar: new ConexaoContaPedidoPagamento(ctx, filters, order, listenerContaPedidoPagamento).executar();
                break;
                case cxIncluir: new ConexaoContaPedidoPagamento(ctx, contaPedidoPagamento, listenerContaPedidoPagamento).executar();
                break;
                case cxAlterar: new ConexaoContaPedidoPagamento(ctx, contaPedidoPagamento, listenerContaPedidoPagamento).executar();
                break;
                default:listenerContaPedidoPagamento.erro("Opção Pagamento Não Encotrado!");
              }
            } else {
            try {
            switch (tipoConexao) {
                case cxConsultar:
                new ConexaoContaPedidoPagamentoCompartilhado(ctx, filters, order, listenerContaPedidoPagamento).executar();
                break;
                case cxIncluir:
                new ConexaoContaPedidoPagamentoCompartilhado(ctx, contaPedidoPagamento, listenerContaPedidoPagamento).executar();
                break;
                case cxAlterar: new ConexaoContaPedidoPagamentoCompartilhado(ctx, contaPedidoPagamento, listenerContaPedidoPagamento).executar();
                break;
                default:
                    listenerContaPedidoPagamento.erro("Opção Pagamento Não Encotrado!");
            }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                listenerContaPedidoPagamento.erro(e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                listenerContaPedidoPagamento.erro(e.getMessage());
            }

        }

        }

}
