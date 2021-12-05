package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.entity.Caixa;

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
       // delete() ;
/*
        db.execSQL("DROP TABLE IF EXISTS CAIXA") ;
        db.execSQL("CREATE TABLE IF NOT EXISTS CAIXA ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "USUARIO_ID INTEGER, "
                + "DATA DATETIME, "
                + "DATA_FINAL DATETIME, "
                + "VALOR DOUBLE,"
                + "STATUS INTEGER,"
                + "GEREZIM_ID INTEGER) "
        );

 */
     }


    public Caixa getCaixaAberto(int usuario_id) {
        Caixa caixa = null ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        Date dtf = null;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, USUARIO_ID, DATA, " +
                "DATA_FINAL, VALOR,  STATUS, GEREZIM_ID " +
                " FROM CAIXA WHERE (USUARIO_ID = ?) AND (STATUS = ?) ", new String[]{String.valueOf(usuario_id), String.valueOf(1)});
        res.moveToFirst();
        if (!res.isAfterLast()){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndex("DATA_FINAL")));
            caixa = new Caixa(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("UUID")),
                    dt,
                    dtf,
                    res.getInt(res.getColumnIndex("USUARIO_ID")),
                    res.getInt(res.getColumnIndex("STATUS")),
                    res.getDouble(res.getColumnIndex("VALOR")),
                    res.getInt(res.getColumnIndex("GEREZIM_ID"))) ;
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("erro", e.getMessage()) ;
            }
            res.moveToNext();
        }
        return caixa;
    }

    public List<Caixa> getCaixas() {
        List<Caixa> list = new ArrayList<Caixa>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        Date dtf = null;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, USUARIO_ID, DATA, " +
                "DATA_FINAL, VALOR,  STATUS FROM CAIXA ", null);
        res.moveToFirst();
        if (!res.isAfterLast()){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndex("DATA_FINAL")));
                list.add(new Caixa(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("UUID")),
                        dt,
                        dtf,
                        res.getInt(res.getColumnIndex("USUARIO_ID")),
                        res.getInt(res.getColumnIndex("STATUS")),
                        res.getDouble(res.getColumnIndex("VALOR")),
                        res.getInt(res.getColumnIndex("GEREZIM_ID")))
                ) ;

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
        contentValues.put("GEREZIM_ID", caixa.getGerezim_id());
        caixa.setId( (int) db.insert("CAIXA", null, contentValues));
    }

    public void caixa_recebido(int id, int gerezim_id){
        ContentValues contentValues = new ContentValues();
        contentValues.put("GEREZIM_ID", gerezim_id);
        db.update("CAIXA", contentValues, "ID = ?", new String[] {String.valueOf(id)});
    }

    public void caixa_fechamento(Caixa caixa){
        ContentValues contentValues = new ContentValues();
        contentValues.put("STATUS", caixa.getStatus());
        contentValues.put("GEREZIM_ID", caixa.getGerezim_id());
        db.update("CAIXA", contentValues, "ID = ?", new String[] {String.valueOf(caixa.getId())});
    }

    private void delete() {
        db.delete("CAIXA", null, null) ;
    }

}
