package com.anequimplus.anequimdroid;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.AcompanhamentoAdapter;
import com.anequimplus.entity.Acompanhamento;
import com.anequimplus.entity.Acompanhamento_Item;
import com.anequimplus.entity.Acompanhamento_produto;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ItemSelect;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.entity.Produto;
import com.anequimplus.entity.ProdutoAcompanhamento;
import com.anequimplus.utilitarios.DisplaySet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityItemSelect extends AppCompatActivity {

    private Toolbar toolbar ;
    private PedidoItem pedidoItem ;
    private ItemSelect itemSelect ;
    private TextView textProduto ;
    private TextView totalAcomp ;
    private ImageButton inclusaoAcomp ;
    private RecyclerView grade ;
    private List<Acompanhamento_produto> acompanhamento_produtos ;
    private List<ProdutoAcompanhamento> produtoAcompanhamentos ;
    private String mensagem_status ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_select);
        toolbar = findViewById(R.id.toolbar_acomp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pedidoItem = DaoDbTabela.getItemSelectADO().get(getIntent().getIntExtra("id", 0)) ;
        itemSelect = pedidoItem.getItemSelect();
        textProduto = findViewById(R.id.textProdutoAcomp) ;
        grade       = findViewById(R.id.listaAcompanhamanto) ;
        totalAcomp  = findViewById(R.id.totalAcomp) ;
        inclusaoAcomp = findViewById(R.id.inclusaoAcomp) ;
        inclusaoAcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInclusaoAcomp() ;
            }
        });
        construirTela() ;
        setResult(RESULT_CANCELED);
    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Acompanhamento");
        toolbar.setSubtitle(itemSelect.getProduto().getDescricao());
    }

    private void construirTela() {
        textProduto.setText(itemSelect.getProduto().getDescricao());
        toolbar.setTitle("Acompanhamento");
        toolbar.setSubtitle(itemSelect.getProduto().getDescricao());
        carregarAcomp() ;
    }

    private void carregarAcomp(){
        filtrar() ;
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(DisplaySet.getNumeroDeColunasGrade(this), StaggeredGridLayoutManager.VERTICAL);
        grade.setLayoutManager(layoutManager);
        grade.setAdapter(new AcompanhamentoAdapter(this, getListAcompanhamento()) {
            @Override
            public void onSetChange() {
               onChange() ;
            }
        }) ;
    }

    private void onChange(){
      setTotal() ;
    }

    private void setInclusaoAcomp() {
        if (getStatus()){
            confirmar() ;
            //Toast.makeText(this, "VERDADEIRO", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,  mensagem_status, Toast.LENGTH_SHORT).show();
        }
    }

    private void  confirmar() {
        pedidoItem.getItemSelect().setQuantidade(1); ;
        pedidoItem.getAcompanhamentos().clear();
        for (ProdutoAcompanhamento pa : produtoAcompanhamentos) {
            for (Acompanhamento_Item it : pa.getAcompanhamento_items()) {
                if (it.getQuantidade() > 0) {
                    Produto p = DaoDbTabela.getProdutoADO(this).getProdutoId(it.getProduto_id()) ;
                    Double q  = pedidoItem.getItemSelect().getQuantidade() * it.getQuantidade()  ;
                    Double vl = q * it.getPreco() ;
                    Double com = vl * (p.getComissao() / 100) ;
                    ItemSelect item = new ItemSelect(0, p, q, it.getPreco(), 0, com, vl, "", 1) ;
                    PedidoItemAcomp acomp = new PedidoItemAcomp(0, pedidoItem.getId(), item);
                    pedidoItem.getAcompanhamentos().add(acomp);
                }
            }
        }
        setResult(RESULT_OK);
        finish();
    }

    private Boolean getStatus(){
      mensagem_status = "" ;
      Boolean resp = true ;
        for (ProdutoAcompanhamento pa : produtoAcompanhamentos) {
            Double vq = 0.0;
            for (Acompanhamento_Item it : pa.getAcompanhamento_items()) {
                vq = vq + it.getQuantidade();
                if (!((it.getQuantidade() >= it.getMin()) && (it.getQuantidade() <= it.getMax()))) {
                    resp = false;
                    Produto p = DaoDbTabela.getProdutoADO(this).getProdutoId(it.getProduto_id());
                    mensagem_status = "Verifique a Quantidade do Produto " + p.getDescricao();
                }
            }
            if (!((vq >= pa.getAcompanhamento().getMin()) && (vq <= pa.getAcompanhamento().getMax()))) {
                resp = false;
                mensagem_status = "Verifique o Acompanhamento " + pa.getAcompanhamento().getDescricao();
            }
        }
      return resp ;
    }

    private void setTotal(){
        DecimalFormat frmv = new DecimalFormat("R$ #0.00");
        Double vl = 0.0 ;
        for (ProdutoAcompanhamento pa : produtoAcompanhamentos){
            vl = vl + pa.getAcompanhamento().getPreco() ;
            for (Acompanhamento_Item aci : pa.getAcompanhamento_items()){
                vl = vl + aci.getQuantidade() * aci.getPreco() ;
            }
        }
        totalAcomp.setText(frmv.format(vl));
    }

    private void filtrar() {

        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("AFOOD_PRODUTO_ID", "=", String.valueOf(itemSelect.getProduto().getId())));
        acompanhamento_produtos = DaoDbTabela.getAcompanhamanto_ProdutoADO(this).getList(filters, "");
    }

    private List<ProdutoAcompanhamento> getListAcompanhamento(){
        produtoAcompanhamentos = new ArrayList<ProdutoAcompanhamento>() ;
        for (Acompanhamento_produto it : acompanhamento_produtos){
            Acompanhamento ac = DaoDbTabela.getAcompanhamentoADO(this).get(it.getAcomp_id()) ;
            produtoAcompanhamentos.add(new ProdutoAcompanhamento(ac, getListAcompanhamento_Item(ac.getId()))) ;
        }
        return produtoAcompanhamentos;
    }

    private List<Acompanhamento_Item> getListAcompanhamento_Item(int ac_id){
        FilterTables f = new FilterTables();
        f.add(new FilterTable("AFOOD_ACOMP_ID", "=", String.valueOf(ac_id)));
        return DaoDbTabela.getAcompanhamento_ItemADO(this).getList(f, "ID") ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}