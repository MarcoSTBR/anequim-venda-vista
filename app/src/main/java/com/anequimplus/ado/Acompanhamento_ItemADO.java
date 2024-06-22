package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.Acompanhamento_Item;
import com.anequimplus.entity.FilterTables;

import java.util.ArrayList;
import java.util.List;

public class Acompanhamento_ItemADO extends TableDao{

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "ACOMP_ITEM" ;

    public Acompanhamento_ItemADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<Acompanhamento_Item> getList(FilterTables filters, String order){
        List<Acompanhamento_Item> l = new ArrayList<Acompanhamento_Item>();
        Cursor res =  db.rawQuery( "SELECT ID, AFOOD_ACOMP_ID, AFOOD_PRODUTO_ID, OBS, MIN, MAX, PRECO " +
                "FROM " + DB_TABLE + " "+getWhere(filters, order),null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            l.add(new Acompanhamento_Item(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getInt(res.getColumnIndexOrThrow("AFOOD_ACOMP_ID")),
                    res.getInt(res.getColumnIndexOrThrow("AFOOD_PRODUTO_ID")),
                    res.getString(res.getColumnIndexOrThrow("OBS")),
                    res.getInt(res.getColumnIndexOrThrow("MIN")),
                    res.getInt(res.getColumnIndexOrThrow("MAX")),
                    res.getDouble(res.getColumnIndexOrThrow("PRECO"))
            ));
            res.moveToNext();
        }
        return l ;
    }

    public void incluir(Acompanhamento_Item it){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", it.getId());
        contentValues.put("AFOOD_ACOMP_ID", it.getAcomp_id());
        contentValues.put("AFOOD_PRODUTO_ID", it.getProduto_id());
        contentValues.put("OBS", it.getObs());
        contentValues.put("MIN", it.getMin());
        contentValues.put("MAX", it.getMax());
        contentValues.put("PRECO", it.getPreco());
        db.insert(DB_TABLE, null, contentValues) ;
    }

    public void excluir(Acompanhamento_Item it){
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(it.getId())}) ;
    }

    public void excluir(){
        db.delete(DB_TABLE, "ID > ?", new String[]{String.valueOf(-1)}) ;
    }

    public void setJSON(List<Acompanhamento_Item> l) {
        excluir();
        for (Acompanhamento_Item it : l){
            incluir(it);
        }
    }
}
