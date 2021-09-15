package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.ado.Dao;
import com.anequimplus.entity.ItenSelect;

public class ActivityProdutoAdd extends AppCompatActivity {

    private TextView textViewProdAdd ;
    private TextView textViewProdAddProduto ;
    private EditText editTextObs ;
    private EditText editQuant ;
    private ImageButton imageButtonMais ;
    private ImageButton imageButtonMenos ;
    private ItenSelect itenSelect ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtoadd);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itenSelect = Dao.getItemSelectADO(getBaseContext()).getItemSelectId(getIntent().getIntExtra("ITEMSELECT_ID",-1)) ;
        textViewProdAdd = findViewById(R.id.textViewProdAdd);
        textViewProdAddProduto = findViewById(R.id.textViewProdAddProduto);
        editTextObs = findViewById(R.id.editTextObs);
        editQuant = findViewById(R.id.editQuant);
        editQuant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    double q = Double.valueOf(String.valueOf(editQuant.getText()));
                    itenSelect.setQuantidade(q);
                } else {
                    double q = 1.0 ;
                    itenSelect.setQuantidade(q);
                }

            }
        });
        imageButtonMais = findViewById(R.id.imageButtonMais);
        imageButtonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double q = Double.valueOf(String.valueOf(editQuant.getText())) ;
                q = q + 1.0 ;
                editQuant.setText(Double.toString(q));
                itenSelect.setQuantidade(q);
                itenSelect.setObs(editTextObs.getText().toString()) ;
            }
        });
        imageButtonMenos = findViewById(R.id.imageButtonMenos);
        imageButtonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double q = Double.valueOf(String.valueOf(editQuant.getText())) ;
                if (q <= 1) {
                    excluir();

                } else {
                   q = q - 1.0 ;
                   editQuant.setText(Double.toString(q));
                   itenSelect.setQuantidade(q);
                   itenSelect.setObs(editTextObs.getText().toString()) ;
                }
            }
        });
        setResult(RESULT_CANCELED);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (itenSelect == null) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_notifications_black_24dp)
                    .setTitle("Erro")
                    .setMessage("Item não encontrado!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();

        } else {
              textViewProdAdd.setText(itenSelect.getProduto().getCodBarra());
              textViewProdAddProduto.setText(itenSelect.getProduto().getDescricao());
              editTextObs.setText(itenSelect.getObs());
              editQuant.setText(Double.toString(itenSelect.getQuantidade()));
        }
    }


    private void excluir() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atenção:")
                .setMessage("Quantidade Inválida, Deseja Sair?")
                .setCancelable(false)
                .setNegativeButton("Não",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                    }
                }).show();
    }

    private void onClickEntrar() {
        if (itenSelect.getQuantidade() > 0) {
            setResult(RESULT_OK);
            itenSelect.setObs(editTextObs.getText().toString()) ;
        }
        finish();
    }

    private void alert(String s) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro:")
                .setMessage(s)
                .setCancelable(false)
                .setPositiveButton("Ok",null).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_produto_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_produto_add_ok) {
            onClickEntrar();
        }

        //return super.onOptionsItemSelected(item);
        return true ;
    }
}
