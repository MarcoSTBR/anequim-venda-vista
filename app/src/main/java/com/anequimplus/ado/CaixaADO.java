package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CaixaADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;

    public CaixaADO(Context ctx) {
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
     }

    public Caixa getCaixaAberto(int usuario_id) {
        Caixa caixa = null ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("USUARIO_ID", "=", String.valueOf(usuario_id))) ;
        List<Caixa> lista = getList(filters) ;
        for (Caixa cp : lista){
            if (cp.getStatus() == 1) caixa = cp ;
        }
        return caixa;
    }

    public Caixa get(int caixa_id) {
        Caixa caixa = null ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(caixa_id))) ;
        List<Caixa> lista = getList(filters) ;
        for (Caixa cp : lista){
           caixa = cp ;
        }
        return caixa;
    }

    public List<Caixa> getList(List<FilterTable> l) {
        List<Caixa> list = new ArrayList<Caixa>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        Date dtf = null;
        String where = "" ;
        if (l.size() > 0){
            for (FilterTable f : l){
               if (where.length() == 0)
                  where = "("+ f.getCampo() +" "+ f.getOperador() +" "+ f.getVariavel() +")" ;
                else
                  where = where + " AND ("+ f.getCampo() +" "+ f.getOperador() +" "+ f.getVariavel() +")" ;
            }
            where = " WHERE "+where ;
        }
        Cursor res =  db.rawQuery( "SELECT ID, UUID, USUARIO_ID, DATA, " +
                "DATA_FINAL, VALOR,  STATUS FROM CAIXA "+
                where, null);
        res.moveToFirst();
        Log.i("getList", "SELECT ID, UUID, USUARIO_ID, DATA, " +
                "DATA_FINAL, VALOR,  STATUS FROM CAIXA "+
                where) ;
        if (!res.isAfterLast()){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FINAL")));
                list.add(new Caixa(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        dt,
                        dtf,
                        res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")))
                ) ;

                Log.i("getList", list.get(l.size()-1).getJson().toString()) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return list ;

    }

    public boolean ifCaixaaberto(int usuario_id) {
        return getCaixaAberto(usuario_id) != null ? true : false ;
    }

    public void setCaixa(Caixa caixa) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", caixa.getUuid());
        contentValues.put("USUARIO_ID", caixa.getUsuario_Id());
        contentValues.put("DATA", df.format(caixa.getData()));
        contentValues.put("DATA_FINAL", df.format(caixa.getDataFechamento()));
        contentValues.put("STATUS", caixa.getStatus());
        contentValues.put("VALOR", caixa.getValor());
        caixa.setId( (int) db.insert("CAIXA", null, contentValues));
    }

    public void alterar(Caixa caixa) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", caixa.getUuid());
        contentValues.put("USUARIO_ID", caixa.getUsuario_Id());
        contentValues.put("DATA", df.format(caixa.getData()));
        contentValues.put("DATA_FINAL", df.format(caixa.getDataFechamento()));
        contentValues.put("STATUS", caixa.getStatus());
        contentValues.put("VALOR", caixa.getValor());
        db.update("CAIXA",contentValues, "ID = ?", new String[]{String.valueOf(caixa.getId())});
    }

    public void caixa_fechamento(Caixa caixa){
        ContentValues contentValues = new ContentValues();
        contentValues.put("STATUS", caixa.getStatus());
        db.update("CAIXA", contentValues, "ID = ?", new String[] {String.valueOf(caixa.getId())});
    }

    private void delete() {
        db.delete("CAIXA", null, null) ;
    }

    private void delete(int id) {
        db.delete("CAIXA", "ID = ?", new String[]{String.valueOf(id)}) ;
    }

}
