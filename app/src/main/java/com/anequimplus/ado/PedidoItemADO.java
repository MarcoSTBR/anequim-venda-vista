package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoItemADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "PEDIDO_ITEM" ;

    public PedidoItemADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<PedidoItem> getList(List<FilterTable> filters){
        List<PedidoItem> l = new ArrayList<PedidoItem>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        String where ="" ;
        if (filters.size() > 0 ){
            for (FilterTable f : filters) {
                if (where.length() == 0)
                    where = "("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
                else
                    where = where + " AND ("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
            }
            where = " WHERE "+ where ;
        }
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO_ID, PRODUTO_ID, QUANTIDADE, " +
                "PRECO, DESCONTO, COMISSAO, VALOR, STATUS, OBS "+
                " FROM PEDIDO_ITEM "+ where, null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
                l.add(new PedidoItem(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("PEDIDO_ID")),
                        new ItenSelect(res.getInt(res.getColumnIndexOrThrow("ID")),
                        Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID"))),
                        res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                        res.getDouble(res.getColumnIndexOrThrow("PRECO")),
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        res.getDouble(res.getColumnIndexOrThrow("COMISSAO")),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getString(res.getColumnIndexOrThrow("OBS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")))));
            res.moveToNext();
        }
        return l ;
    }

    public PedidoItem get(int id){
        PedidoItem it = null ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(id))) ;
        List<PedidoItem> itens = getList(filters) ;
        if (itens.size() > 0)
            it = itens.get(0) ;
        return it ;

    }

    public void incluir(PedidoItem p){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO_ID", p.getPedido_id());
        contentValues.put("PRODUTO_ID", p.getItenSelect().getProduto().getId());
        contentValues.put("QUANTIDADE", p.getItenSelect().getQuantidade());
        contentValues.put("PRECO", p.getItenSelect().getPreco());
        contentValues.put("DESCONTO", p.getItenSelect().getDesconto());
        contentValues.put("COMISSAO", p.getItenSelect().getComissao());
        contentValues.put("VALOR", p.getItenSelect().getValor());
        contentValues.put("OBS", p.getItenSelect().getObs());
        p.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }
    public void alterar(PedidoItem p) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO_ID", p.getPedido_id());
        contentValues.put("PRODUTO_ID", p.getItenSelect().getProduto().getId());
        contentValues.put("QUANTIDADE", p.getItenSelect().getQuantidade());
        contentValues.put("PRECO", p.getItenSelect().getPreco());
        contentValues.put("DESCONTO", p.getItenSelect().getDesconto());
        contentValues.put("COMISSAO", p.getItenSelect().getComissao());
        contentValues.put("VALOR", p.getItenSelect().getValor());
        contentValues.put("OBS", p.getItenSelect().getObs());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[] {String.valueOf(p.getId())});
    }

    public void excluir(PedidoItem pedidoItem) {
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(pedidoItem.getId())}) ;
    }

    public void excluir(Pedido pedido) {
        db.delete(DB_TABLE, "PEDIDO_ID = ?", new String[]{String.valueOf(pedido.getId())}) ;
    }

}
