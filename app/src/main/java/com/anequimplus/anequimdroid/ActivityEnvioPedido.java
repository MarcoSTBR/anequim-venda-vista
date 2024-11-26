package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.EdicaoItem;
import com.anequimplus.adapter.PedidoEnvioAdapter;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildEnviarPedido;
import com.anequimplus.builds.BuildEnvioComandaRemota;
import com.anequimplus.controler.ControleAcompanhamentoSelecao;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ItemSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.entity.Produto;
import com.anequimplus.listeners.ListenerAcompanhamentoSelect;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerEnvioComandaRemota;
import com.anequimplus.listeners.ListenerEnvioPedido;
import com.anequimplus.utilitarios.DisplaySet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityEnvioPedido extends AppCompatActivity {

    private ImageButton imagelupaproduto ;
    private EditText editProdutoPesquisa ;
    private RecyclerView listaPrdPedido ;
    private Toolbar toolbar ;
    private Pedido pedido ;
    private String pedidoSelecionado ;
    private EdicaoItem edicaoItem = null;
    private final int ACOMPANHAMENTO = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviopedido);
        pedidoSelecionado = getIntent().getStringExtra("PEDIDO") ;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle("Envio de Pedido");
/*

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarConexaoPedidos();
            }
        });
*/

        Button pedido_enviar = findViewById(R.id.botao_pedido_enviar);
        pedido_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarConexaoPedidos();
            }
        });

        listaPrdPedido  = findViewById(R.id.listaPrdPedido);
        imagelupaproduto  = findViewById(R.id.imagelupaproduto);
        imagelupaproduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityEnvioPedido.this, ActivityProdutoList.class) ;
                Bundle params = new Bundle() ;
                params.putString("PEDIDO", pedidoSelecionado);
                intent.putExtras(params) ;
                startActivity(intent);
            }
        });


        editProdutoPesquisa  = findViewById(R.id.editProdutoPesquisa);
        editProdutoPesquisa.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        addProduto();
                        return true;
                    }

                }
                return false;
            }
        });

        Button botao_produto = findViewById(R.id.botao_pedido_produto) ;
        botao_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProdutoPeloGrupoProduto();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Pedido : "+pedidoSelecionado);
        edicao() ;
        carregaList() ;
        editProdutoPesquisa.setText("");
        getConta() ;
    }


    /*
        private void addProduto() {
            String codigo = editProdutoPesquisa.getText().toString() ;
            if (codigo.length() > 0) {
                Produto p = DaoDbTabela.getProdutoADO(this).getCodigo(codigo) ;
                if (p != null){
                    Pedido ped = DaoDbTabela.getPedidoADO(this).getPedido(pedidoSelecionado) ;
                    Double com = p.getComissao() / 100 * p.getPreco() ;
                    PedidoItem pedidoItem = new PedidoItem(0, ped.getId(), new ItemSelect(0, p, 1, p.getPreco(), 0, com, p.getPreco(), "", 1)) ;
                    DaoDbTabela.getPedidoItemADO(this).incluir(pedidoItem);
                    editProdutoPesquisa.setText("");
                    carregaList() ;
                } else {
                    editProdutoPesquisa.setText("");
                    Toast.makeText(this, "Código "+codigo+" Não Encontrado!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    */
    private void addProduto() {
        String codigo = editProdutoPesquisa.getText().toString() ;
        if (codigo.length() > 0) {
            Produto p = DaoDbTabela.getProdutoADO(this).getCodigo(codigo) ;
            if (p != null){
                addItem(p) ;
            } else {
                editProdutoPesquisa.setText("");
                Toast.makeText(this, "Código "+codigo+" Não Encontrado!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addItem(Produto p){
        final Pedido ped = DaoDbTabela.getPedidoADO(this).getPedido(pedidoSelecionado) ;
        Double com = p.getComissao() / 100 * p.getPreco() ;
        ItemSelect item = new ItemSelect(0, p, 0, p.getPreco(), 0, com, p.getPreco(), "", 1) ;
        PedidoItem it = new PedidoItem(0, ped.getId(), item, new ArrayList<PedidoItemAcomp>()) ;
        DaoDbTabela.getItemSelectADO().getList().clear();
        DaoDbTabela.getItemSelectADO().getList().add(it) ;

            new ControleAcompanhamentoSelecao(ActivityEnvioPedido.this, it, new ListenerAcompanhamentoSelect() {
                @Override
                public void ok(PedidoItem it) {
                    //PedidoItem it =  DaoDbTabela.getItemSelectADO().get(0) ;
                    DaoDbTabela.getPedidoItemADO(ActivityEnvioPedido.this).incluir(DaoDbTabela.getItemSelectADO().get(0));
                    editProdutoPesquisa.setText("");
                    carregaList();
                }

                @Override
                public void setAcompanhamento(PedidoItem it) {
                    Bundle params = new Bundle();
                    Intent intent = new Intent(ActivityEnvioPedido.this, ActivityItemSelect.class);
                    params.putInt("id",it.getId());
                    intent.putExtras(params);
                    startActivityForResult(intent, ACOMPANHAMENTO);
                }

                @Override
                public void erro(String msg) {
                    Toast.makeText(ActivityEnvioPedido.this, msg, Toast.LENGTH_SHORT).show();
                }
            }).mais();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACOMPANHAMENTO) {
            if (resultCode == RESULT_OK){
                PedidoItem it =  DaoDbTabela.getItemSelectADO().get(0) ;
                DaoDbTabela.getPedidoItemADO(ActivityEnvioPedido.this).incluir(it);
                editProdutoPesquisa.setText("");
                carregaList();
            }
        }
    }
    private void getConta(){
        FilterTables filter = new FilterTables() ;
        filter.add(new FilterTable("PEDIDO", "=", pedidoSelecionado ));
        filter.add(new FilterTable("STATUS", "=", "1" ));
        new BuildContaPedido(this, filter.getList(), "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                if (l.size()>0){
                    if (l.get(0).getNum_impressao() > 0){
                        alertAviso("Esta Conta Já Foi Emitida!") ;
                    }
                }
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(ActivityEnvioPedido.this, msg, Toast.LENGTH_SHORT).show();

            }
        }).executar();

    }

    private void alertAviso(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Aviso:")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    private void edicao() {
      edicaoItem = new EdicaoItem() {
          @Override
          public void mais(PedidoItem pedidoItem, double q) {
              pedidoItem.getItemSelect().setQuantidade(q);
              setValoresGrade(pedidoItem);
          }

          @Override
          public void menos(PedidoItem pedidoItem, double q) {
              if (q <= 0) {
                  new AlertDialog.Builder(ActivityEnvioPedido.this)
                          .setIcon(android.R.drawable.presence_busy)
                          .setTitle(pedidoItem.getItemSelect().getProduto().getDescricao())
                          .setMessage("Deseja Excluir o Produto?")
                          .setCancelable(false).setNegativeButton("Não", null)
                          .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  carregaList();

                              }
                          })
                          .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  DaoDbTabela.getPedidoItemADO(ActivityEnvioPedido.this).excluir(pedidoItem);
                                  carregaList();
                              }
                          }).show();
              } else {
                  pedidoItem.getItemSelect().setQuantidade(q);
                  setValoresGrade(pedidoItem);
              }
          }

          @Override
          public void editar(PedidoItem pedidoItem) {
              Intent intent = new Intent(ActivityEnvioPedido.this, ActivityProdutoAdd.class) ;
              Bundle params = new Bundle() ;
              params.putInt("ITEMSELECT_ID", pedidoItem.getId());
              intent.putExtras(params) ;
              startActivity(intent);
          }
      };

    }

    private void setValoresGrade(PedidoItem pedidoItem) {
        double q = pedidoItem.getItemSelect().getQuantidade() ;
        double prc = pedidoItem.getItemSelect().getPreco() ;
        double vl = q * prc ;
        double cm = pedidoItem.getItemSelect().getComissao() / 100 * vl ;
        pedidoItem.getItemSelect().setValor(vl);
        pedidoItem.getItemSelect().setComissao(cm);
        DaoDbTabela.getPedidoItemADO(this).alterar(pedidoItem);
    }


    private void enviarConexaoPedidos()  {
       // Pedido ped = DaoDbTabela.getPedidoADO(this).getPedido(pedidoSelecionado) ;
        List<Pedido> lped = DaoDbTabela.getPedidoADO(this).getList(new FilterTables(), "ID") ;
       // lped.add(ped);
        new BuildEnviarPedido(this, lped, new ListenerEnvioPedido() {
            @Override
            public void envioOK(List<Pedido> l) {
                comandaRemota(l) ;
            }

            @Override
            public void erroEnvio(String msg) {
                alert(msg);

            }
        }).executar();
/*
        try {
            new ConexaoEnvioPedido(this, ped) {
                @Override
                public void envioOK(List<Pedido> l) {
                    enviado() ;

                }

                @Override
                public void erroEnvio(String msg) {
                            alert(msg);
                        }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            alert(e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
*/
    }

    private void comandaRemota(List<Pedido> l){
        new BuildEnvioComandaRemota(this, l, 0, new ListenerEnvioComandaRemota() {
            @Override
            public void ok(String msg) {
                new AlertDialog.Builder(ActivityEnvioPedido.this)
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

            @Override
            public void erro(String msg) {
                Toast.makeText(ActivityEnvioPedido.this, msg, Toast.LENGTH_SHORT).show();

            }
        }).executar();
    }

    private void addProdutoPeloGrupoProduto() {
        Intent intent = new Intent(getBaseContext(), ActivityGradeVendas.class) ;
        Bundle params = new Bundle() ;
        params.putString("PEDIDO", pedidoSelecionado);
        params.putString("SUBTITULO", "Pedido "+pedidoSelecionado);
        intent.putExtras(params) ;
        //startActivityForResult(intent, GRUPO_RETORNO);
        startActivity(intent);
    }

    private void carregaList(){
        pedido  = DaoDbTabela.getPedidoADO(this).getPedido(pedidoSelecionado) ;
        if (pedido != null) {
            //GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
            StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(DisplaySet.getNumeroDeColunasGrade(this), StaggeredGridLayoutManager.VERTICAL);
            listaPrdPedido.setLayoutManager(layoutManager);
            listaPrdPedido.setAdapter(new PedidoEnvioAdapter(this, pedido.getListPedidoItem(), edicaoItem));
            diplayQuantidade();
            Log.i("listpedidoaberto", " pedido "+pedido.getPedido()+" size "+pedido.getListPedidoItem().size()) ;

        } else   Log.i("listpedidoaberto", "pedido.getListPedidoItem() nulo") ;
    }

    private void diplayQuantidade(){
      DecimalFormat frmQ = new DecimalFormat("#0.###");
      DecimalFormat frmV = new DecimalFormat("R$ #0.00");
      Double q = 0.0 ;
      Double v = 0.0 ;
      for (PedidoItem it : pedido.getListPedidoItem()){
          q = q + it.getItemSelect().getQuantidade() ;
          v = v + it.getItemSelect().getValor() + it.getItemSelect().getComissao();
      }
      toolbar.setSubtitle("Item(s) "+frmQ.format(q)+" = "+frmV.format(v))  ;
    }

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
                        DaoDbTabela.getPedidoItemADO(getBaseContext()).excluir(pit);
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
                DaoDbTabela.getPedidoItemADO(ctx).alterar(pit) ;
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
            DaoDbTabela.getPedidoItemADO(ctx).alterar(pit) ;
        }
    }

 */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_envio_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        if (item.getItemId() == R.id.action_envitem_icl_prod){
            addProdutoPeloGrupoProduto();
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
