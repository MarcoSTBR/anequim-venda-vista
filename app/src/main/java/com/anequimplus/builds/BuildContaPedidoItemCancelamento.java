package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.conexoes.ConexaoContaPedidoItemCancelamentoCompartinlhado;
import com.anequimplus.conexoes.ConexaoContaPedidoItemCancelar;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerItemCancelamento;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;

public class BuildContaPedidoItemCancelamento {

    private Activity ctx ;
    private ContaPedidoItemCancelamento contaPedidoItemCancelamento = null  ;
    private FilterTables filters ;
    private String order ;
    private ListenerItemCancelamento listenerItemCancelamento ;
    private TipoConexao tipoConexao ;
    private ContaPedido conta ;
    private ContaPedidoItem item ;

    public BuildContaPedidoItemCancelamento(Activity ctx, FilterTables filters, String order, ListenerItemCancelamento listenerItemCancelamento) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order;
        this.listenerItemCancelamento = listenerItemCancelamento ;
        tipoConexao = TipoConexao.cxConsultar ;
    }


    public BuildContaPedidoItemCancelamento(Activity ctx, ContaPedidoItemCancelamento contaPedidoItemCancelamento, ListenerItemCancelamento listenerItemCancelamento) {
        this.ctx = ctx;
        this.contaPedidoItemCancelamento = contaPedidoItemCancelamento;
        this.listenerItemCancelamento = listenerItemCancelamento ;
        tipoConexao = TipoConexao.cxIncluir ;
    }

    public BuildContaPedidoItemCancelamento(Activity ctx, ContaPedidoItemCancelamento contaPedidoItemCancelamento, ContaPedido conta, ContaPedidoItem item, ListenerItemCancelamento listenerItemCancelamento) {
        this.ctx = ctx;
        this.contaPedidoItemCancelamento = contaPedidoItemCancelamento;
        this.listenerItemCancelamento = listenerItemCancelamento ;
        this.conta = conta ;
        this.item = item ;
        tipoConexao = TipoConexao.cxIncluir ;
    }

    public void executar(){

     try
       {
        if (!Configuracao.getPedidoCompartilhado(ctx)) {
           switch (tipoConexao) {
               case cxConsultar: new ConexaoContaPedidoItemCancelar(ctx, filters, order, listenerItemCancelamento).executar();
               break;
               case cxIncluir:  new ConexaoContaPedidoItemCancelar(ctx, contaPedidoItemCancelamento, listenerItemCancelamento).executar();
               break;
           }
        }  else {
            switch (tipoConexao) {
                case cxConsultar: new ConexaoContaPedidoItemCancelamentoCompartinlhado(ctx, filters, order, listenerItemCancelamento).executar();
                    break;
                case cxIncluir: new ConexaoContaPedidoItemCancelamentoCompartinlhado(ctx, contaPedidoItemCancelamento, conta, item, listenerItemCancelamento).executar();
                break;
            }
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
        listenerItemCancelamento.erro(e.getMessage());
    } catch (JSONException e) {
        e.printStackTrace();
        listenerItemCancelamento.erro(e.getMessage());
    }
    }


}
