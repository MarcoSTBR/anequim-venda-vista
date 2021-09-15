package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class PedidoItem {

    private int id ;
    private Pedido pedido ;
    private ItenSelect itenSelect ;

    public PedidoItem(int id, Pedido pedido, ItenSelect itenSelect) {
        this.id = id;
        this.pedido = pedido;
        this.itenSelect = itenSelect ;

    }
    public JSONObject getJSon() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID",id) ;
        j.put("PRODUTO_ID",itenSelect.getProduto().getId()) ;
        j.put("QUANTIDADE",itenSelect.getQuantidade()) ;
        j.put("PRECO",itenSelect.getPreco()) ;
        j.put("DESCONTO",itenSelect.getDesconto()) ;
        j.put("COMISSAO",0.00) ;
        j.put("OBS",itenSelect.getObs()) ;
        j.put("VALOR",itenSelect.getValor()) ;
        j.put("STATUS",1) ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ItenSelect getItenSelect() {
        return itenSelect;
    }

    public void setItenSelect(ItenSelect itenSelect) {
        this.itenSelect = itenSelect;
    }


}
