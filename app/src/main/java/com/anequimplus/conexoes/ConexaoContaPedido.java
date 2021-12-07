package com.anequimplus.conexoes;

import static com.anequimplus.tipos.Link.fConsultaPedido;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

public abstract class ConexaoContaPedido extends ConexaoServer {

    private List<ContaPedido> list ;
    private int idFiltro = -1 ;

    public ConexaoContaPedido(Context ctx) {
        super(ctx);
        msg = "Conta Pedido" ;
        try {
            maps.put("class","AfoodContaPedido") ;
            maps.put("method","consultarAbertos") ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(fConsultaPedido).getUrl();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erro(e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }
    public ConexaoContaPedido(Context ctx, int idFiltro) {
        super(ctx);
        this.idFiltro = idFiltro ;
        msg = "Conta Pedido" ;
        try {
            maps.put("class","AfoodContaPedido") ;
            maps.put("method","consultarAbertos") ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(fConsultaPedido).getUrl();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erro(e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public void execute(){
        if (Configuracao.getPedidoCompartilhado(ctx)) {
            this.execute() ;
        } else {
            if (idFiltro == -1) {
                list = Dao.getContaPedidoInternoDAO(ctx).getList();
            } else {
                list = Dao.getContaPedidoInternoDAO(ctx).getList(idFiltro);
            }
            Dao.getContaPedidoADO(ctx).contaPedidoInternoAdd(list);
            oK() ;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                  Dao.getContaPedidoADO(ctx).contaPedidoAdd(j.getJSONArray("data"));
                  oK() ;
            } else {
                erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public abstract void oK() ;
    public abstract void erro(String mgg) ;
}
