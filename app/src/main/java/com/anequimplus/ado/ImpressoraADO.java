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

    }

    public void ImpressoraADD(JSONArray jarr) throws JSONException {
       excluir() ;
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
            TipoImpressora tp = TipoImpressora.valueOf (res.getString(res.getColumnIndexOrThrow("TIPOIMPRESSORA"))) ;
            it  = new Impressora(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getInt(res.getColumnIndexOrThrow("TAMCOLUNA")),
                    tp,
                    res.getInt(res.getColumnIndexOrThrow("STATUS"))) ;
            res.moveToNext();
        }
        return it;


    }
    public List<Impressora> getList() {
        List<Impressora> list = new ArrayList<Impressora>();
        list.add(getNenhuma()) ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, TAMCOLUNA, TIPOIMPRESSORA, STATUS FROM IMPRESSORA  ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            TipoImpressora tp = TipoImpressora.valueOf (res.getString(res.getColumnIndexOrThrow("TIPOIMPRESSORA"))) ;
            list.add(new Impressora(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getInt(res.getColumnIndexOrThrow("TAMCOLUNA")),
                    tp,
                    res.getInt(res.getColumnIndexOrThrow("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

   public Impressora consultar(int id){
       Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, TAMCOLUNA, TIPOIMPRESSORA, STATUS FROM IMPRESSORA WHERE ID = ? ", new String[]{String.valueOf(id)});
       Impressora it = null ;
       res.moveToFirst();
       while(res.isAfterLast() == false){
           TipoImpressora tp = TipoImpressora. valueOf (res.getString(res.getColumnIndexOrThrow("TIPOIMPRESSORA"))) ;
           it  = new Impressora(res.getInt(res.getColumnIndexOrThrow("ID")),
                   res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                   res.getInt(res.getColumnIndexOrThrow("TAMCOLUNA")),
                   tp,
                   res.getInt(res.getColumnIndexOrThrow("STATUS"))) ;
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

    public void excluir(){
        db.delete("IMPRESSORA", null, null) ;
    }

    public Impressora getNenhuma(){
        return new Impressora(0, "Nenhum", 40, TipoImpressora.tpNenhum, 1) ;
    }
}
