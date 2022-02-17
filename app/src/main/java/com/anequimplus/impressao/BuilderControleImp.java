package com.anequimplus.impressao;

import android.content.Context;

import com.anequimplus.tipos.TipoImpressora;

public class BuilderControleImp {

    private static ControleImpressora c ;
    public static ControleImpressora getImpressora(Context ctx,  TipoImpressora tipo){
        switch (tipo){
            case tpElginM10: c = new ContaPedidoM10(ctx) ;
            break;
            case tpI9: c = new ContaPedidoI9(ctx) ;
            break;
            case tpNenhum: c = new ContaPedidoNenhum(ctx) ;
            break ;
            default:
                c = new ContaPedidoNenhum(ctx) ;
        };
        return c ;
    }


}
