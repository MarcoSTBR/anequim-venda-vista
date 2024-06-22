package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoADO extends TableDao{

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "PEDIDO" ;

    public PedidoADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<Pedido> getList(FilterTables filters, String order){
        List<Pedido> l = new ArrayList<Pedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, DATA " +
                " FROM PEDIDO "+ getWhere(filters, order), null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                FilterTables fitersItem = new FilterTables() ;
                fitersItem.add(new FilterTable("PEDIDO_ID", "=", res.getString(res.getColumnIndexOrThrow("ID")))) ;
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                Pedido p = new Pedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        DaoDbTabela.getPedidoItemADO(ctx).getList(fitersItem, "ID")) ;
                l.add(p) ;
                Log.i("listpedidos", " "+ p.getJsonExportacao().toString()) ;
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public Pedido get(int id){
        Pedido p = null ;
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("ID", "=", String.valueOf(id))) ;
        List<Pedido> pedidos = getList(filters, "PEDIDO") ;
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
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("PEDIDO", "=", pedidoSelecionado)) ;
        List<Pedido> pedidos = getList(filters, "PEDIDO") ;
        if (pedidos.size() > 0)
            p = pedidos.get(0) ;
          else {
            p = new Pedido(0, pedidoSelecionado, new Date(), new ArrayList<PedidoItem>()) ;
            incluir(p);
        }
        return p ;
    }

    public void limparPedidosVazios(){
        FilterTables filters = new FilterTables() ;
        List<Pedido> pedidos = getList(filters, "PEDIDO") ;
        for (Pedido p : pedidos){
            if (p.getListPedidoItem().size() == 0) {
                excluir(p);
            }
        }
    }


    public void excluir(Pedido ped) {
        for (PedidoItem it : ped.getListPedidoItem()) {
            for (PedidoItemAcomp ac : it.getAcompanhamentos()){
                DaoDbTabela.getPedidoItemAcompADO(ctx).excluir(ac);
            }
            DaoDbTabela.getPedidoItemADO(ctx).excluir(it);
        }
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(ped.getId())}) ;
    }

    public void excluir(){
        DaoDbTabela.getPedidoItemAcompADO(ctx).excluir();
        DaoDbTabela.getPedidoItemADO(ctx).excluir();
        db.delete(DB_TABLE, "ID > ?", new String[]{String.valueOf(-1)}) ;
    }
}
