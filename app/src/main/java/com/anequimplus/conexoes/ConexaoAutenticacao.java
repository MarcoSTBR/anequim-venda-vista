package com.anequimplus.conexoes;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.anequimplus.utilitarios.TokenSet;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoAutenticacao extends ConexaoServer {

    private String cnpj;
    private String login;
    private String password;

    public ConexaoAutenticacao(Context ctx, String cnpj, String login, String password) throws MalformedURLException {
        super(ctx);
        //method = "POST" ;
        msg = "Autenticação";
        token = "";
        this.cnpj = cnpj;
        this.login = login;
        this.password = password;
        maps.put("class", "AfoodAutenticacao");
        maps.put("method", "autenticacao");
        maps.put("cnpj", this.cnpj);
        maps.put("login", this.login);
        maps.put("password", UtilSet.md5(ctx, this.password));
        url = new URL(UtilSet.getServidorMaster(ctx)); //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAutenticacao).getUrl();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("ConexaoAutenticacarest", "Cod " + codInt + " " + s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                String token = j.optJSONObject("data").getString("access_token");
                TokenSet.setToken(ctx, token);
                UtilSet.setCnpj(ctx, cnpj);
                UtilSet.setLojaId(ctx, TokenSet.getLojaId(ctx));
                UtilSet.setLojaNome(ctx, TokenSet.getLojaNome(ctx));
                oK(cnpj, login);
            } else erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, e.getMessage());
        }
    }

    public abstract void oK(String cnpj, String login);

    public abstract void erro(int cod, String msg);

}
