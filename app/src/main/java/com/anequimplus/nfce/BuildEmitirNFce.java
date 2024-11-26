package com.anequimplus.nfce;

import android.app.Activity;

import com.anequimplus.builds.BuildProdutoImposto;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoDest;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ProdutoImposto;
import com.anequimplus.listeners.ListenerProdutoImposto;
import com.anequimplus.utilitarios.Configuracao;

import java.net.MalformedURLException;
import java.util.List;

public class BuildEmitirNFce {

    private Activity ctx ;
    private ListenerEmitirNFce listenerEmitirNFce;
    private ContaPedido contaPedido ;
    private List<ProdutoImposto> listImpostos ;
    private ContaPedidoDest contaPedidoDest = null;

    public BuildEmitirNFce(Activity ctx, ContaPedido contaPedido, ListenerEmitirNFce listenerEmitirNFce) {
        this.ctx = ctx;
        this.listenerEmitirNFce = listenerEmitirNFce;
        this.contaPedido = contaPedido;
    }

    public void executar(){
        FilterTables filters = new FilterTables();
        filters.add(new FilterTable("PRODUTO_ID", "IN", getIn()));
        new BuildProdutoImposto(ctx, filters, "PRODUTO_ID", new ListenerProdutoImposto() {
            @Override
            public void ok(List<ProdutoImposto> l) {
                listImpostos = l ;
                emitirNFce() ;
            }
            @Override
            public void erro(String msg) {
                listenerEmitirNFce.erro(msg);
            }
        }).executar();
    }

    private String getIn(){
        String in = "" ;
        for (ContaPedidoItem i : contaPedido.getListContaPedidoItemAtivosAgrupados()){
            if (in.length() == 0)
                in = "("+i.getProduto().getId() ;
            else
                in = in + ","+ i.getProduto().getId() ;
        }
        return in + ")" ;
    }

    private void emitirNFce(){
       new Cliente_Destinatario_NFce(ctx, contaPedido, contaPedidoDest, new ListenerClienteDestinatario() {
           @Override
           public void ok(ContaPedidoDest c) {
               contaPedidoDest = c ;
               emissaoNFce() ;
           }

           @Override
           public void cancelar(String msg) {
               listenerEmitirNFce.erro(msg);
           }
       }).executar();
    }

    private void emissaoNFce(){
        try {
           switch (Configuracao.getOpcaoNFCE(ctx)) {
               case 1 : new ConexaoEmitirNFCeServerLocal(ctx, contaPedido, contaPedidoDest, listImpostos, listenerEmitirNFce).executar();
               break ;
               case 2 : new ConexaoEmitirNFCeCloudNFCe(ctx, contaPedido, contaPedidoDest, listImpostos, listenerEmitirNFce).executar();
               break ;
               default: listenerEmitirNFce.erro("Emissão De NFCe Não Habilitada!");
           }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerEmitirNFce.erro(e.getMessage());
        }
    }


}
