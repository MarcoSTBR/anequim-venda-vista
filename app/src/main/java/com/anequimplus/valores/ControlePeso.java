package com.anequimplus.valores;

import android.content.Context;

import com.anequimplus.utilitarios.MaskTypes;

public abstract class ControlePeso {

    private Context ctx ;

    public ControlePeso(Context ctx) {
        this.ctx = ctx;
    }


    public void getPeso(){
        getPesoQuantidade();
    }

    protected void getPesoQuantidade(){
        new ControleValorInsert(ctx).setQuantidade(MaskTypes.FORMAT_QUANT_PESO, new ListenerControleValorInsert() {
            @Override
            public void ok(Double quantidade) {
                setQuantidade(quantidade);
            }

            @Override
            public void erro(String msg) {
                setErro(msg) ;
            }
        });
    }


    public abstract void setQuantidade(Double q) ;
    public abstract void setErro(String msg) ;


}
