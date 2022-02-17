package com.anequimplus.anequimdroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.anequimplus.conexoes.ConexaoConfTerminal;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.OpcaoMenuPrincipal;
import com.anequimplus.entity.Terminal;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.TokenSet;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityPrincipal extends AppCompatActivity {

   private ListView gradePrimcipal ;
   private List<OpcaoMenuPrincipal> listOpcoesPricipais ;
   private static int ABERTURACAIXA = 3 ;
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
                        startActivity(new Intent(getBaseContext(), ActivityContaList.class));
                        break;
                    case 3:
                        abrirFecharCaixa() ;
                        break;
                }
            }
        });

    }

    public void abrirFecharCaixa(){
        //Toast.makeText(this,"Abrir", Toast.LENGTH_SHORT).show();
        Caixa caixa = Dao.getCaixaADO(getBaseContext()).getCaixaAberto(UtilSet.getUsuarioId(getBaseContext())) ;

        if (caixa == null) {
            Intent intent = new Intent(getBaseContext(), ActivityValor.class);
            Bundle params = new Bundle();
            params.putString("TITULO","Abertura de Caixa");
            params.putString("SUBTITULO","Valor do Suprimento");
            params.putDouble("VALOR", 0);
            intent.putExtras(params);
            startActivityForResult(intent, ABERTURACAIXA);
        } else {
            startActivity(new Intent(getBaseContext(), ActivityFechamentoCaixa.class));
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Usuário: "+UtilSet.getUsuarioNome(this)) ;
        //toolbar.setSubtitle(UtilSet.getUsuarioNome(this)) ;
        iniciarMenu() ;
        verificaToken() ;

    }

    public void verificaToken(){
        try {
            TokenSet.validate(getBaseContext()) ;
        } catch (TokenSet.ExceptionTokenExpirado e) {
            e.printStackTrace();
            startActivity(new Intent(getBaseContext(), ActivityLogin.class));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ABERTURACAIXA){
            if (resultCode == RESULT_OK) {
                double valor = data.getDoubleExtra("VALOR", 0.0);
                setCaixa(valor);
            }
        }
    }

    private void setCaixa(double v){
        Modalidade modalidade = null ;
        Date data = new Date() ;
        Caixa caixa = new Caixa(0, UtilSet.getUUID(), data, data, UtilSet.getUsuarioId(this),1, v) ;
        Dao.getCaixaADO(this).setCaixa(caixa);
        Toast.makeText(this, "Caixa Aberto", Toast.LENGTH_SHORT).show();
        Log.i("Caixa", caixa.getJson().toString()) ;
        enviarCaixa(caixa);
    }

    private void enviarCaixa(Caixa caixa){

        List<Caixa> caixas = new ArrayList<Caixa>() ;
        caixas.add(caixa) ;
        try {
            new ConexaoCaixa(this, caixas) {

                @Override
                public void Ok(List<Caixa> l) {
                  //  Dao.getCaixaADO(getBaseContext()).caixa_recebido(l);
                    Toast.makeText(getBaseContext(), "Caixa Enviado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void erro(String msg) {
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

                }
            }.execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage());
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
            startActivity(new Intent(getBaseContext(), ActivityAutenticacao.class));
            return true;
        }
        return true ;//super.onOptionsItemSelected(item);
    }


}
