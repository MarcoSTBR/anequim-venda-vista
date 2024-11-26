package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.conexoes.ConexaoContaPeditoItem;
import com.anequimplus.conexoes.ConexaoContaPeditoItemCompartilhado;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerContaPedidoItem;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;

public class BuildContaPedidoItem {

    private Activity ctx ;
    private FilterTables filters ;
    private String order ;
    private ListenerContaPedidoItem listenerContaPedidoItem ;
    private ContaPedidoItem contaPedidoItem ;
    private TipoConexao tipoConexao ;
    private int id_delecao = 0 ;


    public BuildContaPedidoItem(Activity ctx, FilterTables filters, String order, ListenerContaPedidoItem listenerContaPedidoItem) {
        this.ctx = ctx;
        this.filters = filters;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        this.order = order ;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildContaPedidoItem(Activity ctx, ContaPedidoItem contaPedidoItem, ListenerContaPedidoItem listenerContaPedidoItem) {
        this.ctx = ctx;
        this.contaPedidoItem = contaPedidoItem;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        tipoConexao = TipoConexao.cxAlterar ;
    }

    public BuildContaPedidoItem(Activity ctx, int id_delecao, ListenerContaPedidoItem listenerContaPedidoItem) {
        this.ctx = ctx;
        this.contaPedidoItem = contaPedidoItem;
        this.listenerContaPedidoItem = listenerContaPedidoItem ;
        this.tipoConexao = TipoConexao.cxDeletar ;
        this.id_delecao = id_delecao ;
    }

    public void executar(){
        try {
        if (Configuracao.getPedidoCompartilhado(ctx)){
                switch (tipoConexao){
                    case cxConsultar: new ConexaoContaPeditoItemCompartilhado(ctx, filters, order, listenerContaPedidoItem).executar();
                    break;
                    case cxAlterar: new ConexaoContaPeditoItemCompartilhado(ctx, contaPedidoItem, listenerContaPedidoItem).executar();
                    break;
                    case cxDeletar: new ConexaoContaPeditoItemCompartilhado(ctx, id_delecao, listenerContaPedidoItem).executar();
                    break;
                    default: listenerContaPedidoItem.erro("tipo Não Encontrado");
                }
            } else {
            switch (tipoConexao){
                case cxConsultar: new ConexaoContaPeditoItem(ctx, filters, order, listenerContaPedidoItem).executar();
                    break;
                case cxAlterar: new ConexaoContaPeditoItem(ctx, contaPedidoItem, listenerContaPedidoItem).executar();
                    break;
                case cxDeletar: listenerContaPedidoItem.erro("Não permitido na versão 14");
                    break;
                default: listenerContaPedidoItem.erro("tipo Não Encontrado");
            }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerContaPedidoItem.erro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerContaPedidoItem.erro(e.getMessage());
        }

    }


}
