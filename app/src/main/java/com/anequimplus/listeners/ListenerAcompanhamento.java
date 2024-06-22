package com.anequimplus.listeners;

import com.anequimplus.entity.Acompanhamento;

import java.util.List;

public interface ListenerAcompanhamento {
    void ok(List<Acompanhamento> l) ;
    void erro(int cod, String msg) ;

}
