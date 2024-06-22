package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.exportacao.ExportacaoAdapter;
import com.anequimplus.exportacao.ExportacaoCabNotaSaida;
import com.anequimplus.exportacao.ExportacaoCaixaV13;
import com.anequimplus.exportacao.ExportacaoCaixaV14;
import com.anequimplus.exportacao.ExportacaoContaPedido;
import com.anequimplus.exportacao.ExportacaoContaPedidoCancel;
import com.anequimplus.exportacao.ExportacaoContaPedidoTranferencia;
import com.anequimplus.exportacao.ExportacaoPedidoV13;
import com.anequimplus.exportacao.ExportacaoTranferenciaV13;
import com.anequimplus.exportacao.ListenerExportacao;
import com.anequimplus.exportacao.ParamExportacao;
import com.anequimplus.exportacao.RegExport;
import com.anequimplus.exportacao.RegExportClass;
import com.anequimplus.exportacao.TipoExportacao;

import java.util.ArrayList;
import java.util.List;

public class ActivityUpLoad extends AppCompatActivity {

    private Toolbar toolbar ;
    private Button exportar;
    private Button limpar_envio ;
    private RecyclerView listagem ;

    private ListenerExportacao listenerExportacao ;

    private static boolean flagOk = true ;

    private List<ParamExportacao> listreg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load);
        toolbar = findViewById(R.id.toolbarExportar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        exportar        = findViewById(R.id.exportar_exportar);
        exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envio_exportacao() ;
            }
        });
        limpar_envio    = findViewById(R.id.exportar_limpar);
        limpar_envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpar_registros_envio() ;
            }
        });
        listagem        = findViewById(R.id.grade_exportacao);
        repostas() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificaSeCaixasFechados() ;
    }

    private void verificaSeCaixasFechados(){
        FilterTables filters = new FilterTables();
        filters.add(new FilterTable("STATUS", "=", "1"));
         if (DaoDbTabela.getCaixaADO(this).getList(filters.getList()).size() == 0)
            carregarList() ;
        else alertSairOK("Feche o Caixa!") ;
    }

    private void carregarList() {
        flagOk = true ;
        limpar_envio.setVisibility(View.INVISIBLE);
        listreg   = new ArrayList<ParamExportacao>() ;
        listreg.add(new ExportacaoCaixaV13(this, new RegExportClass(TipoExportacao.teCaixaAfoodV13 , "", null), listenerExportacao)) ;
        listreg.add(new ExportacaoCaixaV14(this, new RegExportClass(TipoExportacao.teCaixaAfoodV14, "", null), listenerExportacao)) ;
        listreg.add(new ExportacaoCabNotaSaida(this, new RegExportClass(TipoExportacao.teNotas13, "", null), listenerExportacao)) ;
        listreg.add(new ExportacaoPedidoV13(this, new RegExportClass(TipoExportacao.tePedidoV13, "", null), listenerExportacao)) ;
        listreg.add(new ExportacaoContaPedido(this, new RegExportClass(TipoExportacao.tePedidoV14, "", null), listenerExportacao)) ;
        listreg.add(new ExportacaoTranferenciaV13(this, new RegExportClass(TipoExportacao.teTransferenciaV13, "", null), listenerExportacao)) ;
        listreg.add(new ExportacaoContaPedidoTranferencia(this, new RegExportClass(TipoExportacao.teTransferenciaV14, "", null), listenerExportacao)) ;
        listreg.add(new ExportacaoContaPedidoCancel(this, new RegExportClass(TipoExportacao.teCancelamento14, "", null), listenerExportacao)) ;
        listagem.setAdapter(new ExportacaoAdapter(this, listreg));
    }

    private void repostas(){
        listenerExportacao = new ListenerExportacao() {
            @Override
            public void ok(RegExport regExport) {
                //regExport.getProgressDisplay().progress(10,10);
                if (regExport.getTipoExportacao() == TipoExportacao.teCancelamento14) {
                    if (flagOk)
                       limpar_registros_envio() ;
                     else
                      Toast.makeText(ActivityUpLoad.this, "Houve Erro no Envio!, Tente Novamente ou Chame o Suporte Técnica", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void erro(RegExport regExport, int cod, String msg) {
                flagOk = false ;
                alert("COD "+cod+"  "+regExport.getTipoExportacao().valor+"  "+msg) ;
            }
        };
    }


    private void executarList(){
        carregarList();
        for (ParamExportacao i : listreg) {
            i.executar();
        }
    }

    private void envio_exportacao() {
        executarList() ;
    }

    private void limpar_registros_envio() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage("Movimento Enviados Com Sucesso, Deseja Limpar os Dados?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DaoDbTabela.getContaPedidoInternoDAO(ActivityUpLoad.this).excluir();
                        DaoDbTabela.getContaPedidoItemInternoDAO(ActivityUpLoad.this).excluir();
                        DaoDbTabela.getContaPedidoPagamentoADO(ActivityUpLoad.this).excluir();
                        DaoDbTabela.getContaPedidoItemCancelamentoDAO(ActivityUpLoad.this).excluir();
                        DaoDbTabela.getTransferenciaDAO(ActivityUpLoad.this).excluir();
                        DaoDbTabela.getSangriaADO(ActivityUpLoad.this).excluir();
                        DaoDbTabela.getSuprimentoADO(ActivityUpLoad.this).excluir();
                        alertSairOK("Exportação Enviada Com Sucesso!") ;
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertSairOK("Exportação Enviada Com Sucesso!") ;
                    }
                })
                .show();
    }


    private void alert(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Continuar", null)
                .show();
    }

    private void alertSairOK(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_up_load_exportacao, menu);
        return true ;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.exportar_limpar){
            Toast.makeText(this, "Limpar Dados...", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


}