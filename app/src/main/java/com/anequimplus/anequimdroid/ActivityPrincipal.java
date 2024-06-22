package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.MenuPrincipalAdapter;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.builds.BuildCaixa;
import com.anequimplus.builds.BuildControleAcesso;
import com.anequimplus.controler.ControleAutenticacao;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.listeners.ListenerControleAcesso;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.OpcaoMenuPrincipal;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import java.util.Date;
import java.util.List;

public class ActivityPrincipal extends AppCompatActivity {

    private ListView gradePrimcipal;
    private List<OpcaoMenuPrincipal> listOpcoesPricipais;
    private static int ABERTURACAIXA = 3;
    private Toolbar toolbar;
    private Caixa caixa = null;
    private TextView usuario_logado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        toolbar = findViewById(R.id.toolbarPrincipal);
        // toolbar.setTitle("Usuário: "+UtilSet.getUsuarioNome(this));
        usuario_logado = findViewById(R.id.usuario_logado);
        usuario_logado.setText("Usuário: " + UtilSet.getUsuarioNome(this));
        //toolbar.setSubtitle("Usuário: "+UtilSet.getUsuarioNome(this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
        gradePrimcipal = findViewById(R.id.menuPrincipal);
        gradePrimcipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (listOpcoesPricipais.get(i).getId()) {
                    case 1:
                        // startActivity(new Intent(ActivityPrincipal.this, ActivityDF.class));
                        break;
                    case 2:
                        entraPedido();
                        break;
                    case 3:
                        abrirFecharCaixa();
                        break;
                    case 4:
                        suprimentoSangria();
                        break;
                    case 5:
                        startActivity(new Intent(ActivityPrincipal.this, ActivityUpLoad.class));
                        break;
                }
            }
        });
    }

    private void suprimentoSangria() {
        getCaixa(1);
        //  Caixa caixa = DaoDbTabela.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this)) ;
    }

    private void addSuprimento() {
        Log.i("usuario_id", "id = " + UtilSet.getUsuarioId(this));
        if (caixa != null) {
            Intent intent = new Intent(ActivityPrincipal.this, ActivitySuprimentoSangria.class);
            Bundle params = new Bundle();
            params.putString("TITULO", "Suprimento Sangria");
            params.putString("SUBTITULO", "Valor do Suprimento e Sangra");
            params.putDouble("VALOR", 0);
            intent.putExtras(params);
            startActivity(intent);
            //startActivityForResult(intent, ABERTURACAIXA);
        } else {
            Toast.makeText(this, "Caixa Fechado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void entraPedido() {
        if (!Configuracao.getPedidoCompartilhado(this)) {
            entrandoPedido();
/*
            if (caixa != null) {
                startActivity(new Intent(this, ActivityContaList.class));
            } else alert("Caixa Fechado, Abra o Caixa!");*/
        } else {
            startActivity(new Intent(this, ActivityContaList.class));
        }

    }

    private void entrandoPedido() {
        FilterTables f = new FilterTables();
        f.add(new FilterTable("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(this))));
        f.add(new FilterTable("STATUS", "=", "1"));
        new BuildCaixa(this, f, "", new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                if (l.size() > 0) {
                    caixa = l.get(0);
                    startActivity(new Intent(ActivityPrincipal.this, ActivityContaList.class));
                } else alert("Caixa Fechado, Abra o Caixa!");
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
    }

    private void addAbrirFecharCaixa() {
        Log.i("usuario_id", "id = " + UtilSet.getUsuarioId(this));
        if (caixa == null) {
            new BuildControleAcesso(this, 1, new ListenerControleAcesso() {
                @Override
                public void ok(int usuario_id) {
                    abrirCaixa();
                }

                @Override
                public void erro(String msg) {
                    alert(msg);
                }
            }).executar();
        } else {
            startActivity(new Intent(this, ActivityFechamentoCaixa.class));
        }
    }

    private void abrirCaixa() {
        if (!Configuracao.getPedidoCompartilhado(this)) {
            FilterTables f = new FilterTables();
            f.add(new FilterTable("STATUS", "=", "1"));
            new BuildCaixa(this, f, "", new ListenerCaixa() {
                @Override
                public void ok(List<Caixa> l) {
                    if (l.size() > 0)
                        alert("Existe [" + l.size() + "] caixa(s) aberto(s)");
                    else
                        getValorCaixa();
                }

                @Override
                public void erro(String msg) {
                    alert(msg);
                }
            }).executar();

        } else {
            getValorCaixa();
        }
    }

    private void getValorCaixa() {
        Intent intent = new Intent(ActivityPrincipal.this, ActivityValor.class);
        Bundle params = new Bundle();
        params.putString("TITULO", "Abertura de Caixa");
        params.putString("SUBTITULO", "Valor do Suprimento");
        params.putDouble("VALOR", 0);
        intent.putExtras(params);
        startActivityForResult(intent, ABERTURACAIXA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ABERTURACAIXA) {
            if (resultCode == RESULT_OK) {
                double valor = data.getDoubleExtra("VALOR", 0.0);
                setCaixa(valor);
            }
        }
    }

    public void abrirFecharCaixa() {
        //Toast.makeText(this,"Abrir", Toast.LENGTH_SHORT).show();
//        Caixa caixa = DaoDbTabela.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this)) ;
        getCaixa(2);
    }

    private void getCaixa(int op) {
        FilterTables f = new FilterTables();
        f.add(new FilterTable("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(this))));
        f.add(new FilterTable("STATUS", "=", "1"));
        new BuildCaixa(this, f, "", new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                if (l.size() > 0) {
                    caixa = l.get(0);
                } else caixa = null;
                switch (op) {
                    case 1:
                        addSuprimento();
                        break;
                    case 2:
                        addAbrirFecharCaixa();
                        break;
                    default:
                        alert("Opção Inválida");
                }
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        usuario_logado.setText("Usuário: " + UtilSet.getUsuarioNome(this));
        //toolbar.setSubtitle(UtilSet.getUsuarioNome(this)) ;
        iniciarMenu();
        verificarToken();

    }

    private void verificarToken() {
        new ControleAutenticacao(this) {
            @Override
            public void tokenOk() {
                //Toast.makeText(ActivityPrincipal.this, "tokenOk()", Toast.LENGTH_SHORT).show();
                verificarLogin();
            }

            @Override
            public void autenticar(String msg) {
                Toast.makeText(ActivityPrincipal.this, msg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivityPrincipal.this, ActivityAutenticacao.class));
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }

        }.verificarAutenticacao();
    }

    public void verificarLogin() {
        if (UtilSet.getUsuarioId(ActivityPrincipal.this) == 0)
            startActivity(new Intent(ActivityPrincipal.this, ActivityLogin.class));

    }

    private void erroNoToken(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro:")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ActivityPrincipal.this, ActivityLogin.class));
                    }
                }).show();


    }

    private void setCaixa(double v) {
        Modalidade modalidade = null;
        Date data = new Date();
        Caixa caixa = new Caixa(0, UtilSet.getUUID(), data, data, UtilSet.getUsuarioId(this), 1, v);
        new BuildCaixa(this, caixa, new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                Toast.makeText(ActivityPrincipal.this, "Caixa Aberto", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void erro(String msg) {

            }
        }).executar();
    }

    private void iniciarMenu() {
        toolbar.setSubtitle(UtilSet.getUsuarioNome(this));
        listOpcoesPricipais = DaoDbTabela.getOpcoesPrincipalADO(this).getList();
        gradePrimcipal.setAdapter(new MenuPrincipalAdapter(this, listOpcoesPricipais));
    }

    private void alert(String message) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro:")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_conf) {
            startActivity(new Intent(ActivityPrincipal.this, ActivityConfiguracao.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            startActivity(new Intent(ActivityPrincipal.this, ActivityLogin.class));
            return true;
        }
        if (item.getItemId() == R.id.action_conf_autenticacao) {
            startActivity(new Intent(ActivityPrincipal.this, ActivityAutenticacao.class));
            return true;
        }
        return true;//super.onOptionsItemSelected(item);
    }
}
