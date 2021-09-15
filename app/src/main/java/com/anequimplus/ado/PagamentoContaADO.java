package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.PagamentoConta;

import org.json.JSONException;
import org.json.JSONObject;

public class PagamentoContaADO {

    private Context ctx ;
    //private static List<PagamentoConta> list ;

    public PagamentoContaADO(Context ctx) {
        this.ctx = ctx;
      //  list = new ArrayList<PagamentoConta>();
    }

    public PagamentoConta pagamentoADD(ContaPedido c, JSONObject j) throws JSONException {
        PagamentoConta p = new PagamentoConta(j.getInt("id"),
                new Modalidade(j.getJSONObject("MODALIDADE")),
                j.getDouble("VALOR"));
        return p ;
    }
}
