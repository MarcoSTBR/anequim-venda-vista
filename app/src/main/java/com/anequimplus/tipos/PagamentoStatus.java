package com.anequimplus.tipos;

public enum PagamentoStatus {
    CANCELADO(0), SUCESSO(1), ERRO(2) ;

    public int valor;
    PagamentoStatus(int v) {
        valor = v;
    }

}
