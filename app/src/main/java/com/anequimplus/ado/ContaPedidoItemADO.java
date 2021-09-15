package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.Produto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContaPedidoItemADO {

    private Context ctx ;
    private static List<ContaPedidoItem> listContaItem ;

    public ContaPedidoItemADO(Context ctx) {
        this.ctx = ctx;
        listContaItem = new ArrayList<ContaPedidoItem>();
    }


    public ContaPedidoItem contaPedidoItemAdd(ContaPedido c, Produto p, JSONObject j) throws JSONException {
        ContaPedidoItem it = new ContaPedidoItem(j.getInt("id"),
                p,
                j.getDouble("QUANTIDADE"),
                j.getDouble("PRECO"),
                j.getDouble("DESCONTO"),
                j.getDouble("VALOR"),
                j.getString("OBS"));
       return it ;

    }

    private int getIdTotal() {
        int cont = 0 ;
        for (ContaPedido cp : Dao.getContaPedidoADO(ctx).getList()){
            cont = cont + cp.getListContaPedidoItem().size() ;
        }
        return cont ;
    }

}
