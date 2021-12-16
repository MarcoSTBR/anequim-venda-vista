package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.PedidoEnvioAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.conexoes.ConexaoEnvioPedido;
import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.utilitarios.DisplaySet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class ActivityEnvioPedido extends AppCompatActivity {

    private ImageButton imagelupaproduto ;
    private EditText editProdutoPesquisa ;
    private RecyclerView listaPrdPedido ;
    private Pedido pedido ;
    private PedidoItem pedidoItem ;
    private int idPed = 0 ;
    private Toolbar toolbar ;
    private static int GRUPO_RETORNO = 1 ;
    private static int ITEM_EDICAO = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviopedido);
        idPed = getIntent().getIntExtra("PEDIDO_ID",0) ;
        pedido = Dao.getPedidoADO(getBaseContext()).getId(idPed) ;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle("Envio de Pedido");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarConexaoPedidos();
            }
        });

        listaPrdPedido  = findViewById(R.id.listaPrdPedido);
        imagelupaproduto  = findViewById(R.id.imagelupaproduto);
        editProdutoPesquisa  = findViewById(R.id.editProdutoPesquisa);

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Pedido: "+pedido.getPedido());
        carregaList() ;

        editProdutoPesquisa.setText("");
    }


    private void enviarConexaoPedidos()  {
                new ConexaoEnvioPedido(this) {
                    @Override
                    public void envioOK(int count) {
                        enviado() ;
                        //carregaList() ;
                    }
                    @Override
                    public void erroEnvio(String msg) {
                        alert(msg);
                    }
                }.execute();
    }

    private void enviado(){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("OK")
                .setMessage("Pedido(s) enviado com sucesso!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private void addProduto() {
        Intent intent = new Intent(getBaseContext(), ActivityGradeVendas.class) ;
        Bundle params = new Bundle() ;
        params.putString("SUBTITULO", "Pedido "+pedido.getPedido());
        intent.putExtras(params) ;
        startActivityForResult(intent, GRUPO_RETORNO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GRUPO_RETORNO) {
            if (resultCode == RESULT_OK){
                Log.i("resposta_Pedidos", "Tamanho"+Dao.getItemSelectADO(getBaseContext()).getList().size()) ;
                for (ItenSelect it : Dao.getItemSelectADO(getBaseContext()).getList()){
                    if (it.getQuantidade()>0) {
                        Log.i("resposta_Pedidos", it.getProduto().getDescricao());
                        Dao.getPedidoItemADO(getBaseContext()).pedidoItemAdd(pedido, it);
                    }
                }
                carregaList();
            }
        }
        if (requestCode == ITEM_EDICAO){
            if (resultCode == RESULT_OK) {
                Log.i("resposta_edicao", Dao.getItemSelectADO(getBaseContext()).getList().get(0).getProduto().getDescricao());
                pedidoItem.setItenSelect(Dao.getItemSelectADO(getBaseContext()).getList().get(0));
                Log.i("DescricaoAlteracao",Dao.getItemSelectADO(getBaseContext()).getList().get(0).getProduto().getDescricao());
                Log.i("DescricaoAlteracao",Dao.getItemSelectADO(getBaseContext()).getList().get(0).getObs());
                Dao.getPedidoItemADO(getBaseContext()).alterar(pedidoItem);
                carregaList();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void carregaList(){
        pedido = Dao.getPedidoADO(this).getId(idPed) ;
        if (pedido != null) {
            GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
            // at last set adapter to recycler view.
            listaPrdPedido.setLayoutManager(layoutManager);
            listaPrdPedido.setAdapter(new PedidoEnvioAdapter(this, pedido.getListPedidoItem()));
            diplayQuantidade();
            Log.i("listpedidoaberto", "id "+idPed+" pedido "+pedido.getPedido()+" size "+pedido.getListPedidoItem().size()) ;
        } else   Log.i("listpedidoaberto", "pedido.getListPedidoItem() nulo") ;

    }

    private void diplayQuantidade(){
      DecimalFormat frmQ = new DecimalFormat("#0.###");
      DecimalFormat frmV = new DecimalFormat("R$ #0.00");
      Double q = 0.0 ;
      Double v = 0.0 ;
      for (PedidoItem it : pedido.getListPedidoItem()){
          q = q + it.getItenSelect().getQuantidade() ;
          v = v + it.getItenSelect().getValor() ;
      }
      toolbar.setSubtitle("Item(s) "+frmQ.format(q)+" = "+frmV.format(v));
    }
/*

    private void perguntar() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Pedido: "+pedido.getPedido())
                .setMessage("Deseja Incluir Produtos?")
                .setCancelable(false).setNegativeButton("Não", null)
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addProduto();
                    }
                }).show();
    }

*/
    private void alert(String txt){
         new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private void excluirItem(final PedidoItem pit) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage("Deseja Estornar o Item?")
                .setCancelable(false).setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dao.getPedidoItemADO(getBaseContext()).delete(pit);
                        carregaList();

                    }
                }).show();
    }
/*
    private class getAdapterPedidoItem extends BaseAdapter {
        private Context ctx ;
        private List<PedidoItem> list ;
        public getAdapterPedidoItem(Context ctx, List<PedidoItem> list) {
            this.ctx = ctx ;
            this.list = list ;
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int i) {
            return list.get(i);
        }
        @Override
        public long getItemId(int i) {
            return list.get(i).getId();
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            final View  row  = inflater.inflate(R.layout.layout_grade_pedidoitem, null) ;
            final PedidoItem pit = list.get(i);
            setValores(row, pit);
            ImageButton bmais = row.findViewById(R.id.ib_mais);
            bmais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMais(pit) ;
                    setValores(row, pit) ;
                }
            });

            ImageButton ib_menos = row.findViewById(R.id.ib_menos);
            ib_menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMenos(pit) ;
                    setValores(row, pit) ;
                }
            });

            ImageButton ib_editar = row.findViewById(R.id.ib_editar);
            ib_editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setEditar(pit) ;

                }
            });

            return row;
        }

        private void setMenos(PedidoItem pit) {
            if (pit.getItenSelect().getQuantidade() <= 1){
                excluirItem(pit) ;
            } else {
                pit.getItenSelect().setQuantidade(pit.getItenSelect().getQuantidade() - 1);
             //   pit.getItenSelect().setValor(pit.getItenSelect().getQuantidade() * pit.getItenSelect().getProduto().getPreco());
                Dao.getPedidoItemADO(ctx).alterar(pit) ;
            }
        }

        private void setValores(View row, PedidoItem pit){
            TextView descricao = row.findViewById(R.id.textViewPedidoItem);
            TextView q_Preco = row.findViewById(R.id.textQPrPedidoItem);
            TextView obs  = row.findViewById(R.id.textIemPrdObs);
            descricao.setText(pit.getItenSelect().getProduto().getDescricao());
            obs.setText(pit.getItenSelect().getObs());
            if (pit.getItenSelect().getObs().equals(""))
                obs.setVisibility(View.GONE); else obs.setVisibility(View.VISIBLE);
            DecimalFormat frm = new DecimalFormat("R$ #0.00");
            DecimalFormat qrm = new DecimalFormat("#0.###");
            q_Preco.setText(qrm.format(pit.getItenSelect().getQuantidade())+" x "+
              frm.format(pit.getItenSelect().getProduto().getPreco())+" = "+frm.format(pit.getItenSelect().getValor()));
            diplayQuantidade() ;
        }

        private void setMais(PedidoItem pit) {
            pit.getItenSelect().setQuantidade(pit.getItenSelect().getQuantidade()+1);
//            pit.getItenSelect().setValor(pit.getItenSelect().getQuantidade()*pit.getItenSelect().getProduto().getPreco());
            Dao.getPedidoItemADO(ctx).alterar(pit) ;
        }
    }

 */   private void setEditar(PedidoItem pit) {
        pedidoItem = pit ;
        Dao.getItemSelectADO(getBaseContext()).getList().clear();
        Dao.getItemSelectADO(getBaseContext()).getList().add(pedidoItem.getItenSelect()) ;
        Intent intent = new Intent(getBaseContext(), ActivityProdutoAdd.class) ;
        Bundle params = new Bundle() ;
        params.putInt("ITEMSELECT_ID", pedidoItem.getItenSelect().getId());
        intent.putExtras(params) ;
        startActivityForResult(intent , ITEM_EDICAO);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_envio_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_envitem_icl_prod){
            addProduto();
        }
        //return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        carregaList();
    }
}
