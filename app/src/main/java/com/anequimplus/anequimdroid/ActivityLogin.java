package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.builds.BuildUsuarioAcesso;
import com.anequimplus.conexoes.ConexaoAcompanhamento;
import com.anequimplus.conexoes.ConexaoAcompanhamentoItem;
import com.anequimplus.conexoes.ConexaoAcompanhamentoProduto;
import com.anequimplus.conexoes.ConexaoConfTerminal;
import com.anequimplus.conexoes.ConexaoGradeVendas;
import com.anequimplus.conexoes.ConexaoImpressoras;
import com.anequimplus.conexoes.ConexaoModalidade;
import com.anequimplus.conexoes.ConexaoProdutos;
import com.anequimplus.conexoes.ConexaoUsuario;
import com.anequimplus.controler.ControleLogin;
import com.anequimplus.entity.Acompanhamento;
import com.anequimplus.entity.Acompanhamento_Item;
import com.anequimplus.entity.Acompanhamento_produto;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Terminal;
import com.anequimplus.entity.Usuario;
import com.anequimplus.entity.UsuarioAcesso;
import com.anequimplus.listeners.ListenerAcompanhamento;
import com.anequimplus.listeners.ListenerAcompanhamentoItem;
import com.anequimplus.listeners.ListenerAcompanhamentoProduto;
import com.anequimplus.listeners.ListenerUsuario;
import com.anequimplus.listeners.ListenerUsuarioAcesso;
import com.anequimplus.utilitarios.UtilSet;

import java.net.MalformedURLException;
import java.util.List;

public class ActivityLogin extends AppCompatActivity {

    private EditText editTextUsuario ;
    private EditText editTextSenha ;
    private TextView TextViewLoja ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);
        TextViewLoja = (TextView) findViewById(R.id.TextViewLoja) ;
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextSenha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        logar();
                        return true;
                    }

                }
                return false;
            }
        });
        setResult(RESULT_CANCELED);

    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextUsuario.setText(UtilSet.lerParamString(getBaseContext(), "USUARIO_TEMP"));
        editTextSenha.setText("");
        TextViewLoja.setText(UtilSet.getLojaNome(getBaseContext()));
        setUsuarios();
    }

    private void logar() {
      if (editTextUsuario.getText().equals("") || editTextSenha.equals("")){
          alert("Preencha os Campos Corretamente!");
      } else {
          new ControleLogin(this) {
              @Override
              public void ok(Usuario u) {
                  UtilSet.setLogin(ActivityLogin.this, u.getLogin());
                  UtilSet.setUsuarioId(ActivityLogin.this, u.getId());
                  UtilSet.setUsuarioNome(ActivityLogin.this, u.getNome());
                  setTerminal() ;
              }

              @Override
              public void erro(String msg) {
                  alert(msg);
              }
          }.login(editTextUsuario.getText().toString(), editTextSenha.getText().toString());
      }
    }

    private void alert(String text){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro:")
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("Ok",null).show();
    }


    private void sairAlert(String text){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }


    private void setTerminal(){
        String mac = UtilSet.getMAC(this) ;
        new ConexaoConfTerminal(this, mac) {
            @Override
            public void ok(Terminal t) {
                setProdutos();
              //  setAcessos() ;

            }

            @Override
            public void erroMsg(int cod, String msg) {
                startActivity(new Intent(getBaseContext(), ActivityTerminal.class));
            }
        }.execute();
    }

    private void setUsuarios(){
        new ConexaoUsuario(this, new FilterTables(), "", new ListenerUsuario() {
            @Override
            public void ok(List<Usuario> l) {
                DaoDbTabela.getUsuarioADO(ActivityLogin.this).inserirList(l) ;
                setAcessos() ;
            }

            @Override
            public void erro(int cod, String msg) {

            }
        }).executar();
    }

    private void setAcessos(){
        FilterTables filters = new FilterTables() ;
      //  String idusu = String.valueOf(UtilSet.getUsuarioId(this)) ;
      //  filters.add(new FilterTable("ACESSO_USUARIO_ID", "=", idusu));
     //   Log.i("login", "usuario "+idusu) ;
        new BuildUsuarioAcesso(this, filters, "ACESSO_USUARIO_ID", new ListenerUsuarioAcesso() {
            @Override
            public void ok(List<UsuarioAcesso> l) {
                DaoDbTabela.getUsuarioAcessoADO(ActivityLogin.this).setlist(l) ;

            }

            @Override
            public void erro(String msg) {
                alertContinuacao(msg);
            }
        }).executar();

    }


    private void setProdutos() {
        new ConexaoProdutos(this){

            @Override
            public void Ok() {
                setAcompanhamantoProduto() ;
                //setImpressoras() ;
            }

            @Override
            public void erro(String msg) {
                alertContinuacao(msg); ;
            }
        }.execute() ;
    }

    private void setAcompanhamantoProduto(){
        try {
            new ConexaoAcompanhamentoProduto(this, new FilterTables(), "", new ListenerAcompanhamentoProduto() {
                @Override
                public void ok(List<Acompanhamento_produto> l) {
                    setAcompanhamento();
                }

                @Override
                public void erro(int cod, String msg) {
                    alertContinuacao(msg) ;
                }
            }).execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alertContinuacao(e.getMessage()) ;
        }
    }
    private void setAcompanhamento(){
        try {
            new ConexaoAcompanhamento(this, new FilterTables(), "", new ListenerAcompanhamento() {
                @Override
                public void ok(List<Acompanhamento> l) {
                    setAcompanhamentoItem() ;
                }

                @Override
                public void erro(int cod, String msg) {
                    alertContinuacao(msg) ;
                }
            }).execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alertContinuacao(e.getMessage()) ;
        }
    }

    private void setAcompanhamentoItem(){
        try {
            new ConexaoAcompanhamentoItem(this, new FilterTables(), "", new ListenerAcompanhamentoItem() {
                @Override
                public void ok(List<Acompanhamento_Item> l) {
                    setImpressoras() ;
                }

                @Override
                public void erro(int cod, String msg) {
                    alertContinuacao(msg) ;
                }
            }).execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alertContinuacao(e.getMessage()) ;
        }

    }


    public void setImpressoras(){
        new ConexaoImpressoras(this){

            @Override
            public void Ok() {
                setGrade();
            }

            @Override
            public void erroMensagem(String msg) {
                alertContinuacao(msg) ;
            }
        }.execute() ;

    }
    public void setGrade(){
        new ConexaoGradeVendas(this){

            @Override
            public void oK() {
                setModalidades() ;
            }

            @Override
            public void erro(String msg) {
                alertContinuacao(msg); ;
            }
        }.execute() ;


    }

    public void setModalidades(){
        new ConexaoModalidade(this){
            @Override
            public void oK() {
                sairAlert("Usuário Logado!") ;
               //finish();

            }

            @Override
            public void erro(String msg) {
                alertContinuacao(msg) ;
            }
        }.execute();

    }

    private void alertContinuacao(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).
                setNegativeButton("Repetir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTerminal();
                            }
                        }
                ).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_login_autentic) {
            startActivity(new Intent(getBaseContext(), ActivityAutenticacao.class));
            return true;
        }
        if (item.getItemId() == R.id.action_login_ok) {
            logar() ;
            return true;
        }
        return true ;//super.onOptionsItemSelected(item);
    }


}


