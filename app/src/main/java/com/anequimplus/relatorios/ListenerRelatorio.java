package com.anequimplus.relatorios;

import com.anequimplus.utilitarios.RowImpressao;

import java.util.List;

public interface ListenerRelatorio {
    void ok(List<RowImpressao> l) ;
    void erroMensagem(String msg) ;
}
