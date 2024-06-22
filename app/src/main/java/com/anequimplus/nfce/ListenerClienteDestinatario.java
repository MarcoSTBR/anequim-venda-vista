package com.anequimplus.nfce;

import com.anequimplus.entity.ContaPedidoDest;

public interface ListenerClienteDestinatario {

    void ok(ContaPedidoDest contaPedidoDest);
    void cancelar(String msg) ;
}
