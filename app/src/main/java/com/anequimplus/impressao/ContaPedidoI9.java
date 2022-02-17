package com.anequimplus.impressao;

import android.content.Context;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.relatorios.ImpressaoContaPedidoI9;
import com.anequimplus.utilitarios.RowImpressao;
import com.elgin.e1.Impressora.Termica;

import java.util.List;

public class ContaPedidoI9 implements ControleImpressora {

    private Context ctx ;
    private ListenerImpressao listenerImpressao ;
    private ContaPedido contapedido ;

    public ContaPedidoI9(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void open() {
        String enderecoIP = "192.168.10.98" ;
       // int retorno = Termica.AbreConexaoImpressora(3, "I9", enderecoIP, 9100) ;
    }

    @Override
    public void close() {
      //  if Termica.
       int retorno = Termica.FechaConexaoImpressora() ;

    }

    @Override
    public void setListenerImpressao(ListenerImpressao listenerImpressao) {
     this.listenerImpressao = listenerImpressao ;
    }

    @Override
    public void imprimeConta(ContaPedido conta) {
        this.contapedido = contapedido ;
        ImpressaoContaPedidoI9 impressaoContaPedido = new ImpressaoContaPedidoI9(48) ;
        impressaoContaPedido.setContaPedido(conta);
        List<ContaPedidoI9.LinhaImpressao> list = impressaoContaPedido.getListLinhas() ;
        int retorno = 0 ;
        for (ContaPedidoI9.LinhaImpressao l : list){
          //  retorno = Termica.ImpressaoTexto(l.getLinha(), l.getAling().valor, l.getEst().valor ,  l.getTam().valor);
        }
        retorno = Termica.Corte(10) ;
        if (retorno != 0) listenerImpressao.onError(retorno,"Erro na Impressão COD: "+retorno) ;
        listenerImpressao.onImpressao(retorno);
    }

    @Override
    public void imprimirXML(String txt) {


    }

    @Override
    public void impressaoLivre(List<RowImpressao> l) {

    }

    public static class LinhaImpressao{

    }
}
