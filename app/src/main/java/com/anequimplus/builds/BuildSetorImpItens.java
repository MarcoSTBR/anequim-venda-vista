package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.conexoes.ConexaoSetorImpItensCompartilhado;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImpItens;
import com.anequimplus.listeners.ListenerSetorImpItens;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class BuildSetorImpItens {
    private Activity ctx ;
    private FilterTables filter ;
    private String order ;
    private SetorImpItens setorImpItens ;
    private ListenerSetorImpItens listenerSetorImpItens ;
    private TipoConexao tipoConexao ;
    private List<SetorImpItens> listsetorImpItens ;

    public BuildSetorImpItens(Activity ctx, FilterTables filter, String order, ListenerSetorImpItens listenerSetorImpItens) {
        this.ctx = ctx;
        this.filter = filter;
        this.order = order;
        this.listenerSetorImpItens = listenerSetorImpItens;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildSetorImpItens(Activity ctx, SetorImpItens setorImpItens, ListenerSetorImpItens listenerSetorImpItens) {
        this.ctx = ctx;
        this.setorImpItens = setorImpItens;
        this.listenerSetorImpItens = listenerSetorImpItens;
        if (setorImpItens.getId() > 0)
            tipoConexao = TipoConexao.cxAlterar ;
        else tipoConexao = TipoConexao.cxIncluir ;
    }

    public BuildSetorImpItens(Activity ctx, List<SetorImpItens> listsetorImpItens, ListenerSetorImpItens listenerSetorImpItens) {
        this.ctx = ctx;
        this.listsetorImpItens = listsetorImpItens;
        this.listenerSetorImpItens = listenerSetorImpItens;
        tipoConexao = TipoConexao.cxIncluir ;
    }

    public BuildSetorImpItens(Activity ctx, FilterTables filter, ListenerSetorImpItens listenerSetorImpItens) {
        this.ctx = ctx;
        this.filter = filter;
        this.order = order;
        this.listenerSetorImpItens = listenerSetorImpItens;
        tipoConexao = TipoConexao.cxDeletar ;
    }

    public void executar(){
        try {
            if (Configuracao.getLinkComandaRemota(ctx).length()>0){
                switch (tipoConexao){
                    case cxConsultar: new ConexaoSetorImpItensCompartilhado(ctx, filter, order, listenerSetorImpItens).executar();
                        break;
                    case cxIncluir:{
                        if (setorImpItens == null) {
                            new ConexaoSetorImpItensCompartilhado(ctx, listsetorImpItens, listenerSetorImpItens).executar();
                        }
                        else new ConexaoSetorImpItensCompartilhado(ctx, setorImpItens, listenerSetorImpItens).executar();
                    }
                    break;
                    case cxDeletar: new ConexaoSetorImpItensCompartilhado(ctx, filter, listenerSetorImpItens).executar();
                    break;
                    default:listenerSetorImpItens.erro("Imprementar SetorImp Compartilhado");
                }
            } else {
                listenerSetorImpItens.erro("Imprementar SetorImp Não compartilhado");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerSetorImpItens.erro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerSetorImpItens.erro(e.getMessage());
        }


    }
}
