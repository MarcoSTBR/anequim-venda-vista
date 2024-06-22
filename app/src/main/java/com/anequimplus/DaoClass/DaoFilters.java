package com.anequimplus.DaoClass;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DaoFilters {

    private List<DaoFilter> list ;

    public DaoFilters(){
        clear() ;
    }

    public void clear(){
        list = new ArrayList<DaoFilter>() ;

    }
    public void add(DaoFilter f){
        list.add(f) ;
    }

    public List<DaoFilter> getList() {
        return list;
    }

    public JSONArray getJSON() {
        JSONArray j = new JSONArray() ;
        j.put("") ;
        return j ;
    }
}
