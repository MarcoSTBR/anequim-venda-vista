package com.anequimplus.anequimdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.MenuPrincipalAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoCaixa;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.OpcaoMenuPrincipal;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import java.net.MalformedURLException;
import java.util.List;

public class ActivePrincipal extends AppCompatActivity {

   private ListView gradePrimcipal ;
   private List<OpcaoMenuPrincipal> listOpcoesPricipais ;
   private static int ABERTURACAIXA = 3 ;
   private static int VERIFICARCHAVE = 4 ;
   private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);
        gradePrimcipal = findViewById(R.id.menuPrincipal);
        gradePrimcipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (listOpcoesPricipais.get(i).getId()) {
                    case 1:
                        startActivity(new Intent(getBaseContext(), ActivityPedido.class));
                        break;
                    case 2:
                        startActivity(new Intent(getBaseContext(), ActivityConta.class));
                        break;
                    case 3:
                        abrirFecharCaixa() ;
                        break;
                    case 4:
                        startActivity(new Intent(getBaseContext(), ActivityVendaVista.class));
                        break;
                }
            }
        });

    }

    public void abrirFecharCaixa(){
        try {
            new ConexaoCaixa(this, Link.fConsultaCaixa, 0.00) {
                @Override
                public void caixaAberto(Caixa caixa) {
                    startActivity(new Intent(getBaseContext(), ActivityFechamentoCaixa.class));
                }

                @Override
                public void caixaFechado(String msg) {
                    Intent intent = new Intent(getBaseContext(), ActivityValor.class);
                    Bundle params = new Bundle();
                    params.putString("TITULO","Abertura de Caixa");
                    params.putString("SUBTITULO","Valor do Suprimento");
                    params.putDouble("VALOR", 0);
                    intent.putExtras(params);
                    startActivityForResult(intent, ABERTURACAIXA);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ((UtilSet.getChave(getBaseContext()) == null) || (UtilSet.getChave(getBaseContext()).length() == 0)){
            startActivityForResult(new Intent(getBaseContext(), ActivityAutenticacaoAntigo.class), VERIFICARCHAVE);
        } else {
            if (!UtilSet.logado(getBaseContext())) {
                startActivity(new Intent(getBaseContext(), ActivityLogin.class));
            }
        }
        toolbar.setSubtitle(UtilSet.getNome_Usuario(getBaseContext())) ;
        iniciarMenu() ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ABERTURACAIXA){
            if (resultCode == RESULT_OK) {
                double valor = data.getDoubleExtra("VALOR", 0.0) ;
                try {
                    new ConexaoCaixa(this, Link.fAberturaCaixa, valor) {
                        @Override
                        public void caixaAberto(Caixa caixa) {
                            Toast.makeText(getBaseContext(), "Caixa Aberto!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void caixaFechado(String msg) {
                            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
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
            }
        }
        if (requestCode == VERIFICARCHAVE){
            if (resultCode != RESULT_OK){
                finish();
            } else {
                startActivity(new Intent(getBaseContext(), ActivityConfiguracao.class));
            }
        }
    }


    private void iniciarMenu(){
        toolbar.setSubtitle(UtilSet.getNome_Usuario(getBaseContext()));
        listOpcoesPricipais = Dao.getOpcoesPrincipalADO(getBaseContext()).getList() ;
        gradePrimcipal.setAdapter(new MenuPrincipalAdapter(getBaseContext(),
                listOpcoesPricipais));

    }


    private void alert(String message) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro:")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    private void setAlert(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Alerta")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("OK",null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_conf) {
            startActivity(new Intent(getBaseContext(), ActivityConfiguracao.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            startActivity(new Intent(getBaseContext(), ActivityLogin.class));
            return true;
        }
        if (item.getItemId() == R.id.action_conf_autenticacao) {
            startActivity(new Intent(getBaseContext(), ActivityAutenticacaoAntigo.class));
            return true;
        }


        return true ;//super.onOptionsItemSelected(item);
    }


}
