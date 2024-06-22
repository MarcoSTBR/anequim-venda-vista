package com.anequimplus.DaoClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.entity.Usuario;
import com.anequimplus.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class Dao {

    private SQLiteDatabase db ;
    protected Context ctx ;

    public Dao(Context ctx) {
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public abstract String getTabela() ;

    public abstract DaoPrimaryKeyTipo getTipoPrimaryKey() ;

    public abstract String getID() ;

    public abstract List<DaoCampos> getCampos() ;

    public void insert(JSONObject j) throws JSONException {
        ContentValues contentValues = new ContentValues();
        Log.i("dao", "insert "+j.toString()) ;
        Log.i("dao", "insert "+getID()+" "+j.getString(getID())) ;
        contentValues.put(getID(), j.getString(getID())) ;
        for(DaoCampos c : getCampos()){
            if (!getID().equals(c.getNome())) {
                Log.i("dao", "campo "+c.getNome()+" valor "+j.getString(c.getNome())) ;
                contentValues.put(c.getNome(), j.getString(c.getNome()));
            } else Log.i("dao", "campo id "+getID()+" valor "+j.getString(c.getNome())) ;
        }
        db.insert(getTabela(), null, contentValues);
        /*int i =  (int) */
        // Log.i("dao","inseriu id "+i) ;
    }

    public void update(JSONObject j) throws JSONException {
        ContentValues contentValues = new ContentValues();
        String id = j.getString(getID()) ;
        for(DaoCampos c : getCampos()){
            if (!getID().equals(c.getNome()))
                contentValues.put(c.getNome(), j.getString(c.getNome())) ;
        }
        db.update(getTabela(), contentValues, getID()+" = ?", new String[]{id});
    }

    public void delete(String id) {
        db.delete(getTabela(), getID()+" = ?", new String[]{id}) ;

    }

    public void deleteAll() {
        db.delete(getTabela(), getID()+" <> ?" , new String[]{"-1"});
    }

    public JSONArray findAll (DaoFilters it, String order) throws JSONException {
        JSONArray j = new JSONArray() ;
        String fromtab = "FROM "+getTabela() ;
        String select = getSelect()+" "+fromtab+"  "+ getWhere(it)+" "+getOrder(order) ;
        Log.i("dao", select) ;
        Cursor res =  db.rawQuery( select, null);
        res.moveToFirst();
        Log.i("dao", "res "+res.getCount()) ;
        while(res.isAfterLast() == false){
            JSONObject item = new JSONObject() ;
            item.put(getID(), res.getString(res.getColumnIndexOrThrow(getID()))) ;
            setCampos(res, item) ;
            Log.i("dao", "findALL "+item.toString()) ;
            j.put(item) ;
            res.moveToNext();
        }
        Log.i("dao", getTabela()+" findALL "+j.toString()) ;
        return j ;
    }

    public JSONObject get(String id) throws JSONException {
        JSONObject item = null ;
        String fromtab = "FROM "+getTabela() ;
        String select = getSelect()+" "+fromtab+"  WHERE "+getID()+" = ?" ;
        Log.i("dao", getID()+" = "+id) ;
        Log.i("dao", select) ;
        Cursor res =  db.rawQuery( select, new String[]{id});
        Log.i("dao", "res "+res.getCount()) ;
        while(res.isAfterLast() == false){
            item = new JSONObject() ;
            item.put(getID(), res.getString(res.getColumnIndexOrThrow(getID()))) ;
            for(DaoCampos c : getCampos()){
                item.put(c.getNome(), res.getString(res.getColumnIndexOrThrow(c.getNome()))) ;
            }
            Log.i("dao", "get "+item.toString()) ;
            res.moveToNext();
        }
        return item ;
    }

    private void setCampos(Cursor res, JSONObject item) throws JSONException {
        List<DaoCampos> campos = getCampos() ;
        item.put(getID(), res.getString(res.getColumnIndexOrThrow(getID()))) ;
        for(DaoCampos c : campos){
            Log.i("dao", "pesquisa campo "+campos.size()+" "+c.getNome()) ;
            item.put(c.getNome(), res.getString(res.getColumnIndexOrThrow(c.getNome()))) ;
        }
    }

    private String getWhere(DaoFilters filters){
        if (filters.getList().size() > 0) {
            String where = "" ;
            for(DaoFilter d : filters.getList()){
                String  w = "("+d.getCampo()+" "+d.getOperador()+" " + '"' +d.getValor()+ '"'+")" ;
                if (where.length() == 0)
                    where += "WHERE " + w ;
                else where += " AND "+ w ;
            }
            return where ;
        } else return "" ;
    }

    private String getOrder(String order){
        if (order.length() > 0 ){
            return "ORDER BY "+order ;
        } else return "" ;
    }

    private String getSelect(){
        String select = "SELECT "+getID() ;
        for(DaoCampos c : getCampos()){
            select += ", "+c.getNome() ;
        }
        return select ;
    }

    public void inserirList(List<Usuario> l){
        try {
         deleteAll();
         for (Usuario u : l){
                 insert(u.geJSON());
         }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
