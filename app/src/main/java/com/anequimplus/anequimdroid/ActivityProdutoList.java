package com.anequimplus.anequimdroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.ProdutoListAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.conexoes.ConexaoProdutos;
import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.Produto;
import com.anequimplus.utilitarios.DisplaySet;

import java.util.ArrayList;
import java.util.List;

public class ActivityProdutoList extends AppCompatActivity {

    private EditText editTextTexProduto ;
    private RecyclerView grade_produto;
    private ImageButton atualizarprodutos ;
    private String pedido ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_list);
        pedido = getIntent().getStringExtra("PEDIDO") ;
        editTextTexProduto = findViewById(R.id.editTextProduto) ;
        editTextTexProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listar();

            }
        });
        grade_produto = findViewById(R.id.grade_produto_list) ;
        atualizarprodutos = findViewById(R.id.atualizarprodutos) ;
        atualizarprodutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarProdutosGrade() ;
            }
        });
    }

    private void atualizarProdutosGrade() {
        new ConexaoProdutos(this) {
            @Override
            public void Ok() {
                listar() ;
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(ActivityProdutoList.this, msg, Toast.LENGTH_SHORT).show();

            }
        }.execute() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        listar() ;
    }

    private void listar() {
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        grade_produto.setLayoutManager(layoutManager);
        grade_produto.setAdapter(new ProdutoListAdapter(this, getGrade()) {
            @Override
            public void selecionado(Produto p) {
                Pedido ped = Dao.getPedidoADO(ActivityProdutoList.this).getPedido(pedido) ;
                Double com = p.getComissao() / 100 * p.getPreco() ;
                PedidoItem pedidoItem = new PedidoItem(0, ped.getId(), new ItenSelect(0, p, 1, p.getPreco(), 0, com, p.getPreco(), "", 1)) ;
                Dao.getPedidoItemADO(ActivityProdutoList.this).incluir(pedidoItem);
                finish();
            }
        });
    }

    private List<Produto> getGrade(){
        List<Produto> l  = new ArrayList<Produto>() ;
        String filtro = editTextTexProduto.getText().toString().toUpperCase() ;
        for (Produto p : Dao.getProdutoADO(this).getTodos()){
          if ((p.getStatus() == 1) && (p.getPreco() > 0)) {
              if ((p.getDescricao().toUpperCase().indexOf(filtro) > -1) || (filtro.isEmpty()))
              l.add(p);
          }
        }
        return l ;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        listar();
    }
}