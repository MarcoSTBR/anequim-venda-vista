package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildContaPedidoNFCe;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerContaPedidoNfce;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Impressora;
import com.anequimplus.impressao.BuilderControleImp;
import com.anequimplus.impressao.ControleImpressora;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.nfce.BuildImprimirNfce;
import com.anequimplus.nfce.ListenerImprimirNFCe;
import com.anequimplus.utilitarios.UtilSet;

import java.util.List;

public class ActivityDisplayNFce extends AppCompatActivity {

    private String UUID ;
    private String xml_conta ;
    private Spinner spinnerImp ;
    private ControleImpressora controlImp = null ;
    private Impressora impressoraPadrao ;
    private ContaPedidoNFCe contaPedidoNFCe = null ;
    private ContaPedido contaPedido = null ;
    private TextView text_nfce ;
    private TextView chave_nfce ;
    private Button imprimir_nfce ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_nfce);
        UUID = getIntent().getStringExtra("UUID") ;
        Toolbar toolbar = findViewById(R.id.toolbar_nfce);
        toolbar.setTitle("Impressão NFCe");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinnerImp    = (Spinner) findViewById(R.id.spinnerImpNfce) ;
        text_nfce     = findViewById(R.id.text_nfce) ;
        chave_nfce    = findViewById(R.id.chave_nfce) ;
        imprimir_nfce = findViewById(R.id.imprimir_nfce) ;
        imprimir_nfce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimirXml() ;
            }
        });
        getImpressora() ;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controlImp.close();
        Log.i("open_imp_close", "Activity") ;
    }


    @Override
    protected void onResume() {
        super.onResume();
        display() ;
    }

    private void imprimirXml(){
        new BuildImprimirNfce(this, contaPedidoNFCe, new ListenerImprimirNFCe() {
            @Override
            public void ok(String xml) {
                xml_conta = xml ;
                controlImp.imprimirXML(xml_conta);
            }

            @Override
            public void erro(String msg) {
                alerta(msg);
            }
        }).executar();
    }

    private void display() {
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("UUID", "=",UUID));
        new BuildContaPedidoNFCe(this, filters, "", new ListenerContaPedidoNfce() {
            @Override
            public void ok(List<ContaPedidoNFCe> l) {
                if (l.size()>0) {
                    contaPedidoNFCe = l.get(0);
                    getConta() ;
                } else alerta("Nfce não encontrada!") ;
            }

            @Override
            public void erro(String msg) {
                alerta(msg) ;

            }
        }).executar();
    }

    private void getConta(){
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(contaPedidoNFCe.getContaPedido_id())));
        new BuildContaPedido(this, filters.getList(), "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                if (l.size()>0){
                    contaPedido = l.get(0);
                    displayContaPedido();
                } else alerta("Conta não encontrada!"); ;
            }

            @Override
            public void erro(String msg) {
                alerta(msg);
            }
        }).executar();

    }

    private void displayContaPedido(){


        text_nfce.setText("Conta : "+contaPedido.getPedido());
        chave_nfce.setText(contaPedidoNFCe.getChave());

    }

    private void alerta(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
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
        setImpressoraPadrao();
        Log.i("open_imp", "getImpressora") ;
        spinnerImp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                impressoraPadrao = DaoDbTabela.getImpressoraADO(getBaseContext()).getList().get(i) ;
                UtilSet.setImpPadraoContaPedido(getBaseContext(), impressoraPadrao.getDescricao()) ;
                setImpressoraPadrao() ;
                Log.i("open_imp", "Select") ;
                //UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao()) ;
                //carregaRelatorio() ;
            }
        });
    }

    private void setImpressoraPadrao() {
        String descImp = UtilSet.getImpPadraoContaPedido(this);
        impressoraPadrao = null ;
        for (int i = 0 ; i<spinnerImp.getCount() ; i++){
            spinnerImp.getItemAtPosition(i);
            Impressora impSp = (Impressora)spinnerImp.getItemAtPosition(i) ;
            // Log.i("List Imp","i "+impSp.getDescricao()+" padrao "+descImp) ;
            if (descImp.equals(impSp.getDescricao())) {
                spinnerImp.setSelection(i);
                impressoraPadrao = impSp ;
            }
        }
        instanciarImp(impressoraPadrao) ;
    }

    private void instanciarImp(Impressora impressoraPadrao) {
        if (impressoraPadrao != null) {
            Log.i("open_imp", "impressora is null") ;
            if (controlImp != null) controlImp.close();
            controlImp = BuilderControleImp.getImpressora(this, impressoraPadrao);
            ListenerImpressao limp = new ListenerImpressao() {
                @Override
                public void onImpressao(int status) {
                    finish();
                }

                @Override
                public void onError(int status, String messagem) {
                    Toast.makeText(ActivityDisplayNFce.this, messagem, Toast.LENGTH_SHORT).show();

                }
            };

            controlImp.setListenerImpressao(limp);
            controlImp.open();
            Log.i("open_imp_arir", impressoraPadrao.getDescricao()) ;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true ;
    }

}