package com.anequimplus.anequimdroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.GradeVendasItensAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.entity.GradeVendas;
import com.anequimplus.entity.GradeVendasItem;
import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.utilitarios.DisplaySet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityGradeVendaItem extends AppCompatActivity {

    private RecyclerView gradeProduto ;
    private EditText editTextFiltroProduto ;
    private Toolbar toolbar ;
    private GradeVendas gradeVendas ;
    private List<ItenSelect> itensList ;
    private String pedido ;

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
        gradeVendas =  Dao.getGradeVendasADO(this).getId(getIntent().getIntExtra("GRADE_ID",0)) ;
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
            finalizar() ;
        }
        return true;
    }

    private void finalizar(){
        for (ItenSelect i : itensList){
            if (i.getQuantidade() > 0){
                Pedido p = Dao.getPedidoADO(this).getPedido(pedido) ;
                if (p == null) {
                    p = new Pedido(0, pedido, new Date(), new ArrayList<PedidoItem>()) ;
                    Dao.getPedidoADO(this).incluir(p); ;
                }
                PedidoItem item = new PedidoItem(0, p.getId(), i) ;
                Dao.getPedidoItemADO(this).incluir(item);
            }
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarInicial();
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
        itensList = new ArrayList<ItenSelect>() ;
        for (GradeVendasItem it : Dao.getGradeVendasItemADO(this).getGradeVendasItem(gradeVendas)) {
            if (it.getStatus() == 1){
                itensList.add(new ItenSelect(it.getId(), it.getProduto(), 0,it.getProduto().getPreco(),0,0,0, "",1));
            }
        }
    }

    private void display(){
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        // at last set adapter to recycler view.
        gradeProduto.setLayoutManager(layoutManager);
        gradeProduto.setAdapter(new GradeVendasItensAdapter(this, getList()));
    }

    private List<ItenSelect> getList(){
        List<ItenSelect> l = new ArrayList<ItenSelect>() ;
        String filtro = editTextFiltroProduto.getText().toString().toUpperCase() ;
        if (filtro.equals("")) {
            l = itensList ;
        } else {
            for (ItenSelect it : itensList) {
                if (it.getProduto().getDescricao().toUpperCase().indexOf(filtro) > -1)
                    l.add(it);
            }
        }
        return l ;
    }
/*


    private void setValor(){
        DecimalFormat frm = new DecimalFormat("R$ #0.00");
        String tv = gradeVendas.getDescricao()+"  "+frm.format(valorPedidos()) ;
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
//            v = v + it.getQuantidade() * it.getProduto().getPreco();
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
            final View  row  = inflater.inflate(R.layout.layout_grade_grade_venda_item, null) ;
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
            TextView q = row.findViewById(R.id.textQPrPedidoItemS);
            TextView obs  = row.findViewById(R.id.textViewPedidoItemOBS);
            descricao.setText(prd.getDescricao()+" ( "+frm.format(prd.getPreco())+" )");
            if (pit.getObs().equals("")) {
                obs.setVisibility(View.GONE);
            } else {
                obs.setVisibility(View.VISIBLE);
                obs.setText(pit.getObs());
            }

            q.setText(qrm.format(pit.getQuantidade()));
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

*/


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        display();
    }

}


