package com.anequimplus.entity;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FilterTables {

    private List<FilterTable> list ;

    public FilterTables() {
        list = new ArrayList<FilterTable>() ;
    }

    public FilterTables(List<FilterTable> list) {
        this.list = list;
    }

    public List<FilterTable> getList() {
        return list ;

    }

    public void add(FilterTable filter) {
        list.add(filter) ;
    }

    public void clear() {
        list.clear();
    }

    public JSONArray getJSON(){
        JSONArray jaa = new JSONArray();
        for (int i = 0 ; i < list.size() ; i++) {
            jaa.put(list.get(i).getJSON());
        }

        Log.i("getJSON", jaa.toString()) ;
        return jaa ;
    }




}
