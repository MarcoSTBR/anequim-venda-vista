package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.conexoes.ConexaoUsuarioAcesso;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.UsuarioAcesso;
import com.anequimplus.listeners.ListenerUsuarioAcesso;
import com.anequimplus.tipos.TipoConexao;

import java.net.MalformedURLException;

public class BuildUsuarioAcesso {

    private Context ctx ;
    private FilterTables filters ;
    private String order ;
    private UsuarioAcesso usuarioAcesso ;
    private ListenerUsuarioAcesso listenerUsuarioAcesso ;
    private TipoConexao tipoConexao ;

    public BuildUsuarioAcesso(Context ctx, FilterTables filters, String order, ListenerUsuarioAcesso listenerUsuarioAcesso) {
        this.ctx = ctx;
        this.filters = filters;
        this.order = order;
        this.listenerUsuarioAcesso = listenerUsuarioAcesso;
        tipoConexao = TipoConexao.cxConsultar ;
    }

    public void executar(){
        try {
          switch (tipoConexao) {
              case cxConsultar: new ConexaoUsuarioAcesso(ctx, filters, order, listenerUsuarioAcesso).executar();
              break ;
              default: listenerUsuarioAcesso.erro("tipo de Conexão Inválido");
          }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerUsuarioAcesso.erro(e.getMessage());
        }
    }
}
