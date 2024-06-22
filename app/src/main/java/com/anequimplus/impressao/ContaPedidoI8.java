package com.anequimplus.impressao;

import android.content.Context;

import com.anequimplus.entity.Impressora;

public class ContaPedidoI8 extends ContaPedidoI9{

    public ContaPedidoI8(Context ctx, Impressora impressora) {
        super(ctx, impressora);
    }

    @Override
    protected String getModelo() {
        return "i8" ;
    }
}
