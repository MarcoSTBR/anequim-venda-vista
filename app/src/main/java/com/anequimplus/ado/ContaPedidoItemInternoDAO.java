package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.Produto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoItemInternoDAO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "PEDIDO_ITEM_I" ;

    public ContaPedidoItemInternoDAO(android.content.Context ctx){
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
/*
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM_I ") ;
        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM_I ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "DATA DATETIME, "
                + "PEDIDO_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "VALOR DOUBLE, "
                + "STATUS INTEGER, "
                + "OBS TEXT) ");
*/

    }

    public List<ContaPedidoItem> listItens(ContaPedido cp){
        List<ContaPedidoItem> l = new ArrayList<ContaPedidoItem>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, DATA, PEDIDO_ID, " +
                " PRODUTO_ID, QUANTIDADE, PRECO, DESCONTO, COMISSAO, VALOR, " +
                " STATUS, OBS FROM PEDIDO_ITEM_I WHERE  PEDIDO_ID = ? ", new String[]{String.valueOf(cp.getId())});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                Produto p = Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))) ;
                ContaPedidoItem it = new ContaPedidoItem(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("UUID")),
                        p,
                        res.getDouble(res.getColumnIndex("QUANTIDADE")),
                        res.getDouble(res.getColumnIndex("PRECO")),
                        res.getDouble(res.getColumnIndex("DESCONTO")),
                        res.getDouble(res.getColumnIndex("COMISSAO")),
                        res.getDouble(res.getColumnIndex("VALOR")),
                        res.getString(res.getColumnIndex("OBS")),
                        res.getInt(res.getColumnIndex("STATUS"))
                );
                l.add(it) ;
                Log.i("listContasItens", it.getProduto().getDescricao()) ;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public ContaPedidoItem get(int id){
        ContaPedidoItem it = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, DATA, PEDIDO_ID, " +
                " PRODUTO_ID, QUANTIDADE, PRECO, DESCONTO, COMISSAO, VALOR, " +
                " STATUS, OBS FROM PEDIDO_ITEM_I WHERE  ID = ? ", new String[]{String.valueOf(id)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                Produto p = Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))) ;
                it = new ContaPedidoItem(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("UUID")),
                        p,
                        res.getDouble(res.getColumnIndex("QUANTIDADE")),
                        res.getDouble(res.getColumnIndex("PRECO")),
                        res.getDouble(res.getColumnIndex("DESCONTO")),
                        res.getDouble(res.getColumnIndex("COMISSAO")),
                        res.getDouble(res.getColumnIndex("VALOR")),
                        res.getString(res.getColumnIndex("OBS")),
                        res.getInt(res.getColumnIndex("STATUS"))
                );
                Log.i("listContasItens", it.getProduto().getDescricao()) ;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return it ;
    }


    public List<ContaPedidoItem> listItensGroup(ContaPedido cp){
        List<ContaPedidoItem> l = new ArrayList<ContaPedidoItem>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT MAX(ID) ID, MAX(UUID) UUID, MAX(DATA) DATA, PEDIDO_ID, " +
                " PRODUTO_ID, SUM(QUANTIDADE) QUANTIDADE, PRECO, SUM(DESCONTO) DESCONTO, " +
                "SUM(COMISSAO) COMISSAO, SUM(VALOR) VALOR, " +
                " STATUS, OBS " +
                " FROM PEDIDO_ITEM_I WHERE  PEDIDO_ID = ? " +
                " GROUP BY PRODUTO_ID, PRECO, STATUS, OBS ", new String[]{String.valueOf(cp.getId())});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                Produto p = Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))) ;
                ContaPedidoItem it = new ContaPedidoItem(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("UUID")),
                        p,
                        res.getDouble(res.getColumnIndex("QUANTIDADE")),
                        res.getDouble(res.getColumnIndex("PRECO")),
                        res.getDouble(res.getColumnIndex("DESCONTO")),
                        res.getDouble(res.getColumnIndex("COMISSAO")),
                        res.getDouble(res.getColumnIndex("VALOR")),
                        res.getString(res.getColumnIndex("OBS")),
                        res.getInt(res.getColumnIndex("STATUS"))
                );
                l.add(it) ;
                Log.i("listContasItens", it.getProduto().getDescricao()) ;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }


    public void inserir(ContaPedido cp, ContaPedidoItem p){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", p.getUUID());
        contentValues.put("DATA", df.format(new Date()));
        contentValues.put("PEDIDO_ID", cp.getId());
        contentValues.put("PRODUTO_ID", p.getProduto().getId());
        contentValues.put("QUANTIDADE", p.getQuantidade());
        contentValues.put("PRECO", p.getPreco());
        contentValues.put("DESCONTO", p.getDesconto());
        contentValues.put("VALOR", p.getValor());
        contentValues.put("OBS", p.getObs());
        contentValues.put("STATUS", p.getStatus());
        Log.i("contapedidoitem", p.getProduto().getDescricao());
        p.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }

    public void alterar(ContaPedidoItem p){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", p.getUUID());
        contentValues.put("DATA", df.format(new Date()));
        contentValues.put("PEDIDO_ID", p.getId());
        contentValues.put("PRODUTO_ID", p.getProduto().getId());
        contentValues.put("QUANTIDADE", p.getQuantidade());
        contentValues.put("PRECO", p.getPreco());
        contentValues.put("DESCONTO", p.getDesconto());
        contentValues.put("VALOR", p.getValor());
        contentValues.put("OBS", p.getObs());
        contentValues.put("STATUS", p.getStatus());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[]{String.valueOf(p.getId())});

    }

    public void excluir(ContaPedidoItem p){
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(p.getId())}) ;
    }

    public void excluir(){
        db.delete(DB_TABLE, null, null) ;
    }


    public void cancelar(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("STATUS", 0);
        db.update(DB_TABLE, contentValues, "ID = ?", new String[]{String.valueOf(id)});

        Log.i("contaPedidoItem", "id "+id)  ;
        Log.i("contaPedidoItem", get(id).toString() ) ;


    }
}
