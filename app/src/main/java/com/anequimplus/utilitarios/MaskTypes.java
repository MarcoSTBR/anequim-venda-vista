package com.anequimplus.utilitarios;

public enum MaskTypes {

    FORMAT_VALOR("#0.00"), FORMAT_CURRENCY("R$ #0.00"), FORMAT_QUANT_PESO("0.000");

    public String formato;

    MaskTypes(String s) {
        formato = s ;
    }
}
