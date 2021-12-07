package com.anequimplus.anequimdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.ModalidadeAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.conexoes.ConexaoModalidade;

import java.text.DecimalFormat;

public class ActivityPagamento extends AppCompatActivity {

    private double valor = 0;
    private ListView listModalidade ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DecimalFormat frmV = new DecimalFormat("   R$  #0.00");
        valor = getIntent().getDoubleExtra("VALOR",0.0) ;
        toolbar.setTitle("Pagamento");
        toolbar.setSubtitle(frmV.format(valor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listModalidade = findViewById(R.id.listModalidade);
        listModalidade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getIntent().putExtra("MODALIDADE_ID",Dao.getModalidadeADO(getBaseContext()).getList().get(i).getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        setResult(RESULT_CANCELED, getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaModalidade() ;
       // getMod() ;
    }

    private void carregaModalidade() {
        listModalidade.setAdapter(new ModalidadeAdapter(this,Dao.getModalidadeADO(this).getList()));
    }

    private void getMod(){
            new ConexaoModalidade(this) {
                @Override
                public void oK() {
                  //  Dao.getModalidadeADO(getBaseContext()).modalidadeADD(j.getJSONArray("MODALIDADES")) ;
                    carregaModalidade() ;
                }

                @Override
                public void erro(String msg) {
                    alert(msg) ;

                }
            }.execute();

    }

    private void alert(String message) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro:")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_modalidade){
            getMod();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pagamento, menu);

        return true ;//super.onCreateOptionsMenu(menu);
    }
}
