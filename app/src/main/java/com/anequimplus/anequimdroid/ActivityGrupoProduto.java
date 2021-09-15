package com.anequimplus.anequimdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.ado.Dao;
import com.anequimplus.entity.Grupo;
import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Produto;

import java.text.DecimalFormat;
import java.util.List;

public class ActivityGrupoProduto extends AppCompatActivity {

    private ListView gradeProduto ;
    private Grupo grupo ;
    private EditText editTextFiltroProduto ;
    private Toolbar toolbar ;
    private List<Produto> produtoList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_produto);
        grupo =  Dao.getGrupoDAO(getBaseContext()).getGrupoID(getIntent().getIntExtra("GRUPO_ID",0)) ;
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(grupo.getDescricao());
        toolbar.setSubtitle("Selecione o Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextFiltroProduto = findViewById(R.id.editTextFiltroProduto );
        editTextFiltroProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filtrar() ;
            }
        });
        gradeProduto = findViewById(R.id.gradeProdutoSelct);
        setResult(RESULT_CANCELED);
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
        }
        if (item.getItemId() == R.id.action_produto_ok){
            //Intent intent = getIntent().putExtra("",m.getId());
            setResult(RESULT_OK, getIntent());
            finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        produtoList = Dao.getProdutoADO(this).getList(grupo) ;
       // Toast.makeText(this, "Grupo "+grupo.getDescricao()+ " n "+produtoList.size(), Toast.LENGTH_LONG ).show();
        editTextFiltroProduto.setText("");
        filtrar() ;
    }

    private void filtrar() {
        gradeProduto.setAdapter(new getProdutoAdapter(getBaseContext(),
                Dao.getProdutoADO(getBaseContext()).getFiltro(produtoList,
                editTextFiltroProduto.getText().toString()))) ;
    }

    private void setValor(){
        DecimalFormat frm = new DecimalFormat("R$ #0.00");
        String tv = grupo.getDescricao()+"  "+frm.format(valorPedidos()) ;
        toolbar.setSubtitle(tv) ;
    }

    private double quantidadePedidos(){
        double v = 0 ;
        for (ItenSelect it : Dao.getItemSelectADO(this).getList()){
            v = v + it.getQuantidade() ;
        }
        return v ;
    }

    private double valorPedidos(){
        double v = 0 ;
        for (ItenSelect it : Dao.getItemSelectADO(this).getList()){
            v = v + it.getQuantidade() * it.getProduto().getPreco();
        }
        return v ;

    }


    private void alerta(String txt){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Erro:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    public class getProdutoAdapter extends BaseAdapter {

        private Context ctx ;
        private List<Produto> listProduto ;
        private DecimalFormat frmQ = new DecimalFormat("#0.###");


        public getProdutoAdapter(Context ctx, List<Produto> listProduto) {
            this.ctx = ctx;
            this.listProduto = listProduto;
        }

        @Override
        public int getCount() {
            return listProduto.size();
        }

        @Override
        public Object getItem(int i) {
            return listProduto.get(i);
        }


        @Override
        public long getItemId(int i) {
            return listProduto.get(i).getId() ;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            final View  row  = inflater.inflate(R.layout.layout_grade_produto, null) ;
            final Produto prod = listProduto.get(i) ;
            final ItenSelect pit = getItemSelect(prod);
            setValores(row, pit, prod);

            ImageButton bmais = row.findViewById(R.id.ib_maisS);
            bmais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pit.setQuantidade(pit.getQuantidade()+1);
                    pit.setValor(pit.getQuantidade() * pit.getPreco());
                    setValores(row, pit, prod) ;
                }
            });

            ImageButton ib_menos = row.findViewById(R.id.ib_menosS);
            ib_menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pit.getQuantidade() > 0){
                        pit.setQuantidade(pit.getQuantidade() - 1);
                        pit.setValor(pit.getQuantidade() * pit.getPreco());
                    }
                    setValores(row, pit, prod) ;
                }
            });

            return row;
        }

        private void setValores(View row, ItenSelect pit, Produto prd){
            DecimalFormat frm = new DecimalFormat("R$ #0.00");
            DecimalFormat qrm = new DecimalFormat("#0.###");
            LinearLayout grade = row.findViewById(R.id.grade_produto_layout);

            TextView descricao = row.findViewById(R.id.textViewPedidoItemS);
            TextView qPreco = row.findViewById(R.id.textQPrPedidoItemS);
            //TextView obs  = row.findViewById(R.id.textIemPrdObsS);
            descricao.setText(prd.getDescricao()+" ( "+frm.format(prd.getPreco())+" )");
            qPreco.setText(qrm.format(pit.getQuantidade()));
            setValor() ;

        }

        private ItenSelect getItemSelect(Produto prd){
            ItenSelect it = null ;

            for (ItenSelect pp : Dao.getItemSelectADO(ctx).getList()){
                if (pp.getProduto().getId() == prd.getId())
                    it = pp ;
            }
            if (it == null) {
                it = new ItenSelect(Dao.getItemSelectADO(ctx).getList().size() ,prd,0,prd.getPreco(),0,0,"") ;
                Dao.getItemSelectADO(ctx).getList().add(it) ;
            }
            return it ;
        }

    }



}


