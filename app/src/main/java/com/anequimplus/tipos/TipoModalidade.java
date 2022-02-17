package com.anequimplus.tipos;

public enum TipoModalidade {
    pgTroco("pgTroco"),pgDinheiro("pgDinheiro"),pgCartao("pgCartao"),
    pgCartaoLio("pgCartaoLio"), pgDesconto("pgDesconto"), pgGorjeta("pgGorjeta");

    public String valor;

    TipoModalidade(String v) {
        valor = v;
    }
}
