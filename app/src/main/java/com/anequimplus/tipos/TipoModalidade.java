package com.anequimplus.tipos;

public enum TipoModalidade {
    pgTroco("pgTroco"),
    pgDinheiro("pgDinheiro"),
    pgCartao("pgCartao"),
    pgCartaoLio("pgCartaoLio"),
    pgDesconto("pgDesconto"),
    pgGorjeta("pgGorjeta"),
    pgPIX("pgPIX"),
    pgCortesia("pgCortesia"),
    pgContaCorrente("pgContaCorrente");

    public String valor;

    TipoModalidade(String v) {
        valor = v;
    }
}
