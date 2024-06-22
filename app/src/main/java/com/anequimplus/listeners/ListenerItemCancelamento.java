package com.anequimplus.listeners;

import com.anequimplus.entity.ContaPedidoItemCancelamento;

import java.util.List;

public interface ListenerItemCancelamento {
    void ok(List<ContaPedidoItemCancelamento> l) ;
    void erro(String msg) ;
}
