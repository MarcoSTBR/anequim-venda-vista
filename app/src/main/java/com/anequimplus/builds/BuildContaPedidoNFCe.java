package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.conexoes.ConexaoContaPedidoNFCeCompartilhado;
import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerContaPedidoNfce;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BuildContaPedidoNFCe {

    private Activity ctx ;
    private ContaPedidoNFCe contaPedidoNfce = null;
    private FilterTables filter ;
    private String order ;
    private ListenerContaPedidoNfce listenerEmissaoNfce ;
    private TipoConexao tipoConexao ;

    public BuildContaPedidoNFCe(Activity ctx, FilterTables filter, String order, ListenerContaPedidoNfce listenerEmissaoNfce) {
        this.ctx = ctx;
        this.filter = filter;
        this.order = order;
        this.listenerEmissaoNfce = listenerEmissaoNfce;
        this.tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildContaPedidoNFCe(Activity ctx, ContaPedidoNFCe contaPedidoNfce, TipoConexao tipoConexao, ListenerContaPedidoNfce listenerEmissaoNfce) {
        this.ctx = ctx;
        this.contaPedidoNfce = contaPedidoNfce;
        this.listenerEmissaoNfce = listenerEmissaoNfce;
        this.tipoConexao = tipoConexao ;
    }

    public void executar(){
        //if ()


        if (Configuracao.getPedidoCompartilhado(ctx)) {
            try {
                switch (tipoConexao) {
                    case cxConsultar:
                        new ConexaoContaPedidoNFCeCompartilhado(ctx, filter, order, listenerEmissaoNfce).execute();
                        break;
                    default:new ConexaoContaPedidoNFCeCompartilhado(ctx, contaPedidoNfce, tipoConexao, listenerEmissaoNfce).execute();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                listenerEmissaoNfce.erro(e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                listenerEmissaoNfce.erro(e.getMessage());
            }
        } else {

            switch (tipoConexao) {
                case cxConsultar: listenerEmissaoNfce.ok(DaoDbTabela.getContaPedidoNFceADO(ctx).getList(filter, order)) ;
                break;
                default: {
                    List<ContaPedidoNFCe> l = new ArrayList<ContaPedidoNFCe>();
                    if (tipoConexao == TipoConexao.cxIncluir) {
                        DaoDbTabela.getContaPedidoNFceADO(ctx).incluir(contaPedidoNfce) ;
                        l.add(contaPedidoNfce) ;
                        listenerEmissaoNfce.ok(l);
                    } else {
                        DaoDbTabela.getContaPedidoNFceADO(ctx).alterar(contaPedidoNfce) ;
                        l.add(contaPedidoNfce) ;
                        listenerEmissaoNfce.ok(l);
                    }
                }
            }
        }

    }

}

