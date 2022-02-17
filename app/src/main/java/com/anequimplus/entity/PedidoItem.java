package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class PedidoItem {

    private int id ;
    private int pedido_id ;
    private ItenSelect itenSelect ;

    public PedidoItem(int id, int pedido_id, ItenSelect itenSelect) {
        this.id = id;
        this.pedido_id = pedido_id ;
        this.itenSelect = itenSelect ;
    }

    public JSONObject getJSon() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID",id) ;
        j.put("PEDIDO_ID",getPedido_id()) ;
        j.put("PRODUTO_ID",itenSelect.getProduto().getId()) ;
        j.put("QUANTIDADE",itenSelect.getQuantidade()) ;
        j.put("PRECO",itenSelect.getPreco()) ;
        j.put("DESCONTO",itenSelect.getDesconto()) ;
        j.put("COMISSAO",itenSelect.getComissao()) ;
        j.put("OBS",itenSelect.getObs()) ;
        j.put("VALOR",itenSelect.getValor()) ;
        j.put("STATUS",itenSelect.getStatus()) ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(int pedido_id) {
        this.pedido_id = pedido_id;
    }

    public ItenSelect getItenSelect() {
        return itenSelect;
    }

    public void setItenSelect(ItenSelect itenSelect) {
        this.itenSelect = itenSelect;
    }


}
