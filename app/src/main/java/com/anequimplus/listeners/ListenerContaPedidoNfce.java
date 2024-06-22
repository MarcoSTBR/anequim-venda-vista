package com.anequimplus.listeners;

import com.anequimplus.entity.ContaPedidoNFCe;

import java.util.List;

public interface ListenerContaPedidoNfce {

    void ok(List<ContaPedidoNFCe> l) ;
    void erro(String msg) ;
}
