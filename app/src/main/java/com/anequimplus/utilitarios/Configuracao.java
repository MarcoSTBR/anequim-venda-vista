package com.anequimplus.utilitarios;

import android.content.Context;

public class Configuracao {

    public static Boolean getVendaVistaPerguntarImpressao(Context ctx){
      if (UtilSet.lerParamString(ctx,"VendaVistaPerguntarImpressao").equals("1")) return true ;
      else  return false ;
    }

    public static void setVendaVistaPerguntarImpressao(Context ctx,  Boolean b){
       if(b) UtilSet.gravaParamString(ctx, "VendaVistaPerguntarImpressao","1") ;
       else UtilSet.gravaParamString(ctx, "VendaVistaPerguntarImpressao","0") ;

    }

    public static Boolean getVendaVistaPerguntarNFCe(Context ctx){
        if (UtilSet.lerParamString(ctx,"VendaVistaPerguntarNFCe").equals("1")) return true ;
        else  return false ;
    }

    public static void setVendaVistaPerguntarNFCe(Context ctx,  Boolean b){
        if(b) UtilSet.gravaParamString(ctx, "VendaVistaPerguntarNFCe","1") ;
        else UtilSet.gravaParamString(ctx, "VendaVistaPerguntarNFCe","0") ;

    }

    public static void setPedidoCompartilhado(Context ctx, boolean b){
        if(b) UtilSet.gravaParamString(ctx, "pedidoCompartinhado","1") ;
        else UtilSet.gravaParamString(ctx, "pedidoCompartinhado","0") ;
    }

    public static Boolean getPedidoCompartilhado(Context ctx){
        if (UtilSet.lerParamString(ctx,"pedidoCompartinhado").equals("1")) return true ;
        else  return false ;
    }

    public static void setLinkContaCompartilhada(Context ctx, String s) {
        UtilSet.gravaParamString(ctx, "linkContaCompartilhada",s) ;
    }

    public static String getLinkContaCompartilhada(Context ctx) {
        return UtilSet.lerParamString(ctx, "linkContaCompartilhada") ;
    }
}
