package com.anequimplus.utilitarios;

import android.content.Context;

public class Configuracao {

 /*   public static Boolean getVendaVistaPerguntarImpressao(Context ctx){
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

    }*/

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

    public static void setAcumulaPedidos(Context ctx, boolean checked) {
        if (checked)
          UtilSet.gravaParamString(ctx, "AcumulaPedidos","1") ;
         else
            UtilSet.gravaParamString(ctx, "AcumulaPedidos","0") ;
    }

    public static boolean getAcumulaPedidos(Context ctx) {
        if (UtilSet.lerParamString(ctx, "AcumulaPedidos").equals("1"))
            return true ;
        else return false ;
    }

    public static void setApiVersao(Context ctx, String vsao) {
        UtilSet.gravaParamString(ctx, "VersaoApi", vsao);
    }

    public static void setConf_conta_cliente_pergunta_nfce(Context ctx, boolean checked) {
        if (checked)
            UtilSet.gravaParamString(ctx, "conta_cliente_pergunta_nfce","1") ;
        else
            UtilSet.gravaParamString(ctx, "conta_cliente_pergunta_nfce","0") ;
    }

    public static boolean getConf_conta_cliente_pergunta_nfce(Context ctx) {
        if (UtilSet.lerParamString(ctx, "conta_cliente_pergunta_nfce").equals("1"))
            return true ;
        else return false ;
    }

    public static String getApiVersao(Context ctx) {
        String v = UtilSet.lerParamString(ctx, "VersaoApi") ;
        if ((v == null) || (v.equals("")))
        {
            return "V14";
        } else {
          return v ;
        }
    }

    public static void setLinkComandaRemota(Context ctx, String s) {
        UtilSet.gravaParamString(ctx, "linkComandaRemota",s) ;
    }

    public static String getLinkComandaRemota(Context ctx) {
        return UtilSet.lerParamString(ctx, "linkComandaRemota") ;
    }

    public static void setSeComandaRemota(Context ctx, boolean flag){
        if (flag) UtilSet.gravaParamString(ctx, "seComandaRemota","TRUE") ;
         else UtilSet.gravaParamString(ctx, "seComandaRemota","FALSE") ;
    }
    public static boolean getSeComandaRemota(Context ctx){
        return (UtilSet.lerParamString(ctx, "seComandaRemota").equals("TRUE")) ;
    }

    public static Integer getOpcaoNFCE(Context ctx){
        String a = UtilSet.lerParamString(ctx,"OpcaoNFCE") ;
        if (a.equals("")) return 0 ;
        return Integer.valueOf(a);
    }

    public static void setOpcaoNFCE(Context ctx, Integer op){
        UtilSet.gravaParamString(ctx, "OpcaoNFCE",String.valueOf(op)) ;
    }

}
