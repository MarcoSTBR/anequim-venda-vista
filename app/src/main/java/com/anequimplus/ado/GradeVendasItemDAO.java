package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.GradeVendas;
import com.anequimplus.entity.GradeVendasItem;
import com.anequimplus.entity.Produto;

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
    }

/*
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
*/

    public void setGradeVendasItem(JSONArray jarr) throws JSONException {
      //  List<GradeVendasItem> ll = getGradeVendasItem() ;
      //  GradeVendasItem gi = null ;
      //GradeVendasItem resp = null ;
        for (int i = 0 ; i < jarr.length() ; i++) {
            GradeVendasItem gi = new GradeVendasItem(jarr.getJSONObject(i)) ;
            inserir(gi);
/*
            resp = getGradeItemNaList(ll, gi.getId()) ;
            if (resp == null) {
               inserir(gi);
            } else {
                alterar(gi);
            }
             */
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
            list.add(new GradeVendasItem(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getInt(res.getColumnIndexOrThrow("GRADE_VENDAS_ID")),
                    res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    DaoDbTabela.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID"))))
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
            Produto p = DaoDbTabela.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID"))) ;
            list.add(new GradeVendasItem(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getInt(res.getColumnIndexOrThrow("GRADE_VENDAS_ID")),
                    res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    p));
            res.moveToNext();
        }
        return list ;
    }

    public List<Produto> getGradeVendasProdutos(GradeVendas g){
        List<Produto> list = new ArrayList<Produto>() ;
        Cursor res =  db.rawQuery( "SELECT ID, GRADE_VENDAS_ID, PRODUTO_ID, STATUS " +
                "FROM GRADE_VENDAS_ITEM WHERE GRADE_VENDAS_ID = ? " +
                "ORDER BY PRODUTO_ID  ",new String[]{String.valueOf(g.getId())});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(
                    DaoDbTabela.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID")))
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

    public void cancelar(ContaPedidoItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("STATUS", 0);
        db.update("GRADE_VENDAS_ITEM", contentValues, "ID = ?", new String[] {String.valueOf(item.getId())});
    }

    public void excluir(){
        db.delete("GRADE_VENDAS_ITEM", "ID > ?", new String[]{"0"}) ;
       // db.delete("GRADE_VENDAS_ITEM", null, null) ;
    }
}
