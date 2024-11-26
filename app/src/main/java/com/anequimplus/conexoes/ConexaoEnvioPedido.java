package com.anequimplus.conexoes;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Pedido;
import com.anequimplus.listeners.ListenerEnvioPedido;
import com.anequimplus.listeners.ListerConexao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class ConexaoEnvioPedido {

    private Activity ctx ;
    private ListerConexao listerConexao ;
    private List<Pedido> list ;
    private ListenerEnvioPedido listenerEnvioPedido ;

    public ConexaoEnvioPedido(Activity ctx, List<Pedido> list, ListenerEnvioPedido listenerEnvioPedido) throws MalformedURLException, JSONException {
       // super(ctx);
       // msg = "Enviando Pedido" ;
        this.ctx = ctx ;
        this.list = list ;
        this.listenerEnvioPedido = listenerEnvioPedido ;
       // setParametros();
    }

/*

    private JSONArray getList() throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (Pedido p : list){
            jaa.put(p.getJSon()) ;
        }
        return jaa ;
    }

    private void setParametros() throws JSONException, MalformedURLException  {
        maps.put("class","AfoodContaPedidoItem") ;
        maps.put("method","incluir") ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getUsuarioId(ctx)) ;
        maps.put("data",getList()) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fIncluirPedido).getUrl();
    }
*/

    public void execute(){
            DaoDbTabela.getContaPedidoInternoDAO(ctx).atualizar(list) ;
            apagarPedidos() ;
            listenerEnvioPedido.envioOK(list);
    }

    private void apagarPedidos(){
        for (Pedido p : list) {
            DaoDbTabela.getPedidoADO(ctx).excluir(p);
        }
    }

/*
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExecute_Envio",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                apagarPedidos() ;
                listenerEnvioPedido.envioOK(list) ;
            } else listenerEnvioPedido.erroEnvio(j.getString("data")) ;
        } catch (Exception e) {
            e.printStackTrace();
            listenerEnvioPedido.erroEnvio(e.getMessage()) ;
        }
    }
*/
}
