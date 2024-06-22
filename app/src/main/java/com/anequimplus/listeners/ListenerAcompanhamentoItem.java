package com.anequimplus.listeners;

import com.anequimplus.entity.Acompanhamento_Item;

import java.util.List;

public interface ListenerAcompanhamentoItem {

    void ok(List<Acompanhamento_Item> l) ;
    void erro(int cod, String msg) ;
}
