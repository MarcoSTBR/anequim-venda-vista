package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.ModalidadeAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoContaPedido;
import com.anequimplus.conexoes.ConexaoModalidade;
import com.anequimplus.conexoes.ConexaoPagamentoConta;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.DisplaySet;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityPagamento extends AppCompatActivity {


    private Caixa caixa ;
    private int conta_id ;
    private ContaPedido contaPedido = null ;
    private double valor = 0;
    private RecyclerView gradeModalidade;
    private DecimalFormat frmV = new DecimalFormat("   R$  #0.00");
    private Button botaoValorPag ;
    private final int MUDAR_VALOR = 3 ;
    private TextView totaldaconta ;
    private TextView totalapagar ;
    private TextView totalpago ;
 //   private LinearLayout layoutPagamento ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Pagamento");
        conta_id = getIntent().getIntExtra("CONTA_ID",0) ;
        totaldaconta = findViewById(R.id.totaldaconta);
        totalapagar = findViewById(R.id.totalapagar);
        totalpago = findViewById(R.id.totalpago);
   //     layoutPagamento = findViewById(R.id.layoutPagamento);

        gradeModalidade = findViewById(R.id.listModalidade);
        botaoValorPag = findViewById(R.id.botaoValorPag) ;
        botaoValorPag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ActivityValor.class);
                Bundle params = new Bundle();
                params.putDouble("VALOR", valor);
                intent.putExtras(params);
                startActivityForResult(intent, MUDAR_VALOR);
            }
        });

        carregaModalidade() ;
        carregarContaInicial() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
       // getMod() ;
    }

    private void getCaixa() {
       caixa = Dao.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this)) ;
       if (caixa == null){
           new AlertDialog.Builder(ActivityPagamento.this)
                   .setIcon(R.drawable.ic_notifications_black_24dp)
                   .setTitle("Erro:")
                   .setMessage("Caixa Fechado")
                   .setCancelable(false)
                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           finish();
                       }
                   }).show();
       }
    }

    private void carregarContaInicial() {
        try {
            List<FilterTable> filters = new ArrayList<FilterTable>();
            filters.add(new FilterTable("ID", "=", String.valueOf(conta_id))) ;
            new ConexaoContaPedido(this, filters) {
                @Override
                public void oK(List<ContaPedido> l) {
                    for (ContaPedido c : l){
                        if (c.getId() == conta_id) {
                            contaPedido = c;
                            valor = c.getTotalaPagar();
                            botaoValorPag.setText(frmV.format(valor));
                            totaldaconta.setText(frmV.format(contaPedido.getTotal()));
                            totalapagar.setText(frmV.format(contaPedido.getTotalaPagar()));
                            totalpago.setText(frmV.format(contaPedido.getTotalPagamentos()));
                            if (c.getTotalaPagar() < 0){
                                if (c.getStatus() == 1){
                                    fecharPedido(contaPedido) ;
                                } else alertSair("Pagamento OK");
                            }

                        }
                    }
                }

                @Override
                public void erro(String mgg) {
                    new AlertDialog.Builder(ActivityPagamento.this)
                            .setIcon(R.drawable.ic_notifications_black_24dp)
                            .setTitle("Erro:")
                            .setMessage("Conta Não Encontrada")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();

                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage()) ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage()) ;
        }
    }

    private void fecharPedido(ContaPedido contaPedido) {
        try {
            contaPedido.setStatus(2);
            contaPedido.setData(new Date());
            new ConexaoContaPedido(this, contaPedido, Link.fAlterarPedido) {

                @Override
                public void oK(List<ContaPedido> l) {
                    if (l.get(0).getStatus() == 2) {
                        finish();
                    }
                }

                @Override
                public void erro(String mgg) {
                    alert(mgg);

                }
            }.execute();

        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado | JSONException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MUDAR_VALOR) {
            if (resultCode == RESULT_OK) {
                Double vlr = data.getDoubleExtra("VALOR", 0.0) ;
                if (vlr > 0){
                    valor = vlr ;
                    botaoValorPag.setText(frmV.format(valor));
                    Toast.makeText(ActivityPagamento.this, "OK \n"+frmV.format(vlr), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityPagamento.this, "Valor Inválido \n"+frmV.format(vlr), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void carregaModalidade() {

        //layoutPagamento.setOrientation(2);
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        // at last set adapter to recycler view.
        gradeModalidade.setLayoutManager(layoutManager);
        gradeModalidade.setAdapter(new ModalidadeAdapter(this, Dao.getModalidadeADO(this).getList()) {
            @Override
            public void selecionado(Modalidade modalidade) {
                getCaixa() ;
                verificarParamento(modalidade) ;
            }
        });
    }

    private void verificarParamento(Modalidade modalidade) {

        Log.i("modalidade", modalidade.toString()) ;
        switch (modalidade.getTipoModalidade()){
            case pgTroco: Toast.makeText(this, "Erro: Modalidade de Troco!", Toast.LENGTH_SHORT).show();
                break;
            case pgCartao: efetuarPag(modalidade);
                break;
            case pgDinheiro: efetuarPag(modalidade);
                break;
            case pgCartaoLio: pagamentoLIO(modalidade) ;
                break;
            default: Toast.makeText(this, "Erro: Tipo de Modalidade Não Encontrado!", Toast.LENGTH_SHORT).show();

        }
    }

    private void pagamentoLIO(Modalidade modalidade) {
        Toast.makeText(this, "Implementar PAGAMENTO LIO!", Toast.LENGTH_LONG).show();
    }

    private void efetuarPag(Modalidade modalidade){
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        ContaPedidoPagamento pg = new ContaPedidoPagamento(0, contaPedido.getId(), UtilSet.getUUID(), new Date(), caixa.getId(), modalidade, valor, 1) ;
        new ConexaoPagamentoConta(this, pg) {
            @Override
            public void oK() {
                carregarContaInicial();

            }

            @Override
            public void erro(int cod, String msg) {
                alert(msg);

            }
        }.execute();

    }

    private void getModAtualizada(){
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

    private void alertSair(String message) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Alerta")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_modalidade){
            getModAtualizada();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pagamento, menu);
        return true ;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        carregaModalidade() ;
    }
}
