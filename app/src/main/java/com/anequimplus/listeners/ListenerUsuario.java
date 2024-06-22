package com.anequimplus.listeners;

import com.anequimplus.entity.Usuario;

import java.util.List;

public interface ListenerUsuario {
    void ok(List<Usuario> l) ;
    void erro(int cod, String msg) ;

}
