package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.Acompanhamento_produto;
import com.anequimplus.entity.FilterTables;

import java.util.ArrayList;
import java.util.List;

public class Acompanhamento_ProdutoADO extends TableDao{

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "ACOMP_PROD" ;

    public Acompanhamento_ProdutoADO(Context ctx){
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<Acompanhamento_produto> getList(FilterTables filters, String order){
        List<Acompanhamento_produto> l = new ArrayList<Acompanhamento_produto>();
        Cursor res =  db.rawQuery( "SELECT ID, AFOOD_PRODUTO_ID, AFOOD_ACOMP_ID " +
                "FROM " + DB_TABLE + " "+getWhere(filters, order),null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
           l.add(new Acompanhamento_produto(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("AFOOD_PRODUTO_ID")),
                        res.getInt(res.getColumnIndexOrThrow("AFOOD_ACOMP_ID"))
            ));
            res.moveToNext();
        }
        return l ;
    }

    public void incluir(Acompanhamento_produto it){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", it.getId());
        contentValues.put("AFOOD_PRODUTO_ID", it.getProduto_id());
        contentValues.put("AFOOD_ACOMP_ID", it.getAcomp_id());
        db.insert(DB_TABLE, null, contentValues) ;
    }

    public void excluir(Acompanhamento_produto it){
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(it.getId())}) ;
    }

    public void excluir(){
        db.delete(DB_TABLE, "ID > ?", new String[]{String.valueOf(-1)}) ;
    }

    public void setJSON(List<Acompanhamento_produto> l) {
        excluir();
        for (Acompanhamento_produto it : l){
            incluir(it);
        }

    }
}
