package com.anequimplus.listeners;

import com.anequimplus.entity.Sangria;

import java.util.List;

public interface ListenerSangria {
    void ok(List<Sangria> l) ;
    void erro(String msg) ;
}
