package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class PedidoItemAcomp extends Entidade {

    private int id ;
    private int pedidoItem_id ;
    private ItemSelect itemSelect;

    public PedidoItemAcomp(int id, int pedidoItem_id, ItemSelect itemSelect) {
        this.id = id;
        this.pedidoItem_id = pedidoItem_id;
        this.itemSelect = itemSelect;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID",id) ;
        j.put("PEDIDO_ITEM_ID",pedidoItem_id) ;
        j.put("PRODUTO_ID", itemSelect.getProduto().getId()) ;
        j.put("PRODUTO", itemSelect.getProduto().getJSONBase()) ;
        j.put("QUANTIDADE", itemSelect.getQuantidade()) ;
        j.put("PRECO", itemSelect.getPreco()) ;
        j.put("DESCONTO", itemSelect.getDesconto()) ;
        j.put("COMISSAO", itemSelect.getComissao()) ;
        j.put("OBS", itemSelect.getObs()) ;
        j.put("VALOR", itemSelect.getValor()) ;
        j.put("STATUS", itemSelect.getStatus()) ;
        return j ;
    }

    public JSONObject getExportacaoJSON() throws JSONException {
        JSONObject j = new JSONObject();
       // j.put("ID",id) ;
        j.put("PEDIDO_ITEM_ID",pedidoItem_id) ;
        j.put("PRODUTO_ID", itemSelect.getProduto().getId()) ;
        j.put("PRODUTO", itemSelect.getProduto().getJSONBase()) ;
        j.put("QUANTIDADE", itemSelect.getQuantidade()) ;
        j.put("PRECO", itemSelect.getPreco()) ;
        j.put("DESCONTO", itemSelect.getDesconto()) ;
        j.put("COMISSAO", itemSelect.getComissao()) ;
        j.put("OBS", itemSelect.getObs()) ;
        j.put("VALOR", itemSelect.getValor()) ;
        j.put("STATUS", itemSelect.getStatus()) ;
        return j ;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoItem_id() {
        return pedidoItem_id;
    }

    public void setPedidoItem_id(int pedidoItem_id) {
        this.pedidoItem_id = pedidoItem_id;
    }

    public ItemSelect getItemSelect() {
        return itemSelect;
    }

    public void setItemSelect(ItemSelect itemSelect) {
        this.itemSelect = itemSelect;
    }

}
