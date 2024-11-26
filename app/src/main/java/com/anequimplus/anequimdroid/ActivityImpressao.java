package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.builds.BuildCaixa;
import com.anequimplus.conexoes.ConexaoRelatorios;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Impressora;
import com.anequimplus.impressao.BuilderControleImp;
import com.anequimplus.impressao.ControleImpressora;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.relatorios.ListenerRelatorio;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.util.List;

public class ActivityImpressao extends AppCompatActivity {

    private TextView textViewImpressao ;
    private int opcao  ;
    private Caixa caixa ;
    private Toolbar toolbar ;
    private Spinner spinnerImp ;
    private Impressora impressoraPadrao = null ;
    private ControleImpressora controleImpressora = null ;
    private List<RowImpressao> listImpressao ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressao);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        opcao          = getIntent().getIntExtra("OPCAO_ID",-1) ;
        int caixa_id   = getIntent().getIntExtra("CAIXA_ID",-1) ;
        toolbar.setTitle(getIntent().getStringExtra("TITULO"));
        toolbar.setSubtitle(getIntent().getStringExtra("SUBTITULO"));
        textViewImpressao = (TextView) findViewById(R.id.textViewImpressao) ;
        spinnerImp = (Spinner) findViewById(R.id.spinnerImp) ;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controleImpressora != null){
            controleImpressora.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCaixa() ;

    }

    private void getCaixa(){
//        caixa = DaoDbTabela.getCaixaADO(this).get(caixa_id) ;
        FilterTables f = new FilterTables() ;
        f.add(new FilterTable("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(this)))) ;
        f.add(new FilterTable("STATUS", "=", "1")) ;
        new BuildCaixa(this, f, "", new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                if (l.size()>0) {
                    caixa = l.get(0);
                    carregaImp() ;
                }
                 else alertaErro("Caixa Fechado!");

            }

            @Override
            public void erro(String msg) {
                alertaErro(msg) ;
            }
        }).executar();

    }

    private void carregaImp() {
        ImpressoraAdapter impAdp = new ImpressoraAdapter(getBaseContext(), DaoDbTabela.getImpressoraADO(getBaseContext()).getList()) ;
        spinnerImp.setAdapter(impAdp);
        spinnerImp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                impressoraPadrao = DaoDbTabela.getImpressoraADO(getBaseContext()).getList().get(i) ;
                UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao()) ;
                setImpressoraPadrao();
                //carregaRelatorio() ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setImpressoraPadrao();
    }

    private void setImpressoraPadrao() {

        String descImp = UtilSet.getImpPadraoFechamento(this);
        for (int i = 0 ; i<spinnerImp.getCount() ; i++){
            spinnerImp.getItemAtPosition(i);
            Impressora impSp = (Impressora)spinnerImp.getItemAtPosition(i) ;
            // Log.i("List Imp","i "+impSp.getDescricao()+" padrao "+descImp) ;
            if (descImp.equals(impSp.getDescricao())) {
                spinnerImp.setSelection(i);
                impressoraPadrao = impSp ;
                carregaRelatorio() ;
            }
        }
        if (impressoraPadrao == null){
            impressoraPadrao = DaoDbTabela.getImpressoraADO(getBaseContext()).getList().get(0) ;
            UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao());
            spinnerImp.setSelection(0) ;
            carregaRelatorio() ;
        }
    }

    private void carregaRelatorio() {
        instanciarImp() ;
           new ConexaoRelatorios(this, impressoraPadrao, caixa, opcao, new ListenerRelatorio() {
               @Override
               public void ok(List<RowImpressao> l) {
                   listImpressao = l ;
                   display();
               }

               @Override
               public void erroMensagem(String msg) {
                   textViewImpressao.setText(msg);
                   alertaErro(msg);

               }
           }).executar();
    }

    private void display() {
       String s = "" ;
       for (RowImpressao r : listImpressao) {
           s = s + r.getLinha() + "\n" ;
       }
        textViewImpressao.setText(s);
    }

    private void alertaErro(String mensagem) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção")
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_impressao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(item.getItemId() == R.id.action_imprimir_ok){
            imprimirRelatorio() ;
            return true ;
        }
        return true ;
    }

    private void instanciarImp(){
        if (controleImpressora != null) controleImpressora.close();
        controleImpressora = BuilderControleImp.getImpressora(this, impressoraPadrao) ;
        ListenerImpressao listener = new ListenerImpressao() {
            @Override
            public void onImpressao(int status) {
                Toast.makeText(ActivityImpressao.this, "Impressão OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int status, String messagem) {
                Toast.makeText(ActivityImpressao.this, messagem, Toast.LENGTH_SHORT).show();

            }
        };
        controleImpressora.setListenerImpressao(listener);
        controleImpressora.open();
    }


    private void imprimirRelatorio() {
        controleImpressora.impressaoLivre(listImpressao);
    }

}
