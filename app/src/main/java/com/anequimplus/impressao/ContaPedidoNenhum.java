package com.anequimplus.impressao;

import android.content.Context;
import android.widget.Toast;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.utilitarios.RowImpressao;

import java.util.List;

public class ContaPedidoNenhum implements ControleImpressora {
    private Context ctx ;

    public ContaPedidoNenhum(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void open() {
        Toast.makeText(ctx, "Selecione Uma Impressora", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void close() {

    }

    @Override
    public void setListenerImpressao(ListenerImpressao listenerImpressao) {
        Toast.makeText(ctx, "Selecione Uma Impressora", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void imprimeConta(ContaPedido conta) {
        Toast.makeText(ctx, "Selecione Uma Impressora", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void imprimirXML(String txt) {
        Toast.makeText(ctx, "Selecione Uma Impressora", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void impressaoLivre(List<RowImpressao> l) {
        Toast.makeText(ctx, "Selecione Uma Impressora", Toast.LENGTH_SHORT).show();
    }
}
