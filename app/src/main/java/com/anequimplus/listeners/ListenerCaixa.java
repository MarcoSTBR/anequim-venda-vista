package com.anequimplus.listeners;

import com.anequimplus.entity.Caixa;

import java.util.List;

public interface ListenerCaixa {
    void ok(List<Caixa> l) ;
    void erro(String msg) ;
}
