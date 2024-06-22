package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.conexoes.ConexaoCaixaCompartilhado;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BuildCaixa {

    private Context ctx ;
    private FilterTables filters ;
    private String order ;
    private Caixa caixa ;
    private ListenerCaixa listenercaixa ;
    private TipoConexao tipoConexao ;


    public BuildCaixa(Context ctx, FilterTables filters, String order, ListenerCaixa listenercaixa) {
        this.ctx = ctx;
        this.filters = filters;
        this.listenercaixa = listenercaixa;
        this.order = order ;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public BuildCaixa(Context ctx, Caixa caixa, ListenerCaixa listenercaixa) {
        this.ctx = ctx;
        this.caixa = caixa;
        this.listenercaixa = listenercaixa;
        if (caixa.getId() == 0)
            tipoConexao = TipoConexao.cxIncluir ;
         else tipoConexao = TipoConexao.cxAlterar ;
    }



    public void executar(){
        if (!Configuracao.getPedidoCompartilhado(ctx)){

            switch (tipoConexao) {
                case cxConsultar: listenercaixa.ok(DaoDbTabela.getCaixaADO(ctx).getList(filters.getList()));
                break;
                case cxIncluir: {
                    List<Caixa> l = new ArrayList<Caixa>() ;
                    DaoDbTabela.getCaixaADO(ctx).setCaixa(caixa);
                    l.add(caixa) ;
                    listenercaixa.ok(l);
                    }
                break;
                case cxAlterar: {
                     List<Caixa> l = new ArrayList<Caixa>() ;
                     DaoDbTabela.getCaixaADO(ctx).alterar(caixa);
                     l.add(caixa) ;
                     listenercaixa.ok(l);
                     }
                break;
                default:listenercaixa.erro("Tipo de Caixa Incorreto!");
            }

        } else {
          try {
              switch (tipoConexao) {
                  case cxConsultar:
                      new ConexaoCaixaCompartilhado(ctx, filters, listenercaixa).executar();
                      break;
                  case cxIncluir :
                      new ConexaoCaixaCompartilhado(ctx, caixa, listenercaixa).executar();
                      break;
                  case cxAlterar:
                      new ConexaoCaixaCompartilhado(ctx, caixa, listenercaixa).executar();
                      break;
                  default:listenercaixa.erro("Tipo Conexão Caixa Não Implementado!");

              }
          } catch (MalformedURLException e) {
              e.printStackTrace();
              listenercaixa.erro(e.getMessage());
          }
        }
    }


}
