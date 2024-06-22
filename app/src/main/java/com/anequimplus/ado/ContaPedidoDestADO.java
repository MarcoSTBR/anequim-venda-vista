package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.ContaPedidoDest;
import com.anequimplus.entity.FilterTables;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContaPedidoDestADO extends TableDao{

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String TABLE = "CONTA_PEDIDO_DEST" ;

    public ContaPedidoDestADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<ContaPedidoDest> consultar(FilterTables filters, String order){
        List<ContaPedidoDest> list = new ArrayList<ContaPedidoDest>() ;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, CONTA_PEDIDO_ID, CPFCNPJ, " +
                "NOME, STATUS FROM "+TABLE+" "+
                getWhere(filters, order), null);
        res.moveToFirst();
        if (!res.isAfterLast()){
                list.add(new ContaPedidoDest(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ID")),
                        res.getString(res.getColumnIndexOrThrow("CPFCNPJ")),
                        res.getString(res.getColumnIndexOrThrow("NOME")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")))
                ) ;
            res.moveToNext();
        }
        return list ;
    }

    public void inserir(ContaPedidoDest it){
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", it.getUUID());
        contentValues.put("CONTA_PEDIDO_ID", it.getContaPedido_id());
        contentValues.put("CPFCNPJ", it.getCpfcnpj());
        contentValues.put("NOME", it.getNome());
        contentValues.put("STATUS", it.getStatus());
        it.setId( (int) db.insert(TABLE, null, contentValues));
    }

    public void alterar(ContaPedidoDest it) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", it.getUUID());
        contentValues.put("CONTA_PEDIDO_ID", it.getContaPedido_id());
        contentValues.put("CPFCNPJ", it.getCpfcnpj());
        contentValues.put("NOME", it.getNome());
        contentValues.put("STATUS", it.getStatus());
        db.update(TABLE,contentValues, "ID = ?", new String[]{String.valueOf(it.getId())});
    }
}
