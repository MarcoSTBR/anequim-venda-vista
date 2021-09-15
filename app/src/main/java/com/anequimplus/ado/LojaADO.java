package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.Loja;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LojaADO {
    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "LOJA" ;

    public LojaADO(Context ctx)
    {
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public void add(JSONArray j) throws JSONException {
        //"data":[{"id":"1","CODIGO":"9999","LOJA":"S&T Sistemas e Tecnologia do","STATUS":"1"}]
        excluir();
        for (int i = 0; i < j.length(); i++) {
            inserir(new Loja(j.getJSONObject(i))) ;
        }
    }

    public List<Loja> getList() {
        List<Loja> list = new ArrayList<Loja>() ;
        Cursor res =  db.rawQuery( "SELECT ID, NOME, STATUS FROM LOJA ", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(new Loja(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("NOME")),
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

    public Loja getLoja(int i) {
        Loja l = null;
        Cursor res =  db.rawQuery( "SELECT ID, NOME, STATUS FROM LOJA WHERE ID = "+String.valueOf(i), null );
        res.moveToFirst();
        if (res.isAfterLast() == false){
            l = new Loja(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("NOME")),
                    res.getInt(res.getColumnIndex("STATUS"))) ;
        }
        return l ;
    }


    private void inserir(@org.jetbrains.annotations.NotNull Loja loja){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", loja.getId());
        contentValues.put("NOME", loja.getNome());
        contentValues.put("STATUS", loja.getStatus());
        db.insert(DB_TABLE, null, contentValues) ;
    }

    private void excluir(){
        db.delete(DB_TABLE, null, null) ;
    }

}
