package com.anequimplus.listeners;

import com.anequimplus.entity.UsuarioAcesso;

import java.util.List;

public interface ListenerUsuarioAcesso {
    void ok(List<UsuarioAcesso> l) ;
    void erro(String msg) ;
}
