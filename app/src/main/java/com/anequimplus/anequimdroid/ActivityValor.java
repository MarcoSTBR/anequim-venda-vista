package com.anequimplus.anequimdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ActivityValor extends AppCompatActivity {

    private TextView textViewValorTitulo;
    private TextView textViewValorSubTitulo ;
    private TextView textViewValorDisplay ;
    private double valor = 0 ;
    private String txValor ;
    private DecimalFormat frmV ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valor);
        //valor = getIntent().getDoubleExtra("VALOR",0.0) ;
        textViewValorTitulo = (TextView) findViewById(R.id.textViewValorTitulo) ;
        textViewValorSubTitulo = (TextView) findViewById(R.id.textViewValorSubTitulo) ;
        textViewValorDisplay = (TextView) findViewById(R.id.textViewValorDisplay) ;
        textViewValorTitulo.setText(getIntent().getStringExtra("TITULO")) ;
        textViewValorSubTitulo.setText(getIntent().getStringExtra("SUBTITULO")) ;
       // editTextValor = (EditText) findViewById(R.id.editTextValor) ;
        DecimalFormat f = new DecimalFormat("#0") ;
        frmV = new DecimalFormat("   R$  #0.00");
        valor = getIntent().getDoubleExtra("VALOR",0) ;

        txValor = f.format(valor * 100) ;
        setResult(RESULT_CANCELED, getIntent());
        carregarControle() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        display() ;
    }

    private void carregarControle() {
       // txValor = "" ;
        Button b0 = (Button) findViewById(R.id.b0) ;
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "0" ;
                display();
            }
        });
        Button b1 = (Button) findViewById(R.id.b1) ;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "1" ;
                display();
            }
        });
        Button b2 = (Button) findViewById(R.id.b2) ;
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "2" ;
                display();
            }
        });
        Button b3 = (Button) findViewById(R.id.b3) ;
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "3" ;
                display();
            }
        });
        Button b4 = (Button) findViewById(R.id.b4) ;
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "4" ;
                display();
            }
        });
        Button b5 = (Button) findViewById(R.id.b5) ;
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "5" ;
                display();
            }
        });
        Button b6 = (Button) findViewById(R.id.b6) ;
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "6" ;
                display();
            }
        });
        Button b7 = (Button) findViewById(R.id.b7) ;
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "7" ;
                display();
            }
        });
        Button b8 = (Button) findViewById(R.id.b8) ;
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "8" ;
                display();
            }
        });
        Button b9 = (Button) findViewById(R.id.b9) ;
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "9" ;
                display();
            }
        });
        ImageButton bponto = (ImageButton) findViewById(R.id.bponto) ;
        bponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txValor = txValor + "00" ;
                display();
            }
        });
        ImageButton ibvoltar = (ImageButton) findViewById(R.id.ibvoltar) ;
        ibvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = "" ;
                Log.i("txValor", "Tamanho "+txValor.length()) ;
                for (int i = 0; i < txValor.length()-1; i++) {
                    aux = aux + txValor.substring(i, i+1) ;
                   // Log.i("txValor", i+" "+txValor.substring(i, i+1)) ;
                }
                txValor = aux ;
                display();
            }
        });
        ImageButton ibok = (ImageButton) findViewById(R.id.ibok) ;
        ibok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntent().putExtra("VALOR",valor) ;
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
        ImageButton bcancelar = (ImageButton) findViewById(R.id.bcancelar) ;
        bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getIntent().putExtra("VALOR",valor) ;
                //setResult(RESULT_OK, getIntent());
                finish();
            }
        });

    }



    private void display(){
        if (txValor.equals("")){
            valor = 0 ;
        } else {
            valor = Double.valueOf(txValor) / 100 ;
        }

        textViewValorDisplay.setText(frmV.format(valor));
    }
}
