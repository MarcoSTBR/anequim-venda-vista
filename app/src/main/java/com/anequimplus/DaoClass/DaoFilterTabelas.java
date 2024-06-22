package com.anequimplus.DaoClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class DaoFilterTabelas<T> {


    public List<T> getAll(JSONArray j) throws JSONException {
        List<T> l = new ArrayList<>() ;
        for (int i = 0 ; i < j.length() ; i++){
            l.add(getNew(j.getJSONObject(i))) ;
        }
        return l ;
    }

    public abstract T getNew(JSONObject j) throws JSONException;

}
