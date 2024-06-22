package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.conexoes.ConexaoSuprimentoCompartilhado;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Suprimento;
import com.anequimplus.listeners.ListenerSuprimento;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BuildSuprimento {
    private Context ctx ;
    private FilterTables filters ;
    private String order ;
    private Suprimento suprimento ;
    private ListenerSuprimento listenerSuprimento ;
    private TipoConexao tipoConexao ;

    public BuildSuprimento(Context ctx, FilterTables filters, String order, ListenerSuprimento listenerSuprimento) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order ;
        this.listenerSuprimento = listenerSuprimento;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildSuprimento(Context ctx, Suprimento suprimento, ListenerSuprimento listenerSuprimento) {
        this.ctx = ctx;
        this.suprimento = suprimento;
        this.listenerSuprimento = listenerSuprimento;
        tipoConexao = TipoConexao.cxIncluir ;
    }

    public void executar(){
        if (!Configuracao.getPedidoCompartilhado(ctx)){
            switch (tipoConexao){
                case cxConsultar: listenerSuprimento.ok(DaoDbTabela.getSuprimentoADO(ctx).getList(filters)) ;
                break ;
                case cxIncluir: {
                        List<Suprimento> l = new ArrayList<Suprimento>() ;
                        DaoDbTabela.getSuprimentoADO(ctx).incluir(suprimento);
                        l.add(suprimento);
                        listenerSuprimento.ok(l);
                    }
                 break ;
                default:listenerSuprimento.erro("opção conexão inválida suprmento!");
            }

        } else {
            try {
               switch (tipoConexao){
                case cxConsultar: new ConexaoSuprimentoCompartilhado(ctx, filters, listenerSuprimento).executar() ;
                    break ;
                case cxIncluir:
                        new ConexaoSuprimentoCompartilhado(ctx, suprimento, listenerSuprimento).executar() ;
                    break ;
                default:listenerSuprimento.erro("opção conexão inválida suprmento!");

               }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                listenerSuprimento.erro(e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                listenerSuprimento.erro(e.getMessage());
            }
        }
    }


}
