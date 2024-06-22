package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.conexoes.ConexaoEnvioPedido;
import com.anequimplus.conexoes.ConexaoEnvioPedidoCompartilhado;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.listeners.ListenerEnvioPedido;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class BuildEnviarPedido {

    private Context ctx ;
    private ListenerEnvioPedido listenerEnvioPedido ;
    private List<Pedido> list ;

    public BuildEnviarPedido(Context ctx, List<Pedido> list, ListenerEnvioPedido listenerEnvioPedido){
        this.ctx = ctx ;
        this.list= list ;
        this.listenerEnvioPedido = listenerEnvioPedido ;
    }

    private void recalcularAcompanhamentos(){
        for (Pedido p : list){
            for (PedidoItem it : p.getListPedidoItem()){
                for (PedidoItemAcomp ac : it.getAcompanhamentos()){
                    Double q = it.getItemSelect().getQuantidade() * ac.getItemSelect().getQuantidade() ;
                    Double vl = q * ac.getItemSelect().getPreco()  ;
                    Double com = 0.0 ;
                    ac.getItemSelect().setQuantidade(q) ;
                    ac.getItemSelect().setValor(vl);
                }
            }

        }
    }

    public void executar() {
        recalcularAcompanhamentos() ;
     try {
           if (!Configuracao.getPedidoCompartilhado(ctx)){
               new ConexaoEnvioPedido(ctx, list, listenerEnvioPedido).execute();
           } else {
               new ConexaoEnvioPedidoCompartilhado(ctx, list, listenerEnvioPedido).execute();
           }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerEnvioPedido.erroEnvio(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEnvioPedido.erroEnvio(e.getMessage());
        }
    }

}
