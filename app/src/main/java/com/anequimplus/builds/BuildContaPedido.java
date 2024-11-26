package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.conexoes.ConexaoContaPedido;
import com.anequimplus.conexoes.ConexaoContaPedidoCompartilhado;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class BuildContaPedido {
    private Activity ctx ;
    private List<FilterTable> filters ;
    private String order ;
    private ListenerContaPedido listenerContaPedido ;
    private ContaPedido contaPedido ;
    private TipoConexao tipoConexao ;

    public BuildContaPedido(Activity ctx,  List<FilterTable> filters, String order, ListenerContaPedido listenerContaPedido){
        this.ctx     = ctx ;
        this.filters = filters ;
        this.order   = order ;
        this.listenerContaPedido = listenerContaPedido ;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildContaPedido(Activity ctx,  ContaPedido contaPedido, ListenerContaPedido listenerContaPedido){
        this.ctx     = ctx ;
        this.contaPedido = contaPedido ;
        this.listenerContaPedido = listenerContaPedido ;
        tipoConexao = TipoConexao.cxAlterar ;
    }

    public void executar(){
        try {
            if (Configuracao.getPedidoCompartilhado(ctx)) {
                switch (tipoConexao) {
                    case cxConsultar: new ConexaoContaPedidoCompartilhado(ctx, filters, order, listenerContaPedido).execute() ;
                    break;
                    case cxAlterar:  new ConexaoContaPedidoCompartilhado(ctx, contaPedido, listenerContaPedido).execute() ;
                    break;
                    default: listenerContaPedido.erro("Opção Inválida!");
                }

            } else {
                switch (tipoConexao) {
                    case cxConsultar :  new ConexaoContaPedido(ctx, filters, order, listenerContaPedido).executar() ;
                    break;
                    case cxAlterar:   new ConexaoContaPedido(ctx, contaPedido, listenerContaPedido).executar() ;
                    break;
                    default: listenerContaPedido.erro("Opção Inválida!");
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerContaPedido.erro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerContaPedido.erro(e.getMessage());
        }
    }

}
