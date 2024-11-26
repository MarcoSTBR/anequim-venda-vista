package com.anequimplus.builds;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.conexoes.ConexaoSangriaCompartilhada;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Sangria;
import com.anequimplus.listeners.ListenerSangria;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BuildSangria {
    private Activity ctx ;
    private FilterTables filters ;
    private String order ;
    private Sangria sangria ;
    private ListenerSangria listenerSangria ;
    private TipoConexao tipoConexao ;

    public BuildSangria(Activity ctx, FilterTables filters, String order, ListenerSangria listenerSangria) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order ;
        this.listenerSangria = listenerSangria;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildSangria(Activity ctx, Sangria sangria, ListenerSangria listenerSangria) {
        this.ctx = ctx;
        this.sangria = sangria;
        this.listenerSangria = listenerSangria;
        if (sangria.getId() == 0)
            tipoConexao = TipoConexao.cxIncluir;
        else tipoConexao = TipoConexao.cxAlterar ;
    }

    public void executar(){
        if (!Configuracao.getPedidoCompartilhado(ctx)){
            switch (tipoConexao){
                case cxConsultar: listenerSangria.ok(DaoDbTabela.getSangriaADO(ctx).getList(filters)) ;
                    break ;
                case cxIncluir: {
                    List<Sangria> l = new ArrayList<Sangria>() ;
                    DaoDbTabela.getSangriaADO(ctx).incluir(sangria);
                    l.add(sangria);
                    listenerSangria.ok(l);
                }
                break ;
                default:listenerSangria.erro("opção conexão inválida sangria!");
            }

        } else {
            try {
                switch (tipoConexao){
                    case cxConsultar: new ConexaoSangriaCompartilhada(ctx, filters, listenerSangria).executar() ;
                        break ;
                    case cxIncluir:
                        new ConexaoSangriaCompartilhada(ctx, sangria, listenerSangria).executar() ;
                        break ;
                    default:listenerSangria.erro("opção conexão inválida sangria!");

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                listenerSangria.erro(e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                listenerSangria.erro(e.getMessage());
            }
        }

    }
}
