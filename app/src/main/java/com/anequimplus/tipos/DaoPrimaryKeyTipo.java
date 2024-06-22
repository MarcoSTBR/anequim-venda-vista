package com.anequimplus.tipos;

public enum DaoPrimaryKeyTipo {
    tpkNormal("normal"), tpkMax("max"), tpkSerial("serial"), tpkUUID("uuid");

    public String descricao;

    DaoPrimaryKeyTipo(String d) {
        descricao = d ;
    }

}
