package com.anequimplus.listeners;

import com.anequimplus.entity.Loja;

import java.util.List;

public interface ListenerLoja {
    void ok(List<Loja> l) ;
    void erro(String msg) ;
}
