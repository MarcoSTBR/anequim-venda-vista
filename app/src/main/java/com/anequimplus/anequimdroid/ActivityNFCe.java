package com.anequimplus.anequimdroid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoImagem;
import com.anequimplus.conexoes.ConexaoNFCe;
import com.anequimplus.conexoes.ConexaoQrCode;
import com.anequimplus.conexoes.ConexaoImpressoras;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.NFCe;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.net.MalformedURLException;
import java.util.List;

public class ActivityNFCe extends AppCompatActivity {

    private String chave ;
    private NFCe nfce ;
    private Impressora impressoraPadrao ;
    private Spinner spinnerImp ;
    private ImageView imgNFCeLogoTipo = null ;
    private ImageView imgNfceQrcode = null ;
    private TextView tVNFCeCorpo ;
    private TextView tVNFCeRodape ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfce);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chave = getIntent().getStringExtra("CHAVE") ;
        spinnerImp     = (Spinner) findViewById(R.id.spinnernfce) ;

        imgNFCeLogoTipo = (ImageView)  findViewById(R.id.imgNFCeLogoTipo) ;
        imgNfceQrcode = (ImageView)  findViewById(R.id.imgNfceQrcode)  ;
        tVNFCeCorpo = (TextView)  findViewById(R.id.tVNFCeCorpo) ;
        tVNFCeRodape = (TextView)  findViewById(R.id.tVNFCeRodape) ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true ;
    }
    @Override
    protected void onResume() {
        super.onResume();
        carregaImp() ;
        executar() ;

    }

    private void executar(){
        try {
            new ConexaoNFCe(this, chave, impressoraPadrao) {
                @Override
                public void setNFCE(NFCe nfce) {
                    display(nfce) ;
                }

                @Override
                public void erro(String msg) {
                    Toast.makeText(getBaseContext(),msg, Toast.LENGTH_LONG).show();
                    alert(msg);

                }
            }.execute();
        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    private void display(NFCe nfce) {
        this.nfce = nfce ;
        tVNFCeCorpo.setText(getText(nfce.getCorpo()));
        tVNFCeRodape.setText(getText(nfce.getRodape()));
        setQRCode() ;
        setLogoTipo();
    }

    private void setLogoTipo() {
        if (!nfce.getLogoTipo().getUrl().equals("")){
            try {
                new ConexaoImagem(this, nfce.getLogoTipo()) {
                    @Override
                    public void setBitmap(Bitmap bt) {
                        imgNFCeLogoTipo.setVisibility(View.VISIBLE);
                        imgNFCeLogoTipo.setImageBitmap(bt);
                    }

                    @Override
                    public void erro(String msg) {
                        imgNFCeLogoTipo.setVisibility(View.GONE);

                    }
                }.execute() ;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                imgNFCeLogoTipo.setVisibility(View.GONE);

            }
        }
    }

    private void setQRCode(){
            new ConexaoQrCode(getBaseContext(), chave) {
                @Override
                public void setBitmap(Bitmap bt) {
                    imgNfceQrcode.setVisibility(View.VISIBLE);
                    imgNfceQrcode.setImageBitmap(bt);
                }

                @Override
                public void erro(String msg) {
                    imgNfceQrcode.setVisibility(View.GONE);
                    //Toast.makeText(getBaseContext(),msg, Toast.LENGTH_SHORT) ;
                    alert(msg) ;
                }
            }.execute();


    }

    private void alert(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();


    }

    private String getText(List<RowImpressao> l){
        String linha = "";
        for (RowImpressao r : l){
            linha = linha + r.getLinha() + "\n" ;
        }
        return linha ;
    }


    private void carregaImp() {
            new ConexaoImpressoras(this) {
                @Override
                public void Ok() {
                    ImpressoraAdapter impAdp = new ImpressoraAdapter(getBaseContext(), Dao.getImpressoraADO(getBaseContext()).getList()) ;
                    spinnerImp.setAdapter(impAdp);
                    spinnerImp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            impressoraPadrao = Dao.getImpressoraADO(getBaseContext()).getList().get(i) ;
                            UtilSet.setImpPadraoNFCe(getBaseContext(), impressoraPadrao.getDescricao()) ;
                            setImpressoraPadrao() ;
                            //UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao()) ;
                            //carregaRelatorio() ;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    setImpressoraPadrao();
                    // carregaRelatorio() ;
                }

                @Override
                public void erroMensagem(String msg) {
                    Toast.makeText(getBaseContext(),msg, Toast.LENGTH_LONG).show();

                }
            }.execute();

    }

    private void setImpressoraPadrao() {

        String descImp = UtilSet.getImpPadraoNFce(this);
        for (int i = 0 ; i<spinnerImp.getCount() ; i++){
            spinnerImp.getItemAtPosition(i);
            Impressora impSp = (Impressora)spinnerImp.getItemAtPosition(i) ;
            // Log.i("List Imp","i "+impSp.getDescricao()+" padrao "+descImp) ;
            if (descImp.equals(impSp.getDescricao())) {
                spinnerImp.setSelection(i);
                impressoraPadrao = impSp ;
            }
        }
    }



}
