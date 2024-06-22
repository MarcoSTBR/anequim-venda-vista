package com.anequimplus.listeners;

import com.anequimplus.entity.ContaPedido;

import java.util.List;

public interface ListenerContaPedido {
    void ok(List<ContaPedido> l) ;
    void erro(String msg) ;
}
