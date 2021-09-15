package com.anequimplus.anequimdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoCaixa;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.VendaVista;
import com.anequimplus.entity.VendaVistaItem;
import com.anequimplus.entity.VendaVistaPagamento;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityVendaVista extends AppCompatActivity {

    private Toolbar toolbar  ;
    private Caixa caixa = null;
    private VendaVista vendaVista ;
    private VendaVistaItem vendaVistaItem ;
    private List<VendaVistaItem> listVendaVistaItems ;
    private static int GRUPO_RETORNO = 3 ;
    private static int ITEM_EDICAO = 4 ;
    private final int PAGAMENTO = 5 ;
    private ListView listViewVendas ;
    private TextView textViewCabecalho ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_vista);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listViewVendas = (ListView) findViewById(R.id.listGradeVenda);
        textViewCabecalho = (TextView) findViewById(R.id.textViewCabecalho);
        caixa = null;
        BottomNavigationView menubottomAppBarConta = findViewById(R.id.navigation_venda_vista);

        menubottomAppBarConta.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_venda_vista_produto){
                    addProduto();
                    return true ;
                }
                if (menuItem.getItemId() == R.id.action_venda_vista_pagamento) {
                    pagamento();
                    return true;
                }
                if (menuItem.getItemId() == R.id.action_consulta_fechamento) {
                    setFechamento() ;
                    return true;
                }
                return true;
            }
        });
    }

    private void setFechamento(){

    }

    private void setLista(){
        try {
            if (Dao.getVendaVistaADO(this).getVedaVIstaAberto() == null){
                startActivity(new Intent(this, ActivityVendaVistaLista.class));

            } else {
                Toast.makeText(this, "Venda aberta!", Toast.LENGTH_SHORT).show(); ;
               // Snackbar.make(this, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show(); ;
        }

    }

    private void pagamento() {
        if (vendaVista != null) {
            Intent intent = new Intent(getBaseContext(), ActivityVendaVistaPagamento.class);
            Bundle params = new Bundle();
            //params.putString("SUBTITULO", "VENDA VISTA "+vendaVista.getId());
            params.putInt("VENDA_VISTA_ID", vendaVista.getId());
            intent.putExtras(params);
            startActivityForResult(intent, PAGAMENTO);
        } else {
            Toast.makeText(this,"Venda Fechada!",Toast.LENGTH_SHORT).show();
        }
    }

    private void addProduto() {
        if (caixa != null) {
          if (vendaVista == null){
                vendaVista = new VendaVista(0, caixa.getId(), new Date(), 1, new ArrayList<VendaVistaItem>(), new ArrayList<VendaVistaPagamento>());
                Dao.getVendaVistaADO(getBaseContext()).inserir(vendaVista);
          }
          Dao.getItemSelectADO(this).getList().clear();
          Intent intent = new Intent(getBaseContext(), ActivityGrupo.class) ;
          Bundle params = new Bundle() ;
          params.putString("SUBTITULO", "VENDA VISTA "+vendaVista.getId());
          intent.putExtras(params) ;
          startActivityForResult(intent, GRUPO_RETORNO);
        } else verificarCaixa() ;
    }

    private void verificarCaixa() {
        try {
            new ConexaoCaixa(this, Link.fConsultaCaixa, 0) {
                @Override
                public void caixaAberto(Caixa c) {
                    caixa = c;
                    addProduto() ;
                }

                @Override
                public void caixaFechado(String msg) {
                    erroSair(msg);
                }

                @Override
                public void erro(String msg) {
                    erroSair(msg);

                }
            }.execute();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado | MalformedURLException e) {
            e.printStackTrace();
            erroSair(e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaList() ;
        //if (caixa == null) setCaixa() ;
    }

    private void carregaList(){
        try {
            vendaVista = Dao.getVendaVistaADO(getBaseContext()).getVedaVIstaAberto() ;
            if (vendaVista == null) {
                textViewCabecalho.setText("Nenhuma Venda Aberta!");
                listViewVendas.setVisibility(View.GONE);
            } else {
                listViewVendas.setVisibility(View.VISIBLE);
                listVendaVistaItems = Dao.getVendaVistaItemADO(getBaseContext()).getList(vendaVista) ;
                listViewVendas.setAdapter(new getAdapterItem(getBaseContext(), listVendaVistaItems));
            }
            display();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };



    private void display(){
        if (vendaVista == null) {
            toolbar.setTitle("VENDA A VISTA");
            toolbar.setSubtitle("Usuário-" + UtilSet.getNome_Usuario(getBaseContext()));
            textViewCabecalho.setText("Nenhuma Venda Aberta!");
        } else {
            toolbar.setTitle("VENDA A VISTA");
            toolbar.setSubtitle("Usuário-" + UtilSet.getNome_Usuario(getBaseContext()));
            DecimalFormat frmQ = new DecimalFormat("#0.###");
            DecimalFormat frmV = new DecimalFormat("R$ #0.00");
            Double q = 0.0 ;
            Double v = 0.0 ;
            for (VendaVistaItem it : listVendaVistaItems){
                q = q + it.getItenSelect().getQuantidade() ;
                v = v + it.getItenSelect().getValor() ;
            }

            textViewCabecalho.setText("Itens ("+listVendaVistaItems.size()+") Quant. ("+frmQ.format(q)+")\nValor "+frmV.format(v));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_venda_vista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_venda_carregar) {
            carregaList();
            return true;
        }
        if (item.getItemId() == R.id.action_venda_login) {
            startActivity(new Intent(getBaseContext(), ActivityLogin.class));
            return true;
        }
        if (item.getItemId() == R.id.action_venda_pagamento) {
            pagamento() ;
            return true;
        }
        if (item.getItemId() == R.id.action_venda_lista) {
            setLista();

            return true;
        }


        return true ; //super.onOptionsItemSelected(item);
    }


    private void setCaixa() {
        try {
            new ConexaoCaixa(this, Link.fConsultaCaixa,  0) {
                @Override
                public void caixaAberto(Caixa c) {
                    caixa = c ;
                    toolbar.setTitle(UtilSet.getNome_Usuario(getBaseContext()));
                }

                @Override
                public void caixaFechado(String msg) {
                    erroSair(msg) ;
                }

                @Override
                public void erro(String msg) {
                    erroSair(msg) ;

                }
            }.execute() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado | MalformedURLException e) {
            e.printStackTrace();
            erroSair(e.getMessage()) ;
        }
    }

    private void erroSair(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Alerta")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GRUPO_RETORNO) {
            if (resultCode == RESULT_OK){
                Log.i("resposta_Pedidos", "Tamanho"+Dao.getItemSelectADO(getBaseContext()).getList().size()) ;
                for (ItenSelect it : Dao.getItemSelectADO(getBaseContext()).getList())
                {
                    if (it.getQuantidade()>0) {
                        Log.i("resposta_VendaVista", it.getProduto().getDescricao());
                        Dao.getVendaVistaItemADO(getBaseContext()).vendaVistaItemAdd(vendaVista, it);
                    }
                }
                carregaList();
            }
        }
        if (requestCode == ITEM_EDICAO){
            if (resultCode == RESULT_OK) {
                Log.i("resposta_edicao", Dao.getItemSelectADO(getBaseContext()).getList().get(0).getProduto().getDescricao());
                vendaVistaItem.setItenSelect(Dao.getItemSelectADO(getBaseContext()).getList().get(0));
                Log.i("DescricaoAlteracao",Dao.getItemSelectADO(getBaseContext()).getList().get(0).getProduto().getDescricao());
                Log.i("DescricaoAlteracao",Dao.getItemSelectADO(getBaseContext()).getList().get(0).getObs());
                Dao.getVendaVistaItemADO(getBaseContext()).alterar(vendaVistaItem);
                carregaList();
            }
        }
        if (requestCode == PAGAMENTO){
            if (resultCode == RESULT_OK) {
                carregaList();
            } else {
                carregaList();

            }
        }

    }

    private class getAdapterItem extends BaseAdapter {
        private Context ctx ;
        private List<VendaVistaItem> list ;
        public getAdapterItem(Context ctx, List<VendaVistaItem> list) {
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
            final VendaVistaItem pit = list.get(i);
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

        private void setMenos(VendaVistaItem pit) {
            if (pit.getItenSelect().getQuantidade() <= 1){
                excluirItem(pit) ;
            } else {
                pit.getItenSelect().setQuantidade(pit.getItenSelect().getQuantidade() - 1);
                pit.getItenSelect().setPreco(pit.getItenSelect().getProduto().getPreco());
                pit.getItenSelect().setValor(pit.getItenSelect().getQuantidade() * pit.getItenSelect().getProduto().getPreco());
                Dao.getVendaVistaItemADO(ctx).alterar(pit) ;

            }
        }

        private void setValores(View row, VendaVistaItem pit){
            TextView descricao = row.findViewById(R.id.textViewPedidoItem);
            TextView qPreco = row.findViewById(R.id.textQPrPedidoItem);
            TextView obs  = row.findViewById(R.id.textIemPrdObs);
            //descricao.setText(pit.getId()+" "+ pit.getItenSelect().getId()+" "+pit.getItenSelect().getProduto().getDescricao());
            descricao.setText(pit.getItenSelect().getProduto().getDescricao());
            obs.setText(pit.getItenSelect().getObs());
            if (pit.getItenSelect().getObs().equals(""))
                obs.setVisibility(View.GONE); else obs.setVisibility(View.VISIBLE);
            DecimalFormat frm = new DecimalFormat("R$ #0.00");
            DecimalFormat qrm = new DecimalFormat("#0.###");
            qPreco.setText(qrm.format(pit.getItenSelect().getQuantidade())+" x "+
                    frm.format(pit.getItenSelect().getPreco())+" = "+frm.format(pit.getItenSelect().getValor()));
            display() ;
            //diplayQuantidade() ;
        }

        private void setMais(VendaVistaItem pit) {
            pit.getItenSelect().setQuantidade(pit.getItenSelect().getQuantidade()+1);
            pit.getItenSelect().setPreco(pit.getItenSelect().getProduto().getPreco());
            pit.getItenSelect().setValor(pit.getItenSelect().getQuantidade()*pit.getItenSelect().getProduto().getPreco());
            Dao.getVendaVistaItemADO(ctx).alterar(pit) ;
        }
    }

    private void excluirItem(final VendaVistaItem pit) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage("Deseja Estornar o Item?")
                .setCancelable(false).setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dao.getVendaVistaItemADO(getBaseContext()).excluir(pit);
                        carregaList();

                    }
                }).show();
    }

    private void setEditar(VendaVistaItem pit) {
        vendaVistaItem = pit ;
        Dao.getItemSelectADO(getBaseContext()).getList().clear();
        Dao.getItemSelectADO(getBaseContext()).getList().add(vendaVistaItem.getItenSelect()) ;
        Intent intent = new Intent(getBaseContext(), ActivityProdutoAdd.class) ;
        Bundle params = new Bundle() ;
        params.putInt("ITEMSELECT_ID", vendaVistaItem.getItenSelect().getId());
        intent.putExtras(params) ;
        startActivityForResult(intent , ITEM_EDICAO);
    }

}
