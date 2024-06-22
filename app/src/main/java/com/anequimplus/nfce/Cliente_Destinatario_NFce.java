package com.anequimplus.nfce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoDest;
import com.anequimplus.utilitarios.UtilSet;

public class Cliente_Destinatario_NFce {

    private Context ctx ;
    private EditText cpf_cnpj_dest ;
    private EditText nome_dest ;
    private Button cancelar_dest ;
    private Button ok_dest ;
    private ContaPedidoDest contaPedidoDest ;
    private ContaPedido contaPedido ;
    private ListenerClienteDestinatario listenerClienteDestinatario ;
    private AlertDialog.Builder myBuiler ;
    private AlertDialog janela ;

    public Cliente_Destinatario_NFce(Context ctx, ContaPedido contaPedido, ContaPedidoDest contaPedidoDest, ListenerClienteDestinatario listenerClienteDestinatario){
        this.ctx = ctx ;
        this.contaPedido = contaPedido ;
        this.contaPedidoDest = contaPedidoDest ;
        this.listenerClienteDestinatario = listenerClienteDestinatario ;
        bind() ;

    }

    private void bind(){
        myBuiler = new AlertDialog.Builder(ctx) ;
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_cliente_destinatario, null);
        cpf_cnpj_dest = view.findViewById(R.id.cpf_cnpj_dest) ;
        nome_dest     = view.findViewById(R.id.nome_dest) ;
        cancelar_dest = view.findViewById(R.id.cancelar_dest) ;
        cancelar_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                janela.dismiss();
                listenerClienteDestinatario.cancelar("A Nota Não Foi Emitida!");
            }
        });
        ok_dest = view.findViewById(R.id.ok_dest) ;
        ok_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                janela.dismiss();
                if (cpf_cnpj_dest.getText().length() > 0){
                    contaPedidoDest = new ContaPedidoDest(0, UtilSet.getUUID(), contaPedido.getId(),cpf_cnpj_dest.getText().toString(), nome_dest.getText().toString(), 1) ;
                }
                listenerClienteDestinatario.ok(contaPedidoDest);
            }
        });
        myBuiler.setView(view) ;

    }

    public void executar(){
        janela = myBuiler.create() ;
        janela.show();
    }



}
