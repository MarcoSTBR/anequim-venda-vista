package com.anequimplus.listeners;

import com.anequimplus.entity.ContaPedidoDest;

import java.util.List;

public interface ListenerContaPedidoDest {
    void ok(List<ContaPedidoDest> l) ;
    void erro(String msg) ;
}
