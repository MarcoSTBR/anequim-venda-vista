package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.conexoes.ConexaoProdutoImposto;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ProdutoImposto;
import com.anequimplus.listeners.ListenerProdutoImposto;
import com.anequimplus.tipos.TipoConexao;

import java.net.MalformedURLException;

public class BuildProdutoImposto {

    private Context ctx ;
    private FilterTables filters ;
    private String order ;
    private ProdutoImposto produtoImposto;
    private ListenerProdutoImposto listenerProdutoImposto ;
    private TipoConexao tipoConexao ;

    public BuildProdutoImposto(Context ctx, FilterTables filters, String order, ListenerProdutoImposto listenerProdutoImposto) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order ;
        this.listenerProdutoImposto = listenerProdutoImposto;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public void executar(){
        try {
        switch (tipoConexao){
                case cxConsultar :
                        new ConexaoProdutoImposto(ctx, filters, order, listenerProdutoImposto).execute() ;
                    break;
                default:listenerProdutoImposto.erro("BuildProdutoImposto não implementado");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerProdutoImposto.erro(e.getMessage());
        }

    }

}
