package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.VendaVista;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaVistaADO {

    private Context ctx ;
    private SQLiteDatabase db ;
    private final String DB_TABLE = "VENDA_VISTA" ;

    public VendaVistaADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
/*
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA_PAGAMENTO") ;
        DBHelper.getDB(ctx).criarTabelas(db) ;
  */


    }


    public VendaVista getVedaVIstaAberto() throws ParseException {
        VendaVista vendaVista = null ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor res =  db.rawQuery( "SELECT ID, CAIXA_ID, DATA, STATUS FROM VENDA_VISTA WHERE STATUS = 1", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Date d = null;
            VendaVista tmp = null ;
                d = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                tmp = new VendaVista(res.getInt(res.getColumnIndex("ID")),
                        res.getInt(res.getColumnIndex("CAIXA_ID")),
                        d,
                        res.getInt(res.getColumnIndex("STATUS")),
                        null,
                        null) ;
                tmp.setVendaVistaItems(Dao.getVendaVistaItemADO(ctx).getList(tmp));
                tmp.setVendaVistaPagamentos(Dao.getVendaVistaPagamentoADO(ctx).getList(tmp));
                if (!tmp.fechado()) {
                    vendaVista = tmp ;
                }
               // list.add(vendaVista) ;
            res.moveToNext();
        }
        return vendaVista ;
    }

    public List<VendaVista> getList() throws ParseException {
        List<VendaVista> list = new ArrayList<VendaVista>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor res =  db.rawQuery( "SELECT ID, CAIXA_ID, DATA, STATUS FROM VENDA_VISTA ORDER BY ID", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
                Date d = null;
                d = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                VendaVista vendaVista = new VendaVista(res.getInt(res.getColumnIndex("ID")),
                        res.getInt(res.getColumnIndex("CAIXA_ID")),
                        d,
                        res.getInt(res.getColumnIndex("STATUS")),
                        null,
                        null) ;
                vendaVista.setVendaVistaItems(Dao.getVendaVistaItemADO(ctx).getList(vendaVista));
                vendaVista.setVendaVistaPagamentos(Dao.getVendaVistaPagamentoADO(ctx).getList(vendaVista));
                list.add(vendaVista) ;
            res.moveToNext();
        }
        return list ;
    }

    public JSONArray getListArrayJson() throws ParseException, JSONException {
        JSONArray list = new JSONArray() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor res =  db.rawQuery( "SELECT ID, CAIXA_ID, DATA, STATUS FROM VENDA_VISTA WHERE  STATUS <> -1 ORDER BY ID", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Date d = null;
            d = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
            VendaVista vendaVista = new VendaVista(res.getInt(res.getColumnIndex("ID")),
                    res.getInt(res.getColumnIndex("CAIXA_ID")),
                    d,
                    res.getInt(res.getColumnIndex("STATUS")),
                    null,
                    null) ;
            vendaVista.setVendaVistaItems(Dao.getVendaVistaItemADO(ctx).getList(vendaVista));
            vendaVista.setVendaVistaPagamentos(Dao.getVendaVistaPagamentoADO(ctx).getList(vendaVista));
            list.put(vendaVista.toJson()) ;
            res.moveToNext();
        }
        return list ;
    }

    public void inserir(VendaVista vendaVista){
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //contentValues.put("ID", getNovoID());
        contentValues.put("DATA", df.format(vendaVista.getData()));
        contentValues.put("CAIXA_ID", vendaVista.getCaixa_id());
        contentValues.put("STATUS", vendaVista.getStatus());
        vendaVista.setId((int) db.insert(DB_TABLE, null, contentValues)) ;
    }

    public void alterar(VendaVista vendaVista){
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        contentValues.put("DATA", df.format(vendaVista.getData()));
        contentValues.put("CAIXA_ID", vendaVista.getCaixa_id());
        contentValues.put("STATUS", vendaVista.getStatus());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[] {String.valueOf(vendaVista.getId())});
    }

    public void excluir(VendaVista vendaVista){
        ContentValues contentValues = new ContentValues();
        contentValues.put("STATUS", 0);
        db.update(DB_TABLE, contentValues, "ID = ?", new String[] {Integer.toString(vendaVista.getId())});
    }

    public void excluir(){
        db.delete(DB_TABLE, null,null);
    }


    public VendaVista getVendaVista(int venda_vista_id) throws ParseException {
        VendaVista vendaVista = null ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor res =  db.rawQuery( "SELECT ID, CAIXA_ID, DATA, STATUS FROM VENDA_VISTA WHERE ID = ?", new String[]{Integer.toString(venda_vista_id)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
                Date d = null;
                d = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                vendaVista = new VendaVista(res.getInt(res.getColumnIndex("ID")),
                        res.getInt(res.getColumnIndex("CAIXA_ID")),
                        d,
                        res.getInt(res.getColumnIndex("STATUS")),
                        null,
                        null) ;
                vendaVista.setVendaVistaItems(Dao.getVendaVistaItemADO(ctx).getList(vendaVista));
                vendaVista.setVendaVistaPagamentos(Dao.getVendaVistaPagamentoADO(ctx).getList(vendaVista));
                // list.add(vendaVista) ;
            res.moveToNext();
        }
        return vendaVista ;
    }
}
