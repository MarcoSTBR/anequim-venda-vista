package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.GradeVendasAdapter;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.conexoes.ConexaoGradeVendas;
import com.anequimplus.entity.GradeVendas;
import com.anequimplus.utilitarios.DisplaySet;

import java.util.List;

public class ActivityGradeVendas extends AppCompatActivity {

    private RecyclerView GradeVendas;
    private List<GradeVendas> listGradevendas;
    private String pedido ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_vendas);
        pedido = getIntent().getStringExtra("PEDIDO") ;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(getIntent().getStringExtra("SUBTITULO"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GradeVendas = (RecyclerView) findViewById(R.id.listGradeVendase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        grade();
    }

    public void grade(){
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        GradeVendas.setLayoutManager(layoutManager);
        listGradevendas = DaoDbTabela.getGradeVendasADO(this).getGradeVendas() ;
        GradeVendas.setAdapter(new GradeVendasAdapter(this, listGradevendas) {
            @Override
            public void selecionado(GradeVendas g) {
                seguirParaItens(g) ;
            }
        });
    }


    public void seguirParaItens(GradeVendas g){
        Intent intent = new Intent(ActivityGradeVendas.this, ActivityGradeVendaItem.class) ;
        Bundle params = new Bundle() ;
        params.putInt("GRADE_ID", g.getId());
        params.putString("PEDIDO",pedido);
        intent.putExtras(params) ;
        // startActivityForResult(intent, RESULT_PRODUTO);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grade_vendas, menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_grupo_atualizar) {
            setGradeVendas() ;
            return true ;
        }
        return true ;
    }

    public void setGradeVendas(){
       new ConexaoGradeVendas(this) {
                @Override
                public void oK() {
                    grade();
                }

                @Override
                public void erro(String msg) {
                   // Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    alerta(msg) ;

                }
            }.execute() ;
    }


    private void alerta(String txt){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        grade();
    }


}
