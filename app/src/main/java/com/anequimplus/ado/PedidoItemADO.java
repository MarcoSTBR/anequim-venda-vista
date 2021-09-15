package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.Produto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoItemADO {
    private Context ctx ;
    private final String DB_TABLE = "PEDIDO_ITEM" ;
    private SQLiteDatabase db ;

    public PedidoItemADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public void pedidoItemAdd(Pedido pedido, ItenSelect itenSelect) {
        PedidoItem it = new PedidoItem(0, pedido, itenSelect) ;
        inserir(it);
    }

    public void atualiza(PedidoItem pedidoItem) {
        alterar(pedidoItem);
    }


    public int getListItens() {
        int n = 0 ;
        List<Pedido> list = Dao.getPedidoADO(ctx).getList() ;
        for (Pedido p : list) {
            for (PedidoItem it : p.getListPedidoItem()) {
                n = n + 1 ;
                }
            }
        return n ;
    }

    public PedidoItem getPedidoItemId(int nid) {
        PedidoItem pedidoItem = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO_ID, PRODUTO_ID, " +
                "QUANTIDADE, PRECO, DESCONTO, VALOR, OBS FROM PEDIDO_ITEM " +
                "WHERE ID = ?", new String[] {String.valueOf(nid)} );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Pedido pedido   = Dao.getPedidoADO(ctx).getId(res.getInt(res.getColumnIndex("PEDIDO_ID"))) ;
            Produto produto = Dao.getProdutoADO(ctx).getProdtoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))) ;
            ItenSelect itenSelect = new ItenSelect(res.getInt(res.getColumnIndex("ID")),
                    produto,
                    res.getDouble(res.getColumnIndex("QUANTIDADE")),
                    res.getDouble(res.getColumnIndex("PRECO")),
                    res.getDouble(res.getColumnIndex("DESCONTO")),
                    res.getDouble(res.getColumnIndex("VALOR")),
                    res.getString(res.getColumnIndex("OBS")));
            pedidoItem = new PedidoItem(
                    res.getInt(res.getColumnIndex("ID")),
                    pedido,
                    itenSelect) ;
            res.moveToNext();
        }
        return pedidoItem ;
    }

    public List<PedidoItem> getListPedidoItem(Pedido pedido){
        List<PedidoItem> list = new ArrayList<PedidoItem>() ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO_ID, PRODUTO_ID, " +
                "QUANTIDADE, PRECO, DESCONTO, VALOR, OBS FROM PEDIDO_ITEM WHERE PEDIDO_ID = ?", new String[] {String.valueOf(pedido.getId())} );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Produto produto = Dao.getProdutoADO(ctx).getProdtoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))) ;
            ItenSelect itenSelect = new ItenSelect(res.getInt(res.getColumnIndex("ID")),
                    produto,
                    res.getDouble(res.getColumnIndex("QUANTIDADE")),
                    res.getDouble(res.getColumnIndex("PRECO")),
                    res.getDouble(res.getColumnIndex("DESCONTO")),
                    res.getDouble(res.getColumnIndex("VALOR")),
                    res.getString(res.getColumnIndex("OBS")));
            list.add(new PedidoItem(
                    res.getInt(res.getColumnIndex("ID")),
                    pedido,
                    itenSelect)) ;
            res.moveToNext();
        }
        return list ;
    }

    private void inserir(PedidoItem pedidoItem){
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO_ID", pedidoItem.getPedido().getId());
        contentValues.put("PRODUTO_ID", pedidoItem.getItenSelect().getProduto().getId());
        contentValues.put("QUANTIDADE", pedidoItem.getItenSelect().getQuantidade());
        contentValues.put("PRECO", pedidoItem.getItenSelect().getPreco());
        contentValues.put("DESCONTO", pedidoItem.getItenSelect().getDesconto());
        contentValues.put("OBS", pedidoItem.getItenSelect().getObs());
        contentValues.put("VALOR", pedidoItem.getItenSelect().getValor());
        pedidoItem.setId((int) db.insert(DB_TABLE, null, contentValues)) ;
    }

    public void alterar(PedidoItem pedidoItem){
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO_ID", pedidoItem.getPedido().getId());
        contentValues.put("PRODUTO_ID", pedidoItem.getItenSelect().getProduto().getId());
        contentValues.put("QUANTIDADE", pedidoItem.getItenSelect().getQuantidade());
        contentValues.put("PRECO", pedidoItem.getItenSelect().getPreco());
        contentValues.put("DESCONTO", pedidoItem.getItenSelect().getDesconto());
        contentValues.put("OBS", pedidoItem.getItenSelect().getObs());
        contentValues.put("VALOR", pedidoItem.getItenSelect().getValor());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[] {String.valueOf(pedidoItem.getId())});
    }

    public void delete(){
        db.delete(DB_TABLE,null, null) ;
    }

    public void delete(Pedido pedido){
        db.delete(DB_TABLE,"PEDIDO_ID = ?",new String[]{ String.valueOf(pedido.getId()) }) ;
    }

    public void delete(PedidoItem pedidoItem){
        db.delete(DB_TABLE,"ID = ?",new String[]{ String.valueOf(pedidoItem.getId()) }) ;
    }

    public void delete(int id){
        db.delete(DB_TABLE,"ID = ?",new String[]{ String.valueOf(id) }) ;
    }

}
