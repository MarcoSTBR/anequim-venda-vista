package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.conexoes.ConexaoSetorImpCompartilhado;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImp;
import com.anequimplus.listeners.ListenerSetorImp;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class BuildSetorImp {
    private Context ctx ;
    private FilterTables filter ;
    private String order ;
    private SetorImp setorImp = null ;
    private List<SetorImp> setorImps = null ;
    private ListenerSetorImp listenerSetorImp ;
    private TipoConexao tipoConexao ;

    public BuildSetorImp(Context ctx, FilterTables filter, String order, ListenerSetorImp listenerSetorImp) {
        this.ctx = ctx;
        this.filter = filter;
        this.order = order;
        this.listenerSetorImp = listenerSetorImp;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildSetorImp(Context ctx, SetorImp setorImp, ListenerSetorImp listenerSetorImp) {
        this.ctx = ctx;
        this.setorImp = setorImp;
        this.listenerSetorImp = listenerSetorImp;
        if (setorImp.getId() > 0)
            tipoConexao = TipoConexao.cxAlterar ;
        else tipoConexao = TipoConexao.cxIncluir ;
    }

    public BuildSetorImp(Context ctx, List<SetorImp> setorImps, ListenerSetorImp listenerSetorImp) {
        this.ctx = ctx;
        this.setorImps = setorImps;
        this.listenerSetorImp = listenerSetorImp;
        tipoConexao = TipoConexao.cxIncluir ;
    }

    public BuildSetorImp(Context ctx, FilterTables filter, ListenerSetorImp listenerSetorImp) {
        this.ctx = ctx;
        this.filter = filter;
        this.listenerSetorImp = listenerSetorImp;
        tipoConexao = TipoConexao.cxDeletar ;
    }

    public void executar(){
        try {
        if (Configuracao.getLinkComandaRemota(ctx).length()>0){
                switch (tipoConexao){
                    case cxConsultar: new ConexaoSetorImpCompartilhado(ctx, filter, order, listenerSetorImp).executar();
                    break;
                    case cxIncluir:{
                        if (setorImp == null) {
                                new ConexaoSetorImpCompartilhado(ctx, setorImps, listenerSetorImp).executar();
                        }
                         else new ConexaoSetorImpCompartilhado(ctx, setorImp, listenerSetorImp).executar();
                    }
                    break;
                    case cxDeletar: new ConexaoSetorImpCompartilhado(ctx, filter, listenerSetorImp).executar();
                    break;
                    default:listenerSetorImp.erro("Imprementar SetorImp Compartilhado");
                }
            } else {
                listenerSetorImp.erro("Imprementar SetorImp Não compartilhado");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerSetorImp.erro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerSetorImp.erro(e.getMessage());
        }

    }
}
