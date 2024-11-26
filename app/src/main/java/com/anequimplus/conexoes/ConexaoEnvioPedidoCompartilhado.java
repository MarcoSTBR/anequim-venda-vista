package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Pedido;
import com.anequimplus.listeners.ListenerEnvioPedido;
import com.anequimplus.listeners.ListerConexao;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ConexaoEnvioPedidoCompartilhado extends ConexaoServer {

    private ListerConexao listerConexao ;
    private List<Pedido> list ;
    private ListenerEnvioPedido listenerEnvioPedido ;

    public ConexaoEnvioPedidoCompartilhado(Activity ctx, List<Pedido> list, ListenerEnvioPedido listenerEnvioPedido) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Enviando Pedido" ;
        method = "POST" ;
        this.list = list ;
        this.listenerEnvioPedido = listenerEnvioPedido ;
//        maps.put("loja_id", UtilSet.getLojaId(ctx)) ;
//        maps.put("MAC",UtilSet.getMAC(ctx)) ;
//        maps.put("system_user_id",UtilSet.getUsuarioId(ctx)) ;
        maps.put("data",getList()) ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/enviar_pedido") ;
    }


    private JSONArray getList() throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (Pedido p : list){
            JSONObject j = p.getJsonExportacao() ;
            j.put("LOJA_ID", UtilSet.getLojaId(ctx) );
            j.put("USUARIO", UtilSet.getLogin(ctx) );
            j.put("VENDEDOR", UtilSet.getLogin(ctx) );
            j.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx));
            j.put("SYSTEM_USER_ID", UtilSet.getUsuarioId(ctx));
            j.put("UUID", UtilSet.getUUID());
            for (int i = 0 ; i < j.getJSONArray("ITENS").length() ; i++){
                j.getJSONArray("ITENS").getJSONObject(i).put("UUID",UtilSet.getUUID()) ;
                j.getJSONArray("ITENS").getJSONObject(i).put("CONF_TERMINAL_ID",UtilSet.getTerminalId(ctx)) ;
            }
            jaa.put(j) ;
        }
        return jaa ;
    }

/*
    public void execute(){
        if (Configuracao.getPedidoCompartilhado(ctx)) {
            this.execute() ;
        } else {
            DaoDbTabela.getContaPedidoInternoDAO(ctx).atualizar(list) ;
            apagarPedidos() ;
            listenerEnvioPedido.envioOK(list);
        }

    }
*/

    private void apagarPedidos(){
        for (Pedido p : list) {
            DaoDbTabela.getPedidoADO(ctx).excluir(p);
           // DaoDbTabela.getPedidoItemADO(ctx).excluirTodosPedidos(p);
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
                listenerEnvioPedido.envioOK(list) ;
            } else listenerEnvioPedido.erroEnvio(j.getString("data")) ;
        } catch (Exception e) {
            e.printStackTrace();
            listenerEnvioPedido.erroEnvio(e.getMessage()) ;
        }
    }
}
