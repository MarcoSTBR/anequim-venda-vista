package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.GradeVendas;
import com.anequimplus.entity.GradeVendasItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GradeVendasItemDAO {

    private Context ctx ;
    private SQLiteDatabase db = null ;

    public GradeVendasItemDAO(Context ctx){
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
        /*
        db.execSQL("DROP TABLE IF EXISTS GRADE_VENDAS_ITEM") ;
        db.execSQL("CREATE TABLE IF NOT EXISTS GRADE_VENDAS_ITEM ( "
                + "ID INTEGER , "
                + "GRADE_VENDAS_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "PRECO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "STATUS INTEGER)");

         */

    }

   public void setGradeVendasItem(JSONArray jarr) throws JSONException {
       List<GradeVendasItem> ll = getGradeVendasItem() ;
       GradeVendasItem gi = null ;
       GradeVendasItem resp = null ;
       for (int i = 0 ; i < jarr.length() ; i++) {
           gi = new GradeVendasItem(jarr.getJSONObject(i)) ;
           resp = getGradeItemNaList(ll, gi.getId()) ;
           if (resp == null) {
               inserir(gi);
           } else {
               alterar(gi);
           }
       }
   }

   private GradeVendasItem getGradeItemNaList(List<GradeVendasItem>  l, int i){
        GradeVendasItem resp = null ;
        for (GradeVendasItem g : l) {
            if (g.getId() == i) resp = g ;
        }
        return resp ;
   }

    public List<GradeVendasItem> getGradeVendasItem(GradeVendas g){
        List<GradeVendasItem> list = new ArrayList<GradeVendasItem>() ;
        Cursor res =  db.rawQuery( "SELECT ID, GRADE_VENDAS_ID, PRODUTO_ID, STATUS " +
                "FROM GRADE_VENDAS_ITEM WHERE GRADE_VENDAS_ID = ? AND STATUS = ? ",
                new String[]{String.valueOf(g.getId()),String.valueOf(1)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(new GradeVendasItem(res.getInt(res.getColumnIndex("ID")),
                    res.getInt(res.getColumnIndex("GRADE_VENDAS_ID")),
                    res.getInt(res.getColumnIndex("PRODUTO_ID")),
                    res.getInt(res.getColumnIndex("STATUS")),
                    Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))))
            );
            res.moveToNext();
        }
        return list ;
    }

    private List<GradeVendasItem> getGradeVendasItem(){
        List<GradeVendasItem> list = new ArrayList<GradeVendasItem>() ;
        Cursor res =  db.rawQuery( "SELECT ID, GRADE_VENDAS_ID, PRODUTO_ID, STATUS " +
                        "FROM GRADE_VENDAS_ITEM ORDER BY GRADE_VENDAS_ID  ",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(new GradeVendasItem(res.getInt(res.getColumnIndex("ID")),
                    res.getInt(res.getColumnIndex("GRADE_VENDAS_ID")),
                    res.getInt(res.getColumnIndex("PRODUTO_ID")),
                    res.getInt(res.getColumnIndex("STATUS")),
                    Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))))
            );
            res.moveToNext();
        }
        return list ;
    }

    public void inserir(GradeVendasItem g){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", g.getId());
        contentValues.put("GRADE_VENDAS_ID", g.getGradeVendasId());
        contentValues.put("PRODUTO_ID", g.getProduto_id());
        contentValues.put("STATUS", g.getStatus());
        db.insert("GRADE_VENDAS_ITEM", null, contentValues) ;
    }

    public void alterar(GradeVendasItem g){
        ContentValues contentValues = new ContentValues();
        contentValues.put("GRADE_VENDAS_ID", g.getGradeVendasId());
        contentValues.put("PRODUTO_ID", g.getProduto_id());
        contentValues.put("STATUS", g.getStatus());
        db.update("GRADE_VENDAS_ITEM", contentValues, "ID = ?", new String[] {String.valueOf(g.getId())});
    }
}
