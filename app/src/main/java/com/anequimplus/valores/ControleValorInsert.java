package com.anequimplus.valores;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.listeners.ListenerMaskValor;
import com.anequimplus.utilitarios.MaskTypes;
import com.anequimplus.utilitarios.MaskValor;

import java.text.DecimalFormat;

public class ControleValorInsert extends Dialog implements ListenerMaskValor {

    private AlertDialog.Builder builder;
    private Context ctx;
    private ListenerControleValorInsert listenerControleValorInsert;
    private EditText valor;
    private MaskTypes maskType;
    private Double valorFinal;

    public ControleValorInsert(Context ctx) {
        super(ctx);
        this.ctx = ctx;
        valorFinal = 0.0;
    }

    public void setQuantidade(MaskTypes maskType, ListenerControleValorInsert listenerControleValorInsert) {
        this.listenerControleValorInsert = listenerControleValorInsert;
        this.maskType = maskType;
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_troca_preco, null);
        valor = view.findViewById(R.id.valor);
        valor.addTextChangedListener(MaskValor.mask(valor, this.maskType, this::valorChange));
        //valor.addTextChangedListener((TextWatcher) maskValor);
        setValores();
        builder = new AlertDialog.Builder(ctx);
        builder.setView(view)
                .setPositiveButton("Sim", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        validade();

                    }
                })
                .setNegativeButton("Não", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listenerControleValorInsert.erro("Operação Cancelada!");

                            }
                        }
                );
        builder.show();
    }

    public void validade() {
        listenerControleValorInsert.ok(valorFinal);
    }

    private void setValores() {
        DecimalFormat frm = new DecimalFormat(maskType.formato);
        valor.setText(frm.format(valorFinal));
    }

    @Override
    public void valorChange(double v) {
        valorFinal = v;
    }
}
