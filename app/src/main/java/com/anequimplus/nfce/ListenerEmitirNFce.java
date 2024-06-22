package com.anequimplus.nfce;

import com.anequimplus.entity.ContaPedidoNFCe;

public interface ListenerEmitirNFce {
    void ok(ContaPedidoNFCe it) ;
    void erro(String msg) ;
}
