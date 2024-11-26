package com.anequimplus.anequimdroid.fragment_conta_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.PedidoAdapterView;
import com.anequimplus.anequimdroid.ActivityConta;
import com.anequimplus.anequimdroid.ActivityEnvioPedido;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.builds.BuildContaPedidoView;
import com.anequimplus.builds.BuildEnviarPedido;
import com.anequimplus.builds.BuildEnvioComandaRemota;
import com.anequimplus.entity.ContaPedidoView;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Pedido;
import com.anequimplus.listeners.ListenerContaPedidoView;
import com.anequimplus.listeners.ListenerEnvioComandaRemota;
import com.anequimplus.listeners.ListenerEnvioPedido;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.DisplaySet;

import java.util.ArrayList;
import java.util.List;

public class Fragment_pedidos extends Fragment {

    private EditText editPedido ;
    private RecyclerView listPedido ;
    private List<ContaPedidoView> pedidoListView;
    private Pedido pedido = null ;
    private Button entrar ;
    private ImageButton atualiza_contas ;
    private Button enviar_Pedidos ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        editPedido = view.findViewById(R.id.editTextPedido) ;
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
        listPedido = (RecyclerView) view.findViewById(R.id.listPedidoGrade) ;
        entrar = view.findViewById(R.id.entrar) ;
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPedido() ;
            }
        });
        atualiza_contas = view.findViewById(R.id.atualiza_contas) ;
        atualiza_contas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display();
            }
        });
        enviar_Pedidos = view.findViewById(R.id.enviar_Pedidos) ;
        enviar_Pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPedidos() ;
            }
        });

        //iniciar() ;
        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        editPedido.setText("");
        Log.i("pedido", "OnResume") ;
        iniciar() ;
    }

    private void iniciar(){
        DaoDbTabela.getPedidoADO(getContext()).limparPedidosVazios();
        if (Configuracao.getAcumulaPedidos(getContext())){
            display() ;
        } else {
            enviarPedidos();
        }

    }

    private void addPedido(){
        String ped = editPedido.getText().toString() ;
        if (!ped.equals("") && (!ped.isEmpty())) {
            Intent intent = new Intent(getContext(), ActivityEnvioPedido.class);
            Bundle params = new Bundle();
            params.putString("PEDIDO", ped);
            intent.putExtras(params);
            startActivity(intent);
        } else alert("Digite o número da conta!");
    }

    private void setAlert(String txt){
        new AlertDialog.Builder(getContext())
                .setTitle("Alerta")
                .setMessage(txt)
                .setPositiveButton("OK",null).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        displayView(pedidoListView);
    }


    public void viewConta(ContaPedidoView p){
        Log.i("viewConta", "viewConta "+p.getPedido()+" id "+p.getId()) ;
        Intent intent = new Intent(getContext(), ActivityConta.class);
        Bundle params = new Bundle();
        params.putInt("CONTA_ID", p.getId());
        intent.putExtras(params);
        startActivity(intent);
    }

    private void enviarPedidos() {
        DaoDbTabela.getPedidoADO(getContext()).limparPedidosVazios() ;
        List<Pedido> list = DaoDbTabela.getPedidoADO(getContext()).getList(new FilterTables(), "PEDIDO") ;
        if (list.size() > 0) {
            new BuildEnviarPedido(getActivity(), list, new ListenerEnvioPedido() {
                @Override
                public void envioOK(List<Pedido> l) {
                    comandaRemota(l) ;
                }

                @Override
                public void erroEnvio(String msg) {
                    alert(msg);
                }
            }).executar();

        } else display();
    }

    private void comandaRemota(List<Pedido> l){

        new BuildEnvioComandaRemota(getActivity(), l, 0, new ListenerEnvioComandaRemota() {
            @Override
            public void ok(String msg) {
                display();
            }

            @Override
            public void erro(String msg) {
                alert(msg);
                //Toast.makeText(ActivityEnvioPedido.this, msg, Toast.LENGTH_SHORT).show();

            }
        }).executar();
    }

    private void alert(String txt){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok",null).show();
    }



    private void display(){
        Log.i("display", "acionou") ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("STATUS", "=", "1")) ;
        new BuildContaPedidoView(getActivity(), filters, "", new ListenerContaPedidoView() {
            @Override
            public void ok(List<ContaPedidoView> l) {
               addPedidoView();
               for (ContaPedidoView c : l){
                   pedidoListView.add(c) ;
               }
                displayView(pedidoListView);
            }

            @Override
            public void erro(int cod, String msg) {
                alert(cod+" "+msg);

            }
        }).executar();
    }

    private void addPedidoView(){
        pedidoListView = new ArrayList<ContaPedidoView>() ;
        List<Pedido> ll = DaoDbTabela.getPedidoADO(getContext()).getList(new FilterTables(), "PEDIDO") ;
        for (Pedido p : ll) {
            Log.i("addPedidoView", p.getPedido()) ;
           pedidoListView.add(new ContaPedidoView(0, p.getPedido(), p.getData(), 2, .0, .0));
        }
    }


    private void displayView(List<ContaPedidoView> l){
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(), DisplaySet.getNumeroDeColunasGrade(getContext()));
        listPedido.setLayoutManager(layoutManager);
        listPedido.setAdapter(new PedidoAdapterView(getContext(), l) {
            @Override
            public void setConta(ContaPedidoView p) {
                if (p.getStatus() == 1) {
                    viewConta(p) ;
                } else {
                    new AlertDialog.Builder(getContext())
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
            public void setContaAbrir(ContaPedidoView p) {
                editPedido.setText(p.getPedido()) ;
                addPedido();
            }
        });
    }


}