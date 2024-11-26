package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.conexoes.ConexaoSetorImpTerminalCompartilhado;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImpTerminal;
import com.anequimplus.listeners.ListenerSetorImpTerminal;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class BuildSetorImpTerminal {
    private Activity ctx ;
    private FilterTables filter ;
    private String order ;
    private SetorImpTerminal setorImpTerminal = null ;
    private List<SetorImpTerminal> listSetorImpTerminals = null ;
    private ListenerSetorImpTerminal listenerSetorImpTerminal ;
    private TipoConexao tipoConexao ;

    public BuildSetorImpTerminal(Activity ctx, FilterTables filter, String order, ListenerSetorImpTerminal listenerSetorImpTerminal) {
        this.ctx = ctx;
        this.filter = filter;
        this.order = order;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildSetorImpTerminal(Activity ctx, SetorImpTerminal setorImpTerminal, ListenerSetorImpTerminal listenerSetorImpTerminal) {
        this.ctx = ctx;
        this.setorImpTerminal = setorImpTerminal;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        if (setorImpTerminal.getId() > 0)
            tipoConexao = TipoConexao.cxAlterar ;
        else tipoConexao = TipoConexao.cxIncluir ;
    }

    public BuildSetorImpTerminal(Activity ctx, List<SetorImpTerminal> listSetorImpTerminals, ListenerSetorImpTerminal listenerSetorImpTerminal) {
        this.ctx = ctx;
        this.listSetorImpTerminals = listSetorImpTerminals;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        tipoConexao = TipoConexao.cxIncluir ;
    }

    public BuildSetorImpTerminal(Activity ctx, FilterTables filter, ListenerSetorImpTerminal listenerSetorImpTerminal) {
        this.ctx = ctx;
        this.filter = filter;
        this.listenerSetorImpTerminal = listenerSetorImpTerminal;
        tipoConexao = TipoConexao.cxDeletar ;
    }

    public void executar(){
        try {
        if (Configuracao.getLinkComandaRemota(ctx).length()>0){
                switch (tipoConexao){
                    case cxConsultar: new ConexaoSetorImpTerminalCompartilhado(ctx, filter, order, listenerSetorImpTerminal).executar();
                    break;
                    case cxIncluir:{
                        if (setorImpTerminal == null) {
                                new ConexaoSetorImpTerminalCompartilhado(ctx, listSetorImpTerminals, listenerSetorImpTerminal).executar();
                        }
                         else new ConexaoSetorImpTerminalCompartilhado(ctx, setorImpTerminal, listenerSetorImpTerminal).executar();
                    }
                    break;
                    case cxDeletar: new ConexaoSetorImpTerminalCompartilhado(ctx, filter, listenerSetorImpTerminal).executar();
                    break;
                    default:listenerSetorImpTerminal.erro("Imprementar SetorImp Compartilhado");
                }
            } else {
                listenerSetorImpTerminal.erro("Imprementar SetorImp Não compartilhado");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerSetorImpTerminal.erro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerSetorImpTerminal.erro(e.getMessage());
        }

    }
}
