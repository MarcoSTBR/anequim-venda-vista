package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoItemCancelamentoDAO extends TableDao {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String BANCO = "PEDIDO_ITEM_CANCEL" ;

    public ContaPedidoItemCancelamentoDAO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public void incluir(ContaPedidoItemCancelamento it){
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", it.getUuid());
        contentValues.put("CONTA_PEDIDO_ITEM_ID", it.getContaPedidoItem_id());
        contentValues.put("USUARIO_ID", it.getUsuario_id());
        contentValues.put("QUANTIDADE", it.getQuantidade());
        contentValues.put("DATA", fdate.format(it.getData()));
        contentValues.put("STATUS", it.getStatus());
        contentValues.put("CAIXA_ID", it.getCaixa_id());
        it.setId((int) db.insert("PEDIDO_ITEM_CANCEL", null, contentValues) );
    }

    public List<ContaPedidoItemCancelamento> getList(FilterTables filters, String order) {
        Date dt = null;
        List<ContaPedidoItemCancelamento> list = new ArrayList<ContaPedidoItemCancelamento>() ;

        Log.i("sql_cancel", "SELECT ID, UUID, CONTA_PEDIDO_ITEM_ID, " +
                " USUARIO_ID, DATA, QUANTIDADE, STATUS, CAIXA_ID "+
                "FROM PEDIDO_ITEM_CANCEL "+getWhere(filters, order)) ;

        Cursor res =  db.rawQuery( "SELECT ID, UUID, CONTA_PEDIDO_ITEM_ID, " +
                " USUARIO_ID, DATA, QUANTIDADE, STATUS, CAIXA_ID "+
                "FROM PEDIDO_ITEM_CANCEL "+getWhere(filters, order),null);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));

                list.add(new ContaPedidoItemCancelamento(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ITEM_ID")),
                        dt,
                        res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                        res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                        UtilSet.getTerminalId(ctx)));
                Log.i("sql_cancel",list.get(list.size()-1).getExportacaoJSON().toString()) ;

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            res.moveToNext();
        }
        return list ;
    }

    public List<ContaPedidoItemCancelamento> getList(List<FilterTable> filters){
        List<ContaPedidoItemCancelamento> list = new ArrayList<ContaPedidoItemCancelamento>() ;
        String where = "" ;
        if (filters.size() > 0){
            for (FilterTable f : filters){
                if (where.length() == 0)
                    where = "("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
                else
                    where = where + " AND ("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
            }
            where = " WHERE " + where ;
        }
        Cursor res =  db.rawQuery( "SELECT ID, UUID, CONTA_PEDIDO_ITEM_ID, " +
                " USUARIO_ID, DATA, QUANTIDADE, STATUS, CAIXA_ID "+
                "FROM PEDIDO_ITEM_CANCEL "+where,null);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Date dt = null;
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));

                list.add(new ContaPedidoItemCancelamento(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("UUID")),
                    res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ITEM_ID")),
                    dt,
                    res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                    res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                    UtilSet.getTerminalId(ctx)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            res.moveToNext();
        }
        return list ;
    }

    public void excluir() {
        db.delete(BANCO, "ID >= ?", new String[]{"0"}) ;
    }
}
