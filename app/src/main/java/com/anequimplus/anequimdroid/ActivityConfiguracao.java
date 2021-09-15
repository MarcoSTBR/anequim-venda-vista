package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.ado.Dao;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class ActivityConfiguracao extends AppCompatActivity implements View.OnClickListener{

    private CheckBox checkedVendaVistaImpressao ;
    private CheckBox checkBoxVendaVistaNFCe ;
    private String msgExterna = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
        Toolbar toolbar = findViewById(R.id.toolbarConfiguracao);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Configuração");

        checkedVendaVistaImpressao = (CheckBox) findViewById(R.id.checkedVendaVistaImpressao);
        checkedVendaVistaImpressao.setOnClickListener(this);
        checkBoxVendaVistaNFCe = (CheckBox) findViewById(R.id.checkBoxVendaVistaNFCe);
        checkBoxVendaVistaNFCe.setOnClickListener(this);

        TextView textViewMac = (TextView) findViewById(R.id.textViewMac) ;
        textViewMac.setText(UtilSet.getMAC(getBaseContext()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkedVendaVistaImpressao.setChecked(Configuracao.getVendaVistaPerguntarImpressao(this));
        checkBoxVendaVistaNFCe.setChecked(Configuracao.getVendaVistaPerguntarNFCe(this));
    }

    public void onClick(View view){

        switch (view.getId()) {
            case R.id.checkedVendaVistaImpressao: Configuracao.setVendaVistaPerguntarImpressao(this, checkedVendaVistaImpressao.isChecked()) ;
                                                  break;
            case R.id.checkBoxVendaVistaNFCe: Configuracao.setVendaVistaPerguntarNFCe(this, checkBoxVendaVistaNFCe.isChecked()) ;
                                                break;

            default: alert("Erro");

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_configuracao, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_conf_ok) {
            return true ;
        }

        if (item.getItemId() == R.id.action_conf_scan) {
            IntentIntegrator integrator = new IntentIntegrator(this) ;
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES) ;
            integrator.setPrompt("Configurações") ;
            integrator.setCameraId(0) ;
            integrator.initiateScan();
            return true ;
        }

        if (item.getItemId() == R.id.action_conf_autenticacao) {
            startActivity(new Intent(getBaseContext(), ActivityAutenticacaoAntigo.class));

            return true ;
        }

        return true ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data) ;
        if (result != null){
            if (result.getContents() != null){
                resultScan(result.getContents()) ;
              //  Toast.makeText(getBaseContext(),result.getContents(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(),"Nenhum resultado", Toast.LENGTH_SHORT).show();
            }
        } else
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void resultScan(String txt){
        try {
            JSONObject j = new JSONObject(txt);
           // editTextServidor.setText(j.getString("URL"));
           // editTextPorta.setText(j.getString("PORTA"));
  //          executar();
        } catch (JSONException e) {
            e.printStackTrace();
            alert("Erro em "+txt);
        }


    }

/*
    private void executar(){
        UtilSet.gravaParamString(getBaseContext(),"SERVIDOR", editTextServidor.getText().toString());
        UtilSet.gravaParamString(getBaseContext(),"PORTA", editTextPorta.getText().toString());

        new ConexaoTeste(this) {
            @Override
            public void oK(String msg) {
                msgExterna = msg ;
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                preparaLink() ;
            }

            @Override
            public void erro(String msg) {
                alert(msg) ;
            }
        }.execute() ;
    }
*/
    private void alert(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    private void alertOK(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private void preparaLink() {
        try {
            if (Dao.getLinkAcessoADO(getBaseContext()).getList().size() == 0) {
               // setLnks();
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_notifications_black_24dp)
                        .setTitle("Links")
                        .setMessage("Deseja Atualizar os Links de Acesso?")
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               // setLnks();
                            }
                        }).show();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /*
    public void setLnks(){
        try {
            new ConexaoLinkAcesso(this) {
                @Override
                public void oK() {
                    //Toast.makeText(getBaseContext(), "Conexão OK", Toast.LENGTH_SHORT).show();
                    alertOK(msgExterna) ;

                }

                @Override
                public void erro(String msg) {
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();

                }
            }.execute();
        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage());
        }

    }
*/
}
