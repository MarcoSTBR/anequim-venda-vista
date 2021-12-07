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

import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoImpressaoLocal;
import com.anequimplus.conexoes.ConexaoImpressoras;
import com.anequimplus.conexoes.ConexaoRelatorios;
import com.anequimplus.conexoes.ConexaoServidor;
import com.anequimplus.entity.Impressora;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.impressao.RelatorioA7;
import com.anequimplus.impressao.RelatorioLIO;
import com.anequimplus.tipos.TipoImpressora;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.net.MalformedURLException;
import java.util.List;

public class ActivityImpressao extends AppCompatActivity {

    private TextView textViewImpressao ;
    private int opcao  ;
    private int caixa_id ;
    private Toolbar toolbar ;
    private Spinner spinnerImp ;
    private ConexaoServidor cx ;
    private Impressora impressoraPadrao = null ;
    private ConexaoRelatorios conexaoRelatorios ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressao);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        opcao          = getIntent().getIntExtra("OPCAO_ID",-1) ;
        caixa_id       = getIntent().getIntExtra("CAIXA_ID",-1) ;
        toolbar.setTitle(getIntent().getStringExtra("TITULO"));
        toolbar.setSubtitle(getIntent().getStringExtra("SUBTITULO"));
        textViewImpressao = (TextView) findViewById(R.id.textViewImpressao) ;
        spinnerImp     = (Spinner) findViewById(R.id.spinnerImp) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaImp() ;
        //carregaRelatorio() ;
    }
    private void carregaRelatorio() {
        try {
            conexaoRelatorios = new ConexaoRelatorios(this, impressoraPadrao, caixa_id, opcao) {
                @Override
                public void Ok(List<RowImpressao> l) {
                    textViewImpressao.setText(conexaoRelatorios.getStringLinhas());
                }

                @Override
                public void erroMensagem(String msg) {
                    textViewImpressao.setText(msg);
                    //alertaErro(msg);

                }
            } ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alertaErro(e.getMessage());
        }
        conexaoRelatorios.execute();

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
            }
        }
        if (impressoraPadrao == null){
            impressoraPadrao = Dao.getImpressoraADO(getBaseContext()).getList().get(0) ;
            UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao());
            spinnerImp.setSelection(0) ;

        }
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

    private void carregaImp() {
            new ConexaoImpressoras(this) {
                @Override
                public void Ok() {
                    ImpressoraAdapter impAdp = new ImpressoraAdapter(getBaseContext(),Dao.getImpressoraADO(getBaseContext()).getList()) ;
                    spinnerImp.setAdapter(impAdp);
                    spinnerImp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            impressoraPadrao = Dao.getImpressoraADO(getBaseContext()).getList().get(i) ;
                            UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao()) ;
                            setImpressoraPadrao();
                            carregaRelatorio() ;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    setImpressoraPadrao();
                    carregaRelatorio() ;
                }

                @Override
                public void erroMensagem(String msg) {
                    Toast.makeText(getBaseContext(),msg, Toast.LENGTH_LONG).show();

                }
            }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(item.getItemId() == R.id.action_imprimir_ok){
            setOpFEchamento() ;
            return true ;
        }
        return true ;
    }

    private void setOpFEchamento(){
        if (impressoraPadrao == null) {
           alertaErro("Selecione uma Impressora!");

        } else {
           if (impressoraPadrao.getTipoImpressora() == TipoImpressora.tpILocal) {
               try {
                   new ConexaoImpressaoLocal(this, impressoraPadrao, caixa_id, opcao) {
                       @Override
                       public void ok(String s) {
                           Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                       }

                       @Override
                       public void erroMensagem(String msg) {
                           alertaErro(msg);
                       }
                   }.execute() ;
               } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado | MalformedURLException e) {
                   e.printStackTrace();
                   alertaErro(e.getMessage());
               }
           } else {
               imprimirRelatorio() ;
           }
        }

    }
    private void imprimirRelatorio() {

        ListenerImpressao listenerImpressao = new ListenerImpressao() {
            @Override
            public void onImpressao(int status) {
                Toast.makeText(getBaseContext(),"Impressão OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int status, String messagem) {
                alertaErro(messagem) ;
            }
        };
        if (impressoraPadrao.getTipoImpressora() == TipoImpressora.tpILio){
            RelatorioLIO relatorioLIO = new RelatorioLIO(getBaseContext(), conexaoRelatorios, listenerImpressao);
            relatorioLIO.executar();

        }
        if (impressoraPadrao.getTipoImpressora() == TipoImpressora.tpIV7){
            RelatorioA7 A7 = new RelatorioA7(getBaseContext(), conexaoRelatorios, listenerImpressao );
            A7.executar();
        }

    }

}
