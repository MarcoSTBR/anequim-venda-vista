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
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.builds.BuildCaixa;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildContaPedidoPagamento;
import com.anequimplus.conexoes.ConexaoModalidade;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerContaPedidoPagamento;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.utilitarios.DisplaySet;
import com.anequimplus.utilitarios.UtilSet;

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
    private TextView totaldaconta ;
    private TextView totalapagar ;
    private TextView totalpago ;
    private static int IF_FECHAMENTO = 4 ;
    private final int MUDAR_VALOR = 3 ;
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
        getCaixa() ;
    }

    private void getCaixa() {
        FilterTables f = new FilterTables() ;
        f.add(new FilterTable("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(this)))) ;
        f.add(new FilterTable("STATUS", "=", "1")) ;
        new BuildCaixa(this, f, "", new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                if (l.size()>0) {
                    caixa = l.get(0) ;
                } else alertFinal("Caixa Fechado!");

            }

            @Override
            public void erro(String msg) {
                alertFinal(msg) ;
            }
        }).executar();

/*


        caixa = DaoDbTabela.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this)) ;
       if (caixa == null){
           new AlertDialog.Builder(ActivityPagamento.this)
                   .setIcon(R.drawable.ic_notifications_black_24dp)
                   .setTitle("Atenção")
                   .setMessage("Caixa Fechado")
                   .setCancelable(false)
                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           finish();
                       }
                   }).show();
       }
*/
    }

    private void alertFinal(String msg){
        new AlertDialog.Builder(ActivityPagamento.this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private void carregarContaInicial() {

//        try {
            List<FilterTable> filters = new ArrayList<FilterTable>();
            filters.add(new FilterTable("ID", "=", String.valueOf(conta_id))) ;
            new BuildContaPedido(this, filters, "", new ListenerContaPedido() {
                @Override
                public void ok(List<ContaPedido> l) {
                            contaPedido = l.get(0);
                            valor = contaPedido.getTotalaPagar() - contaPedido.getDesconto() ;
                            botaoValorPag.setText(frmV.format(valor));
                            totaldaconta.setText(frmV.format(contaPedido.getTotal()));
                            totalapagar.setText(frmV.format(valor));
                            totalpago.setText(frmV.format(contaPedido.getTotalPagamentos()));
                           // Log.i("containicial", "valor "+valor) ;
                          //  Log.i("containicial", "valor Truncdo "+UtilSet.truncate(valor)) ;

                            if (UtilSet.truncate(valor)  <= 0.00){
                                if (contaPedido.getStatus() == 1)
                                    fecharPedido(contaPedido) ;
                                else finish();
                            }
                }

                @Override
                public void erro(String msg) {
                    new AlertDialog.Builder(ActivityPagamento.this)
                            .setIcon(R.drawable.ic_notifications_black_24dp)
                            .setTitle("Erro")
                            .setMessage("Conta Não Encontrada \n"+msg)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();

                }
            }).executar();

/*
            new ConexaoContaPedido(this, filters, "") {
                @Override
                public void oK(List<ContaPedido> l) {
                    for (ContaPedido c : l){
                        if (c.getId() == conta_id) {
                            contaPedido = c;
                            valor = contaPedido.getTotalaPagar() - contaPedido.getDesconto() ;
                            botaoValorPag.setText(frmV.format(valor));
                            totaldaconta.setText(frmV.format(contaPedido.getTotal()));
                            totalapagar.setText(frmV.format(valor));
                            totalpago.setText(frmV.format(contaPedido.getTotalPagamentos()));
                            if (valor  <= 0){
                                 if (c.getStatus() == 1)
                                    fecharPedido(contaPedido) ;
                                 else finish();
                                }
                        }
                    }
                }

                @Override
                public void erro(String mgg) {
                    new AlertDialog.Builder(ActivityPagamento.this)
                            .setIcon(R.drawable.ic_notifications_black_24dp)
                            .setTitle("Erro")
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
*/

    }

    private void fecharPedido(ContaPedido cp) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção")
                .setMessage("A conta será fechada!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
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
        if (requestCode == IF_FECHAMENTO) {
            if (resultCode == RESULT_OK) {
                finish();
            } else {
                finish();
            }
        }

    }

    private void carregaModalidade() {

        //layoutPagamento.setOrientation(2);
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        // at last set adapter to recycler view.
        gradeModalidade.setLayoutManager(layoutManager);
        gradeModalidade.setAdapter(new ModalidadeAdapter(this, DaoDbTabela.getModalidadeADO(this).getGradeList()) {
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
            case pgDesconto: efetuarPag(modalidade);
                break;
            case pgPIX: efetuarPag(modalidade) ;
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
        //caixa = DaoDbTabela.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this)) ;
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        ContaPedidoPagamento pg = new ContaPedidoPagamento(0, contaPedido.getId(), UtilSet.getUUID(), new Date(), caixa.getId(), modalidade, valor, 1, UtilSet.getTerminalId(this)) ;


        new BuildContaPedidoPagamento(this, pg, new ListenerContaPedidoPagamento() {
            @Override
            public void ok(List<ContaPedidoPagamento> l) {
                if (contaPedido.getDesconto() > 0)
                    addDescontoNaConta() ;
                else {
                    addTroco() ;
                   // finish();
                }

            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
/*

        new ConexaoContaPedidoPagamento(this, pg) {
            @Override
            public void oK() {
                //carregarContaInicial();
                if (contaPedido.getDesconto() > 0)
                     addDescontoNaConta() ;
                    else
                     finish();

            }

            @Override
            public void erro(int cod, String msg) {
                alert(msg);

            }
        }.execute();
*/

    }

    private void addTroco(){
        List<FilterTable> filters = new ArrayList<>();
        filters.add(new FilterTable("ID", "=", String.valueOf(conta_id))) ;
        new BuildContaPedido(this, filters, "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                ContaPedido c = l.get(0) ;
                if (c.getTotalPagamentos() >= c.getTotal() && (c.getStatus() == 1)) {
                    Double vrtoco = c.getTotalPagamentos() - c.getTotal();
                    if (vrtoco > 0) {
                        ContaPedidoPagamento troco = new ContaPedidoPagamento(0, c.getId(), UtilSet.getUUID(), new Date(), caixa.getId(), DaoDbTabela.getModalidadeADO(ActivityPagamento.this).getModalidadeTroco(), vrtoco, 1, UtilSet.getTerminalId(ActivityPagamento.this));
                        setTroco(troco);
                      //  DaoDbTabela.getContaPedidoPagamentoADO(ctx).incluir(troco);
                    } else finish();
/*
                    c.setStatus(2);
                    c.setData_fechamento(new Date());
                    DaoDbTabela.getContaPedidoInternoDAO(ctx).store(c);
*/
                } else finish();
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
/*
        ContaPedido c = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(contaPedidoPagamento.getContaPedido_id());
        if (c.getTotalPagamentos() >= c.getTotal()) {
            Double vrtoco = c.getTotalPagamentos() - c.getTotal();
            if (vrtoco > 0) {
                ContaPedidoPagamento troco = new ContaPedidoPagamento(0, contaPedidoPagamento.getContaPedido_id(), UtilSet.getUUID(), new Date(), contaPedidoPagamento.getCaixa_id(), modtroco, vrtoco, 1);
                DaoDbTabela.getContaPedidoPagamentoADO(ctx).incluir(troco);
            }
            c.setStatus(2);
            c.setData_fechamento(new Date());
            DaoDbTabela.getContaPedidoInternoDAO(ctx).store(c);
        }*/

    }

    private void setTroco(ContaPedidoPagamento troco){
        new BuildContaPedidoPagamento(this, troco, new ListenerContaPedidoPagamento() {
            @Override
            public void ok(List<ContaPedidoPagamento> l) {
                fecharConta() ;
            }

            @Override
            public void erro(String msg) {
                alert("setar Troco "+msg);

            }
        }).executar();

    }

    private void fecharConta(){

        contaPedido.setStatus(2);
        contaPedido.setData_fechamento(new Date());
        //DaoDbTabela.getContaPedidoInternoDAO(ctx).store(c);
        new BuildContaPedido(this, contaPedido, new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                finish();
            }

            @Override
            public void erro(String msg) {
                alert("Erro ao Fechar - "+msg);
            }
        }).executar();
    }

    private void addDescontoNaConta(){
        valor = contaPedido.getDesconto() ;
        Modalidade m = DaoDbTabela.getModalidadeADO(this).getModalidadeDesconto() ;
        if (contaPedido.getDesconto() != contaPedido.getTotalPagamentoDesconto()) {
            efetuarDesconto(m) ;

        } else addTroco();

    }

    private void efetuarDesconto(Modalidade modalidade){
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        ContaPedidoPagamento pg = new ContaPedidoPagamento(0, contaPedido.getId(), UtilSet.getUUID(), new Date(), caixa.getId(), modalidade, valor, 1, UtilSet.getTerminalId(ActivityPagamento.this)) ;
        new BuildContaPedidoPagamento(this, pg, new ListenerContaPedidoPagamento() {
            @Override
            public void ok(List<ContaPedidoPagamento> l) {
                carregarContaInicial();
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
/*

        new ConexaoContaPedidoPagamento(this, pg) {
            @Override
            public void oK() {
                    carregarContaInicial();
            }

            @Override
            public void erro(int cod, String msg) {
                alert(msg);

            }
        }.execute();
*/

    }

    private void getModAtualizada(){
            new ConexaoModalidade(this) {
                @Override
                public void oK() {
                  //  DaoDbTabela.getModalidadeADO(getBaseContext()).modalidadeADD(j.getJSONArray("MODALIDADES")) ;
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
                .setTitle("Atenção")
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
            getModAtualizada();
            return true;
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
