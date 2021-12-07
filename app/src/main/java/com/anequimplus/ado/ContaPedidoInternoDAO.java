package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.Produto;
import com.anequimplus.utilitarios.UtilSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoInternoDAO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "PEDIDO_I" ;

    public ContaPedidoInternoDAO(Context ctx){
        this.ctx = ctx ;

        db = DBHelper.getDB(ctx).getWritableDatabase() ;
        /*
        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_I ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PEDIDO TEXT , "
                + "UUID TEXT, "
                + "STATUS INTEGER, "
                + "DATA DATETIME) ");

         */
    }

    public void atualizar(List<Pedido> l){

        ContaPedido cp = null ;
        for (Pedido p : l) {
            cp = consultarSimples(p.getPedido()) ;
            if (cp == null) {
                cp = new ContaPedido(0, UtilSet.getUUID(), p.getPedido(), p.getData(), 0,0,null,null,1) ;
                inserir(cp);
            }
            for (PedidoItem it : p.getListPedidoItem()) {
                Produto prd = it.getItenSelect().getProduto() ;
                double comissao = prd.getComissao() / 100 * it.getItenSelect().getValor() ;
                ContaPedidoItem cpi = new ContaPedidoItem(0, UtilSet.getUUID(), prd, it.getItenSelect().getQuantidade(),
                        it.getItenSelect().getPreco(), it.getItenSelect().getDesconto(), comissao,
                        it.getItenSelect().getValor(), it.getItenSelect().getObs(), 1) ;
                Dao.getContaPedidoItemInternoDAO(ctx).inserir(cp, cpi);
            }
        }

    }

    public List<ContaPedido> getList(){

        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS " +
                " FROM PEDIDO_I ORDER BY PEDIDO ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                ContaPedido cp = new ContaPedido(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("UUID")),
                        res.getString(res.getColumnIndex("PEDIDO")),
                        dt,
                        0,
                        0,
                        null,
                        null,
                        res.getInt(res.getColumnIndex("STATUS"))) ;
//                cp.setListContaPedidoItem(Dao.getContaPedidoItemInternoDAO(ctx).listItens(cp));
                cp.setListContaPedidoItem(Dao.getContaPedidoItemInternoDAO(ctx).listItensGroup(cp));

                cp.setListPagamento(new ArrayList<ContaPedidoPagamento>());
                l.add(cp) ;
                Log.i("listContas", cp.getPedido()) ;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public List<ContaPedido> getList(int id){

        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS " +
                " FROM PEDIDO_I ORDER BY PEDIDO WHERE ID = ? ", new String[]{String.valueOf(id)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                ContaPedido cp = new ContaPedido(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("UUID")),
                        res.getString(res.getColumnIndex("PEDIDO")),
                        dt,
                        0,
                        0,
                        null,
                        null,
                        res.getInt(res.getColumnIndex("STATUS"))) ;
//                cp.setListContaPedidoItem(Dao.getContaPedidoItemInternoDAO(ctx).listItens(cp));
                cp.setListContaPedidoItem(Dao.getContaPedidoItemInternoDAO(ctx).listItensGroup(cp));

                cp.setListPagamento(new ArrayList<ContaPedidoPagamento>());
                l.add(cp) ;
                Log.i("listContas", cp.getPedido()) ;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public List<ContaPedido> getListAbertos(){

        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS " +
                " FROM PEDIDO_I ORDER BY PEDIDO ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                ContaPedido cp = new ContaPedido(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("UUID")),
                        res.getString(res.getColumnIndex("PEDIDO")),
                        dt,
                        0,
                        0,
                        null,
                        null,
                        res.getInt(res.getColumnIndex("STATUS"))) ;
//                cp.setListContaPedidoItem(Dao.getContaPedidoItemInternoDAO(ctx).listItens(cp));
                cp.setListContaPedidoItem(Dao.getContaPedidoItemInternoDAO(ctx).listItensGroup(cp));

                cp.setListPagamento(new ArrayList<ContaPedidoPagamento>());
                l.add(cp) ;
                Log.i("listContas", cp.getPedido()) ;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    private ContaPedido consultarSimples(String pedido){
        ContaPedido cp = null ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS " +
                "FROM PEDIDO_I WHERE PEDIDO = ? ", new String[]{pedido});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                cp = new ContaPedido(res.getInt(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("UUID")),
                    res.getString(res.getColumnIndex("PEDIDO")),
                    dt,
                    0,
                    0,
                    null,
                    null,
                        res.getInt(res.getColumnIndex("STATUS"))) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return cp;
    }


    private void inserir(ContaPedido p){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO", p.getPedido());
        contentValues.put("UUID", p.getUuid());
        contentValues.put("STATUS", p.getStatus());
        contentValues.put("DATA", df.format(p.getData()));
        p.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }

    public void alterar(ContaPedido p){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO", p.getPedido());
        contentValues.put("UUID", p.getUuid());
        contentValues.put("STATUS", p.getStatus());
        contentValues.put("DATA", df.format(p.getData()));
        db.update(DB_TABLE, contentValues, "ID = ?", new String[]{String.valueOf(p.getId())});

    }

    public void excluir(Pedido p){
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(p.getId())}) ;
    }

    public void excluir(){
        db.delete(DB_TABLE, null, null) ;
    }

}
