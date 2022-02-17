package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoItemCancelamentoDAO {

    private Context ctx ;
    private SQLiteDatabase db = null ;

    public ContaPedidoItemCancelamentoDAO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
        //db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM_CANCEL") ;
        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM_CANCEL ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "CONTA_PEDIDO_ITEM_ID INTEGER, "
                + "DATA DATETIME, "
                + "USUARIO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "STATUS INTEGER)") ;

    }

    public void incluir(ContaPedidoItemCancelamento it){
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", it.getUuid());
        contentValues.put("CONTA_PEDIDO_ITEM_ID", it.getContaPedidoItem().getId());
        contentValues.put("USUARIO_ID", it.getUsuario_id());
        contentValues.put("QUANTIDADE", it.getQuantidade());
        contentValues.put("DATA", fdate.format(it.getData()));
        contentValues.put("STATUS", it.getStatus());
        it.setId((int) db.insert("PEDIDO_ITEM_CANCEL", null, contentValues) );
    }

    public List<ContaPedidoItemCancelamento> getList(List<FilterTable> filters){
        List<ContaPedidoItemCancelamento> list = new ArrayList<ContaPedidoItemCancelamento>() ;
        String where = "" ;
        if (filters.size() > 0){
            for (FilterTable f : filters){
                if (where.length() == 0)
                    where = "("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
                else
                    where = "AND ("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
            }
            where = " WHERE " + where ;
        }
        Cursor res =  db.rawQuery( "SELECT ID, UUID, CONTA_PEDIDO_ITEM_ID, " +
                " USUARIO_ID, DATA, QUANTIDADE, STATUS "+
                "FROM PEDIDO_ITEM_CANCEL "+where,null);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Date dt = null;
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));

                list.add(new ContaPedidoItemCancelamento(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("UUID")),
                    Dao.getContaPedidoItemInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ITEM_ID"))),
                    dt,
                    res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                    res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            res.moveToNext();
        }
        return list ;
    }

}
