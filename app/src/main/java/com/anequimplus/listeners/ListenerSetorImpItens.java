package com.anequimplus.listeners;

import com.anequimplus.entity.SetorImpItens;

import java.util.List;

public interface ListenerSetorImpItens {

    void ok(List<SetorImpItens> l) ;
    void erro(String msg) ;

}
