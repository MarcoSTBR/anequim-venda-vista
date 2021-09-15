package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.entity.Impressora;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ImpressoraADO {

    private Context ctx ;
    private static List<Impressora> list ;

    public ImpressoraADO(Context ctx){
        this.ctx = ctx ;
        list = new ArrayList<Impressora>() ;
    }


    public void ImpressoraADD(JSONArray jarr) throws JSONException {
        list.clear();
            for (int i = 0 ; i < jarr.length() ; i++){
                list.add(new Impressora(jarr.getJSONObject(i)));
            }
    }
    public List<Impressora> getList() {
        return list ;
    }
}
