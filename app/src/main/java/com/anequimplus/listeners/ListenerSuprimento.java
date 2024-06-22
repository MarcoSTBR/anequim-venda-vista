package com.anequimplus.listeners;

import com.anequimplus.entity.Suprimento;

import java.util.List;

public interface ListenerSuprimento {
    void ok(List<Suprimento> l) ;
    void erro(String msg) ;
}
