package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.entity.FilterTables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoNFceADO extends TableDao{

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private static String TABLE = "CONTA_PEDIDO_NFCE" ;

    public ContaPedidoNFceADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;

    }

    public List<ContaPedidoNFCe> getList(FilterTables filter, String order){
        List<ContaPedidoNFCe> list = new ArrayList<ContaPedidoNFCe>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor res =  db.rawQuery( "SELECT UUID, CONTA_PEDIDO_ID, DATA, "+
                " CHAVE, PROTOCOLO, XMOTIVO, STACT, STATUS_CANCELAMENTO, " +
                "STATUS_CONTINGENCIA, STATUS, TIPO "+
                " FROM "+TABLE + " "+getWhere(filter, order), null);
        Date dt = null ;
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                list.add(new ContaPedidoNFCe(res.getString(res.getColumnIndexOrThrow("UUID")),
                    res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ID")),
                    dt,
                    res.getString(res.getColumnIndexOrThrow("CHAVE")),
                    res.getString(res.getColumnIndexOrThrow("PROTOCOLO")),
                    res.getString(res.getColumnIndexOrThrow("XMOTIVO")),
                    res.getInt(res.getColumnIndexOrThrow("STACT")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS_CANCELAMENTO")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS_CONTINGENCIA")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getInt(res.getColumnIndexOrThrow("TIPO"))
                    ));
            res.moveToNext();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void incluir(ContaPedidoNFCe it) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", it.getUUID());
        contentValues.put("CONTA_PEDIDO_ID", it.getContaPedido_id());
        contentValues.put("DATA", df.format(it.getData()));
        contentValues.put("CHAVE", it.getChave());
        contentValues.put("PROTOCOLO", it.getProtocolo());
        contentValues.put("XMOTIVO", it.getX_movito());
        contentValues.put("STATUS_CANCELAMENTO", it.getStatus_cancelamento());
        contentValues.put("STATUS_CONTINGENCIA", it.getStatus_contingencia());
        contentValues.put("STACT", it.getStact());
        contentValues.put("STATUS", it.getStatus());
        contentValues.put("TIPO", it.getStatus());
        db.insert(TABLE, null, contentValues);
    }

    public void alterar(ContaPedidoNFCe it) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        //contentValues.put("UUID", it.getUUID());
        contentValues.put("CONTA_PEDIDO_ID", it.getContaPedido_id());
        contentValues.put("DATA", df.format(it.getData()));
        contentValues.put("CHAVE", it.getChave());
        contentValues.put("PROTOCOLO", it.getProtocolo());
        contentValues.put("XMOTIVO", it.getX_movito());
        contentValues.put("STATUS_CANCELAMENTO", it.getStatus_cancelamento());
        contentValues.put("STATUS_CONTINGENCIA", it.getStatus_contingencia());
        contentValues.put("STACT", it.getStact());
        contentValues.put("STATUS", it.getStatus());
        contentValues.put("TIPO", it.getStatus());
        db.update(TABLE, contentValues, "where UUID = ?", new String[]{it.getUUID()});
    }
}
