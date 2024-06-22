package com.anequimplus.listeners;

import com.anequimplus.entity.ProdutoImposto;

import java.util.List;

public interface ListenerProdutoImposto {

    void ok(List<ProdutoImposto> l) ;
    void erro(String msg) ;

}
