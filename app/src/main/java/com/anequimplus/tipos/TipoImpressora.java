package com.anequimplus.tipos;

public enum TipoImpressora {

    tpILocal("tpILocal"), tpIV7("tpIV7"), tpILio("tpILio"),  tpIUSB("tpIUSB"), tpElginM10("tpElginM10");


    public String descricao;

    TipoImpressora(String d) {
        descricao = d ;
    }
}
