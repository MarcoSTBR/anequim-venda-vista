package com.anequimplus.utilitarios;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.anequimplus.entity.Loja;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UtilSet {


    public static boolean logado(Context ctx){
        return !lerParamString(ctx, "AUTENTICACAO").equals("");
    }

    public static void getAutenticacao(Context ctx , JSONObject j) throws JSONException {
        j.put("AUTENTICACAO", getId_Usuario(ctx)) ;
        j.put("CHAVE", getChave(ctx)) ;
    }

    public static void setAutenticacao(Context ctx , String autentic, String nome_usuario) {
        gravaParamString(ctx,"AUTENTICACAO",autentic);
        gravaParamString(ctx,"NOME_USUARIO",nome_usuario);
    }

    public static String getId_Usuario(Context ctx ) {
        return lerParamString(ctx,"AUTENTICACAO");
    }

    public static String getNome_Usuario(Context ctx ) {
        return lerParamString(ctx,"NOME_USUARIO");
    }

    public static String getChave(Context ctx) {
        return lerParamString(ctx,"CHAVE") ;
    }

    public static void setChave(Context ctx , String aut) {
        gravaParamString(ctx,"CHAVE",aut);
    }

    public static String getCnpj(Context ctx) {
        return lerParamString(ctx,"CNPJ") ;
    }

    public static void setCnpj(Context ctx , String aut) {
        gravaParamString(ctx,"CNPJ",aut);
    }


    public static String lerParamString(Context ctx, String chave) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        String ret = preference.getString(chave, "");
        return ret;
    }

    public static void gravaParamString(Context ctx, String chave, String valor) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(chave, valor) ;
        editor.commit() ;
    }

    public static String getServidor(Context ctx) {
        String sv = lerParamString(ctx, "SERVIDOR") ;
        String p = lerParamString(ctx, "PORTA") ;
        if ((p == null) || p.equals("") || p.equals("0")){
            return sv ;
        } else {
            return sv+":"+p ;
        }

    }

    public static String Md5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            //e1.printStackTrace();
            password = "ERRO";
        }
        return password;
    }

    public static boolean seInternet(Context ctx){
        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        //return true ;

        return (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected());

        //        && (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI)) ;

    }
    public static String getMAC(Context ctx){
        /*
        WifiManager manager = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();

         */
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    public static void setImpPadraoContaPedido(Context ctx, String descricao) {
        gravaParamString(ctx, "IMP_CONTA",descricao) ;
    }

    public static String getImpPadraoContaPedido(Context ctx) {
        String t = lerParamString(ctx, "IMP_CONTA") ;
        if (t == null) t = "" ;
        return t ;
    }

    public static void setImpPadraoNFCe(Context ctx, String descricao) {
        gravaParamString(ctx, "IMP_NFCE",descricao) ;
    }

    public static String getImpPadraoNFce(Context ctx) {
        String t = lerParamString(ctx, "IMP_NFCE") ;
        if (t == null) t = "" ;
        return t ;
    }


    public static void setImpPadraoFechamento(Context ctx, String descricao) {
        gravaParamString(ctx, "IMP_FECHAMENTOCONTA",descricao) ;
    }

    public static String getImpPadraoFechamento(Context ctx) {
        String t = lerParamString(ctx, "IMP_FECHAMENTOCONTA") ;
        if (t == null) t = "" ;
        return t ;
    }

    public static Date getData(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try {
            d = dateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            d = new Date() ;
        }
        return d ;
    }

    public static void setLojaId(Context ctx, Loja l) {
        if (l == null) gravaParamString(ctx, "LOJA_ID", "0") ;
        else
        gravaParamString(ctx, "LOJA_ID", Integer.toString(l.getId())) ;
    }

    public static int getLojaId(Context ctx) {
        String valor = lerParamString(ctx, "LOJA_ID") ;
        int r = 0 ;
        Log.i("valor",valor) ;
        if ((valor.equals("") )|| (valor == null)) r = 0 ;
          else  r =  Integer.parseInt(valor) ;
        return r ;
    }

}
