package com.anequimplus.anequimdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoCaixa;
import com.anequimplus.conexoes.ConexaoVendaVista;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.VendaVista;
import com.anequimplus.entity.VendaVistaItem;
import com.anequimplus.entity.VendaVistaLista;
import com.anequimplus.entity.VendaVistaPagamento;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityVendaVistaLista extends AppCompatActivity {

    private Toolbar toolbar;
    private Caixa caixa = null;
    private ListView listaVendaVista ;
    private List<VendaVista> vendaVistaList ;
    private List<VendaVistaLista> vendaVistaListas ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_vista_lista);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listaVendaVista = (ListView) findViewById(R.id.listaVendaVista) ;
        vendaVistaListas = new ArrayList<VendaVistaLista>() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_venda_vista_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        if (item.getItemId() == R.id.action_venda_lista_carregar) {
            getListServer() ;
            return true;
        }

        if (item.getItemId() == R.id.action_venda_lista_login) {
            startActivity(new Intent(getBaseContext(), ActivityLogin.class));
            return true;
        }

        if (item.getItemId() == R.id.action_venda_lista_enviar) {
            setEnviar();
            return true;
        }


        return true ;//super.onOptionsItemSelected(item);
    }

    private void setEnviar()  {
        try {
            new ConexaoVendaVista(this, Dao.getVendaVistaADO(this).getListArrayJson()) {
                @Override
                public void ok(List<VendaVista> l) {
                    getListServer() ;
                }

                @Override
                public void erro(String msg) {
                    alert(msg) ;
                }
            }.execute() ;
        } catch (JSONException | ParseException | MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("VENDA VISTA");
        toolbar.setSubtitle(UtilSet.getNome_Usuario(getBaseContext()));
        setCaixa() ;
     //   getListaLocal() ;
    }
    private void setCaixa() {
        try {
            new ConexaoCaixa(this, Link.fConsultaCaixa,  0) {
                @Override
                public void caixaAberto(Caixa c) {
                    caixa = c ;
                    SimpleDateFormat d = new SimpleDateFormat("dd/MM/yy HH:mm");
                    toolbar.setSubtitle(UtilSet.getNome_Usuario(getBaseContext())+" "+d.format(c.getData()));
                    setEnviar();

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

    private void erroSair(String msg) {
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

    private void getListaLocal(List<VendaVista> l) {
        try {
            vendaVistaListas.clear();
            vendaVistaList = Dao.getVendaVistaADO(this).getList() ;
            for (VendaVista v : vendaVistaList){
                vendaVistaListas.add(new VendaVistaLista(vendaVistaListas.size()+1,v,2));
            }

            for (VendaVista v : l){
                vendaVistaListas.add(new VendaVistaLista(vendaVistaListas.size()+1,v,3));
            }
            listaVendaVista.setAdapter(new getAdapter(this, vendaVistaListas));

        } catch (ParseException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    private void getListServer(){
        try {
            new ConexaoVendaVista(this, caixa) {
                @Override
                public void ok(List<VendaVista> l) {
                    getListaLocal(l) ;
                }

                @Override
                public void erro(String msg) {
                    alert(msg) ;
                }
            }.execute();
        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage()) ;
        }
    }

    private void alert(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                //.setTitle("Alerta")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();

    }


    private class getAdapter extends BaseAdapter{
       private Context ctx ;
       private List<VendaVistaLista> list ;

        public getAdapter(Context ctx, List<VendaVistaLista> list) {
            this.ctx = ctx;
            this.list = list;
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
            return list.get(i).getId() ;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            final View  row  = inflater.inflate(R.layout.layout_grade_venda_vista_lista, null) ;
            final VendaVistaLista vv = list.get(i) ;
            displayItens(row, vv) ;
            displayPg(row, vv) ;
            Button buttonDemo = (Button) row.findViewById(R.id.buttonDemostrativo) ;
            buttonDemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDemostrativo(vv) ;
                }
            });
            Button buttonNfce = (Button) row.findViewById(R.id.buttonNFCe) ;
            buttonNfce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setNFCe(vv) ;
                }
            });
            if (vv.getStatus() != 3) {
                buttonDemo.setVisibility(View.GONE);
                buttonNfce.setVisibility(View.GONE);
            } else {
                buttonDemo.setVisibility(View.VISIBLE);
                buttonNfce.setVisibility(View.VISIBLE);

            }
            return row ;
        }

        private void displayItens(View row, VendaVistaLista vv){
            LinearLayout llDescricao = (LinearLayout) row.findViewById(R.id.LLayoutDescricao) ;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            inflater.inflate(R.layout.layout_grade_venda_vista_lista, null) ;
            DecimalFormat fq = new DecimalFormat("#0.###") ;
            DecimalFormat fv = new DecimalFormat("R$ #0.00") ;
            for (VendaVistaItem it : vv.getVendaVista().getVendaVistaItems()){
                if (it.getStatus() == 1){
                     String d = "("+fq.format(it.getItenSelect().getQuantidade()) + ") "+
                             it.getItenSelect().getProduto().getDescricao();
                     String v = fv.format(it.getItenSelect().getValor()) ;
                     //View l = new View(ctx) ;
                     View l  = inflater.inflate(R.layout.layout_linha_venda_vista_descricao, null) ;
                     TextView tDesc = (TextView)  l.findViewById(R.id.tVendaVistaDescricao) ;
                     TextView tValor = (TextView)  l.findViewById(R.id.tVendaVistaValor) ;
                     tDesc.setText(d);
                     tValor.setText(v);
                     llDescricao.addView(l);

                }
            }
           // TextView text = (TextView) row.findViewById(R.id.textViewVendaVistaItens) ;
            //text.setText(txt);
        }

        private void displayPg(View row, VendaVistaLista vv){
            LinearLayout llDescricao = (LinearLayout) row.findViewById(R.id.LLayoutPG) ;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            inflater.inflate(R.layout.layout_grade_venda_vista_lista, null) ;
            DecimalFormat fq = new DecimalFormat("#0.###") ;
            DecimalFormat fv = new DecimalFormat("R$ #0.00") ;
            double vtotal = 0 ;
            for (VendaVistaItem it : vv.getVendaVista().getVendaVistaItems()){
                if (it.getStatus() == 1){
                    vtotal = vtotal + it.getItenSelect().getValor() ;
                }
            }
            View lT  = inflater.inflate(R.layout.layout_linha_venda_vista_pg, null) ;
            TextView tDescT = (TextView)  lT.findViewById(R.id.tVendaVistaModalidade) ;
            TextView tValorT = (TextView)  lT.findViewById(R.id.tVendaVistaValorPg) ;
            tDescT.setText("TOTAL");
            tValorT.setText(fv.format(vtotal));
            llDescricao.addView(lT);
           // txt =  getString("TOTAL",fv.format(vtotal)) ;
            for (VendaVistaPagamento it : vv.getVendaVista().getVendaVistaPagamentos()){
                if (it.getStatus() == 1){
                    String d = it.getModalidade().getDescricao();
                    String v = fv.format(it.getValor()) ;
                    View l  = inflater.inflate(R.layout.layout_linha_venda_vista_pg, null) ;
                    TextView tDesc = (TextView)  l.findViewById(R.id.tVendaVistaModalidade) ;
                    TextView tValor = (TextView)  l.findViewById(R.id.tVendaVistaValorPg) ;
                    tDesc.setText(d);
                    tValor.setText(v);
                    llDescricao.addView(l);

                }
            }
            //TextView text = (TextView) row.findViewById(R.id.textViewVendaVistaPgs) ;
           // text.setText(txt);
        }

        private String getString(String a, String b) {
            int tam = 48 ;
            String s = a + " " + b ;
            return s ;
        }
    }

    private void setNFCe(VendaVistaLista vv) {
        Toast.makeText(this,"NFCE "+vv.getId(), Toast.LENGTH_SHORT).show();
    }

    private void setDemostrativo(VendaVistaLista vv) {
        Toast.makeText(this,"Demonstrativo "+vv.getId(), Toast.LENGTH_SHORT).show();
    }


}
