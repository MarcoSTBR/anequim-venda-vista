package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.ItenSelect;
import com.anequimplus.entity.Produto;
import com.anequimplus.entity.VendaVista;
import com.anequimplus.entity.VendaVistaItem;

import java.util.ArrayList;
import java.util.List;

public class VendaVistaItemADO {

    private Context ctx ;
    private final String DB_TABLE = "VENDA_VISTA_ITEM" ;
    private SQLiteDatabase db ;

    public VendaVistaItemADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
        /*
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA_ITEM") ;
        db.execSQL("CREATE TABLE IF NOT EXISTS VENDA_VISTA_ITEM ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "VENDA_VISTA_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "VALOR DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "STATUS INTEGER,"
                + "OBS TEXT)") ;

         */

    }

    public List<VendaVistaItem> getList(VendaVista vendaVista){
        List<VendaVistaItem> list = new ArrayList<VendaVistaItem>() ;
        Cursor res =  db.rawQuery( "SELECT ID, VENDA_VISTA_ID, PRODUTO_ID, " +
                "QUANTIDADE, PRECO, DESCONTO, VALOR, OBS, STATUS FROM VENDA_VISTA_ITEM " +
                "WHERE (VENDA_VISTA_ID = ?)  AND (STATUS = 1) ", new String[] {String.valueOf(vendaVista.getId())} );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Produto produto = Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))) ;
            ItenSelect itenSelect = new ItenSelect(res.getInt(res.getColumnIndex("ID")),
                    produto,
                    res.getDouble(res.getColumnIndex("QUANTIDADE")),
                    res.getDouble(res.getColumnIndex("PRECO")),
                    res.getDouble(res.getColumnIndex("DESCONTO")),
                    res.getDouble(res.getColumnIndex("VALOR")),
                    res.getString(res.getColumnIndex("OBS")));
            list.add(new VendaVistaItem(
                    res.getInt(res.getColumnIndex("ID")),
                    vendaVista,
                    itenSelect,
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

    public List<VendaVistaItem> getList(){
        List<VendaVistaItem> list = new ArrayList<VendaVistaItem>() ;
        Cursor res =  db.rawQuery( "SELECT V.ID, V.VENDA_VISTA_ID, V.PRODUTO_ID, VI.DATA, VI.STATUS VSTATUS," +
                "V.QUANTIDADE, V.PRECO, V.DESCONTO, V.VALOR, V.OBS, V.STATUS " +
                "FROM VENDA_VISTA_ITEM V INNER JOIN VENDA_VISTA VI " +
                "ORDER BY V.VENDA_VISTA_ID, V.ID  ",  null);
        VendaVista vendaVista = null ;
        res.moveToFirst();
        while(res.isAfterLast() == false){
            //if (vendaVista == null) vendaVista = new VendaVista() ;
            Produto produto = Dao.getProdutoADO(ctx).getProdutoId(res.getInt(res.getColumnIndex("PRODUTO_ID"))) ;
            ItenSelect itenSelect = new ItenSelect(res.getInt(res.getColumnIndex("ID")),
                    produto,
                    res.getDouble(res.getColumnIndex("QUANTIDADE")),
                    res.getDouble(res.getColumnIndex("PRECO")),
                    res.getDouble(res.getColumnIndex("DESCONTO")),
                    res.getDouble(res.getColumnIndex("VALOR")),
                    res.getString(res.getColumnIndex("OBS")));
            list.add(new VendaVistaItem(
                    res.getInt(res.getColumnIndex("ID")),
                    vendaVista,
                    itenSelect,
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;

    }

    public void vendaVistaItemAdd(VendaVista vendaVista, ItenSelect it) {
        VendaVistaItem  vIt = new VendaVistaItem(0, vendaVista, it, 1) ;
        inserir(vendaVista, vIt);
        //vendaVista.getVendaVistaItems().add(vIt) ;
    }

    public void inserir(VendaVista vendavista, VendaVistaItem vendaVistaItem){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", getProximoId());
        contentValues.put("VENDA_VISTA_ID", vendaVistaItem.getVendaVista().getId());
        contentValues.put("PRODUTO_ID", vendaVistaItem.getItenSelect().getProduto().getId());
        contentValues.put("QUANTIDADE", vendaVistaItem.getItenSelect().getQuantidade());
        contentValues.put("PRECO", vendaVistaItem.getItenSelect().getPreco());
        contentValues.put("DESCONTO", vendaVistaItem.getItenSelect().getDesconto());
        contentValues.put("OBS", vendaVistaItem.getItenSelect().getObs());
        contentValues.put("VALOR", vendaVistaItem.getItenSelect().getValor());
        contentValues.put("STATUS", vendaVistaItem.getStatus());
        vendaVistaItem.setId((int) db.insert(DB_TABLE, null, contentValues)) ;
        vendavista.getVendaVistaItems().add(vendaVistaItem);
    }

    public int getProximoId(){
       return getList().size() + 1 ;

    }

    public void alterar(VendaVistaItem vendaVistaItem){
        ContentValues contentValues = new ContentValues();
        contentValues.put("VENDA_VISTA_ID", vendaVistaItem.getVendaVista().getId());
        contentValues.put("PRODUTO_ID", vendaVistaItem.getItenSelect().getProduto().getId());
        contentValues.put("QUANTIDADE", vendaVistaItem.getItenSelect().getQuantidade());
        contentValues.put("PRECO", vendaVistaItem.getItenSelect().getPreco());
        contentValues.put("DESCONTO", vendaVistaItem.getItenSelect().getDesconto());
        contentValues.put("OBS", vendaVistaItem.getItenSelect().getObs());
        contentValues.put("VALOR", vendaVistaItem.getItenSelect().getValor());
        contentValues.put("STATUS", vendaVistaItem.getStatus());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[] {String.valueOf(vendaVistaItem.getId())});
    }

    public void excluir(VendaVista vendaVista){
        db.delete(DB_TABLE, "VENDA_VISTA_ID = ?", new String[] {String.valueOf(vendaVista.getId())});
    }

    public void excluir(VendaVistaItem v){
        v.setStatus(0);
        alterar(v);
        //db.delete(DB_TABLE, "ID = "+String.valueOf(v.getId()), null);
        //db.delete(DB_TABLE, "ID = "+String.valueOf(v.getId()), null);
       // excluir();
    }

    public void excluir(){
        db.delete(DB_TABLE, null,null);
    }

}
