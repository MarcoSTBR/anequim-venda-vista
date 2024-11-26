package com.anequimplus.anequimdroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.GradeVendasItensAdapter;
import com.anequimplus.entity.GradeVendas;
import com.anequimplus.entity.GradeVendasItem;
import com.anequimplus.entity.ItemSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.listeners.ListenerAcompanhamentoSelect;
import com.anequimplus.utilitarios.DisplaySet;

import java.util.ArrayList;
import java.util.List;

public class ActivityGradeVendaItem extends AppCompatActivity {

    private RecyclerView gradeProduto ;
    private EditText editTextFiltroProduto ;
    private Toolbar toolbar ;
    private GradeVendas gradeVendas ;
    private List<PedidoItem> itensList ;
    private String pedido ;
    private final int ACOMPANHAMENTO = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_venda_item);
        pedido = getIntent().getStringExtra("PEDIDO") ;
        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Selecione o Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextFiltroProduto = findViewById(R.id.editTextFiltroProduto );
        gradeProduto = findViewById(R.id.gradeProdutoSelct);
        gradeVendas =  DaoDbTabela.getGradeVendasADO(this).getId(getIntent().getIntExtra("GRADE_ID",0)) ;
        carregarInicial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_produto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        if (item.getItemId() == R.id.action_produto_ok){
            finalizar() ;
            return true ;
        }
        return true;
    }

    private void finalizar(){
        Pedido p = DaoDbTabela.getPedidoADO(this).getPedido(pedido) ;
        for (PedidoItem it : DaoDbTabela.getItemSelectADO().getList()){
            if (it.getItemSelect().getQuantidade() > 0){
                it.setPedido_id(p.getId());
                DaoDbTabela.getPedidoItemADO(this).incluir(it);
            }
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        toolbar.setTitle(gradeVendas.getDescricao());
        editTextFiltroProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                display() ;
            }
        });
        editTextFiltroProduto.setText("");
    }

    private void carregarInicial(){
        DaoDbTabela.getItemSelectADO().getList().clear();
        for (GradeVendasItem it : DaoDbTabela.getGradeVendasItemADO(this).getGradeVendasItem(gradeVendas)) {
            if ((it.getStatus() == 1) && (it.getProduto().getPreco() > 0)) {
                ItemSelect its = new ItemSelect(it.getId(), it.getProduto(), 0,it.getProduto().getPreco(),0,0,0, "",1) ;
                DaoDbTabela.getItemSelectADO().getList().add(new PedidoItem(it.getId(), 0, its, new ArrayList<PedidoItemAcomp>()));
            }
        }
    }

    private void display(){
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        // at last set adapter to recycler view.
        gradeProduto.setLayoutManager(layoutManager);
        gradeProduto.setAdapter(new GradeVendasItensAdapter(this, getList(), new ListenerAcompanhamentoSelect() {
            @Override
            public void ok(PedidoItem it) {

            }

            @Override
            public void setAcompanhamento(PedidoItem it) {
                Bundle params = new Bundle();
                Intent intent = new Intent(ActivityGradeVendaItem.this, ActivityItemSelect.class);
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
                display();
            }
        }
    }


    private List<PedidoItem> getList(){
        List<PedidoItem> l = new ArrayList<PedidoItem>() ;
        String filtro = editTextFiltroProduto.getText().toString().toUpperCase() ;
        if (filtro.equals("")) {
            l = DaoDbTabela.getItemSelectADO().getList() ;
        } else {
            for (PedidoItem it : DaoDbTabela.getItemSelectADO().getList()) {
                if (it.getItemSelect().getProduto().getDescricao().toUpperCase().indexOf(filtro) > -1)
                    l.add(it);
            }
        }
        return l ;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        display();
    }

}


