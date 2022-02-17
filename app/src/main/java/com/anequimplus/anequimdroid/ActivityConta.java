package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.ContaPedidoViewAdapter;
import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoContaPedido;
import com.anequimplus.conexoes.ConexaoContaPedidoItemCancelar;
import com.anequimplus.conexoes.ConexaoTransferencia;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.impressao.BuilderControleImp;
import com.anequimplus.impressao.ControleImpressora;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.DisplaySet;
import com.anequimplus.utilitarios.UtilSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityConta extends AppCompatActivity {

    private RecyclerView gradeConta ;
    private ContaPedido contaPedido ;
    private Modalidade modalidade ;
    private Toolbar toolbar ;
    private Caixa caixa = null ;
    private static int CONTA_PAGAMENTO = 1 ;
    private static int CONTA_VALOR = 2 ;
    private Spinner spinnerImp ;
    private Impressora impressoraPadrao ;
    private String clientID ;
    private String accessToken ;
    private int idContaSelecionada = -1 ;
    private ControleImpressora controlImp = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        idContaSelecionada = getIntent().getIntExtra("CONTA_ID",-1) ;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationView menubottomAppBarConta = findViewById(R.id.menubottomAppBarConta);

        menubottomAppBarConta.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_consulta_conta){
                    consultarConta() ;
                    return true ;
                }
                if (menuItem.getItemId() == R.id.action_consultar_imprimir) {
                   // ImprimirConta();
                    setImprimirPedido();
                    return true;
                }
                if (menuItem.getItemId() == R.id.action_consulta_pagamento) {
                    receberValor();//verificarCaixaParaPagamento() ;
                    return true;
                }
                if (menuItem.getItemId() == R.id.action_consulta_fechamento) {
                    fechamentoPedido() ;
                    return true;
                }
                return true;
            }
        });
        gradeConta = findViewById(R.id.gradeConta);
        spinnerImp     = (Spinner) findViewById(R.id.spinnerImpConta) ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controlImp != null) controlImp.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getImpressora();
        consultarConta();
    }

    public void getImpressora(){
        ImpressoraAdapter impAdp = new ImpressoraAdapter(getBaseContext(),Dao.getImpressoraADO(getBaseContext()).getList()) ;
        impressoraPadrao = Dao.getImpressoraADO(getBaseContext()).getImpressora(UtilSet.getImpPadraoContaPedido(this)) ;
        spinnerImp.setAdapter(impAdp);
        spinnerImp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                impressoraPadrao = Dao.getImpressoraADO(getBaseContext()).getList().get(i) ;
                UtilSet.setImpPadraoContaPedido(getBaseContext(), impressoraPadrao.getDescricao()) ;
                setImpressoraPadrao() ;
                //UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao()) ;
                //carregaRelatorio() ;
            }
        });
        setImpressoraPadrao();
    }
/*
    private void carregaImp() {
        try {
            new ConexaoImpressoras(this) {
                @Override
                public void Ok() {
                    ImpressoraAdapter impAdp = new ImpressoraAdapter(getBaseContext(),Dao.getImpressoraADO(getBaseContext()).getList()) ;
                    spinnerImp.setAdapter(impAdp);
                    spinnerImp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            impressoraPadrao = Dao.getImpressoraADO(getBaseContext()).getList().get(i) ;
                            UtilSet.setImpPadraoContaPedido(getBaseContext(), impressoraPadrao.getDescricao()) ;
                            setImpressoraPadrao() ;
                            //UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao()) ;
                            //carregaRelatorio() ;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    setImpressoraPadrao();

                   // carregaRelatorio() ;
                }

                @Override
                public void erroMensagem(String msg) {
                    Toast.makeText(getBaseContext(),msg, Toast.LENGTH_LONG).show();

                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }
*/
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
           controlImp = BuilderControleImp.getImpressora(this, impressoraPadrao.getTipoImpressora());
           ListenerImpressao limp = new ListenerImpressao() {
               @Override
               public void onImpressao(int status) {
                   Toast.makeText(ActivityConta.this, "Impressão OK", Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onError(int status, String messagem) {
                   Toast.makeText(ActivityConta.this, messagem, Toast.LENGTH_SHORT).show();

               }
           };
           controlImp.setListenerImpressao(limp);
           controlImp.open();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conta, menu);
        return true ;//super.onCreateOptionsMenu(menu);
    }

    private void getCaixa(){
        caixa = Dao.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this)) ;
        if (caixa != null) {

        } else Toast.makeText(getBaseContext(), "Caixa Fchado!" ,Toast.LENGTH_SHORT).show();
    }
/*
    private void setSubtitleTotal() {
        double v = 0 ;
        ContaPedido c  = null ;
        for (String sl : listContaPedidoSelect){
            c = Dao.getContaPedidoADO(getBaseContext()).getContaPedido(sl) ;
            v = v + c.getTotalItens() - c.getDesconto() + c.getComissao() - c.getTotalPagamentos() ;
        }
        DecimalFormat frmV = new DecimalFormat("R$  #0.00");
        String txt = "Contas(s) ("+listContaPedidoSelect.size()+")"+listContaPedidoSelect.size()+" "+frmV.format(v) ;
        if (v < 0){
            txt = "Contas(s) ("+listContaPedidoSelect.size()+") TROCO "+listContaPedidoSelect.size()+" "+frmV.format(-1 * v) ;
        }
        toolbar.setSubtitle(txt);
    }

 */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_conta_logout) {
            startActivity(new Intent(getBaseContext(), ActivityLogin.class));
            return true;
        }

        if (item.getItemId() == R.id.action_consulta_conta){
            consultarConta() ;
            return true ;
        }
        if (item.getItemId() == R.id.action_consultar_imprimir) {
            // ImprimirConta();
            setImprimirPedido();
            return true;
        }
        if (item.getItemId() == R.id.action_consulta_pagamento) {
            receberValor();//verificarCaixaParaPagamento() ;
            return true;
        }
        if (item.getItemId() == R.id.action_consulta_fechamento) {
            fechamentoPedido() ;
            return true;
        }
        return true;
    }

    private void consultarConta() {
       Log.i("consultarConta", "idContaSelecionada "+idContaSelecionada) ;
       List<FilterTable> filters = new ArrayList<FilterTable>() ;
       filters.add(new FilterTable("ID", "=", String.valueOf(idContaSelecionada))) ;
        try {
            new ConexaoContaPedido(this, filters) {
                   @Override
                   public void oK(List<ContaPedido> l) {
                      selecionanalista(l);
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
    }

    private void selecionanalista(List<ContaPedido> l) {
        contaPedido = null ;
        for (ContaPedido c : l){
            Log.i("consultarConta", "ContaPedido "+c.getPedido()) ;
            if (c.getId() == idContaSelecionada) {
                contaPedido = c;
                verificaConta() ;
            }
        }
        if (contaPedido == null){
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

        } else {
            displayConta() ;
        }

    }

    private void verificaConta(){
        if (contaPedido.getTotalaPagar() <= 0){
            if (contaPedido.getStatus() == 2)
                alertSair(contaPedido.getPedido()+" Fechado ");
             else {
                 fechamentoPedido();
            }
        }

    }

    private void pagamentoConta(double v) {
        /*
      if (listContaPedidoSelect.size() == 1){
         if (v > 0) {
             // contaPedido = listContaPedidoSelect.get(0) ;
             // valor = valor + contaPedido.getTotalItens() + contaPedido.getComissao() - contaPedido.getDesconto() - contaPedido.getTotalPagamentos();
             Intent intent = new Intent(getBaseContext(), ActivityPagamento.class);
             Bundle params = new Bundle();
             params.putDouble("VALOR", v);
             intent.putExtras(params);
             startActivityForResult(intent, CONTA_PAGAMENTO);
         } else alert("Valor incorreto!");
      } else {
          alert("Selecione uma conta somente!") ;
      }

         */
    }

    private void receberValor(){
        caixa = Dao.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this)) ;
        if (caixa != null){
            Bundle params = new Bundle();
            Intent intent = new Intent(this, ActivityPagamento.class);
            params.putInt("CONTA_ID", idContaSelecionada);
            intent.putExtras(params);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Caixa Fechado", Toast.LENGTH_SHORT).show();
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTA_PAGAMENTO){
            if (resultCode == RESULT_OK) {
                modalidade = Dao.getModalidadeADO(this).getModalidade(data.getIntExtra("MODALIDADE_ID",0)) ;
                double valor = data.getDoubleExtra("VALOR",0) ;
                if (valor > 0) {
                    if (modalidade.getTipoModalidade() == TipoModalidade.pgCartaoLio) {
                        setConfiguracaoPagamentoCielo(valor);
                    } else {
                        setPagamentoConta(valor);
                        Log.i("pagamentoADD", modalidade.getDescricao());

                    }
                } else alert("Transação incorreta!");
                //pagamentoADD(data.getDoubleExtra("VALOR",0)) ;
            }
        }
        if (requestCode == CONTA_VALOR){
            if (resultCode == RESULT_OK) {
                double valor = data.getDoubleExtra("VALOR", 0.0) ;
                Toast.makeText(this, "valor "+valor, Toast.LENGTH_LONG).show();
                pagamentoConta(valor);
            }
        }
    }

    private void setConfiguracaoPagamentoCielo(final double v) {
        try {
            new ConexaoConfiguracaoLio(this) {
                @Override
                public void oK(String c, String a) {
                    clientID = c;
                    accessToken = a ;
                    ConexaoPagamentoLio lio = new ConexaoPagamentoLio(getBaseContext(), contaPedido.getPedido(), clientID, accessToken) {
                        @Override
                        protected void returnoPag(Modalidade modalidade, double valor, String msg, PagamentoStatus status) {
                            Log.i("pagamentoADD LIO ",modalidade.getDescricao()) ;
                            if (status == PagamentoStatus.SUCESSO){
                                setPagamentoConta(valor) ;
                            } else {
                                alert(msg);
                            }
                        }
                    };
                    lio.execute(modalidade, v);

                 //   pagamentoCielo(v);
                }

                @Override
                public void erro(String msg) {
                    alert(msg);
                }
            }.execute() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado | MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    private void setPagamentoConta(double valor){
        double v = valor ;
        if (v > 0) {
            try {
                new ConexaoPagamentoConta(this, contaPedido, caixa, modalidade, v) {
                    @Override
                    public void oK() {
                      consultarConta();
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);

                    }
                }.execute();
            } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado | MalformedURLException e) {
                e.printStackTrace();
                alert(e.getMessage());
            }
        } else alert("Valor incorreto!");
    }
*/
    private void alerta(String titulo, String txt){
         new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle(titulo)
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    private void setImprimirPedido(){

        if (impressoraPadrao != null) {
            controlImp.imprimeConta(contaPedido);
        } else {
                alerta("Atenção", "Selecione uma Impressora!");
        }

    }

    private void alert(String t){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(t)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();

    }

    private void alertSair(String t){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(t)
                .setCancelable(false)
                .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();

    }

    public void displayConta(){

        TextView titulo = findViewById(R.id.tituloConta) ;
        titulo.setText("Conta : "+contaPedido.getPedido());


        ContaPedidoViewAdapter cpv = new ContaPedidoViewAdapter(contaPedido.getListContaPedidoItemAtivos()) {
            @Override
            public void cancelar(ContaPedidoItem item) {
                perguntarCancelamento(item) ;
            }

            @Override
            public void transferir(ContaPedidoItem item) {
                setTransfere(item) ;
                //Toast.makeText(ActivityConta.this, "TRANSFERIR "+item.getProduto(), Toast.LENGTH_SHORT).show();

            }
        };
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        gradeConta.setLayoutManager(layoutManager);
        gradeConta.setAdapter(cpv) ;

    }

    private void setTransfere(final ContaPedidoItem item) {
        AlertDialog.Builder myBuiler = new AlertDialog.Builder(ActivityConta.this) ;
        DecimalFormat qrm = new DecimalFormat("#0.###");
        View mView = getLayoutInflater().inflate(R.layout.layout_pergunta_transferencia, null) ;
        myBuiler.setView(mView) ;
        AlertDialog alert = myBuiler.create() ;


        TextView text_item = mView.findViewById(R.id.item_para_transferencia) ;
        text_item.setText(qrm.format(item.getQuantidade())+" "+item.getProduto().getDescricao());
        final EditText conta =  mView.findViewById(R.id.edit_para_conta) ;
        Button confirmar = mView.findViewById(R.id.botao_trasnferencia_confirmar) ;
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!conta.getText().toString().isEmpty()){
                    alert.dismiss();
                    tranferirParaConta(conta.getText().toString(), item) ;;

                } else Toast.makeText(ActivityConta.this, "Digite o número da conta corretamente!", Toast.LENGTH_SHORT).show();
            }
        });

        Button cancelar = mView.findViewById(R.id.botao_trasnferencia_cancela) ;
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void tranferirParaConta(String conta, ContaPedidoItem item) {
        ContaPedido cp = new ContaPedido(UtilSet.getUUID(), conta, new Date(), UtilSet.getUsuarioId(this)) ;
        Transferencia t = new Transferencia(0, UtilSet.getUUID(), new Date(), contaPedido, cp, item, item.getQuantidade(), 1, UtilSet.getUsuarioId(this));
        new ConexaoTransferencia(this, t) {
            @Override
            public void ok() {
                consultarConta();

            }

            @Override
            public void erro(int cod, String msg) {
                alert(msg);

            }
        }.execute();

        
    }

    private void perguntarCancelamento(final ContaPedidoItem item){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Cancelamento")
                .setMessage("Cancelar "+item.getQuantidade()+" "+item.getProduto().getDescricao())
                .setCancelable(false)
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelarItem(item) ;
                    }
                })
                .show();

    }

    private void cancelarItem(ContaPedidoItem item) {
        Log.i("cancelarItem", item.getProduto().getDescricao()) ;
        ContaPedidoItemCancelamento cancel = new ContaPedidoItemCancelamento(0, UtilSet.getUUID(), item, new Date(),
                UtilSet.getUsuarioId(this), item.getQuantidade(), 1) ;

        new ConexaoContaPedidoItemCancelar(this, cancel) {
            @Override
            public void Ok() {
                consultarConta() ;

            }

            @Override
            public void erro(int cod, String msg) {
                alert(msg);

            }
        }.execute() ;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        displayConta() ;
    }

    private void fechamentoPedido() {
        try {
            if (contaPedido.getTotalaPagar() > 0) throw new Exception("Pagamento Não Realizado!") ;
            contaPedido.setStatus(2);
            contaPedido.setData_fechamento(new Date());
            new ConexaoContaPedido(this, contaPedido, Link.fAlterarPedido) {
                @Override
                public void oK(List<ContaPedido> l) {
                    alertSair("Pedido fechado "+l.size());
                    //finish();

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
        } catch (Exception e) {
            e.printStackTrace();
            alert(e.getMessage());
        }

    }

}
