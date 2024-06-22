package com.anequimplus.anequimdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.ContaPedidoViewAdapter;
import com.anequimplus.builds.BuildCaixa;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildContaPedidoItemCancelamento;
import com.anequimplus.builds.BuildContaPedidoTransferencia;
import com.anequimplus.builds.BuildControleAcesso;
import com.anequimplus.builds.BuildEnvioComandaRemota;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ItemSelect;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerControleAcesso;
import com.anequimplus.listeners.ListenerEnvioComandaRemota;
import com.anequimplus.listeners.ListenerItemCancelamento;
import com.anequimplus.listeners.ListenerTransferencia;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.DisplaySet;
import com.anequimplus.utilitarios.UtilSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityConta extends AppCompatActivity {

    private RecyclerView gradeConta ;
    private ContaPedido contaPedido ;
    private ContaPedidoItem contaPedidoItem ;
    private Modalidade modalidade ;
    private Toolbar toolbar ;
    private Caixa caixa = null ;
    private static int FECHAMENTO = 3 ;
    private String clientID ;
    private String accessToken ;
    private int idContaSelecionada = -1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        idContaSelecionada = getIntent().getIntExtra("CONTA_ID",-1) ;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationView menubottomAppBarConta = findViewById(R.id.menubottomAppBarConta);
        menubottomAppBarConta.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.action_consulta_conta){
                    //consultarConta() ;
                    verificalimparConta() ;
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
                     finish();
                    return true;
                }
                return true;

            }
        });

        getCaixa() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //consultarConta();
        verificalimparConta() ;
    }

    private void getCaixa(){
        FilterTables f = new FilterTables() ;
        f.add(new FilterTable("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(this)))) ;
        f.add(new FilterTable("STATUS", "=", "1")) ;
        new BuildCaixa(this, f, "", new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                if (l.size()>0){
                    caixa = l.get(0) ;
                } else {
                    if (!Configuracao.getPedidoCompartilhado(ActivityConta.this))
                       alertSair("Caixa Fechado!");
                }
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conta, menu);
        return true ;//super.onCreateOptionsMenu(menu);
    }

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
            //consultarConta() ;
            verificalimparConta() ;
            return true ;
        }
        if (item.getItemId() == R.id.action_consultar_imprimir) {
            // ImprimirConta();
            setImprimirPedido();
            return true;
        }
        if (item.getItemId() == R.id.action_consulta_pagamento) {
            //verificarCaixaParaPagamento() ;
            receberValor();
            return true;
        }
        if (item.getItemId() == R.id.action_consulta_fechamento) {
            finish();
            return true;
        }
        return true;
    }

    private void verificalimparConta(){
        Log.i("consultarConta", "idContaSelecionada "+idContaSelecionada) ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(idContaSelecionada))) ;
        new BuildContaPedido(this, filters, "ID", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                if (l.size() > 0) {
                    contaPedido = l.get(0);
                    selecionanalista();
                } else
                    alertSair("Nenhuma Conta Encontrada!");

            }
            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();

    }

    private void selecionanalista() {
        displayConta();
        verificaConta();
    }

    private void verificaConta(){
      //  Log.i("verificaConta", "pago "+UtilSet.truncate(contaPedido.getTotalaPagar()));
        if (UtilSet.truncate(contaPedido.getTotalaPagar()) <= 0)
            if (contaPedido.getStatus() == 1){
                displayRecibo();
            } else
            if (contaPedido.getStatus() == 2) {
                displayRecibo() ;
                //alertSair(contaPedido.getPedido() + " Fechado ");
            }
    }


    private void fechamentoPedido() {

            if (contaPedido.getStatus() == 2) {
                alertSair("Conta Fechada!");
            } else {
              // if (caixa != null)
                if (UtilSet.truncate(contaPedido.getTotalaPagar()) > 0)
                    displayRecibo();
            }
    }

    private void displayRecibo(){
        Bundle params = new Bundle();
        Intent intent = new Intent(getBaseContext(), ActivityFechamentoConta.class);
        params.putInt("CONTA_ID", contaPedido.getId());
        intent.putExtras(params);
        startActivityForResult(intent, FECHAMENTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FECHAMENTO) {
            if (resultCode == RESULT_OK) {
                finish();
            } else {
                finish();
            }
        }
    }

    private void receberValor(){
        if (caixa != null){
            if (Configuracao.getPedidoCompartilhado(this)){
                if (!Configuracao.getApiVersao(this).equals("V14")) {
                    alert("Opção Não Disponivel para Essa Versão!");
                } else abrirPagamento();
            } else abrirPagamento() ;
        } else {
            Toast.makeText(this, "Caixa Fechado", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirPagamento(){
        Bundle params = new Bundle();
        Intent intent = new Intent(this, ActivityPagamento.class);
        params.putInt("CONTA_ID", idContaSelecionada);
        intent.putExtras(params);
        startActivity(intent);
    }


    private void setImprimirPedido(){
        Intent intent = new Intent(this, ActivityImprimirConta.class);
        Bundle params = new Bundle();
        params.putInt("CONTA_ID", contaPedido.getId());
        intent.putExtras(params);
        startActivity(intent);
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
        gradeConta = findViewById(R.id.gradeConta) ;
        toolbar.setTitle("CONTA : "+contaPedido.getPedido());
        List<ContaPedidoItem> listItem = contaPedido.getListContaPedidoItemAtivos() ;
        ContaPedidoViewAdapter cpv = new ContaPedidoViewAdapter(listItem) {
            @Override
            public void cancelar(ContaPedidoItem item) {
                new CancelarContaItem(ActivityConta.this, item) {
                    @Override
                    protected void setQuantidadeACancelar(ContaPedidoItem it,  Double quantCancel) {
                        new BuildControleAcesso(ActivityConta.this, 18, new ListenerControleAcesso() {
                            @Override
                            public void ok(int usuario_id) {
                                cancelarItem(it, quantCancel) ;
                            }

                            @Override
                            public void erro(String msg) {
                                alert(msg);

                            }
                        }).executar();
                    }
                }.show();
               // perguntarCancelamento(item) ;
            }

            @Override
            public void transferir(ContaPedidoItem item) {
                new BuildControleAcesso(ActivityConta.this, 16, new ListenerControleAcesso() {
                    @Override
                    public void ok(int usuario_id) {
                        new TransferenciaContaItem( ActivityConta.this, contaPedido ,item).show();
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);
                    }
                }).executar();
            }
        };
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        gradeConta.setAdapter(cpv) ;
        gradeConta.setLayoutManager(layoutManager);
    }

    private void tranferirParaConta(String conta, ContaPedidoItem item, Double q) {
        int idCaixa = 0 ;
        if (caixa != null)
            idCaixa =  caixa.getId() ;
        Transferencia t = new Transferencia(0, UtilSet.getUUID(), new Date(), contaPedido.getId(), 0, item.getId(), q, 1, UtilSet.getUsuarioId(this), idCaixa, UtilSet.getTerminalId(ActivityConta.this));
        new BuildContaPedidoTransferencia(this, conta, t, new ListenerTransferencia() {
            @Override
            public void ok(List<Transferencia> t) {
                verificalimparConta() ;
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
    }

    private void cancelarItem(ContaPedidoItem item, Double q) {
        Log.i("cancelarItem", "id "+item.getId()) ;
        Log.i("cancelarItem", item.getProduto().getId()+" "+item.getProduto().getDescricao()) ;
        contaPedidoItem = item ;

        int idCaixa = 0 ;
        if (caixa != null)
            idCaixa = caixa.getId() ;

        ContaPedidoItemCancelamento cancel = new ContaPedidoItemCancelamento(0,
                UtilSet.getUUID(), item.getId(), new Date(),
                UtilSet.getUsuarioId(this), q, 1, idCaixa, UtilSet.getTerminalId(ActivityConta.this)) ;

        new BuildContaPedidoItemCancelamento(this, cancel, contaPedido, item, new ListenerItemCancelamento() {
            @Override
            public void ok(List<ContaPedidoItemCancelamento> l) {
//                verificalimparConta() ;
                if (l.size()>0)
                   preparaComandaRemota(l.get(0)) ;
                  else alert("Sem retorno de Cancelamento!");

            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
    }

    private void preparaComandaRemota(ContaPedidoItemCancelamento item){
        List<Pedido> listPed = new ArrayList<Pedido>() ;
        Pedido ped = new Pedido(contaPedido.getId(), contaPedido.getPedido(), new Date(), new ArrayList<PedidoItem>()) ;
        ItemSelect is =  new ItemSelect(contaPedidoItem.getId(), contaPedidoItem.getProduto(), item.getQuantidade(),
        contaPedidoItem.getPreco(), contaPedidoItem.getDesconto(), contaPedidoItem.getComissao(), contaPedidoItem.getValor(),
        contaPedidoItem.getObs(), 0);
        ped.getListPedidoItem().add(new PedidoItem(item.getId(),contaPedido.getId() ,is, new ArrayList<PedidoItemAcomp>())) ;
        listPed.add(ped) ;
        comandaRemota(listPed);
    }

    private void comandaRemota(List<Pedido> l){
        new BuildEnvioComandaRemota(this, l, 1, new ListenerEnvioComandaRemota() {
            @Override
            public void ok(String msg) {
                verificalimparConta() ;
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(ActivityConta.this, msg, Toast.LENGTH_SHORT).show();

            }
        }).executar();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        displayConta() ;
    }

   abstract class CancelarContaItem {
        private Context ctx ;
        private ContaPedidoItem contaPedidoItem;
        private TextView descricao ;
        private TextView obs ;
        private TextView quantidade ;
        private ImageButton mais ;
        private ImageButton menos ;
        private Button cancel ;
        private Button confirmar ;
        private Double quantCancel ;
        private AlertDialog janela ;

        public CancelarContaItem(Context ctx, ContaPedidoItem contaPedidoItem){
            this.ctx = ctx ;
            this.contaPedidoItem = contaPedidoItem ;
        }

        public void show(){
            quantCancel = 1.0 ;//contaPedidoItem.getQuantidade() ;
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx) ;
            View view = getLayoutInflater().inflate(R.layout.layout_grade_cancelamento_item, null) ;
            descricao = view.findViewById(R.id.textProdutoDescricao) ;
            obs       = view.findViewById(R.id.textProdutoObs) ;
            quantidade= view.findViewById(R.id.textQPrPedidoItemS) ;
            mais      = view.findViewById(R.id.ib_maisS) ;
            menos     = view.findViewById(R.id.ib_menosS) ;
            cancel    = view.findViewById(R.id.cancelar_cancel) ;
            confirmar = view.findViewById(R.id.cancelar_confirar) ;

            mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (quantCancel < contaPedidoItem.getQuantidade()){
                        quantCancel = quantCancel + 1 ;
                        setValores() ;
                    } else Toast.makeText(ctx, "Valor inválido!", Toast.LENGTH_SHORT).show();

                }
            });
            menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (quantCancel > 1.00){
                        quantCancel = quantCancel - 1 ;
                        setValores() ;
                    } else Toast.makeText(ctx, "Valor inválido!", Toast.LENGTH_SHORT).show();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    janela.dismiss();
                }
            });
            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setQuantidadeACancelar(contaPedidoItem,  quantCancel);
                    janela.dismiss();
                }
            });

            bind();
            builder.setView(view) ;
            janela = builder.create() ;
            janela.show() ;
        }

        private void bind(){
            descricao.setText(contaPedidoItem.getProduto().getDescricao());
            obs.setText(contaPedidoItem.getObs());
            setValores();
            //quantidade.setText(fq.format(contaPedidoItem.getQuantidade()));
        }

        private void setValores(){
            DecimalFormat fq = new DecimalFormat("#0.###") ;
            quantidade.setText(fq.format(quantCancel));
        }

       protected abstract void setQuantidadeACancelar(ContaPedidoItem it, Double quantCancel);
    }

    class TransferenciaContaItem{

        private Context ctx ;
        private ContaPedido contaPedido ;
        private ContaPedidoItem item ;
        private TextView text_item ;
        private TextView quantidade ;
        private EditText edit_conta ;
        private Button confirmar ;
        private Button cancelar ;
        private Double quantTrans ;
        private ImageButton mais ;
        private ImageButton menos ;
        private AlertDialog janela ;

        public TransferenciaContaItem(Context ctx, ContaPedido contaPedido, ContaPedidoItem item){
            this.ctx = ctx ;
            this.item = item ;
            this.contaPedido = contaPedido ;
        }

        public void show(){
            quantTrans = 1.0 ;//item.getQuantidade() ;
            AlertDialog.Builder myBuiler = new AlertDialog.Builder(ctx) ;
            View mView = getLayoutInflater().inflate(R.layout.layout_pergunta_transferencia, null) ;
            text_item = mView.findViewById(R.id.item_para_transferencia) ;
            edit_conta =  mView.findViewById(R.id.edit_para_conta) ;
            confirmar = mView.findViewById(R.id.botao_trasnferencia_confirmar) ;
            cancelar = mView.findViewById(R.id.botao_trasnferencia_cancela) ;
            quantidade = mView.findViewById(R.id.textQPrPedidoItemS) ;

            mais      = mView.findViewById(R.id.ib_maisS) ;
            menos     = mView.findViewById(R.id.ib_menosS) ;

            mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (quantTrans < item.getQuantidade()){
                        quantTrans = quantTrans + 1 ;
                        setValores() ;
                    } else Toast.makeText(ctx, "Valor inválido!", Toast.LENGTH_SHORT).show();

                }
            });
            menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (quantTrans > 1.00){
                        quantTrans = quantTrans - 1 ;
                        setValores() ;
                    } else Toast.makeText(ctx, "Valor inválido!", Toast.LENGTH_SHORT).show();
                }
            });

            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edit_conta.getText().toString().isEmpty()){
                        if (!edit_conta.getText().toString().equals(contaPedido.getPedido())) {
                            janela.dismiss();
                            tranferirParaConta(edit_conta.getText().toString(), item, quantTrans);
                        } else Toast.makeText(ActivityConta.this, "Digite o número da conta corretamente!", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(ActivityConta.this, "Digite o número da conta corretamente!", Toast.LENGTH_SHORT).show();
                }
            });
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    janela.dismiss();
                }
            });

            bind() ;
            myBuiler.setView(mView) ;
            janela = myBuiler.create() ;
            janela.show();
        }

        public void bind(){
            text_item.setText(item.getProduto().getDescricao()) ;
            setValores() ;
        }

        private void setValores(){
            DecimalFormat fq = new DecimalFormat("#0.###") ;
            quantidade.setText(fq.format(quantTrans));
        }
    }

}
