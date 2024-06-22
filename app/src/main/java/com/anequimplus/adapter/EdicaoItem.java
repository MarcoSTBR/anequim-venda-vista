package com.anequimplus.adapter;

import com.anequimplus.entity.PedidoItem;

public interface EdicaoItem {

    public void mais(PedidoItem pedidoItem, double q) ;
    public void menos(PedidoItem pedidoItem, double q) ;
    public void editar(PedidoItem pedidoItem);

}
