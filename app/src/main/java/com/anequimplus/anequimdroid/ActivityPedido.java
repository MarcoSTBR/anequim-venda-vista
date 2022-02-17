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
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoContaPedido;
import com.anequimplus.conexoes.ConexaoEnvioPedido;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.utilitarios.DisplaySet;
import com.anequimplus.utilitarios.UtilSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.net.MalformedURLException;
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
        toolbar.setSubtitle("Usuário: "+ UtilSet.getUsuarioNome(this)) ;

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
                        addPedido();
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
        Dao.getPedidoADO(this).limparPedidosVazios() ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("STATUS", "=", "1")) ;
        try {
            new ConexaoContaPedido(this, filters) {
                @Override
                public void oK(List<ContaPedido> l) {
                    // inserir a conta só pra efeito de display
                    // Lista de Pedidos pendentes ;
                    pedidoList =  Dao.getPedidoADO(getBaseContext()).getList(new ArrayList<FilterTable>()) ;
                    for (ContaPedido cp: l) {
                        Pedido p = new Pedido(cp.getId(), cp.getPedido(), cp.getData(), new ArrayList<PedidoItem>()) ;
                        for (ContaPedidoItem it : cp.getListContaPedidoItem()) {
                            ItenSelect i = new ItenSelect(0, null, it.getQuantidade(), it.getPreco(),it.getDesconto(),it.getComissao(),it.getValor(), it.getObs(),it.getStatus()) ;
                            p.getListPedidoItem().add(new PedidoItem(it.getId(), p.getId(), i)) ;
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
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
                            .setNegativeButton("Não", null)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
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
        try {
            new ConexaoEnvioPedido(this, Dao.getPedidoADO(this).getList(new ArrayList<FilterTable>())) {
                @Override
                public void envioOK(List<Pedido> l) {
                    display();

                }

                @Override
                public void erroEnvio(String msg) {
                    setAlert(msg);
                }

            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            setAlert(e.getMessage());
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            setAlert(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            setAlert(e.getMessage());
        }
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

    private void addPedido(){
        String ped = editPedido.getText().toString() ;
        if (!ped.equals("") && (!ped.isEmpty())) {
            Intent intent = new Intent(getBaseContext(), ActivityEnvioPedido.class);
            Bundle params = new Bundle();
            params.putString("PEDIDO", ped);
            intent.putExtras(params);
            startActivity(intent);
        } else alert("Digite o número da conta!");

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
            addPedido() ;
            return true;
        }
        if (item.getItemId() == R.id.action_pedido_atualizar) {
            display();
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
