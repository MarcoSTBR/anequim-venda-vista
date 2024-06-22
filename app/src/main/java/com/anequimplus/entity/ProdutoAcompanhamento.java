package com.anequimplus.entity;

import java.util.List;

public class ProdutoAcompanhamento {

    private Acompanhamento acompanhamento ;
    private List<Acompanhamento_Item> acompanhamento_items ;

    public ProdutoAcompanhamento(Acompanhamento acompanhamento, List<Acompanhamento_Item> acompanhamento_items) {
        this.acompanhamento = acompanhamento;
        this.acompanhamento_items = acompanhamento_items;
    }

    public Acompanhamento getAcompanhamento() {
        return acompanhamento;
    }

    public void setAcompanhamento(Acompanhamento acompanhamento) {
        this.acompanhamento = acompanhamento;
    }

    public List<Acompanhamento_Item> getAcompanhamento_items() {
        return acompanhamento_items;
    }

    public void setAcompanhamento_items(List<Acompanhamento_Item> acompanhamento_items) {
        this.acompanhamento_items = acompanhamento_items;
    }
}
