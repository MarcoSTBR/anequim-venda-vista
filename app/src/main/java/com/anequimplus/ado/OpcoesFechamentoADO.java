package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.entity.OpcoesFechamento;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class OpcoesFechamentoADO {

    private Context ctx ;
    private static List<OpcoesFechamento> list ;

    public OpcoesFechamentoADO(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<OpcoesFechamento>() ;
    }

    public  List<OpcoesFechamento> getList() {
        return list;
    }

    public void opcoesADD(JSONArray jarr) throws JSONException {
        list.clear();
        for (int i = 0; i < jarr.length(); i++) {
            list.add(new OpcoesFechamento(jarr.getJSONObject(i)));

        }
    }

    public OpcoesFechamento getOpcoesFechamentoID(int id){
        for (OpcoesFechamento o : list){
            if (id == o.getId()) return o ;
        }
        return null ;
    }
}
