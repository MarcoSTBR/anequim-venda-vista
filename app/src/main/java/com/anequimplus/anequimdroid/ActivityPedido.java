package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.PedidoAdapterView;
import com.anequimplus.ado.Dao;
import com.anequimplus.conexoes.ConexaoContaPedido;
import com.anequimplus.conexoes.ConexaoEnvioPedido;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.utilitarios.DisplaySet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityPedido extends AppCompatActivity {

    private EditText editPedido ;
    private RecyclerView listPedido ;
    private List<Pedido> pedidoList ;
    private Pedido pedido = null ;
    private FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPedidos() ;
            }
        });

        editPedido = findViewById(R.id.editTextPedido);
        editPedido.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        setPedido();
                        return true;
                    }
                }
                return false;
            }
        });
        listPedido = (RecyclerView)findViewById(R.id.listPedidoGrade) ;

    }

    @Override
    protected void onResume() {
        super.onResume();
        editPedido.setText("");
        display() ;
    }

    private void display(){
        // Lista de Pedidos pendentes ;
        pedidoList =  Dao.getPedidoADO(getBaseContext()).getList() ;
        new ConexaoContaPedido(this) {
            @Override
            public void oK(List<ContaPedido> l) {
                for (ContaPedido cp: l) {
                    Pedido p = new Pedido(cp.getId(), cp.getPedido(), cp.getData(), new ArrayList<PedidoItem>()) ;
                    for (ContaPedidoItem it : cp.getListContaPedidoItem()) {
                        ItenSelect i = new ItenSelect(0, null, it.getQuantidade(), 0,0,it.getValor(), "") ;
                        p.getListPedidoItem().add(new PedidoItem(it.getId(), p, i)) ;
                    }
                    p.setStatus(2);
                    pedidoList.add(p) ;
                }
                displayView(pedidoList);
            }

            @Override
            public void erro(String mgg) {
                displayView(pedidoList);
                alert(msg);

            }
        }.execute();
    }

    private void displayView(List<Pedido> l){
        GridLayoutManager layoutManager=new GridLayoutManager(this, DisplaySet.getNumeroDeColunasGrade(this));
        listPedido.setLayoutManager(layoutManager);
        listPedido.setAdapter(new PedidoAdapterView(l) {
            @Override
            public void setConta(Pedido p) {
                if (p.getStatus() == 2) {
                    viewConta(p) ;
                } else {
                    new AlertDialog.Builder(ActivityPedido.this)
                            .setIcon(R.drawable.ic_notifications_black_24dp)
                            .setTitle("Pedido pendente")
                            .setMessage("Deseja enviar?")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    enviarPedidos();
                                }
                            }).show();

                }

            }

            @Override
            public void setContaAbrir(Pedido p) {
                editPedido.setText(p.getPedido()) ;
                addPedido();
            }
        });
    }

    public void viewConta(Pedido p){
        Intent intent = new Intent(getBaseContext(), ActivityConta.class);
        Bundle params = new Bundle();
        params.putInt("CONTA_ID", p.getId());
        intent.putExtras(params);
        startActivity(intent);
    }

    private void enviarPedidos() {
            new ConexaoEnvioPedido(this) {
                @Override
                public void envioOK(int count) {
                    display();
                }

                @Override
                public void erroEnvio(String msg) {
                    setAlert(msg);
                }

            }.execute();
    }

    private void alert(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok",null).show();
    }

    private void setAlert(String txt){
        new AlertDialog.Builder(this)
                .setTitle("Alerta")
                .setMessage(txt)
                .setPositiveButton("OK",null).show();
    }

    public void setPedido(){
        if (editPedido.getText().toString().equals("")){
            setAlert("Pedido inválido!") ;
        } else  addPedido();
    }


    private void addPedido(){
        pedido = Dao.getPedidoADO(this).getPedido(editPedido.getText().toString()) ;
        Intent intent = new Intent(getBaseContext(), ActivityEnvioPedido.class) ;
        Bundle params = new Bundle() ;
        params.putInt("PEDIDO_ID", pedido.getId());
        intent.putExtras(params) ;
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_conf) {
            startActivity(new Intent(getBaseContext(), ActivityConfiguracao.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            startActivity(new Intent(getBaseContext(), ActivityLogin.class));
            return true;
        }
        if (item.getItemId() == R.id.action_pedido_ok) {
            setPedido();
            return true;
        }
        return true ; //super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        displayView(pedidoList);
    }

}
