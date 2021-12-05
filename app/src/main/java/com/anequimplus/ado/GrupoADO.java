package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.entity.Grupo;
import com.anequimplus.entity.Produto;

import java.util.ArrayList;
import java.util.List;

public class GrupoADO{
    private Context ctx ;
    private SQLiteDatabase db = null ;

    public GrupoADO(Context ctx) {
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<Grupo> getlistGrade() {
        return getList(1) ;
    }

    public List<Grupo> getList(){
        List<Grupo> ls = new ArrayList<Grupo>();
        List<Produto> produtos = null ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, STATUS FROM GRUPO", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            ls.add(new Grupo(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("DESCRICAO")),
                    res.getInt(res.getColumnIndex("STATUS")),
                    produtos)) ;
            res.moveToNext();
        }
        return ls ;
    }

    public List<Grupo> getList(int status){
        List<Grupo> ls = new ArrayList<Grupo>();
        List<Produto> produtos = null ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, STATUS FROM GRUPO WHERE STATUS = "+status, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ls.add(new Grupo(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("DESCRICAO")),
                    res.getInt(res.getColumnIndex("STATUS")),
                    produtos)) ;
            res.moveToNext();
        }
        return ls ;
    }


    public Grupo getGrupoID(int nid){
        Grupo grupo = null ;
        List<Produto> produtos = null ;
        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, STATUS FROM GRUPO WHERE ID = ? ", new String[]{String.valueOf(nid)});
//        Cursor res =  db.rawQuery( "SELECT ID, DESCRICAO, STATUS FROM GRUPO WHERE ID = "+nid, null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            grupo = new Grupo(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("DESCRICAO")),
                    res.getInt(res.getColumnIndex("STATUS")),
                    produtos) ;
            res.moveToNext();
        }
        return grupo;
    }



    public void inserir(Grupo grupo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", grupo.getId());
        contentValues.put("DESCRICAO", grupo.getDescricao());
        contentValues.put("STATUS", grupo.getStatus());
        db.insert("GRUPO", null, contentValues) ;
    }

    public void alterar(Grupo grupo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", grupo.getId());
        contentValues.put("DESCRICAO", grupo.getDescricao());
        contentValues.put("STATUS", grupo.getStatus());
        db.update("GRUPO", contentValues, "ID = ?", new String[] {String.valueOf(grupo.getId())});
    }


/*
    public Grupo getGrupo(JSONObject j) throws JSONException {
        Grupo gr = null;
        for (Grupo g : getlist()) {
            if (j.getInt("ID")  == g.getId()) {
                gr = g;
            }
         }
        if (gr == null) {
            gr = new Grupo(j.getInt("ID"), j.getString("DESCRICAO"), new ArrayList<Produto>());
            getlist().add(gr);
        }
        return gr;
    }


    public List<Grupo> getlistFiltro(String toString) {
        List<Grupo> lgr = new ArrayList<Grupo>() ;
        for (Grupo g : getlist()){
            for (Produto p : g.getListProdutos()){
                if ((ifEncontrou( p.getDescricao(), toString) || (ifEncontrou(toString, p.getCodBarra())))){
                    Grupo pg = null ;
                    for (Grupo itl : lgr) {
                        if (itl.getId() == g.getId()) {
                            pg = itl;
                        }
                    }
                    if (pg == null){
                        pg = new Grupo(g.getId(),g.getDescricao(),  new ArrayList<Produto>()) ;
                        lgr.add(pg);
                    }
                    pg.getListProdutos().add(p) ;
                }

            }
        }
        return lgr ;
    }

    public void setGrupoProdutoADD(JSONArray jarr) throws JSONException {
        getlist().clear();
        for (int i = 0 ; i < jarr.length() ; i++){
            JSONObject jp = jarr.getJSONObject(i) ;
            JSONObject jg = jp.getJSONObject("GRUPO") ;
            Grupo g = getGrupo(jg);
            Produto p = Dao.getProdutoADO(ctx).addProduto(g, jp) ;
            g.getListProdutos().add(p) ;
        }

    }
*/
    private boolean ifEncontrou (String p1, String p2){
        boolean b  = false;
        if (p1.toUpperCase().contains(p2.toUpperCase())) b = true ;

        Log.e("Comparou","p1 "+p1+" - p2 "+p2+" resp "+b);
        return b ;
    }


    public void delete() {
        SQLiteDatabase db = DBHelper.getDB(ctx).getWritableDatabase() ;
        db.delete("GRUPO",null,null) ;
    }
}


