package com.anequimplus.DaoClass;

public enum DaoCampoTipo {

    dbString("String"), dbInt("Inteiro"), dbDateTime("Data Hora"),
    dbDecimal("Decimal") ;

    public String descricao ;

    DaoCampoTipo(String s){
        descricao = s ;
    }

}
