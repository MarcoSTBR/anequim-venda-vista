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

public class ContaPedidoItemDAO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "PEDIDO_ITEM_I" ;

    public ContaPedidoItemDAO(android.content.Context ctx){
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<ContaPedidoItem> listItens(int id_conta){
        List<ContaPedidoItem> l = new ArrayList<ContaPedidoItem>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, DATA, PEDIDO_ID, " +
                " PRODUTO_ID, QUANTIDADE, PRECO, DESCONTO, COMISSAO, VALOR, " +
                " STATUS, OBS FROM PEDIDO_ITEM_I WHERE  PEDIDO_ID = ? ", new String[]{String.valueOf(id_conta)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                Produto p = Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID"))) ;
                ContaPedidoItem it = new ContaPedidoItem(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("PEDIDO_ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        p,
                        res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                        res.getDouble(res.getColumnIndexOrThrow("PRECO")),
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        res.getDouble(res.getColumnIndexOrThrow("COMISSAO")),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getString(res.getColumnIndexOrThrow("OBS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS"))
                );
                l.add(it) ;
                Log.i("listContasItens", it.toString()) ;

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
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                Produto p = Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID"))) ;
                it = new ContaPedidoItem(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("PEDIDO_ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        p,
                        res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                        res.getDouble(res.getColumnIndexOrThrow("PRECO")),
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        res.getDouble(res.getColumnIndexOrThrow("COMISSAO")),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getString(res.getColumnIndexOrThrow("OBS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS"))
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
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                Produto p = Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID"))) ;
                ContaPedidoItem it = new ContaPedidoItem(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("PEDIDO_ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        p,
                        res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                        res.getDouble(res.getColumnIndexOrThrow("PRECO")),
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        res.getDouble(res.getColumnIndexOrThrow("COMISSAO")),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getString(res.getColumnIndexOrThrow("OBS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS"))
                );
                l.add(it) ;
                Log.i("listContasItens", it.toString()) ;

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

    public void transferir(ContaPedidoItem item, ContaPedido destino) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO_ID", destino.getId());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[]{String.valueOf(item.getId())});

    }
}
