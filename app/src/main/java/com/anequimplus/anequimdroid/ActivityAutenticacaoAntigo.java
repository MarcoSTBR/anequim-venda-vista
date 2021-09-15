package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.LojaAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoAutenticacao;
import com.anequimplus.conexoes.ConexaoConfTeminal;
import com.anequimplus.conexoes.ConexaoLoja;
import com.anequimplus.entity.Loja;
import com.anequimplus.utilitarios.UtilSet;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class ActivityAutenticacaoAntigo extends AppCompatActivity {

    private EditText editTextCnpj ;
    private Spinner spinnerLoja ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao_antigo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinnerLoja = (Spinner) findViewById(R.id.spinnerLoja) ;
        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Loja lj = Dao.getLojaADO(getBaseContext()).getList().get(i) ;
                Toast.makeText(getBaseContext(),lj.getNome(),Toast.LENGTH_SHORT).show();
                //UtilSet.setImpPadraoNFCe(getBaseContext(), impressoraPadrao.getDescricao()) ;
                setlojaPadrao(lj) ;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editTextCnpj = (EditText) findViewById(R.id.editTextCNPJ) ;
        editTextCnpj.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        setCnpj();

                        return true;
                    }

                }
                return false;
            }
        });

        setResult(RESULT_CANCELED, getIntent());
    }

    private void setlojaPadrao(Loja l) {
        UtilSet.setLojaId(this, l) ;
        Toast.makeText(getBaseContext(), "Clique em OK para processar", Toast.LENGTH_SHORT).show();

    }

    private void getlojaPadrao(Loja l) {
        //int p = Dao.getLojaADO(getBaseContext()).getLoja(UtilSet.getLojaId(this)) ;
        if (l != null) {
            for (int i = 0; i < spinnerLoja.getCount(); i++) {
                spinnerLoja.getItemAtPosition(i);
                Loja impSp = (Loja) spinnerLoja.getItemAtPosition(i);
                // Log.i("List Imp","i "+impSp.getDescricao()+" padrao "+descImp) ;
                if (l.getNome().equals(impSp.getNome())) {
                    spinnerLoja.setSelection(i);
                }
            }
        } else Toast.makeText(getBaseContext(),"Selecione a Loja", Toast.LENGTH_SHORT).show();

      //  UtilSet.setLojaId(this, l) ;

    }


    @Override
    protected void onResume() {
        super.onResume();
        editTextCnpj.setText(UtilSet.getCnpj(getBaseContext()));
        spinnerLoja.setAdapter(new LojaAdapter(getBaseContext(), Dao.getLojaADO(getBaseContext()).getList()));
        getlojaPadrao(Dao.getLojaADO(getBaseContext()).getLoja(UtilSet.getLojaId(this))) ;
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
            setCnpj();
            return true ;
        }

        if (item.getItemId() == R.id.action_aut_scan) {
            setQRCode() ;
            return true ;
        }

        return true ;
        //return super.onOptionsItemSelected(item);
    }



    private void setCnpj() {

        try {
            new ConexaoAutenticacao(this, editTextCnpj.getText().toString()) {
                @Override
                public void oK(int cod) {
                   autenticacaoOK() ;
                 //   Toast.makeText(getBaseContext(),"cod "+cod, Toast.LENGTH_LONG).show();
                }

                @Override
                public void erro(int cod, String msg) {
                    alert("Erro="+cod,msg) ;
                }
            }.execute();
        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert("Erro", e.getMessage());
        }


    }

    private void autenticacaoOK(){
      try
      {
          new ConexaoLoja(this) {
              @Override
              public void oK() {
                  spinnerLoja.setAdapter(new LojaAdapter(getBaseContext(), Dao.getLojaADO(getBaseContext()).getList()));
                  setlojaPadrao(Dao.getLojaADO(getBaseContext()).getList().get(0));
                  consultaTerminal() ;
              }

              @Override
              public void erro(String msg) {
                  alert("Erro", msg);
              }
          }.execute();
      } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
          e.printStackTrace();
          alert("Erro", e.getMessage());
      }
    }

    private void consultaTerminal(){
        try {
            new ConexaoConfTeminal(this) {
                @Override
                public void ok(int id, String descricao) {
                    //Toast.makeText(getBaseContext(),"Terminal Castrado", Toast.LENGTH_SHORT).show();
                    alertOK();
                }

                @Override
                public void erroMsg(String msg) {
                    setTerminal();

                }
            }.execute() ;
        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert("Erro", e.getMessage());
        }

    }

    private void setTerminal() {
        try {
            new ConexaoConfTeminal(this, UtilSet.getMAC(this)) {
                @Override
                public void ok(int id, String descricao) {
                    //Toast.makeText(getBaseContext(),"Terminal Castrado", Toast.LENGTH_SHORT).show();
                    alertOK();
                }

                @Override
                public void erroMsg(String msg) {
                    alert("Informação", msg);
                }
            }.execute() ;
        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert("Erro", e.getMessage());
        }
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


}
