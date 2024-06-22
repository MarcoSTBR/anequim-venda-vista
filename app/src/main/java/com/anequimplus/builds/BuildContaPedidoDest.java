package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.conexoes.ConexaoContaPedidoDest;
import com.anequimplus.entity.ContaPedidoDest;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerContaPedidoDest;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BuildContaPedidoDest {

    private Context ctx ;
    private FilterTables filters ;
    private String order  ;
    private ContaPedidoDest contaPedidoDest ;
    private ListenerContaPedidoDest listenerContaPedidoDest ;
    private TipoConexao tipoConexao ;

    public BuildContaPedidoDest(Context ctx, FilterTables filters, String order, ListenerContaPedidoDest listenerContaPedidoDest) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order;
        this.listenerContaPedidoDest = listenerContaPedidoDest;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildContaPedidoDest(Context ctx, ContaPedidoDest contaPedidoDest, ListenerContaPedidoDest listenerContaPedidoDest) {
        this.ctx = ctx;
        this.contaPedidoDest = contaPedidoDest;
        this.listenerContaPedidoDest = listenerContaPedidoDest;
        if (contaPedidoDest.getId() == 0)
            tipoConexao= TipoConexao.cxIncluir ;
         else tipoConexao= TipoConexao.cxAlterar ;
    }

    public void executar(){
        if (Configuracao.getPedidoCompartilhado(ctx)){
            try {
            switch (tipoConexao){
                case cxConsultar:
                    new ConexaoContaPedidoDest(ctx, filters, order, listenerContaPedidoDest).execute() ;
                    break;
                case cxIncluir: new ConexaoContaPedidoDest(ctx, contaPedidoDest, listenerContaPedidoDest).execute();
                    break;
                case cxAlterar: new ConexaoContaPedidoDest(ctx, contaPedidoDest, listenerContaPedidoDest).execute();
                    break;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                listenerContaPedidoDest.erro(e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                listenerContaPedidoDest.erro(e.getMessage());
            }
        } else {
            switch (tipoConexao){
                case cxConsultar:listenerContaPedidoDest.ok(DaoDbTabela.getContaPedidoDestADO(ctx).consultar(filters, order));
                break;
                case cxIncluir:{
                        List<ContaPedidoDest> l = new ArrayList<ContaPedidoDest>();
                        DaoDbTabela.getContaPedidoDestADO(ctx).inserir(contaPedidoDest) ;
                        l.add(contaPedidoDest) ;
                        listenerContaPedidoDest.ok(l);
                    }
                case cxAlterar:{
                        List<ContaPedidoDest> l = new ArrayList<ContaPedidoDest>();
                        DaoDbTabela.getContaPedidoDestADO(ctx).inserir(contaPedidoDest) ;
                        l.add(contaPedidoDest) ;
                        listenerContaPedidoDest.ok(l);
                    }
                break;
            }

        }
    }
}
