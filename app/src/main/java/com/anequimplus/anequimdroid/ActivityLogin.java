package com.anequimplus.anequimdroid;

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

import com.anequimplus.conexoes.ConexaoLogin;
import com.anequimplus.utilitarios.UtilSet;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextUsuario.setText(UtilSet.lerParamString(getBaseContext(), "USUARIO_TEMP"));
        editTextSenha.setText("");
        TextViewLoja.setText(UtilSet.getLojaNome(getBaseContext()));
    }

    private void logar() {
      if (editTextUsuario.getText().equals("") || editTextSenha.equals("")){
          alert("Preencha os Campos Corretamente");
      } else {
                 new ConexaoLogin(this, editTextUsuario.getText().toString(), editTextSenha.getText().toString()) {
                      @Override
                      public void Ok(int code) {

                          finish() ;
                      }

                      @Override
                      public void erro(int code, String msg) {
                          alert(msg) ;
                      }
                  }.execute();
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


