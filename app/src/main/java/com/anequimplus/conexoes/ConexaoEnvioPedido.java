package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Pedido;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoEnvioPedido extends ConexaoServer {

    private ListerConexao listerConexao ;
    private List<Pedido> list ;

    public ConexaoEnvioPedido(Context ctx, List<Pedido> list) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado, JSONException {
        super(ctx);
        msg = "Enviando Pedido" ;
        this.list = list ;
        setParametros();
    }

    public ConexaoEnvioPedido(Context ctx, Pedido pedido) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, JSONException, MalformedURLException {
        super(ctx);
        msg = "Enviando Pedido" ;
       // if (pedido == null) throw new Exception("pedido inválido") ;
        list = new ArrayList<Pedido>();
        list.add(pedido) ;
        setParametros();
    }

    private JSONArray getList() throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (Pedido p : list){
            jaa.put(p.getJSon()) ;
        }
        return jaa ;
    }

    private void setParametros() throws JSONException, MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        maps.put("class","AfoodContaPedidoItem") ;
        maps.put("method","incluir") ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getUsuarioId(ctx)) ;
        maps.put("data",getList()) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fIncluirPedido).getUrl();
    }

    public void execute(){
        if (Configuracao.getPedidoCompartilhado(ctx)) {
            this.execute() ;
        } else {
            Dao.getContaPedidoInternoDAO(ctx).atualizar(list) ;
            apagarPedidos() ;
            envioOK(list);
        }

    }

    private void apagarPedidos(){
        for (Pedido p : list) {
            Dao.getPedidoADO(ctx).excluir(p.getId());
            Dao.getPedidoItemADO(ctx).excluir(p);
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExecute_Envio",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                apagarPedidos() ;
                envioOK(list) ;
            } else erroEnvio(j.getString("data")) ;
        } catch (Exception e) {
            e.printStackTrace();
            erroEnvio(e.getMessage()) ;
        }
    }
    public abstract void envioOK(List<Pedido> l);
    public abstract void erroEnvio(String msg) ;
//    public abstract void ErroImpressao(int status, String msg) ;
}
