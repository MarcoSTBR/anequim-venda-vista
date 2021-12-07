package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.Impressora;
import com.anequimplus.tipos.TipoImpressora;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ImpressoraADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    //private static List<Impressora> list ;

    public ImpressoraADO(Context ctx){
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
        db.execSQL("CREATE TABLE IF NOT EXISTS IMPRESSORA ( "
                + "ID INTEGER PRIMARY KEY, "
                + "DESCRICAO TEXT, "
                + "TAMCOLUNA INTEGER, "
                + "TIPOIMPRESSORA TEXT, "
                + "STATUS INTEGER)"
        );
    }

    public void ImpressoraADD(JSONArray jarr) throws JSONException {
       for (int i = 0 ; i < jarr.length() ; i++){
           Impressora imp = new Impressora(jarr.getJSONObject(i)) ;
           Impressora tmp = consultar(imp.getId()) ;
           if (tmp == null) {
               inserir(imp);
           } else {
               alterar(imp);
           }
       }
    }

    public Impressora getImpressora(String descricao){
        Impressora it = null ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, TAMCOLUNA, TIPOIMPRESSORA, STATUS FROM IMPRESSORA WHERE DESCRICAO = ? ", new String[]{descricao});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            TipoImpressora tp = TipoImpressora.valueOf (res.getString(res.getColumnIndex("TIPOIMPRESSORA"))) ;
            it  = new Impressora(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("DESCRICAO")),
                    res.getInt(res.getColumnIndex("TAMCOLUNA")),
                    tp,
                    res.getInt(res.getColumnIndex("STATUS"))) ;
            res.moveToNext();
        }
        return it;


    }
    public List<Impressora> getList() {
        List<Impressora> list = new ArrayList<Impressora>();
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, TAMCOLUNA, TIPOIMPRESSORA, STATUS FROM IMPRESSORA  ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            TipoImpressora tp = TipoImpressora.valueOf (res.getString(res.getColumnIndex("TIPOIMPRESSORA"))) ;
            list.add(new Impressora(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("DESCRICAO")),
                    res.getInt(res.getColumnIndex("TAMCOLUNA")),
                    tp,
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

   public Impressora consultar(int id){
       Impressora it = null ;
       Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, TAMCOLUNA, TIPOIMPRESSORA, STATUS FROM IMPRESSORA WHERE ID = ? ", new String[]{String.valueOf(id)});
       res.moveToFirst();
       while(res.isAfterLast() == false){
           TipoImpressora tp = TipoImpressora. valueOf (res.getString(res.getColumnIndex("TIPOIMPRESSORA"))) ;
           it  = new Impressora(res.getInt(res.getColumnIndex("ID")),
                   res.getString(res.getColumnIndex("DESCRICAO")),
                   res.getInt(res.getColumnIndex("TAMCOLUNA")),
                   tp,
                   res.getInt(res.getColumnIndex("STATUS"))) ;
           res.moveToNext();
       }
       return it;
   }


    public void inserir(Impressora it){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", it.getId());
        contentValues.put("DESCRICAO",it.getDescricao());
        contentValues.put("TAMCOLUNA", it.getTamColuna());
        contentValues.put("TIPOIMPRESSORA", it.getTipoImpressora().descricao);
        contentValues.put("STATUS", it.getStatus());
        db.insert("IMPRESSORA", null, contentValues) ;
    }

    public void alterar(Impressora it){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRICAO",it.getDescricao());
        contentValues.put("TAMCOLUNA", it.getTamColuna());
        contentValues.put("TIPOIMPRESSORA", it.getTipoImpressora().descricao);
        contentValues.put("STATUS", it.getStatus());
        db.update("IMPRESSORA", contentValues, "ID = ?", new String[] {String.valueOf(it.getId())});
    }
}
