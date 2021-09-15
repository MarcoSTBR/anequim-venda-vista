package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.GrupoAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoGradeVendas;
import com.anequimplus.entity.Grupo;

import java.net.MalformedURLException;
import java.util.List;

public class ActivityGrupo extends AppCompatActivity {

    private ListView listViewGrupo;
    private List<Grupo> grupoList ;
    private static int RESULT_PRODUTO = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(getIntent().getStringExtra("SUBTITULO"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listViewGrupo = findViewById(R.id.listGrupo);
        listViewGrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setProduto(grupoList.get(i)) ;
            }
        });
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        grupoList = Dao.getGrupoDAO(getBaseContext()).getlistGrade() ;
        listViewGrupo.setAdapter(new GrupoAdapter(getBaseContext(), grupoList));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grupo, menu);
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

    private void setGradeVendas(){
        try {
            new ConexaoGradeVendas(this) {
                @Override
                public void oK() {
                    listViewGrupo.setAdapter(new GrupoAdapter(getBaseContext(), Dao.getGrupoDAO(getBaseContext()).getlistGrade()));
                }

                @Override
                public void erro(String msg) {
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    alerta(msg) ;

                }
            }.execute() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado | MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void setProduto(Grupo grupo) {
        Intent intent = new Intent(getBaseContext(), ActivityGrupoProduto.class) ;
        Bundle params = new Bundle() ;
        params.putInt("GRUPO_ID", grupo.getId());
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
