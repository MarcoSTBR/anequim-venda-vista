package com.anequimplus.impressao;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.utilitarios.RowImpressao;

import java.util.List;

public interface ControleImpressora {

    public void open() ;
    public void close() ;
    public void setListenerImpressao(ListenerImpressao listenerImpressao) ;
    public void imprimeConta(ContaPedido conta) ;
    public void imprimirXML(String txt) ;
    public void impressaoLivre(List<RowImpressao> l) ;
    public void imprimirRecibo(ContaPedido conta) ;

}
