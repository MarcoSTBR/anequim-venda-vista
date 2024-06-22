package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.ado.TranformacaoVersao;
import com.anequimplus.ado.TranformacaoVersaoV13;
import com.anequimplus.ado.TranformacaoVersaoV14;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONArray;

import java.util.List;

public class BuildTranformacaoVersao {

    private Context ctx ;
    private TranformacaoVersao tranformacaoVersao = null;

    public BuildTranformacaoVersao(Context ctx) {
        this.ctx = ctx;
        if (Configuracao.getPedidoCompartilhado(ctx)){
            String v = Configuracao.getApiVersao(ctx);
           // Log.i()
            switch (v) {
                case "V13": tranformacaoVersao = new TranformacaoVersaoV13(ctx) ;
                break;
                default: tranformacaoVersao = new TranformacaoVersaoV14(ctx) ;
            }
        }
        else  tranformacaoVersao = new TranformacaoVersaoV14(ctx) ;
    }

    public List<ContaPedido> getContas(JSONArray data) throws Exception {
       return tranformacaoVersao.getContas(data) ;
    }
}
