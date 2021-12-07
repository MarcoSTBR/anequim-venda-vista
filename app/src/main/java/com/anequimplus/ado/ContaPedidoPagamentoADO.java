package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.ContaPedidoPagamento;

import org.json.JSONException;
import org.json.JSONObject;

public class ContaPedidoPagamentoADO {

    private Context ctx ;
    //private static List<ContaPedidoPagamento> list ;

    public ContaPedidoPagamentoADO(Context ctx) {
        this.ctx = ctx;
      //  list = new ArrayList<ContaPedidoPagamento>();
    }

    public ContaPedidoPagamento pagamentoADD(ContaPedido c, JSONObject j) throws JSONException {
        ContaPedidoPagamento p = new ContaPedidoPagamento(j.getInt("id"),
                new Modalidade(j.getJSONObject("MODALIDADE")),
                j.getDouble("VALOR"));
        return p ;
    }
}
