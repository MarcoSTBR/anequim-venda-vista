package com.anequimplus.tipos;

public enum TipoConexao {
    cxIncluir(0), cxConsultar(1), cxAlterar(2), cxDeletar(3) ;

    public int valor;

    TipoConexao(int v) {
        valor = v;
    }
}
