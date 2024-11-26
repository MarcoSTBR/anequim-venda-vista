package com.anequimplus.controler;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.conexoes.ConexaoRefheshToken;
import com.anequimplus.utilitarios.TokenSet;
import com.anequimplus.utilitarios.UtilSet;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ControleAutenticacao {

    private Activity ctx;
    private final String FORM_DATA = "yyyy-MM-dd";

    public ControleAutenticacao(Activity ctx) {
        this.ctx = ctx;
    }

    public void verificarAutenticacao() {
        if ((UtilSet.getCnpj(ctx).isEmpty()) || (TokenSet.getToken(ctx).isEmpty()))
            autenticar("Iniciar!");
        else verificarToken();
    }

    private void verificarToken() {
       if (isDate())
            tokenOk() ;
        else
            refreshToken();
    }

    private void refreshToken() {
        new ConexaoRefheshToken(ctx) {
            @Override
            public void ok(String token) {
                TokenSet.setToken(ctx, token);
                salvarUltData(new Date()) ;
                tokenOk() ;
            }

            @Override
            public void erro(int cod, String msg) {
                verificarValidadeTokenAnterior(cod, msg);
            }
        }.execute() ;
    }

    private void verificarValidadeTokenAnterior(int cod, String msg){

        switch (cod) {
            case 0 : verificarUltimoToken() ;
            break;
            case 401 : autenticar(msg);
            break;
            case 405 : autenticar(msg);
            break;
            default: erro("COD "+cod+" "+msg);
        }
    }

    private void verificarUltimoToken(){
        if (TokenSet.isExpired(ctx))
            autenticar("Token expirado!");
         else tokenOk();
    }


    private boolean isDate() {
        String ultData = UtilSet.lerParamString(ctx, "UltData");
        Log.i("token_", "ultData " +ultData) ;
        return ultData.equals(new SimpleDateFormat(FORM_DATA).format(new Date()));
    }

    private void salvarUltData(Date d) {
        UtilSet.gravaParamString(ctx, "UltData", new SimpleDateFormat(FORM_DATA).format(d));
    }


    public abstract void tokenOk();
    public abstract void autenticar(String msg);
    public abstract void erro(String msg);

}
