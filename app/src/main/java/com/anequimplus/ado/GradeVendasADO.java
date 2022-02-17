package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.GradeVendas;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GradeVendasADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;

    public GradeVendasADO(Context ctx) {
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public void setGradeVendas(JSONArray jarr) throws JSONException {
        List<GradeVendas>  ll = getGradeVendas() ;
        GradeVendas g = null ;
        GradeVendas resp = null ;
        for (int i = 0 ; i < jarr.length() ; i++) {
            g = new GradeVendas(jarr.getJSONObject(i));
            resp = getGradeNaList(ll, g.getId()) ;
            if (resp == null) {
                    inserir(g);
                } else {
                    alterar(g);
            }
            Dao.getGradeVendasItemADO(ctx).setGradeVendasItem(jarr.getJSONObject(i).getJSONArray("ITENS")) ;
        }
    }

    private GradeVendas getGradeNaList(List<GradeVendas>  l, int i){
        GradeVendas resp = null ;
        for (GradeVendas g : l) {
            if (g.getId() == i) resp = g ;
        }
        return resp ;
    }

    public List<GradeVendas> getGradeVendas(){
        List<GradeVendas> list = new ArrayList<GradeVendas>() ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, STATUS, IMAGEM FROM GRADE_VENDAS ORDER BY ID ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(new GradeVendas(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getString(res.getColumnIndexOrThrow("IMAGEM"))));
            res.moveToNext();
        }
        return list ;
    }

    public List<GradeVendas> getGradeVendas(int status){
        List<GradeVendas> list = new ArrayList<GradeVendas>() ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, STATUS, IMAGEM FROM GRADE_VENDAS WHERE STATUS = ? ", new String[]{String.valueOf(status)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(new GradeVendas(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getString(res.getColumnIndexOrThrow("IMAGEM")))
            );
            res.moveToNext();
        }
        return list ;
    }

    public void inserir(GradeVendas g){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", g.getId());
        contentValues.put("DESCRICAO", g.getDescricao());
        contentValues.put("STATUS", g.getStatus());
        contentValues.put("IMAGEM", g.getImagem());
        db.insert("GRADE_VENDAS", null, contentValues) ;
    }

    public void alterar(GradeVendas g){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", g.getId());
        contentValues.put("DESCRICAO", g.getDescricao());
        contentValues.put("STATUS", g.getStatus());
        contentValues.put("IMAGEM", g.getImagem());
        db.update("GRADE_VENDAS", contentValues, "ID = ?", new String[] {String.valueOf(g.getId())});
    }

    public GradeVendas getId(int id) {
        GradeVendas it = null ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, STATUS, IMAGEM FROM GRADE_VENDAS WHERE ID = ? ", new String[]{String.valueOf(id)});
        res.moveToFirst();
        if (res.isAfterLast() == false){
            it = new GradeVendas(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getString(res.getColumnIndexOrThrow("IMAGEM"))
            );
        }
        return it ;

    }
}
