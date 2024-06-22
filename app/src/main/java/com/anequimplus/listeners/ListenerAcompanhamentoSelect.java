package com.anequimplus.listeners;

import com.anequimplus.entity.PedidoItem;

public interface ListenerAcompanhamentoSelect {
    void ok(PedidoItem it) ;
    void setAcompanhamento(PedidoItem it) ;
    void erro(String msg) ;
}
