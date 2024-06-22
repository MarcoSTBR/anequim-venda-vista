package com.anequimplus.relatorios;

import com.anequimplus.entity.ContaPedido;

import java.util.List;

public interface IImpressaoContaPedido<T> {

    public List<T> getListLinhas();
    public void setContaPedido(ContaPedido contaPedido) ;
    public List<T> getRecibo();
}
