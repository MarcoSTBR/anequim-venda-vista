package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Transferencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoTransferenciaADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "TRANSFERENCIA" ;

    public ContaPedidoTransferenciaADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS TRANSFERENCIA") ;
        db.execSQL("CREATE TABLE IF NOT EXISTS TRANSFERENCIA ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "CONTA_PEDIDO_ITEM_ID INTEGER, "
                + "CONTA_PEDIDO_ORIGEM_ID INTEGER, "
                + "CONTA_PEDIDO_DESTINO_ID INTEGER, "
                + "DATA DATETIME, "
                + "USUARIO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "STATUS INTEGER)") ;
    }

    public List<Transferencia> getList(List<FilterTable> filters){
        List<Transferencia> l = new ArrayList<Transferencia>() ;
        String where = "" ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (filters.size() > 0){
            for (FilterTable f : filters){
                if (where.length() == 0)
                    where = "("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
                 else
                    where = "AND ("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
                }
            where = " WHERE " + where ;
        }
        Cursor res =  db.rawQuery( "SELECT ID, UUID, CONTA_PEDIDO_ITEM_ID, CONTA_PEDIDO_ORIGEM_ID, " +
                "CONTA_PEDIDO_DESTINO_ID, DATA, QUANTIDADE, USUARIO_ID, STATUS "+
                "FROM " +DB_TABLE+ " "+ where , null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Date d = null;
            try {
                d = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                l.add(new Transferencia(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("UUID")),
                    d,
                    Dao.getContaPedidoInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ORIGEM_ID"))),
                    Dao.getContaPedidoInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_DESTINO_ID"))),
                    Dao.getContaPedidoItemInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ITEM_ID"))),
                    res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                    res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public void transferir(Transferencia transferencia){
        ContaPedido cp = Dao.getContaPedidoInternoDAO(ctx).store(transferencia.getContaPedido_destino()) ;
        incluir(transferencia) ;
        Dao.getContaPedidoItemInternoDAO(ctx).transferir(transferencia.getContaPedidoItem(), transferencia.getContaPedido_destino());
    }

    private void incluir(Transferencia t){
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", t.getUuid());
        contentValues.put("CONTA_PEDIDO_ITEM_ID", t.getContaPedidoItem().getId());
        contentValues.put("CONTA_PEDIDO_ORIGEM_ID", t.getContaPedido_origem().getId());
        contentValues.put("CONTA_PEDIDO_DESTINO_ID", t.getContaPedido_destino().getId());
        contentValues.put("DATA", fdate.format(t.getData()));
        contentValues.put("USUARIO_ID", t.getUsuario_id());
        contentValues.put("QUANTIDADE", t.getQuantidade());
        contentValues.put("STATUS", t.getStatus());
        t.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }
}
