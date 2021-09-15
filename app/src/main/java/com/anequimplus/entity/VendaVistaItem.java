package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class VendaVistaItem {
    private int id ;
    private VendaVista vendaVista ;
    private ItenSelect itenSelect ;
    private int status ;

    public VendaVistaItem(int id, VendaVista vendaVista, ItenSelect itenSelect, int status) {
        this.id = id;
        this.vendaVista = vendaVista;
        this.itenSelect = itenSelect;
        this.status = status ;
    }

    public VendaVistaItem(VendaVista vendaVista, Produto produto, JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.vendaVista = vendaVista;
        this.itenSelect = new ItenSelect(vendaVista.getId(),
               produto, j.getDouble("QUANTIDADE"), j.getDouble("PRECO"),
               j.getDouble("DESCONTO"),j.getDouble("VALOR"),j.getString("OBS"));
        this.status = j.getInt("STATUS") ;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject j = new JSONObject() ;
       // j.put("ID", id) ;
       // j.put("VENDA_VISTA_ID", vendaVista.getId()) ;
        j.put("PRODUTO_ID", itenSelect.getProduto().getId()) ;
        j.put("QUANTIDADE", itenSelect.getQuantidade()) ;
        j.put("PRECO", itenSelect.getPreco()) ;
        j.put("DESCONTO", itenSelect.getDesconto()) ;
        j.put("VALOR", itenSelect.getValor()) ;
        j.put("OBS", itenSelect.getObs()) ;
        j.put("STATUS", status) ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VendaVista getVendaVista() {
        return vendaVista;
    }

    public void setVendaVista(VendaVista vendaVista) {
        this.vendaVista = vendaVista;
    }

    public ItenSelect getItenSelect() {
        return itenSelect;
    }

    public void setItenSelect(ItenSelect itenSelect) {
        this.itenSelect = itenSelect;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
