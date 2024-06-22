package com.anequimplus.utilitarios;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenSet {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getUsuarioId(Context ctx) throws JSONException {

        JWT jwt = getJWT(ctx);
        Log.i("jwt", jwt.toString());

        JSONObject j = new JSONObject(getPayload(ctx));
        return j.getInt("usuarioId");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getUsuarioNome(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx));
        return j.getString("usuarioNome");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getLojaId(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx));
        return j.getInt("loja_id");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getLojaNome(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx));
        return j.getString("loja");
    }


    public static String getPayload(Context ctx) {
        //Base64.Decoder base64 = Base64.getDecoder() ;
        String token = UtilSet.lerParamString(ctx, "TOKEN"); //getToken(ctx) ;
        String[] arr = token.split("\\.");
        int i = 1;
        Log.i("token", token);
        for (String a : arr) {
            Log.i("token", a);
            if (i == 2) {
                return new String(Base64.decode(a, 0));
            }
            i++;
        }
        return "não encontrou";
    }

    public static void setToken(Context ctx, String token) {
        UtilSet.gravaParamString(ctx, "TOKEN", token);
    }

    public static boolean ifTokenVazio(Context ctx) {
        return TokenSet.getToken(ctx).equals("");
    }


    public static String getTokenUrl(Context ctx) {
        return "Bearer " + TokenSet.getToken(ctx);
    }

    public static String getToken(Context ctx) {
        return UtilSet.lerParamString(ctx, "TOKEN");
    }


    private static JWT getJWT(Context ctx) {
        String t = TokenSet.getToken(ctx);
        return new JWT(t);
    }

    public static boolean isExpired(Context ctx) {
        JWT jwt = getJWT(ctx);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dataInpirado = jwt.getExpiresAt();
        Date hj = new Date();
        Log.i("token_", "ExpiresAt " + s.format(dataInpirado)
                + " IssuedAt " + s.format(jwt.getIssuedAt())
                + " expirado " + hj.after(dataInpirado));


        return (hj.after(dataInpirado));
//        if (hj.after(df)){
//            throw new ExceptionTokenExpirado(String.format("Token Expirado, desde " + s.format( jwt.getExpiresAt()))) ;
//        }
    }

    public static class ExceptionTokenExpirado extends Exception {
        private String msg;

        public ExceptionTokenExpirado(String msg) {
            this.msg = msg;
        }

        @Nullable
        @Override
        public String getMessage() {
            return msg;
        }
    }

    ;
}
