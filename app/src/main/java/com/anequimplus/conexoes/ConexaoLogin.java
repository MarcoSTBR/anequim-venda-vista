package com.anequimplus.conexoes;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.TokenSet;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

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
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fLogar).getUrl();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado exceptionLinkNaoEncontrado) {
            exceptionLinkNaoEncontrado.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(0,e.getMessage());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.i("LOGADO", s) ;
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")){
                String token = j.optJSONObject("data").getString("access_token") ;
                UtilSet.setToken(ctx, token);
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
            erro(codInt, e.getMessage());
        }

    }

   public abstract void Ok(int code) ;
   public abstract void erro(int code, String msg) ;

}
