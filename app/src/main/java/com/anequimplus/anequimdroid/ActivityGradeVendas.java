package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.GradeVendasAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.conexoes.ConexaoGradeVendas;
import com.anequimplus.entity.GradeVendas;

import java.util.List;

public class ActivityGradeVendas extends AppCompatActivity {

    private ListView GradeVendas;
    private List<GradeVendas> listGradevendas;
    private static int RESULT_PRODUTO = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_vendas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(getIntent().getStringExtra("SUBTITULO"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GradeVendas = findViewById(R.id.listGrupo);
        GradeVendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setGradeVendasItens(listGradevendas.get(i)) ;
            }
        });
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listGradevendas = Dao.getGradeVendasADO(this).getGradeVendas() ;
        GradeVendas.setAdapter(new GradeVendasAdapter(this,listGradevendas));
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
                    //Toast.makeText(getBaseContext(), "ok", Toast.LENGTH_SHORT).show();
                    // listViewGrupo.setAdapter(new GrupoAdapter(getBaseContext(), Dao.getGrupoDAO(getBaseContext()).getlistGrade()));
                    listGradevendas = Dao.getGradeVendasADO(getBaseContext()).getGradeVendas() ;
                    GradeVendas.setAdapter(new GradeVendasAdapter(getBaseContext(),listGradevendas));

                }

                @Override
                public void erro(String msg) {
                   // Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    alerta(msg) ;

                }
            }.execute() ;
    }

    private void setGradeVendasItens(GradeVendas grade) {
        Intent intent = new Intent(getBaseContext(), ActivityGrupoProduto.class) ;
        Bundle params = new Bundle() ;
        params.putInt("GRADE_ID", grade.getId());
        intent.putExtras(params) ;
        startActivityForResult(intent, RESULT_PRODUTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_PRODUTO) {
            if (resultCode == RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }
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

}
