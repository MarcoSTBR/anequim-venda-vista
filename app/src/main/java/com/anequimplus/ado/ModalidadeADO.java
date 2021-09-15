package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.UrlParam;
import com.anequimplus.tipos.TipoModalidade;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModalidadeADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "MODALIDADE" ;

    public ModalidadeADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
        /*
        db.execSQL("DROP TABLE IF EXISTS MODALIDADE") ;
        DBHelper.criarTabelas(db) ;

         */
    }

    public void modalidadeADD(JSONArray jarr) throws JSONException {
        excluir() ;
        List<Modalidade> list = getList();
        for (int i = 0 ; i < jarr.length() ; i++){
            JSONObject j = jarr.getJSONObject(i) ;
            Modalidade m = new Modalidade(j);
            if (getModalidade(list, m) == null) {
                inserir(m);
                list.add(m);
            } else {
                alterar(m);
            }
        }
    }


    public List<Modalidade> getList() {
        List<Modalidade> list = new ArrayList<Modalidade>() ;
        Cursor res =  db.rawQuery( "SELECT ID, CODIGO, DESCRICAO, " +
                "TIPOMODALIDADE, COD_RECEBIMENTO, URL, PARAM, STATUS FROM MODALIDADE WHERE STATUS = 1", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(new Modalidade(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("CODIGO")),
                    res.getString(res.getColumnIndex("DESCRICAO")),
                    TipoModalidade.valueOf(res.getString(res.getColumnIndex("TIPOMODALIDADE"))),
                    res.getInt(res.getColumnIndex("COD_RECEBIMENTO")),
                    new UrlParam(res.getString(res.getColumnIndex("URL")),
                            res.getString(res.getColumnIndex("PARAM"))),
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

    public Modalidade getModalidade(int modalidade_id) {
        List<Modalidade> list = getList() ;
        for (Modalidade m : list){
            if (m.getId() == modalidade_id)
               return m;
        }
        return null ;
    }

    public Modalidade getModalidade(List<Modalidade> list, Modalidade md) {
        for (Modalidade m : list){
            if (m.getId() == md.getId())
                return m;
        }
        return null ;
    }


    private void inserir(Modalidade modalidade){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", modalidade.getId());
        contentValues.put("CODIGO", modalidade.getCodigo());
        contentValues.put("DESCRICAO", modalidade.getDescricao());
        contentValues.put("TIPOMODALIDADE", modalidade.getTipoModalidade().valor);
        contentValues.put("COD_RECEBIMENTO", modalidade.getCod_recebimento());
        contentValues.put("URL", modalidade.getFoto().getUrl());
        contentValues.put("PARAM", modalidade.getFoto().getParam());
        contentValues.put("STATUS", modalidade.getStatus());
        db.insert(DB_TABLE, null, contentValues) ;
    }

    private void alterar(Modalidade modalidade){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", modalidade.getId());
        contentValues.put("CODIGO", modalidade.getCodigo());
        contentValues.put("DESCRICAO", modalidade.getDescricao());
        contentValues.put("TIPOMODALIDADE", modalidade.getTipoModalidade().valor);
        contentValues.put("COD_RECEBIMENTO", modalidade.getCod_recebimento());
        contentValues.put("URL", modalidade.getFoto().getUrl());
        contentValues.put("PARAM", modalidade.getFoto().getParam());
        contentValues.put("STATUS", modalidade.getStatus());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[] {String.valueOf(modalidade.getId())});

    }

    private void excluir(){
        db.delete(DB_TABLE, null, null) ;
    }

}
