package com.anequimplus.entity;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaVista {
    private int id ;
    private Date data ;
    private int caixa_id ;
    private int status ;
    private List<VendaVistaItem> vendaVistaItems ;
    private List<VendaVistaPagamento> vendaVistaPagamentos ;


    public VendaVista(int id, int caixa_id, Date data, int status, List<VendaVistaItem> vendaVistaItems, List<VendaVistaPagamento> vendaVistaPagamentos) {
        this.id = id;
        this.caixa_id = caixa_id ;
        this.data = data;
        this.status = status ;
        this.vendaVistaItems = vendaVistaItems ;
        this.vendaVistaPagamentos = vendaVistaPagamentos ;
    }

    public VendaVista(Context ctx, JSONObject j) throws JSONException {
        id = j.getInt("id");
        data = UtilSet.getData(j.getString("DATA")) ;
        status = j.getInt("STATUS") ;
        vendaVistaItems = new ArrayList<VendaVistaItem>() ;
        for (int i=0 ; i < j.getJSONArray("ITENS").length() ; i++ ) {
            Produto produto = Dao.getProdutoADO(ctx).getProdtoId(
                    j.getJSONArray("ITENS").getJSONObject(i).getInt("PRODUTO_ID"));
            vendaVistaItems.add(new VendaVistaItem(this, produto, j.getJSONArray("ITENS").getJSONObject(i)));
        }
        vendaVistaPagamentos = new ArrayList<VendaVistaPagamento>() ;
        for (int i=0 ; i < j.getJSONArray("PAGAMENTOS").length() ; i++ ) {
            Modalidade modalidade = Dao.getModalidadeADO(ctx).getModalidade(
                    j.getJSONArray("PAGAMENTOS").getJSONObject(i).getInt("MODALIDADE_ID")) ;
            vendaVistaPagamentos.add(new VendaVistaPagamento(this, modalidade, j.getJSONArray("PAGAMENTOS").getJSONObject(i)));
        }
    }


    public JSONObject toJson() throws JSONException {
        JSONObject j = new JSONObject() ;
        JSONArray jItem = new JSONArray() ;
        JSONArray jPg = new JSONArray() ;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        //j.put("ID", id) ;
        j.put("CAIXA_ID",caixa_id);
        j.put("DATA", s.format(data)) ;
        j.put("STATUS", status ) ;
        for (VendaVistaItem vi : vendaVistaItems) jItem.put(vi.toJson()) ;
        for (VendaVistaPagamento vp : vendaVistaPagamentos) jPg.put(vp.toJson()) ;
        j.put("ITENS", jItem) ;
        j.put("PAGAMENTOS", jPg) ;
        return j ;
    }


    public double getTotalPagamento(){
        double t = 0 ;
        for (VendaVistaPagamento p : vendaVistaPagamentos) {
            if (p.getStatus() == 1)
                t = t + p.getValor() ;
        }
        return t ;
    }

    public boolean fechado(){
        if (status == 0){
            return true ;
        } else {
            if ((getTotalPagamento() >= getTotalVenda()) && (getTotalVenda() != 0)){
                return true ;
            } else {
                return false ;
            }
        }

    }

    public double getTotalVenda(){
        double t = 0 ;
        for (VendaVistaItem it : vendaVistaItems) {
            if (it.getStatus() == 1)
                t = t + it.getItenSelect().getValor() ;
        }
        return t ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaixa_id() {
        return caixa_id;
    }

    public void setCaixa_id(int caixa_id) {
        this.caixa_id = caixa_id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<VendaVistaItem> getVendaVistaItems() {
        return vendaVistaItems;
    }

    public void setVendaVistaItems(List<VendaVistaItem> vendaVistaItems) {
        this.vendaVistaItems = vendaVistaItems;
    }

    public List<VendaVistaPagamento> getVendaVistaPagamentos() {
        return vendaVistaPagamentos;
    }

    public void setVendaVistaPagamentos(List<VendaVistaPagamento> vendaVistaPagamentos) {
        this.vendaVistaPagamentos = vendaVistaPagamentos;
    }

}
