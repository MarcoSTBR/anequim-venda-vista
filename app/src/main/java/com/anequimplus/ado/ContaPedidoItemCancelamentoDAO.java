package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.ContaPedidoItemCancelamento;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM_CANCEL ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "CONTA_PEDIDO_ITEM_ID INTEGER, "
                + "CONTA_PEDIDO_ITEM_UUID TEXT, "
                + "SYSTEM_USER_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "STATUS INTEGER) ");
    }

    public void incluir(ContaPedidoItemCancelamento it){
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", it.getUuid());
        contentValues.put("CONTA_PEDIDO_ITEM_ID", it.getContaPedidoItem_id());
        contentValues.put("CONTA_PEDIDO_ITEM_UUID", it.getContaPedidoItem_uuid());
        contentValues.put("SYSTEM_USER_ID", it.getUsuario_id());
        contentValues.put("QUANTIDADE", it.getQuantidade());
        contentValues.put("STATUS", it.getStatus());
        it.setId((int) db.insert("PEDIDO_ITEM_CANCEL", null, contentValues) );
    }

    public List<ContaPedidoItemCancelamento> getList(){
        List<ContaPedidoItemCancelamento> list = new ArrayList<ContaPedidoItemCancelamento>() ;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, CONTA_PEDIDO_ITEM_ID, CONTA_PEDIDO_ITEM_UUID, " +
                " SYSTEM_USER_ID, QUANTIDADE, STATUS "+
                "FROM PEDIDO_ITEM_CANCEL ORDER BY ID  ",null);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Date dt = null;
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                list.add(new ContaPedidoItemCancelamento(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("UUID")),
                    res.getInt(res.getColumnIndex("CONTA_PEDIDO_ITEM_ID")),
                    res.getString(res.getColumnIndex("CONTA_PEDIDO_ITEM_UUID")),
                    dt,
                    res.getInt(res.getColumnIndex("SYSTEM_USER_ID")),
                    res.getDouble(res.getColumnIndex("QUANTIDADE")),
                    res.getInt(res.getColumnIndex("STATUS"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            res.moveToNext();
        }
        return list ;

    }


}
