package com.anequimplus.utilitarios;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class UtilSet {

    public static String getChave(Context ctx) {
        return lerParamString(ctx,"CHAVE") ;
    }

    public static void setChave(Context ctx , String aut) {
        gravaParamString(ctx,"CHAVE",aut);
    }

    public static String getCnpj(Context ctx) {
        return lerParamString(ctx,"CNPJ") ;
    }

    public static void setCnpj(Context ctx , String cnpj) {
        gravaParamString(ctx,"CNPJ",cnpj);
    }

    public static String getPassword(Context ctx) {
        return lerParamString(ctx,"PASSWORD") ;
    }

    public static void setPassword(Context ctx , String aut) {
        gravaParamString(ctx,"PASSWORD",aut);
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
                    return "02:00:00:00:00:00";
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
            return "02:00:00:00:00:00";
        }
        return "02:00:00:00:00:00";


    }

    public static String repetir(String s, int count){
        String r = "" ;
        for (int i = 0; i < count; i++) {
            r = r + s ;
        }
        return r ;
    }


    public static void setImpPadraoContaPedido(Context ctx, String descricao) {
        gravaParamString(ctx, "IMP_CONTA",descricao) ;
    }

    public static String getImpPadraoContaPedido(Context ctx) {
        String t = lerParamString(ctx, "IMP_CONTA") ;
        if (t == null) t = "" ;
        return t ;
    }

    public static void setImpPadraoSuprimentoSangria(Context ctx, String descricao) {
        gravaParamString(ctx, "IMP_SUP_SAN",descricao) ;
    }

    public static String getImpPadraoSuprimentoSangria(Context ctx) {
        String t = lerParamString(ctx, "IMP_SUP_SAN") ;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = dateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            d = new Date() ;
        }
        return d ;
    }

    public static void setLojaId(Context ctx, int l) {
        gravaParamString(ctx, "LOJA_ID", Integer.toString(l)) ;
    }

    public static int getLojaId(Context ctx) {
        return Integer.parseInt(lerParamString(ctx, "LOJA_ID")) ;
    }

    public static void setLojaNome(Context ctx, String nome) {
        gravaParamString(ctx, "LOJA_NOME", nome) ;
    }

    public static String getLojaNome(Context ctx) {
        return lerParamString(ctx, "LOJA_NOME") ;
    }

    public static void setLogin(Context ctx, String login) {
        gravaParamString(ctx, "LOGIN", login) ;
    }

    public static String getLogin(Context ctx) {
      return lerParamString(ctx, "LOGIN") ;

    }

    public static void setUsuarioId(Context ctx, int usuario_id) {
        gravaParamString(ctx, "USUARIO_ID", Integer.toString(usuario_id)) ;
    }

    public static int getUsuarioId(Context ctx) {
        try {
            return Integer.parseInt(lerParamString(ctx, "USUARIO_ID"));
        } catch (Exception e){
            e.printStackTrace();
            return 0 ;
        }

    }

    public static void setUsuarioNome(Context ctx, String usuario_nome) {
        gravaParamString(ctx, "USUARIO_NOME", usuario_nome) ;

    }

    public static String getUsuarioNome(Context ctx) {
        return lerParamString(ctx, "USUARIO_NOME") ;

    }

    public static String md5(Context ctx, String st) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(st.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch(UnsupportedEncodingException ex){
        }
        return null;
    }

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString() ;
        return uuid ;

    }

    public static void setServidorMaster(Context ctx, String url_mobile) {
        gravaParamString(ctx, "SERVIDOR_MASTER", url_mobile) ;
    }

    public static String getServidorMaster(Context ctx) {
        // String u =  "http://gerezim.com.br/anequimfood/rest.php" ;
        // String u =  "http://gerezim.com.br/company/rest.php" ; //foi
        // String u =  "http://pampofood.com.br/company/rest.php" ; //foi
        // String u =  "https://www.gileade.com.br/anequimfood/rest.php" ;
        // String u =  "http://gerezim.com.br/company/rest.php" ;
        // return lerParamString(ctx, "SERVIDOR_MASTER")
        //String u =  "https://viacep.com.br/ws/01001000/json/" ;
       // String u =  "http://201.73.1.230/anequimfood/rest.php" ;
        String u =  lerParamString(ctx, "SERVIDOR_MASTER") ;
        return u ;

    }

    public static void setTerminalId(Context ctx, int id){
        gravaParamString(ctx, "TERMINAL_ID", Integer.toString(id)) ;
    }

    public static int getTerminalId(Context ctx){
        try {
            return Integer.valueOf(lerParamString(ctx, "TERMINAL_ID"));
        } catch (Exception e){
            return 0 ;
        }
    }

    public static String getDiaSemana(Date data){
        SimpleDateFormat dt = new SimpleDateFormat("EEE", new Locale("pt", "BR")) ;
        return dt.format(data) ;
        /*
        int dia = Integer.parseInt(dt.format(data)) ;
        switch (dia){
            case 1 : return "Dom" ;
            case 2 : return "Seg" ;
            case 3 : return "Ter" ;
            case 4 : return "Qua" ;
            case 5 : return "Qui" ;
            case 6 : return "Sex" ;
            case 7 : return "Sab" ;
            default: return dt.format(data) ;
        }*/
    }

    private static BigDecimal truncateDecimal(final double x, final int numberofDecimals) {
        BigDecimal v = new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_UP);
        //Log.i("BigDecimal", "Valor "+v) ;
        //Math.round(x) ;
        return v ;
    }

    public static Double truncate(double value) {
      //  return truncateDecimal(value, 2).doubleValue() ;
        DecimalFormat df = new DecimalFormat("00000000.00");
        String v = df.format(value).replace(",",".") ;
        Log.i("truncate", "Valor "+v) ;
        return Double.valueOf(v);
    }
}
