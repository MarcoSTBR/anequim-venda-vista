package com.anequimplus.listeners;

import com.anequimplus.entity.ContaPedidoItem;

import java.util.List;

public interface ListenerContaPedidoItem {

    void ok(List<ContaPedidoItem> l) ;
    void erro(String msg) ;

}
