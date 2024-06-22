package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Impressora;
import com.anequimplus.impressao.BuilderControleImp;
import com.anequimplus.impressao.ControleImpressora;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityFechamentoConta extends AppCompatActivity {

    private Toolbar toolbar ;
    private Spinner spinnerImp ;
    private ControleImpressora controlImp = null ;
    private Impressora impressoraPadrao ;
    private ContaPedido contaPedido ;
    private int idConta ;
    private TextView imprimir_recibo_conta ;
    private TextView imprimir_recibo_abertura ;
    private TextView imprimir_recibo_fechamento ;
    private Button conta_recibo ;
    private Button conta_sair ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechamento_conta);
        idConta = getIntent().getIntExtra("CONTA_ID",-1) ;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinnerImp                = (Spinner) findViewById(R.id.spinnerImpConta) ;
        imprimir_recibo_conta     = findViewById(R.id.imprimir_recibo_conta) ;
        imprimir_recibo_abertura  = findViewById(R.id.imprimir_recibo_abertura) ;
        imprimir_recibo_fechamento= findViewById(R.id.imprimir_recibo_fechamento) ;
        conta_recibo = findViewById(R.id.recibo_recibo) ;
        conta_recibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click", contaPedido.getPedido());
                controlImp.imprimirRecibo(contaPedido);
            }
        });
        conta_sair   = findViewById(R.id.recibo_sair) ;
        conta_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setResult(RESULT_CANCELED);
        getImpressora() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        consultarConta() ;
    }

    @Override
    protected void onDestroy() {
        controlImp.close();
        super.onDestroy();
    }

    private void displayConta(){
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
        imprimir_recibo_conta.setText("CONTA : "+contaPedido.getPedido());
        imprimir_recibo_abertura.setText(UtilSet.getDiaSemana(contaPedido.getData())+" "+dt.format(contaPedido.getData()));
        imprimir_recibo_fechamento.setText(UtilSet.getDiaSemana(contaPedido.getData_fechamento())+" "+dt.format(contaPedido.getData_fechamento()));
    }

    private void consultarConta() {
        Log.i("consultarConta", "idConta "+idConta) ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(idConta))) ;
        new BuildContaPedido(this, filters, "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                if (l.size() > 0){
                    contaPedido = l.get(0) ;
                    displayConta() ;
                } else alert("Conta não encontrada!");
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();


/*
        try {
            new ConexaoContaPedido(this, filters, "") {
                @Override
                public void oK(List<ContaPedido> l) {
                    if (l.size() > 0){
                        contaPedido = l.get(0) ;
                        displayConta() ;
                    } else alert("Conta não encontrada!");
                }

                @Override
                public void erro(String msg) {
                    alert(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
*/
    }

    private void alert(String t){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(t)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();

    }

    public void getImpressora(){
        ImpressoraAdapter impAdp = new ImpressoraAdapter(getBaseContext(), DaoDbTabela.getImpressoraADO(getBaseContext()).getList()) ;
        impressoraPadrao = DaoDbTabela.getImpressoraADO(getBaseContext()).getImpressora(UtilSet.getImpPadraoContaPedido(this)) ;
        spinnerImp.setAdapter(impAdp);
        spinnerImp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                impressoraPadrao = DaoDbTabela.getImpressoraADO(getBaseContext()).getList().get(i) ;
                UtilSet.setImpPadraoContaPedido(getBaseContext(), impressoraPadrao.getDescricao()) ;
                setImpressoraPadrao() ;
                //UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao()) ;
                //carregaRelatorio() ;
            }
        });
        setImpressoraPadrao();
    }

    private void setImpressoraPadrao() {
        String descImp = UtilSet.getImpPadraoContaPedido(this);
        impressoraPadrao = DaoDbTabela.getImpressoraADO(this).getNenhuma() ;
        for (int i = 0 ; i<spinnerImp.getCount() ; i++){
            spinnerImp.getItemAtPosition(i);
            Impressora impSp = (Impressora)spinnerImp.getItemAtPosition(i) ;
            // Log.i("List Imp","i "+impSp.getDescricao()+" padrao "+descImp) ;
            if (descImp.equals(impSp.getDescricao())) {
                spinnerImp.setSelection(i);
                impressoraPadrao = impSp ;
            }
        }
        instanciarImp() ;
    }

    private void instanciarImp() {
        if (controlImp != null) controlImp.close();
        controlImp = BuilderControleImp.getImpressora(this, impressoraPadrao);
            ListenerImpressao limp = new ListenerImpressao() {
                @Override
                public void onImpressao(int status) {
                    Log.i("click", contaPedido.getPedido());
                    fecharConta();
                }

                @Override
                public void onError(int status, String messagem) {
                    alert("Erro na Impressão "+messagem);
                }
            };
            controlImp.setListenerImpressao(limp);
            controlImp.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imprimir_recibo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action_conf) {
            startActivity(new Intent(getBaseContext(), ActivityConfiguracao.class));
            return true;
        }
        return true ;
    }

    private void fecharConta(){
        if (contaPedido.getStatus() == 1) {
            setFecharConta() ;
/*
            try {
                new ConexaoContaPedido(this, contaPedido, Link.fAlterarPedido) {
                    @Override
                    public void oK(List<ContaPedido> l) {
                        setResult(RESULT_OK);
                        alert("Conta Fechada!");
                    }

                    @Override
                    public void erro(String mgg) {
                        alert(mgg);
                    }
                }.execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                alert(e.getMessage());
            } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
                e.printStackTrace();
                alert(e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                alert(e.getMessage());
            }
*/
        } else finish();
    }

    private void setFecharConta(){
        contaPedido.setStatus(2);
        contaPedido.setData_fechamento(new Date());
        new BuildContaPedido(this, contaPedido, new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                setResult(RESULT_OK);
                alert("Conta Fechada!");
            }

            @Override
            public void erro(String msg) {
                alert(msg);

            }
        }).executar();

    }

}