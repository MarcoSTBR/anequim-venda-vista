package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.OpcoesFechamentoAdapter;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.builds.BuildCaixa;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildControleAcesso;
import com.anequimplus.conexoes.ConexaoExportarCaixa;
import com.anequimplus.conexoes.ConexaoOpFechamento;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.OpcoesFechamento;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerControleAcesso;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityFechamentoCaixa extends AppCompatActivity {

    private Toolbar toolbar ;
    private Caixa caixa ;
    private RecyclerView lista ;
    private OpcoesFechamento opcoesFechamento ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechamento_caixa);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lista = findViewById(R.id.listaOpcoesFechamento) ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fechamento, menu);
        return true ;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if ((item.getItemId() == R.id.action_fechar)){
            setFechar() ;
            return true ;
        }
        return true ;
    }

    private void setFechar() {
       if (!Configuracao.getPedidoCompartilhado(this)) {
           FilterTables f = new FilterTables();
           f.add(new FilterTable("STATUS", "=", "1"));
           new BuildContaPedido(this, f.getList(), "", new ListenerContaPedido() {
               @Override
               public void ok(List<ContaPedido> l) {
                   if (l.size() >0)
                      alertaErro(l.size()+" Conta(s) Aberta(s)!");
                    else ifFecha();
               }

               @Override
               public void erro(String msg) {
                   alertaErro(msg);

               }
           }).executar();
       } else {
           ifFecha();
       }
    }

    private void ifFecha(){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção")
                .setMessage("Deseja fechar o movimento do caixa?")
                .setCancelable(false)
                .setNegativeButton("Não",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setCaixa() ;
                    }
                }).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCaixa() ;
      //  getFechamentos();
        //getConsultaFechamento() ;
        //carregaImpressora();

    }

    public void setCaixa(){
       if (caixa.getStatus() == 1) {
           new BuildControleAcesso(this, 12, new ListenerControleAcesso() {
               @Override
               public void ok(int usuario_id) {
                   alterarCaixa() ;
               }

               @Override
               public void erro(String msg) {
                   alertaErro(msg); ;
               }
           }).executar();

       } else Toast.makeText(ActivityFechamentoCaixa.this, "Caixa Fechado!", Toast.LENGTH_SHORT).show();

    }

    public void alterarCaixa(){
        caixa.setStatus(2);
        caixa.setDataFechamento(new Date());
        new BuildCaixa(this, caixa, new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                caixa = l.get(0);
               // if (!Configuracao.getPedidoCompartilhado(ActivityFechamentoCaixa.this)) {
               //     setExpotacao();
              //  } else
              Toast.makeText(ActivityFechamentoCaixa.this, "Caixa Fechado!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void erro(String msg) {
                alertaErro(msg);
            }
        }).executar();

    }

    public void setExpotacao(){
        List<Caixa> l = new ArrayList<Caixa>() ;
        //caixa.setStatus(2);
        //caixa.setData(new Date());
        l.add(caixa) ;

        try {
            new ConexaoExportarCaixa(this, l){

                @Override
                public void ok(List<Caixa> l) {
                    Toast.makeText(ActivityFechamentoCaixa.this, "Exportação OK", Toast.LENGTH_SHORT).show();
                    /*
                    for (Caixa c : l){
                        if (c.getUuid().equals(caixa.getUuid())){
                            DaoDbTabela.getCaixaADO(getBaseContext()).alterar(caixa);
                        }
                    }*/
                }

                @Override
                public void erro(String msg) {
                    alertaErro(msg) ;


                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alertaErro(e.getMessage());
        }
    }


    private void getCaixa() {
        FilterTables f = new FilterTables() ;
        f.add(new FilterTable("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(this)))) ;
        f.add(new FilterTable("STATUS", "=", "1")) ;
        //caixa = DaoDbTabela.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this));
        new BuildCaixa(this, f, "", new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                if (l.size()>0) caixa = l.get(0) ;
                if (caixa  == null){
                    new AlertDialog.Builder(ActivityFechamentoCaixa.this)
                            .setIcon(R.drawable.ic_notifications_black_24dp)
                            .setTitle("Atenção")
                            .setMessage("Caixa Fechado!")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                } else {
                    getMenuFechamento() ;
                }
            }

            @Override
            public void erro(String msg) {
                new AlertDialog.Builder(ActivityFechamentoCaixa.this)
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
        }).executar();

    }

    private void getMenuFechamento(){
        try {
            new ConexaoOpFechamento(this, caixa) {
                @Override
                public void oK(Caixa c, List<OpcoesFechamento> list) {
                    //caixa = c ;
                    toolbar.setTitle("Fechamento Caixa");
                    SimpleDateFormat fdate = new SimpleDateFormat("E dd/MM/yyyy hh:mm:ss");
                    toolbar.setSubtitle(fdate.format(caixa.getData()));

                    OpcoesFechamentoAdapter Adapter = new OpcoesFechamentoAdapter(list) ;
                    Adapter.setOnItemClickListener(new OpcoesFechamentoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            setVisualizarRelatorio(position) ;
                        }
                    });
                    lista.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    lista.setHasFixedSize(true);
                    lista.setAdapter(Adapter) ;

                }

                @Override
                public void erro(String msg) {
                    alertaErro(msg) ;

                }

            }.execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alertaErro(e.getMessage()) ;

        }

    }


    private void setVisualizarRelatorio(int position) {
        Log.i("setVisualizarRelatorio", "position "+position) ;
        opcoesFechamento = DaoDbTabela.getOpcoesFechamentoADO(getBaseContext()).getList().get(position) ;
        Intent intent = new Intent(ActivityFechamentoCaixa.this, ActivityImpressao.class);
        Bundle params = new Bundle();
        SimpleDateFormat dt = new SimpleDateFormat("E, dd/MM/yy hh:mm");
        params.putInt("OPCAO_ID", opcoesFechamento.getId());
        params.putInt("CAIXA_ID", caixa.getId());
        params.putString("TITULO", opcoesFechamento.getDescricao());
        params.putString("SUBTITULO", "Abertura "+dt.format(caixa.getData()));

        intent.putExtras(params);
        startActivity(intent) ;
    }


    private void alertaErro(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }


}
