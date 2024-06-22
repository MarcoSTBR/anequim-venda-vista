package com.anequimplus.anequimdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.anequimplus.adapter.ConfiguracaoPageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityConfiguracao extends AppCompatActivity {

    private TabLayout tabLayout ;
    private ViewPager viewPager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
        Toolbar toolbar = findViewById(R.id.toolbarConfiguracao);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Configuração");
        tabLayout = findViewById(R.id.tabConfig) ;
        viewPager = findViewById(R.id.viewpager_config) ;
        viewPager.setAdapter(new ConfiguracaoPageAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.configuracao)));
        tabLayout.setupWithViewPager(viewPager);

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
            return true ;
        }
        if (item.getItemId() == R.id.action_conf_ok) {
            finish();
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
            startActivity(new Intent(getBaseContext(), ActivityAutenticacao.class));
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


    private void alert(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }
}
