package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.UsuarioAcesso;

import java.util.ArrayList;
import java.util.List;

public class UsuarioAcessoADO extends TableDao{
    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String TABLE = "USUARIO_ACESSO" ;

    public UsuarioAcessoADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
        db.execSQL("CREATE TABLE IF NOT EXISTS USUARIO_ACESSO ( "
                + "ID INTEGER  PRIMARY KEY, "
                + "USUARIO_ID INTEGER, "
                + "ACESSO_ID INTEGER, "
                + "DESCRICAO TEXT)"
        ) ;
    }
    public void setlist(List<UsuarioAcesso> l) {
        excluir();
        for (UsuarioAcesso u : l){
            incluir(u);
            Log.i("Usuario", u.getAcesso_id()+" "+u.getUsuario_id()+" "+u.getDescricao()) ;
        }
    }

    public List<UsuarioAcesso> getList(FilterTables filters, String order){
        List<UsuarioAcesso> list = new ArrayList<UsuarioAcesso>() ;
        Cursor res =  db.rawQuery( "SELECT ID, USUARIO_ID, ACESSO_ID, DESCRICAO " +
                "FROM "+TABLE+" "+getWhere(filters, order), null);

        Log.i("acesso",  "SELECT ID, USUARIO_ID, ACESSO_ID, DESCRICAO " +
                "FROM "+TABLE+" "+getWhere(filters, order)) ;
        res.moveToFirst();
        while (res.isAfterLast() == false){
            Log.i("acesso",  res.getString(res.getColumnIndexOrThrow("DESCRICAO")));
            list.add(new UsuarioAcesso(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                    res.getInt(res.getColumnIndexOrThrow("ACESSO_ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")))) ;
            res.moveToNext();
        }
        return list ;
    }

    public UsuarioAcesso get(int id){
        UsuarioAcesso it = null ;
        Cursor res =  db.rawQuery( "SELECT ID, USUARIO_ID, ACESSO_ID, DESCRICAO " +
                "FROM "+TABLE+" WHERE ID = ? ", new String[]{String.valueOf(id)});
        res.moveToFirst();
        if (!res.isAfterLast()){
               it = new UsuarioAcesso(res.getInt(res.getColumnIndexOrThrow("ID")),
                       res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                       res.getInt(res.getColumnIndexOrThrow("ACESSO_ID")),
                        res.getString(res.getColumnIndexOrThrow("DESCRICAO"))) ;
            res.moveToNext();
        }
        return it ;
    }

    public UsuarioAcesso getUsuario(int id){
        UsuarioAcesso it = null ;
        Cursor res =  db.rawQuery( "SELECT ID, USUARIO_ID, ACESSO_ID, DESCRICAO " +
                "FROM "+TABLE+" WHERE USUARIO_ID = ? ", new String[]{String.valueOf(id)});
        res.moveToFirst();
        if (!res.isAfterLast()){
            it = new UsuarioAcesso(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                    res.getInt(res.getColumnIndexOrThrow("ACESSO_ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO"))) ;
            res.moveToNext();
        }
        return it ;
    }

    public void incluir(UsuarioAcesso it){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", it.getId());
        contentValues.put("USUARIO_ID", it.getUsuario_id());
        contentValues.put("ACESSO_ID", it.getAcesso_id());
        contentValues.put("DESCRICAO", it.getDescricao());
        db.insert(TABLE, null, contentValues);
    }

    private void excluir(){
        db.delete(TABLE, "ID > ?", new String[]{"0"}) ;
    }

}
