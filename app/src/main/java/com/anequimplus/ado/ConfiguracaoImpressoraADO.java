package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.ConfiguracaoImpressora;

public class ConfiguracaoImpressoraADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String TABLE = "CONFIG_IMP" ;

    public ConfiguracaoImpressoraADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public ConfiguracaoImpressora get(int id){
        ConfiguracaoImpressora it = null ;
        Cursor res =  db.rawQuery( "SELECT ID, CONFIG FROM "+TABLE+
            " WHERE ID = ? ", new String[]{String.valueOf(id)});
        res.moveToFirst();
        if (!res.isAfterLast()){
                it = new ConfiguracaoImpressora(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("CONFIG")));
            res.moveToNext();
        }
        return it ;
    }

    public void incluir(ConfiguracaoImpressora it) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", it.getId());
        contentValues.put("CONFIG", it.getConfig());
        db.insert(TABLE, null, contentValues);
    }

    public void alterar(ConfiguracaoImpressora it) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CONFIG", it.getConfig());
        db.update(TABLE, contentValues, "ID = ?", new String[] {String.valueOf(it.getId())});    }
}
