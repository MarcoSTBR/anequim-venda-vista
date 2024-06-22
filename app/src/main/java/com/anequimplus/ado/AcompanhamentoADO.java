package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.Acompanhamento;
import com.anequimplus.entity.FilterTables;

import java.util.ArrayList;
import java.util.List;

public class AcompanhamentoADO extends TableDao{
    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "ACOMP" ;

    public AcompanhamentoADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<Acompanhamento> getList(FilterTables filters, String order){
        List<Acompanhamento> l = new ArrayList<Acompanhamento>();
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, OBS, MIN, MAX, PRECO " +
                "FROM " + DB_TABLE + " "+getWhere(filters, order),null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            l.add(new Acompanhamento(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getString(res.getColumnIndexOrThrow("OBS")),
                    res.getInt(res.getColumnIndexOrThrow("MIN")),
                    res.getInt(res.getColumnIndexOrThrow("MAX")),
                    res.getDouble(res.getColumnIndexOrThrow("PRECO"))
            ));
            res.moveToNext();
        }
        return l ;
    }

    public void incluir(Acompanhamento it){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", it.getId());
        contentValues.put("DESCRICAO", it.getDescricao());
        contentValues.put("OBS", it.getObs());
        contentValues.put("MIN", it.getMin());
        contentValues.put("MAX", it.getMax());
        contentValues.put("PRECO", it.getPreco());
        db.insert(DB_TABLE, null, contentValues) ;
    }

    public void excluir(Acompanhamento it){
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(it.getId())}) ;
    }

    public void excluir(){
        db.delete(DB_TABLE, "ID > ?", new String[]{String.valueOf(-1)}) ;
    }

    public void setJSON(List<Acompanhamento> l) {
        excluir();
        for (Acompanhamento it : l){
            incluir(it);
        }
    }

    public Acompanhamento get(int id) {
        Acompanhamento a = null;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, OBS, MIN, MAX, PRECO " +
                "FROM " + DB_TABLE + " WHERE ID = ? ",new String[]{String.valueOf(id)});
        res.moveToFirst();
        if (res.isAfterLast() == false){
            a = new Acompanhamento(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getString(res.getColumnIndexOrThrow("OBS")),
                    res.getInt(res.getColumnIndexOrThrow("MIN")),
                    res.getInt(res.getColumnIndexOrThrow("MAX")),
                    res.getDouble(res.getColumnIndexOrThrow("PRECO"))
            );
            res.moveToNext();
        }
        return a ;
    }
}
