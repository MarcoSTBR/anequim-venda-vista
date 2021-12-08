package com.anequimplus.anequimdroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.ContaPedidoAdapter;
import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoConfiguracaoLio;
import com.anequimplus.conexoes.ConexaoContaPedido;
import com.anequimplus.conexoes.ConexaoImpressaoContaPedido;
import com.anequimplus.conexoes.ConexaoPagamentoConta;
import com.anequimplus.conexoes.ConexaoPagamentoLio;
import com.anequimplus.conexoes.ConexaoServidor;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.tipos.PagamentoStatus;
import com.anequimplus.tipos.TipoModalidade;
import com.anequimplus.utilitarios.UtilSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityConta extends AppCompatActivity {

    private List<String> listContaPedidoSelect ;
    private ListView gradeConta ;
    private ContaPedido contaPedido ;
    private Modalidade modalidade ;
    private Toolbar toolbar ;
    private ConexaoServidor cx = null ;
    private Caixa caixa = null ;
    private static int CONTA_PAGAMENTO = 1 ;
    private static int CONTA_VALOR = 2 ;
    private Spinner spinnerImp ;
    //private List<Impressora> listImpressora = null;
    private Impressora impressoraPadrao ;
    private String clientID ;
    private String accessToken ;
    private ListenerImpressao listenerImpressao ;
    private int orientation ;
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

        listContaPedidoSelect = new ArrayList<String>() ;
        gradeConta = findViewById(R.id.gradeConta);
        spinnerImp     = (Spinner) findViewById(R.id.spinnerImpConta) ;
       // listImpressora = null ;
        orientation = 2 ;
    }

    private void fechamentoPedido() {
        /*
        if ((listContaPedidoSelect.size() > 0) && (impressoraPadrao != null)) {
            //NFCeSetContaPedido
            try {
                new ConexaoNFCe(getBaseContext(), listContaPedidoSelect) {
                    @Override
                    public void setNFCE(NFCe nfce) {
                        Intent intent = new Intent(getBaseContext(), ActivityNFCe.class);
                        Bundle params = new Bundle();
                        params.putString("CHAVE", nfce.getChave());
                        intent.putExtras(params);
                        startActivity(intent);

                    }

                    @Override
                    public void erro(String msg) {
                        alerta("Erro", msg) ;
                    }
                }.execute();
            } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
                e.printStackTrace();
                alerta("Erro", e.getMessage()) ;
            }
        } else {
            if (listContaPedidoSelect.size() == 0)
                alerta("Atenção", "Selecione uma Conta ou Mais Contas!");
            if (impressoraPadrao == null)
                alerta("Atenção", "Selecione uma Impressora!");
        }
     */

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
        for (int i = 0 ; i<spinnerImp.getCount() ; i++){
            spinnerImp.getItemAtPosition(i);
            Impressora impSp = (Impressora)spinnerImp.getItemAtPosition(i) ;
           // Log.i("List Imp","i "+impSp.getDescricao()+" padrao "+descImp) ;
            if (descImp.equals(impSp.getDescricao())) {
                spinnerImp.setSelection(i);
                impressoraPadrao = impSp ;
            }
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
        Log.i("idContaSelecionada", "idContaSelecionada "+idContaSelecionada) ;
            new ConexaoContaPedido(this, idContaSelecionada) {
                @Override
                public void oK() {
                  preencherGrade();
                }

                @Override
                public void erro(String msg) {
                    alert(msg);
                }
            }.execute() ;
    }

    private void pagamentoConta(double v) {
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
    }

    private void receberValor(){
        caixa = Dao.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this)) ;
        if (caixa != null){
            if ((listContaPedidoSelect.size() == 1) && (caixa != null)) {
                contaPedido = Dao.getContaPedidoADO(getBaseContext()).getContaPedido(listContaPedidoSelect.get(0)) ;
                if (contaPedido.getTotalaPagar() > 0) {
                    DecimalFormat frmV = new DecimalFormat("R$  #0.00");
                    double valor = contaPedido.getTotalaPagar();
                    Intent intent = new Intent(getBaseContext(), ActivityValor.class);
                    Bundle params = new Bundle();
                    params.putString("TITULO", "Pagamento da Conta: " + contaPedido.getPedido());
                    params.putString("SUBTITULO", frmV.format(valor));
                    params.putDouble("VALOR", valor);
                    intent.putExtras(params);
                    startActivityForResult(intent, CONTA_VALOR);
                } else alert("Não há saldo devedor!") ;
            } else {
                if (listContaPedidoSelect.size() != 1)
                    alert("Selecione uma conta somente!") ;
                if (caixa == null)
                    alert("Abra o Caixa!") ;
            }
        } else
            alert("Caixa Fechado!") ;
/*
        try {
            new ConexaoCaixa(this, Link.fConsultaCaixa,  0.0){

                @Override
                public void caixaAberto(Caixa c) {
                    caixa = c ;
                    if ((listContaPedidoSelect.size() == 1) && (caixa != null)) {
                        contaPedido = Dao.getContaPedidoADO(getBaseContext()).getContaPedido(listContaPedidoSelect.get(0)) ;
                        if (contaPedido.getTotalaPagar() > 0) {
                            DecimalFormat frmV = new DecimalFormat("R$  #0.00");
                            double valor = contaPedido.getTotalaPagar();
                            Intent intent = new Intent(getBaseContext(), ActivityValor.class);
                            Bundle params = new Bundle();
                            params.putString("TITULO", "Pagamento da Conta: " + contaPedido.getPedido());
                            params.putString("SUBTITULO", frmV.format(valor));
                            params.putDouble("VALOR", valor);
                            intent.putExtras(params);
                            startActivityForResult(intent, CONTA_VALOR);
                        } else alert("Não há saldo devedor!") ;
                    } else {
                        if (listContaPedidoSelect.size() != 1)
                            alert("Selecione uma conta somente!") ;
                        if (caixa == null)
                            alert("Abra o Caixa!") ;
                    }
                }

                @Override
                public void caixaFechado(String msg) {
                    alert(msg) ;
                }

                @Override
                public void erro(String msg) {
                    alert(msg) ;
                }
            }.execute() ;
        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage()) ;
        }
*/
    }

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


    private void alerta(String titulo, String txt){
         new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle(titulo)
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    private void setImprimirPedido(){
        if ((listContaPedidoSelect.size() > 0) && (impressoraPadrao != null)) {
            //contaPedido = Dao.getContaPedidoADO(getBaseContext()).getContaPedido(listContaPedidoSelect.get(0));
            try {
                 new ConexaoImpressaoContaPedido(this, impressoraPadrao, getListPedidos()) {
                    @Override
                    public void oK() {
                        Toast.makeText(getBaseContext(),"Impressão OK", Toast.LENGTH_LONG) ;
                    }

                    @Override
                    public void erro(String mgg) {
                        alerta("Erro na Impressão", mgg);
                    }
                }.execute() ;
            } catch (MalformedURLException | JSONException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
                e.printStackTrace();
                alerta("Erro na Impressão", e.getMessage());
            }


        } else {
            if (listContaPedidoSelect.size() == 0)
                alerta("Atenção", "Selecione uma Conta ou Mais Contas!");
            if (impressoraPadrao == null)
                alerta("Atenção", "Selecione uma Impressora!");
        }
    }

    private List<ContaPedido> getListPedidos(){
       List<ContaPedido> l = new ArrayList<ContaPedido>() ;
       for (String s : listContaPedidoSelect){
           l.add(Dao.getContaPedidoADO(getBaseContext()).getContaPedido(s)) ;
       }
      return l ;
    }

    private void alert(String t){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(t)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();

    }

    public void preencherGrade() {
       // Toast.makeText(this, "Itens "+Dao.getContaPedidoADO(this).getList().size(), Toast.LENGTH_LONG).show();
        gradeConta.setAdapter(new ContaPedidoAdapter(this, Dao.getContaPedidoADO(this).getList(),
                listContaPedidoSelect, orientation) {
            @Override
            protected void chagoCheckBox() {
                setSubtitleTotal();
            }
        });
        setSubtitleTotal();

      }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientation = newConfig.orientation ;
        int r = orientation ;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "Landscape Mode "+orientation+" "+r, Toast.LENGTH_SHORT).show();
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "Portrait Mode "+orientation+" "+r, Toast.LENGTH_SHORT).show();
        }
        preencherGrade() ;
    }

}
