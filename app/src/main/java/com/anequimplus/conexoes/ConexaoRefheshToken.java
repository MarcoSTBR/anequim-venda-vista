package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoRefheshToken extends ConexaoServer {

    public ConexaoRefheshToken(Context ctx) {
        super(ctx);
        try {
            msg = "Refhesh Token";
            maps.put("class", "AfoodRefreshToken");
            maps.put("method", "refreshtoken");
            maps.put("mac", UtilSet.getMAC(ctx));
            url =  new URL(UtilSet.getServidorMaster(ctx)) ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(0,e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("RefreshToken", "Cod " + codInt + " " + s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success"))
                ok(j.getString("data"));
            else erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, e.getMessage());
        }
    }

    public abstract void ok(String token);

    public abstract void erro(int cod, String msg);

}
