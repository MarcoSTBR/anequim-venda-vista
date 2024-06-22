package com.anequimplus.listeners;

import com.anequimplus.entity.Pedido;

import java.util.List;

public interface ListenerEnvioPedido {
    void envioOK(List<Pedido> l);
    void erroEnvio(String msg) ;


}
