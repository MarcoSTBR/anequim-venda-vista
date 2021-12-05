package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anequimplus.conexoes.ConexaoAutenticacao;
import com.anequimplus.conexoes.ConexaoConfTerminal;
import com.anequimplus.conexoes.ConexaoProdutos;
import com.anequimplus.entity.Terminal;
import com.anequimplus.utilitarios.TokenSet;
import com.anequimplus.utilitarios.UtilSet;

public class ActivityInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciar();
    }

    public void iniciar(){

        if(UtilSet.getCnpj(getBaseContext()).equals("")){
            startActivity(new Intent(getBaseContext(), ActivityAutenticacao.class));

        } else {
            atualizarAutenticacao() ;
        }
    }

    public void atualizarAutenticacao(){
        new ConexaoAutenticacao(this, UtilSet.getCnpj(this), UtilSet.getLogin(this),
                UtilSet.getPassword(this)) {
            @Override
            public void oK(String cnpj, String login) {
                setProdutos() ;
               // Toast.makeText(getBaseContext(), "Autenticação OK", Toast.LENGTH_LONG).show();
            }

            @Override
            public void erro(int cod, String msg) {
              try {
                    TokenSet.validate(getBaseContext()) ;
                    startActivity(new Intent(getBaseContext(), ActivityPrincipal.class));
                } catch (TokenSet.ExceptionTokenExpirado exceptionTokenExpirado) {
                    exceptionTokenExpirado.printStackTrace();
                    startActivity(new Intent(getBaseContext(), ActivityLogin.class));
                }

            }
        }.execute();
    }

    private void setProdutos() {
        new ConexaoProdutos(this){

            @Override
            public void Ok() {
                setTerminal() ;
                //startActivity(new Intent(getBaseContext(), ActivityPrincipal.class));
            }

            @Override
            public void erro(String msg) {
                alert(msg) ;
                //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }.execute() ;
    }

    private void setTerminal(){

       String mac = UtilSet.getMAC(this) ;
       new ConexaoConfTerminal(this, mac) {
            @Override
            public void ok(Terminal t) {
                startActivity(new Intent(getBaseContext(), ActivityPrincipal.class));
            }

            @Override
            public void erroMsg(int cod, String msg) {
                if (cod == 202)
                    startActivity(new Intent(getBaseContext(), ActivityTerminal.class));
                 else
                 //   setTerminal() ;
                    alert(msg) ;

            }
        }.execute();
    }

    private void alert(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getBaseContext(), ActivityPrincipal.class));
                    }
                }).
                setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                }
                ).show();
    }
    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "Landscape Mode", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "Portrait Mode", Toast.LENGTH_SHORT).show();
        }
    }

     */
}