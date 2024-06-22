package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Sangria;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SangriaADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "SANGRIA" ;

    public SangriaADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<Sangria> getList(FilterTables filters) {
        List<Sangria> list = new ArrayList<Sangria>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        String where = "" ;
        if (filters.getList().size() > 0){
            for (FilterTable f : filters.getList()){
                if (where.length() == 0)
                    where = "("+ f.getCampo() +" "+ f.getOperador() +" "+ f.getVariavel() +")" ;
                else
                    where = where + " AND ("+ f.getCampo() +" "+ f.getOperador() +" "+ f.getVariavel() +")" ;
            }
            where = " WHERE "+where ;
        }
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, DATA, CAIXA_ID, MODALIDADE_ID, " +
                "VALOR, UUID, STATUS FROM "+DB_TABLE+" "+
                where,null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try
            {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                list.add(new Sangria(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                        dt,
                        res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                        res.getInt(res.getColumnIndexOrThrow("MODALIDADE_ID")),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")))) ;
                res.moveToNext();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list ;
    }
    public List<Sangria> getList(Caixa caixa){
        List<Sangria> list = new ArrayList<Sangria>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, DATA, CAIXA_ID, MODALIDADE_ID, " +
                "VALOR, UUID, STATUS FROM "+DB_TABLE+
                " WHERE CAIXA_ID = ?  ", new String[]{String.valueOf(caixa.getId())});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try
            {
            dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
            list.add(new Sangria(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    dt,
                    res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                    res.getInt(res.getColumnIndexOrThrow("MODALIDADE_ID")),
                    res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                    res.getString(res.getColumnIndexOrThrow("UUID")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")))) ;
            res.moveToNext();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list ;
    }

    public void incluir(Sangria it){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRICAO", it.getDescricao());
        contentValues.put("DATA", df.format(it.getData()));
        contentValues.put("CAIXA_ID", it.getCaixa_id());
        contentValues.put("MODALIDADE_ID", it.getModalidade_id());
        contentValues.put("VALOR", it.getValor());
        contentValues.put("UUID", it.getUUID());
        contentValues.put("STATUS", it.getStatus());
        it.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }

    public void alterar(Sangria it){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRICAO", it.getDescricao());
        contentValues.put("DATA", df.format(it.getData()));
        contentValues.put("CAIXA_ID", it.getCaixa_id());
        contentValues.put("MODALIDADE_ID", it.getModalidade_id());
        contentValues.put("VALOR", it.getValor());
        contentValues.put("UUID", it.getUUID());
        contentValues.put("STATUS", it.getStatus());
        it.setId((int)db.update(DB_TABLE, contentValues,"ID = ?", new String[]{String.valueOf(it.getId())})) ;
    }

    public void excluir(Sangria it) {
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(it.getId())}) ;
    }

    public JSONArray getJSONList(Caixa c) throws JSONException {
       JSONArray jaa = new JSONArray() ;
        List<Sangria> l = getList(c) ;
        for (Sangria s : l){
            jaa.put(s.getExportacaoJSON()) ;
        }
       return jaa ;
    }

    public void excluir() {
        db.delete(DB_TABLE, "ID >= ?", new String[]{"0"}) ;
    }
}
