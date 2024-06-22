package com.anequimplus.impressao;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.utilitarios.RowImpressao;

import java.util.List;

public class ContaPedidoNenhum implements ControleImpressora {
    private Context ctx ;
    private ListenerImpressao listenerImpressao ;

    public ContaPedidoNenhum(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void open() {
       // Toast.makeText(ctx, "Selecione Uma Impressora", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void close() {

    }

    @Override
    public void setListenerImpressao(ListenerImpressao listenerImpressao) {
        this.listenerImpressao = listenerImpressao ;
        //Toast.makeText(ctx, "Selecione Uma Impressora", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void imprimeConta(ContaPedido conta) {
//        Toast.makeText(ctx, "Selecione Uma Impressora", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(ctx)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage("Selecione Uma Impressora, Deseja Continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listenerImpressao.onImpressao(1);
                    }
                }).
                setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listenerImpressao.onError(0,"Impressão cancelada");
                            }
                        }
                ).show();
    }

    @Override
    public void imprimirXML(String txt) {
        new AlertDialog.Builder(ctx)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage("Selecione Uma Impressora, Deseja Continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listenerImpressao.onImpressao(1);
                    }
                }).
                setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listenerImpressao.onError(0,"Impressão cancelada");
                            }
                        }
                ).show();
//        listenerImpressao.onError(0,"Selecione Uma Impressora");
    }

    @Override
    public void impressaoLivre(List<RowImpressao> l) {
        new AlertDialog.Builder(ctx)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage("Selecione Uma Impressora, Deseja Continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listenerImpressao.onImpressao(1);
                    }
                }).
                setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listenerImpressao.onError(0,"Impressão cancelada");
                            }
                        }
                ).show();
//        listenerImpressao.onError(0,"Selecione Uma Impressora");
    }

    @Override
    public void imprimirRecibo(ContaPedido conta) {
        new AlertDialog.Builder(ctx)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage("Selecione Uma Impressora, Deseja Continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listenerImpressao.onImpressao(1);
                    }
                }).
                setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listenerImpressao.onError(0,"Impressão cancelada");
                            }
                        }
                ).show();
//        listenerImpressao.onError(0,"Selecione Uma Impressora");

    }
}
