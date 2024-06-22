package com.anequimplus.listeners;

import com.anequimplus.entity.Transferencia;

import java.util.List;

public interface ListenerTransferencia {

    void ok(List<Transferencia> t) ;
    void erro(String msg) ;

}
