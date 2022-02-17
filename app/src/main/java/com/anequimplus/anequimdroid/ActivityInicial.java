package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anequimplus.utilitarios.UtilSet;

public class ActivityInicial extends AppCompatActivity {

    private static Boolean flagEntrada = false;
    private Button registrar  ;
    private Button entrar ;
    private static int LOGAR = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        //new DBHelper(this).limparTudo(this) ;
        registrar = findViewById(R.id.buttonRegistrar) ;
        registrar.setText("Registrar CNPJ");
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarCnpj() ;
            }
        });
        entrar = findViewById(R.id.buttonEntrar) ;
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logar() ;
            }
        });
    }

    private void registrarCnpj() {
        startActivity(new Intent(getBaseContext(), ActivityAutenticacao.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciar() ;
    }

    private void logar() {
        startActivityForResult(new Intent(getBaseContext(), ActivityLogin.class), LOGAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGAR) {
            if (resultCode == RESULT_OK) {
                flagEntrada = true ;
                startActivity(new Intent(getBaseContext(), ActivityPrincipal.class));
            }
        }
    }

    public void iniciar(){
       if (UtilSet.getCnpj(this).equals("")) {
           entrar.setVisibility(View.GONE);
           registrar.setVisibility(View.VISIBLE);
        } else {
           entrar.setVisibility(View.VISIBLE);
           registrar.setVisibility(View.GONE);

        }
       if (flagEntrada) {

           new AlertDialog.Builder(this)
                   .setIcon(R.drawable.ic_baseline_refresh_24)
                   .setTitle("Sair")
                   .setMessage("Deseja Sair do Aplicativo?")
                   .setCancelable(false)
                   .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                          finish();
                       }
                   }).
                   setNegativeButton("Não", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   startActivity(new Intent(getBaseContext(), ActivityPrincipal.class));
                               }
                           }
                   ).show();
       }
    }



}