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

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.PedidoItem;

public class ActivityProdutoAdd extends AppCompatActivity {

    private TextView textViewProdAdd ;
    private EditText editTextObs ;
    private EditText editQuant ;
    private ImageButton imageButtonMais ;
    private ImageButton imageButtonMenos ;
    private PedidoItem pedidoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtoadd);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int idPedidoItem = getIntent().getIntExtra("ITEMSELECT_ID",0) ;
        pedidoItem = DaoDbTabela.getPedidoItemADO(this).get(idPedidoItem) ;
        textViewProdAdd = findViewById(R.id.textViewProdAdd);
        editTextObs = findViewById(R.id.editTextObs);
        editTextObs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pedidoItem.getItemSelect().setObs(s.toString());
            }
        });

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
                double q = 0 ;
                if (editable.toString().length() > 0) {
                    q = Double.valueOf(String.valueOf(editQuant.getText()));
                } else {
                    q = 1.0 ;
                }
                if (q <= 0) q = 1 ;
                pedidoItem.getItemSelect().setQuantidade(q);
                setValores() ;
            }
        });

        imageButtonMais = findViewById(R.id.imageButtonMais);
        imageButtonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double q = Double.valueOf(String.valueOf(editQuant.getText())) ;
                q = q + 1.0 ;
                editQuant.setText(Double.toString(q));
                pedidoItem.getItemSelect().setQuantidade(q);
                pedidoItem.getItemSelect().setObs(editTextObs.getText().toString()) ;
                setValores() ;
            }
        });

        imageButtonMenos = findViewById(R.id.imageButtonMenos);
        imageButtonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double q = Double.valueOf(String.valueOf(editQuant.getText())) ;
                if (q <= 0) {
                    excluir();
                } else {
                   q = q - 1.0 ;
                }
                editQuant.setText(Double.toString(q));
                pedidoItem.getItemSelect().setQuantidade(q);
                pedidoItem.getItemSelect().setObs(editTextObs.getText().toString()) ;
                setValores() ;
            }
        });

    }

    private void setValores(){
        Double vl = pedidoItem.getItemSelect().getQuantidade() * pedidoItem.getItemSelect().getPreco() ;
        Double com = vl *  (pedidoItem.getItemSelect().getProduto().getComissao() / 100);
        pedidoItem.getItemSelect().setValor(vl);
        pedidoItem.getItemSelect().setComissao(com);
    }


    @Override
    protected void onResume() {
        super.onResume();
        textViewProdAdd.setText(pedidoItem.getItemSelect().getProduto().getDescricao());
        editTextObs.setText(pedidoItem.getItemSelect().getObs());
        editQuant.setText(Double.toString(pedidoItem.getItemSelect().getQuantidade()));
    }


    private void excluir() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Excluir")
                .setMessage("Deseja Excluir?")
                .setCancelable(false)
                .setNegativeButton("Não",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                           DaoDbTabela.getPedidoItemADO(ActivityProdutoAdd.this).excluir(pedidoItem);
                           finish();
                    }
                }).show();
    }

    private void onClickEntrar() {
        setValores();
        DaoDbTabela.getPedidoItemADO(ActivityProdutoAdd.this).alterar(pedidoItem);
        finish();
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
            return true ;
        }
        if (item.getItemId() == R.id.action_produto_add_ok) {
            onClickEntrar();
        }
        return true ;
    }
}
