package com.anequimplus.anequimdroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.ProdutoListAdapter;
import com.anequimplus.conexoes.ConexaoAcompanhamento;
import com.anequimplus.conexoes.ConexaoAcompanhamentoItem;
import com.anequimplus.conexoes.ConexaoAcompanhamentoProduto;
import com.anequimplus.conexoes.ConexaoProdutos;
import com.anequimplus.entity.Acompanhamento;
import com.anequimplus.entity.Acompanhamento_Item;
import com.anequimplus.entity.Acompanhamento_produto;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ItemSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.entity.Produto;
import com.anequimplus.listeners.ListenerAcompanhamento;
import com.anequimplus.listeners.ListenerAcompanhamentoItem;
import com.anequimplus.listeners.ListenerAcompanhamentoProduto;
import com.anequimplus.listeners.ListenerAcompanhamentoSelect;
import com.anequimplus.utilitarios.DisplaySet;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityProdutoList extends AppCompatActivity {

    private EditText editTextTexProduto ;
    private RecyclerView grade_produto;
    private ImageButton atualizarprodutos ;
    private String pedido ;
    private Button confirmar_itens ;
    private Button produto_list_cancelar ;
    private final int ACOMPANHAMENTO = 1 ;

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
        confirmar_itens = findViewById(R.id.produto_list_confirmar) ;
        confirmar_itens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmar() ;
            }
        });
        produto_list_cancelar = findViewById(R.id.produto_list_cancelar) ;
        produto_list_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        carregarItens() ;
    }

    private void confirmar() {
        Pedido p = DaoDbTabela.getPedidoADO(this).getPedido(pedido) ;
        for (PedidoItem it : DaoDbTabela.getItemSelectADO().getList()){
          //  Log.i("confirmar", "id "+p.getId()+" "+p.getPedido()+" "+
          //          it.getItemSelect().getProduto().getDescricao()+" "+it.getItemSelect().getQuantidade()) ;
            if (it.getItemSelect().getQuantidade() > 0){
                Log.i("confirmar", "entrou id "+p.getId()+" "+p.getPedido()+" "+
                        it.getItemSelect().getProduto().getDescricao()+" "+it.getItemSelect().getQuantidade()) ;
                it.setPedido_id(p.getId());
                DaoDbTabela.getPedidoItemADO(this).incluir(it);
                finish();
            }
        }
    }

    private void carregarItens() {
        DaoDbTabela.getItemSelectADO().getList().clear();
        List<Produto> l = DaoDbTabela.getProdutoADO(this).getTodos() ;
        for (Produto p : l){
            ItemSelect it = new ItemSelect(p.getId(), p, 0,p.getPreco() ,0, 0, 0, "", 1) ;
            DaoDbTabela.getItemSelectADO().getList().add(new PedidoItem(p.getId(), 0, it, new ArrayList<PedidoItemAcomp>())) ;
        }
    }

    private void atualizarProdutosGrade() {
        new ConexaoProdutos(this) {
            @Override
            public void Ok() {
                setAcompanhamantoProduto() ;
                //listar() ;
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(ActivityProdutoList.this, msg, Toast.LENGTH_SHORT).show();

            }
        }.execute() ;
    }

    private void setAcompanhamantoProduto(){
        try {
            new ConexaoAcompanhamentoProduto(this, new FilterTables(), "", new ListenerAcompanhamentoProduto() {
                @Override
                public void ok(List<Acompanhamento_produto> l) {
                    setAcompanhamento();
                }

                @Override
                public void erro(int cod, String msg) {
                    Toast.makeText(ActivityProdutoList.this, msg, Toast.LENGTH_SHORT).show();
                }
            }).execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(ActivityProdutoList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void setAcompanhamento(){
        try {
            new ConexaoAcompanhamento(this, new FilterTables(), "", new ListenerAcompanhamento() {
                @Override
                public void ok(List<Acompanhamento> l) {
                    setAcompanhamentoItem() ;
                }

                @Override
                public void erro(int cod, String msg) {
                    Toast.makeText(ActivityProdutoList.this, msg, Toast.LENGTH_SHORT).show();
                }
            }).execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(ActivityProdutoList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setAcompanhamentoItem(){
        try {
            new ConexaoAcompanhamentoItem(this, new FilterTables(), "", new ListenerAcompanhamentoItem() {
                @Override
                public void ok(List<Acompanhamento_Item> l) {
                    listar(); ;
                }

                @Override
                public void erro(int cod, String msg) {
                    Toast.makeText(ActivityProdutoList.this, msg, Toast.LENGTH_SHORT).show();
                }
            }).execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(ActivityProdutoList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        listar() ;
    }

    private void listar() {

        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        grade_produto.setLayoutManager(layoutManager);
        grade_produto.setAdapter(new ProdutoListAdapter(this, getGrade(), new ListenerAcompanhamentoSelect() {
            @Override
            public void ok(PedidoItem it) {


            }

            @Override
            public void setAcompanhamento(PedidoItem it) {
                Bundle params = new Bundle();
                Intent intent = new Intent(ActivityProdutoList.this, ActivityItemSelect.class);
                params.putInt("id",it.getId());
                intent.putExtras(params);
                startActivityForResult(intent, ACOMPANHAMENTO);
            }

            @Override
            public void erro(String msg) {

            }
        }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACOMPANHAMENTO) {
            if (resultCode == RESULT_OK){

                listar();
            }
        }
    }

    private List<PedidoItem> getGrade(){
        List<PedidoItem> l  = new ArrayList<PedidoItem>() ;
        String filtro = editTextTexProduto.getText().toString().toUpperCase() ;
        for (Produto p : DaoDbTabela.getProdutoADO(this).getTodos()){
          if ((p.getStatus() == 1) && (p.getPreco() > 0)) {
              if ((p.getDescricao().toUpperCase().indexOf(filtro) > -1) || (filtro.isEmpty()))
              l.add(DaoDbTabela.getItemSelectADO().get(p.getId()));
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