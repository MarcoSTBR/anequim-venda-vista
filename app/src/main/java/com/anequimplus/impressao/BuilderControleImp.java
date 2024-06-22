package com.anequimplus.impressao;

import android.content.Context;

import com.anequimplus.entity.Impressora;

public class BuilderControleImp {

    public static ControleImpressora getImpressora(Context ctx,  Impressora impressora){
        switch (impressora.getTipoImpressora()){
            case tpElginM10: return new ContaPedidoM10(ctx) ;
            case tpI9: return new ContaPedidoI9(ctx, impressora) ;
            case tpI8: return new ContaPedidoI8(ctx, impressora) ;
            case tpIUSB: return new ContaPedidoUSB(ctx, impressora) ;
            default: return new ContaPedidoNenhum(ctx) ;
        }
    }

}
