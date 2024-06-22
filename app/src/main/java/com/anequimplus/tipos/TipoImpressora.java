package com.anequimplus.tipos;

public enum TipoImpressora {

    tpNenhum("tpNenhum"), tpILocal("tpILocal"), tpIV7("tpIV7"), tpILio("tpILio"),
    tpIUSB("tpIUSB"), tpElginM10("tpElginM10"), tpI8("tpI8"), tpI9("tpI9")  ;


    public String descricao;

    TipoImpressora(String d) {
        descricao = d ;
    }
}
