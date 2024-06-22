package com.anequimplus.conexoes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.anequimplus.utilitarios.TokenSet;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoLogin extends ConexaoServer {

    private String login ;
    private String password ;

    public ConexaoLogin(Context ctx, String login, String password)  {
        super(ctx);
        token = "" ;
        this.login = login ;
        this.password = password ;
        try {
            msg = "Logando";
            maps.put("class", "AfoodLogar");
            maps.put("method", "logar");
            maps.put("cnpj", UtilSet.getCnpj(ctx));
            maps.put("login", this.login);
            maps.put("password", UtilSet.Md5(this.password));
            maps.put("mac", UtilSet.getMAC(ctx));
            url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fLogar).getUrl();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(0,e.getMessage());
        }

    }


    @SuppressLint("NewApi")
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.i("LOGADO", "cod "+codInt+" "+s) ;
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")){
                UtilSet.setLojaId(ctx, TokenSet.getLojaId(ctx));
                UtilSet.setLojaNome(ctx, TokenSet.getLojaNome(ctx));
                UtilSet.setUsuarioId(ctx, TokenSet.getUsuarioId(ctx));
                UtilSet.setUsuarioNome(ctx, TokenSet.getUsuarioNome(ctx));
               // UtilSet.setCnpj(ctx, UtilSet.getCnpj(ctx));
                UtilSet.setLogin(ctx,login);
                UtilSet.setPassword(ctx, password);
                Ok(codInt);
            } else erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, s);
        }

    }

   public abstract void Ok(int code) ;
   public abstract void erro(int code, String msg) ;

}
