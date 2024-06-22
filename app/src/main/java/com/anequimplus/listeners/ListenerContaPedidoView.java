package com.anequimplus.listeners;

import com.anequimplus.entity.ContaPedidoView;

import java.util.List;

public interface ListenerContaPedidoView {

    void ok(List<ContaPedidoView> l) ;
    void erro(int cod, String msg) ;

}
