package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ItemSelect;
import com.anequimplus.entity.PedidoItemAcomp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoItemAcompADO extends TableDao{

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "PEDIDO_ITEM_ACOMP" ;

    public PedidoItemAcompADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;

    }

    public List<PedidoItemAcomp> getList(FilterTables filters, String order){
        List<PedidoItemAcomp> l = new ArrayList<PedidoItemAcomp>() ;
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO_ITEM_ID, PRODUTO_ID, QUANTIDADE, " +
                "PRECO, DESCONTO, COMISSAO, VALOR, STATUS, OBS "+
                " FROM "+DB_TABLE+" "+ getWhere(filters, order), null);
        res.moveToFirst();
        while(res.isAfterLast() == false){

                ItemSelect itemSelect = new ItemSelect(res.getInt(res.getColumnIndexOrThrow("ID")),
                        DaoDbTabela.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndexOrThrow("PRODUTO_ID"))),
                        res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                        res.getDouble(res.getColumnIndexOrThrow("PRECO")),
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        res.getDouble(res.getColumnIndexOrThrow("COMISSAO")),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getString(res.getColumnIndexOrThrow("OBS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS"))) ;
                l.add(new PedidoItemAcomp(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("PEDIDO_ITEM_ID")),
                        itemSelect));
            res.moveToNext();
        }
        return l ;
    }

    public PedidoItemAcomp get(int id){
        PedidoItemAcomp it = null ;
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(id))) ;
        List<PedidoItemAcomp> itens = getList(filters, "ID") ;
        if (itens.size() > 0)
            it = itens.get(0) ;
        return it ;

    }

    public void incluir(PedidoItemAcomp p){
      //  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO_ITEM_ID", p.getPedidoItem_id());
        contentValues.put("PRODUTO_ID", p.getItemSelect().getProduto().getId());
        contentValues.put("QUANTIDADE", p.getItemSelect().getQuantidade());
        contentValues.put("PRECO", p.getItemSelect().getPreco());
        contentValues.put("DESCONTO", p.getItemSelect().getDesconto());
        contentValues.put("COMISSAO", p.getItemSelect().getComissao());
        contentValues.put("VALOR", p.getItemSelect().getValor());
        contentValues.put("OBS", p.getItemSelect().getObs());
        p.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }
    public void alterar(PedidoItemAcomp p) {
       // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO_ITEM_ID", p.getPedidoItem_id());
        contentValues.put("PRODUTO_ID", p.getItemSelect().getProduto().getId());
        contentValues.put("QUANTIDADE", p.getItemSelect().getQuantidade());
        contentValues.put("PRECO", p.getItemSelect().getPreco());
        contentValues.put("DESCONTO", p.getItemSelect().getDesconto());
        contentValues.put("COMISSAO", p.getItemSelect().getComissao());
        contentValues.put("VALOR", p.getItemSelect().getValor());
        contentValues.put("OBS", p.getItemSelect().getObs());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[] {String.valueOf(p.getId())});
    }

    public void excluir(PedidoItemAcomp it) {
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(it.getId())}) ;
    }

    public void excluir() {
        db.delete(DB_TABLE, "ID > ?", new String[]{String.valueOf(-1)}) ;
    }

}
