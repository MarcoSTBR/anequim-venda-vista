package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "PEDIDO" ;

    public PedidoADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<Pedido> getList(List<FilterTable> filters){
        List<Pedido> l = new ArrayList<Pedido>() ;
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
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, DATA " +
                " FROM PEDIDO "+ where, null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                List<FilterTable> fitersItem = new ArrayList<FilterTable>() ;
                fitersItem.add(new FilterTable("PEDIDO_ID", "=", res.getString(res.getColumnIndexOrThrow("ID")))) ;
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                Pedido p = new Pedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        Dao.getPedidoItemADO(ctx).getList(fitersItem)) ;
                l.add(p) ;
                Log.i("listpedidos", p.toString()) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public Pedido get(int id){
        Pedido p = null ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(id))) ;
        List<Pedido> pedidos = getList(filters) ;
        if (pedidos.size() > 0)
            p = pedidos.get(0) ;
        return p ;
    }

    public void incluir(Pedido p){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO", p.getPedido());
    //    contentValues.put("STATUS", p.getStatus());
        contentValues.put("DATA", df.format(p.getData()));
        p.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }


    public Pedido getPedido(String pedidoSelecionado) {
        Pedido p = null ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("PEDIDO", "=", pedidoSelecionado)) ;
        List<Pedido> pedidos = getList(filters) ;
        if (pedidos.size() > 0)
            p = pedidos.get(0) ;
          else {
            p = new Pedido(0, pedidoSelecionado, new Date(), new ArrayList<PedidoItem>()) ;
            incluir(p);
        }
        return p ;
    }

    public void limparPedidosVazios(){
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        List<Pedido> pedidos = getList(filters) ;
        for (Pedido p : pedidos){
            if (p.getListPedidoItem().size() == 0) {
                excluir(p.getId());
            }
        }
    }


    public void excluir(int id) {
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(id)}) ;
    }
}
