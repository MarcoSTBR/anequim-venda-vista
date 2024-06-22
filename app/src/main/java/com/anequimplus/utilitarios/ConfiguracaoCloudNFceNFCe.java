package com.anequimplus.utilitarios;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfiguracaoCloudNFceNFCe {


    public static Integer getCloudNfce_serie(Context ctx){
        String a = UtilSet.lerParamString(ctx,"NFCE_Serie") ;
        if (a.equals("")) return 1 ;
        return Integer.valueOf(a);
    }

    public static void setCloudNfce_serie(Context ctx,  Integer serie){
        UtilSet.gravaParamString(ctx, "NFCE_Serie",String.valueOf(serie)) ;
    }

    public static Integer getCloudNfce_numero(Context ctx){
        String a = UtilSet.lerParamString(ctx,"NFCE_Numero") ;
        if (a.equals("")) return 1 ;
        return Integer.valueOf(a);
    }

    public static void setCloudNfce_numero(Context ctx,  Integer numero){
        UtilSet.gravaParamString(ctx, "NFCE_Numero",String.valueOf(numero)) ;
    }

    public static String getCloudNfce_token(Context ctx){
        return UtilSet.lerParamString(ctx,"NFCE_Token") ;
    }

    public static void setCloudNfce_token(Context ctx,  String token){
        UtilSet.gravaParamString(ctx, "NFCE_Token",token) ;
    }

    public static Integer getOpcaoAmbiente(Context ctx){
        String a = UtilSet.lerParamString(ctx,"OpcaoNFCE_Ambiente") ;
        if (a.equals("")) return 0 ;
        return Integer.valueOf(a);
    }

    public static void setOpcaoAmbiente(Context ctx,  Integer op){
        UtilSet.gravaParamString(ctx, "OpcaoNFCE_Ambiente",String.valueOf(op)) ;
    }

    public static String getLinkCloudNFce(Context ctx){
        if (getOpcaoAmbiente(ctx) == 0)
            return "https://hom.api.cloud-dfe.com.br/v1/" ;
         else return "https://api.cloud-dfe.com.br/v1/" ;
    }

    public static Date getCertificadoValidade(Context ctx) {
        return UtilSet.getData(UtilSet.lerParamString(ctx,"CertificadoNFCE_validade")) ;
    }

    public static void setCertificadoValidade(Context ctx, Date validade) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        UtilSet.gravaParamString(ctx, "CertificadoNFCE_validade",sd.format(validade)) ;
    }


}
