package com.anequimplus.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PedidoItem extends Entidade{

    private int id ;
    private int pedido_id ;
    private ItemSelect itemSelect;
    private List<PedidoItemAcomp> acompanhamentos ;

    public PedidoItem(int id, int pedido_id, ItemSelect itemSelect, List<PedidoItemAcomp> acompanhamentos) {
        this.id = id;
        this.pedido_id = pedido_id ;
        this.itemSelect = itemSelect;
        this.acompanhamentos = acompanhamentos ;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID",id) ;
        j.put("PEDIDO_ID", pedido_id) ;
        j.put("PRODUTO_ID", itemSelect.getProduto().getId()) ;
        j.put("QUANTIDADE", itemSelect.getQuantidade()) ;
        j.put("PRECO", itemSelect.getPreco()) ;
        j.put("DESCONTO", itemSelect.getDesconto()) ;
        j.put("COMISSAO", itemSelect.getComissao()) ;
        j.put("OBS", itemSelect.getObs()) ;
        j.put("VALOR", itemSelect.getValor()) ;
        j.put("STATUS", itemSelect.getStatus()) ;
        return j;
    }


    public JSONObject getExportacaoJSon() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID",id) ;
        j.put("PEDIDO_ID", pedido_id) ;
        j.put("PRODUTO", itemSelect.getProduto().getJSONBase()) ;
        j.put("QUANTIDADE", itemSelect.getQuantidade()) ;
        j.put("PRECO", itemSelect.getPreco()) ;
        j.put("DESCONTO", itemSelect.getDesconto()) ;
        j.put("COMISSAO", itemSelect.getComissao()) ;
        j.put("OBS", itemSelect.getObs()) ;
        j.put("VALOR", itemSelect.getValor()) ;
        j.put("STATUS", itemSelect.getStatus()) ;
       // j.put("ITEMSELECT", itemSelect.getJSON()) ;
        JSONArray acomp = new JSONArray() ;
        for (int i = 0 ; i < acompanhamentos.size() ; i++){
            acomp.put (acompanhamentos.get(i).getExportacaoJSON()) ;
        }
        j.put("ACOMPANHAMENTOS", acomp) ;
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

    public ItemSelect getItemSelect() {
        return itemSelect;
    }

    public void setItemSelect(ItemSelect itemSelect) {
        this.itemSelect = itemSelect;
    }

    public List<PedidoItemAcomp> getAcompanhamentos() {
        return acompanhamentos;
    }

    public void setAcompanhamentos(List<PedidoItemAcomp> acompanhamentos) {
        this.acompanhamentos = acompanhamentos;
    }

}
