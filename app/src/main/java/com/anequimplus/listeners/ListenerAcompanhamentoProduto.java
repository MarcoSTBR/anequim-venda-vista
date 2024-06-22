package com.anequimplus.listeners;

import com.anequimplus.entity.Acompanhamento_produto;

import java.util.List;

public interface ListenerAcompanhamentoProduto {

    void ok(List<Acompanhamento_produto> l) ;
    void erro(int cod, String msg) ;
}
