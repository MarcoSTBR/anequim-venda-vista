package com.anequimplus.listeners;

import com.anequimplus.entity.ContaPedidoPagamento;

import java.util.List;

public interface ListenerContaPedidoPagamento {
    void ok(List<ContaPedidoPagamento> l) ;
    void erro(String msg) ;
}
