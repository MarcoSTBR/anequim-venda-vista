package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.conexoes.ConexaoConfTerminal;
import com.anequimplus.entity.Terminal;
import com.anequimplus.utilitarios.UtilSet;

public class ActivityTerminal extends AppCompatActivity {

    private Toolbar toolbarTerminal ;
    private TextView textViewLoja ;
    private RadioGroup radioGroup ;
    private EditText editTerminal ;
    private int opcao ;
    private RadioButton bteNenhuma ;
    private RadioButton bteNfce ;
    private RadioButton bteSAT ;
    private Terminal terminal ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        toolbarTerminal = findViewById(R.id.toolbarTerminal) ;
        setSupportActionBar(toolbarTerminal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewLoja = findViewById(R.id.textViewLoja) ;
        radioGroup = findViewById(R.id.radiogrupotipoemissao) ;
        editTerminal = findViewById(R.id.editTerminal) ;
        bteNenhuma = findViewById(R.id.bteNenhuma) ;
        bteNfce    = findViewById(R.id.bteNfce) ;
        bteSAT     = findViewById(R.id.bteSAT)  ;
        terminal = new Terminal(0,UtilSet.getCnpj(this),UtilSet.getMAC(this),"",0,1) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTerminal.setTitle("Terminal");
        textViewLoja.setText(UtilSet.getLojaNome(this));
        selecionar(0);
        getTermnal() ;

    }

    public int selecionado(){

        int op = radioGroup.getCheckedRadioButtonId() ;

        switch (op)
        {
            case R.id.bteNenhuma : opcao = 0 ;
            break;
            case R.id.bteNfce    : opcao = 1 ;
            break;
            case R.id.bteSAT     : opcao = 2 ;
            break;
            default: opcao = 0 ;
                Toast.makeText(this, "Nenhuma Opção Selecionada!", Toast.LENGTH_SHORT).show();

        }
        return opcao ;
    }

    public void selecionar(int op){

        switch (op) {
            case 0 : bteNenhuma.setChecked(true);
            break;
            case 1 : bteNfce.setChecked(true);
            break;
            case 2 : bteSAT.setChecked(true);
            break;

        }
    }

    public void getTermnal(){
        //String m = "38-EA-A7-D2-53-6C" ;
        String m = UtilSet.getMAC(this) ;
        new ConexaoConfTerminal(this, m){


            @Override
            public void ok(Terminal t) {
                setDadosTerminal(t) ;

            }

            @Override
            public void erroMsg(int cod, String msg) {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();

            }
        }.execute() ;
    }

    public void setDadosTerminal(Terminal t){
        editTerminal.setText(t.getDescricao()) ;
        selecionar(t.getTipo_emissao());
        terminal = t ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_terminal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.terminal_ok) {
            atualizar() ;
            return true;
        }
        return true ;//super.onOptionsItemSelected(item);
    }

    private void atualizar() {
        terminal.setDescricao(editTerminal.getText().toString());
        terminal.setTipo_emissao(selecionado());

        new ConexaoConfTerminal(this, terminal){


            @Override
            public void ok(Terminal t) {
                setOK(t);


            }

            @Override
            public void erroMsg(int cod, String msg) {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }
        }.execute() ;

    }

    private void setOK(Terminal t) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_notifications_black_24dp)
                    .setTitle("Terminal OK:")
                    .setMessage("Cadastrado com sucesso\n"+t.getMac()+"\n"+t.getDescricao())
                    .setCancelable(false)
                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                        }
                    }).show();
    }


}