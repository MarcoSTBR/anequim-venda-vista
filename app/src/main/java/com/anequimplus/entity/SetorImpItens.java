package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class SetorImpItens {

    private int id ;
    private int setorImp_id ;
    private int produto_id ;
    private int status ;

    public SetorImpItens(JSONObject j) throws JSONException {
        if (j.isNull("id"))
            id = j.getInt("ID");
          else id = j.getInt("id") ;
        setorImp_id = j.getInt("SETOR_IMP_ID") ;
        produto_id = j.getInt("PRODUTO_ID") ;
        status = j.getInt("STATUS") ;
    }

    public JSONObject getJSON() throws JSONException {
      JSONObject j = new JSONObject() ;
      j.put("ID", id) ;
      j.put("SETOR_IMP_ID",setorImp_id) ;
      j.put("PRODUTO_ID", produto_id) ;
      j.put("STATUS", status) ;
      return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSetorImp_id() {
        return setorImp_id;
    }

    public void setSetorImp_id(int setorImp_id) {
        this.setorImp_id = setorImp_id;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
