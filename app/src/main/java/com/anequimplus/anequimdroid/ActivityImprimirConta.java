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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildControleAcesso;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Impressora;
import com.anequimplus.impressao.BuilderControleImp;
import com.anequimplus.impressao.ContaPedidoNenhum;
import com.anequimplus.impressao.ControleImpressora;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerControleAcesso;
import com.anequimplus.utilitarios.UtilSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityImprimirConta extends AppCompatActivity {

    private Toolbar toolbar ;
    private Spinner spinnerImp ;
    private ControleImpressora controlImp = null ;
    private Impressora impressoraPadrao ;
    private ContaPedido contaPedido ;
    private int idConta ;
    private TextView imprimir_conta ;
    private TextView imprimir_abertura ;
    private Button conta_normal ;
    private Button conta_sem_comissao ;
    private Button conta_com_desconto ;
    private Button conta_sem_comissao_com_desconto ;
    private Button conta_sair ;
    private ImageButton mais ;
    private ImageButton menos ;
    private TextView imprimir_num_pessoas ;
    private int num_pessoas ;
    private static int COM_COMISSAO = 1 ;
    private static int SEM_COMISSAO = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprimir_conta);
        idConta = getIntent().getIntExtra("CONTA_ID",0) ;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinnerImp     = (Spinner) findViewById(R.id.spinnerImpConta) ;
        imprimir_conta = findViewById(R.id.imprimir_conta) ;
        imprimir_abertura = findViewById(R.id.imprimir_abertura) ;

        conta_normal       = findViewById(R.id.conta_normal) ;
        conta_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EMITIR CONTA
                new BuildControleAcesso(ActivityImprimirConta.this, 23, new ListenerControleAcesso() {
                    @Override
                    public void ok(int usuario_id) {
                        imprimirconta(1, 0.00);
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);
                    }
                }).executar();

            }
        });

        conta_sem_comissao = findViewById(R.id.conta_sem_comissao) ;
        conta_sem_comissao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // EMITIR CONTA SEM COMISSAO
                new BuildControleAcesso(ActivityImprimirConta.this, 17, new ListenerControleAcesso() {
                    @Override
                    public void ok(int usuario_id) {
                        imprimirconta(0, 0.00);
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);
                    }
                }).executar();


            }
        });

        conta_com_desconto = findViewById(R.id.conta_desconto) ;
        conta_com_desconto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EMITIR CONTA COM DESCONTO
                new BuildControleAcesso(ActivityImprimirConta.this, 21, new ListenerControleAcesso() {
                    @Override
                    public void ok(int usuario_id) {
                        startActivityForResult(new Intent(ActivityImprimirConta.this, ActivityValor.class), COM_COMISSAO);
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);
                    }
                }).executar();

            }
        });

        conta_sem_comissao_com_desconto = findViewById(R.id.conta_sem_comisao_desconto) ;
        conta_sem_comissao_com_desconto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EMITIR CONTA SEM COMISSAO E COM DESCONTO
                new BuildControleAcesso(ActivityImprimirConta.this, 24, new ListenerControleAcesso() {
                    @Override
                    public void ok(int usuario_id) {
                        startActivityForResult(new Intent(ActivityImprimirConta.this, ActivityValor.class), SEM_COMISSAO);
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);
                    }
                }).executar();

            }
        });

        conta_sair = findViewById(R.id.imprimir_sair) ;
        conta_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imprimir_num_pessoas = findViewById(R.id.imprimir_num_pessoas) ;
        mais  = findViewById(R.id.imprimir_pessoas_mais) ;
        mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num_pessoas = num_pessoas + 1 ;
                displayPessoas() ;
            }
        });
        menos = findViewById(R.id.imprimir_pessoas_menos) ;
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num_pessoas > 1) {
                    num_pessoas = num_pessoas - 1;
                    displayPessoas();
                } else num_pessoas = 1 ;
            }
        });
        getImpressora() ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COM_COMISSAO) {
            if (resultCode == RESULT_OK){
                Double desconto = data.getDoubleExtra("VALOR", 0.00) ;
                imprimirconta(1,desconto); ;
            }
        }
        if (requestCode == SEM_COMISSAO) {
            if (resultCode == RESULT_OK){
                Double desconto = data.getDoubleExtra("VALOR", 0.00) ;
                imprimirconta(0,desconto); ;
            }
        }
    }

    private void imprimirconta(int statusComissao, Double desconto){
        try
          {
            if (desconto > contaPedido.getTotalItens()) throw new Exception("Valor de Desconto Inválido!") ;
            if ((desconto > 0 ) && (!DaoDbTabela.getModalidadeADO(this).ifDesconto())) throw new Exception("Desconto Não Autorizado!") ;
            if (contaPedido.getNum_impressao() > 0){
                new BuildControleAcesso(ActivityImprimirConta.this, 19, new ListenerControleAcesso() {
                    @Override
                    public void ok(int usuario_id) {
                        setImprimirconta(statusComissao, desconto);
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);
                    }
                }).executar();

            } else setImprimirconta(statusComissao, desconto);

        } catch (Exception e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    private void setImprimirconta(int statusComissao, Double desconto){
        contaPedido.setStatus_comissao(statusComissao);
        contaPedido.setDesconto(desconto);
        contaPedido.setNum_impressao(contaPedido.getNum_impressao()+1);
        contaPedido.setNum_pessoas(num_pessoas);
        new BuildContaPedido(this, contaPedido, new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                controlImp.imprimeConta(l.get(0));
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();

    }

    private void impressaoOk(){
        finish();
    }

    private void impressaoErro(int status, String messagem){
        int num = contaPedido.getNum_impressao() ;
        if (num > 0) num = num - 1 ;
        contaPedido.setNum_impressao(num);
        new BuildContaPedido(this, contaPedido, new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                Toast.makeText(ActivityImprimirConta.this, messagem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
    }


    private void displayConta(){
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
        imprimir_conta.setText("Conta : "+contaPedido.getPedido());
        imprimir_abertura.setText(UtilSet.getDiaSemana(contaPedido.getData())+" "+dt.format(contaPedido.getData()));
        num_pessoas = contaPedido.getNum_pessoas() ;
        displayPessoas() ;
    }

    private void  displayPessoas(){
        imprimir_num_pessoas.setText(String.valueOf(num_pessoas));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controlImp.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        consultarConta() ;
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
                controlImp.close(); // mudança aqui
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
        impressoraPadrao = null  ;
        controlImp = new ContaPedidoNenhum(this) ;
        for (int i = 0 ; i<spinnerImp.getCount() ; i++){
            spinnerImp.getItemAtPosition(i);
            Impressora impSp = (Impressora)spinnerImp.getItemAtPosition(i) ;
            if (descImp.equals(impSp.getDescricao())) {
                spinnerImp.setSelection(i);
                impressoraPadrao = impSp ;
            }
        }
        if (impressoraPadrao != null)
          instanciarImp(impressoraPadrao) ;
    }

    private void instanciarImp(Impressora impressoraPadrao) {
      controlImp = BuilderControleImp.getImpressora(this, impressoraPadrao);
      controlImp.setListenerImpressao(getlistererImpressora());
      controlImp.open();
    }

    private ListenerImpressao getlistererImpressora(){
        return new ListenerImpressao() {
            @Override
            public void onImpressao(int status) {
                impressaoOk();
            }

            @Override
            public void onError(int status, String messagem) {
                impressaoErro(status, messagem) ;
//                    Toast.makeText(ActivityImprimirConta.this, messagem, Toast.LENGTH_SHORT).show();

            }
        };

    }

    private void consultarConta() {
        Log.i("consultarConta", "idConta "+idConta) ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(idConta))) ;
        new BuildContaPedido(this, filters, "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                selecionanalista(l);
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();

    }

    private void selecionanalista(List<ContaPedido> l) {
        if (l.size() > 0) {
            contaPedido = l.get(0) ;
            displayConta() ;
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_notifications_black_24dp)
                    .setTitle("Conta Pedido")
                    .setMessage("Conta não encontrada!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imprimir_contal, menu);
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

}