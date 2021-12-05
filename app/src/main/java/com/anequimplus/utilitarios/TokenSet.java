package com.anequimplus.utilitarios;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;


public class TokenSet {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getUsuarioId(Context ctx) throws JSONException {

        JWT jwt = getJWT(ctx) ;
        Log.i("jwt", jwt.toString()) ;

        JSONObject j = new JSONObject(getPayload(ctx)) ;
        return j.getInt("usuarioId") ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getUsuarioNome(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx)) ;
        return j.getString("usuarioNome") ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getLojaId(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx)) ;
        return j.getInt("loja_id") ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getLojaNome(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx)) ;
        return j.getString("loja") ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getPayload(Context ctx) {
        Base64.Decoder base64 = Base64.getDecoder() ;
        String token = UtilSet.getToken(ctx) ;

        String[] arr = token.split("\\.");
        int i = 1 ;
        Log.i("token",token) ;
        for (String a : arr){
            Log.i("token",a) ;
            if (i == 2){
                return new String(base64.decode(a));
            }
            i++ ;
        }
        return "não encontrou" ;
    }

    public static boolean ifTokenVazio(Context ctx){
        return UtilSet.getToken(ctx).equals("");
    }

    public static String getToken(Context ctx) {
        return "Bearer " + UtilSet.getToken(ctx) ;
    }

    private static JWT getJWT(Context ctx) {
        return new JWT(UtilSet.getToken(ctx));
    }

    public static void validate(Context ctx) throws ExceptionTokenExpirado {
        JWT jwt = getJWT(ctx) ;
        if (jwt.isExpired(0)) throw new ExceptionTokenExpirado("Token Expirado!") ;
    }

    public static class ExceptionTokenExpirado extends Exception{
        private String msg ;
        public ExceptionTokenExpirado(String msg){
            this.msg = msg ;
        }

        @Nullable
        @Override
        public String getMessage(){
            return msg ;
        }
    } ;
}
