package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.conexoes.ConexaoAutenticacao;
import com.anequimplus.conexoes.ConexaoCNPJ;
import com.anequimplus.utilitarios.UtilSet;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class ActivityAutenticacao extends AppCompatActivity {

    private EditText editTextCnpj ;
    private EditText editTextLogin ;
    private EditText editTextPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextCnpj = (EditText) findViewById(R.id.editTextCNPJ) ;
        editTextLogin = (EditText) findViewById(R.id.editTextLogin) ;
        editTextPassword = (EditText) findViewById(R.id.editTextPassword) ;
        /*
        editTextCnpj.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        return true;
                    }

                }
                return false;
            }
        });

         */
        setResult(RESULT_CANCELED, getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextCnpj.setText(UtilSet.getCnpj(this));
        editTextLogin.setText("");
        editTextPassword.setText("");
        //editTextLogin.setText(UtilSet.getLogin(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_autenticacao_antigo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            alertOK();
        }
        if (item.getItemId() == R.id.action_aut_ok) {
            verificaCNPJ();
            return true ;
        }

        if (item.getItemId() == R.id.action_aut_scan) {
            setQRCode() ;
            return true ;
        }

        return true ;
        //return super.onOptionsItemSelected(item);
    }

    private void setAutenticacao() {
        try {
            new ConexaoAutenticacao(this, editTextCnpj.getText().toString(), editTextLogin.getText().toString(), editTextPassword.getText().toString()) {

                    @Override
                    public void oK(String cnpj, String login) {
                        sairOk();
                    }

                    @Override
                    public void erro(int cod, String msg) {
                        alert("Erro="+cod,msg) ;
                    }
                }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            setCnpjRepetir(e.getMessage()) ;
        }

    }

    private void sairOk() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Limpar Dados")
                .setMessage("Deseja Limpar os Dados Internos?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DBHelper(ActivityAutenticacao.this).limparTudo(getBaseContext());
                        finish();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show() ;

    }

    private void setCnpjRepetir(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Repetir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setAutenticacao() ;
                    }
                })
                .setNegativeButton("Finalizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show() ;
    }

    private void verificaCNPJ(){

        new ConexaoCNPJ(this, editTextCnpj.getText().toString()) {

            @Override
            public void erro(int cod, String msg) {
                alert("Erro ", msg);
            }

            @Override
            public void oK(String cnpj, String url) {
                setAutenticacao() ;

            }
        }.execute() ;

    }

    private void alert(String Titulo, String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle(Titulo)
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    private void setQRCode(){
        IntentIntegrator integrator = new IntentIntegrator(this) ;
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES) ;
        integrator.setPrompt("Autenticação") ;
        integrator.setCameraId(0) ;
        integrator.initiateScan();
    }

    private void getQRCode(String res){
        try {
            JSONObject j = new JSONObject(res);
            UtilSet.setChave(getBaseContext(),j.getString("A1")) ;
            editTextCnpj.setText(j.getString("A20"));
        } catch (JSONException e) {
            e.printStackTrace();
            alert("Erro:","QRCode não reconhecido!");
        }

    }

    private void alertOK() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Chave encontrada")
                .setMessage(editTextCnpj.getText().toString())
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(RESULT_OK, getIntent());
                        finish();
                    }
                }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data) ;
        if (result != null){
            if (result.getContents() != null){
                getQRCode(result.getContents()) ;
                //  Toast.makeText(getBaseContext(),result.getContents(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(),"Nenhum resultado", Toast.LENGTH_SHORT).show();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
