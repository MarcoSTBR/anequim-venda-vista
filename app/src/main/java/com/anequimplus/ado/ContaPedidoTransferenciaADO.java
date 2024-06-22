package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.utilitarios.UtilSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoTransferenciaADO extends TableDao {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "TRANSFERENCIA" ;

    public ContaPedidoTransferenciaADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase();
    }


    public List<Transferencia> getList(FilterTables filterTables, String order) {
        List<Transferencia> l = new ArrayList<Transferencia>() ;
        Date d = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor res =  db.rawQuery( "SELECT ID, UUID, CONTA_PEDIDO_ITEM_ID, CONTA_PEDIDO_ORIGEM_ID, " +
                "CONTA_PEDIDO_DESTINO_ID, DATA, QUANTIDADE, USUARIO_ID, STATUS, CAIXA_ID "+
                "FROM "+DB_TABLE+ " "+ getWhere(filterTables, order) , null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                d = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                // ContaPedido origem = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ORIGEM_ID"))) ;
                // ContaPedido destino = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_DESTINO_ID"))) ;
                // if (destino == null) destino = new ContaPedido("N", "N", new Date(), 1) ;
                // ContaPedidoItem item = DaoDbTabela.getContaPedidoItemInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ITEM_ID"))) ;
                l.add(new Transferencia(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        d,
                        res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ORIGEM_ID")),
                        res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_DESTINO_ID")),
                        res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ITEM_ID")),
                        res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                        res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                        UtilSet.getTerminalId(ctx)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public List<Transferencia> getList(List<FilterTable> filters){
        List<Transferencia> l = new ArrayList<Transferencia>() ;
        String where = "" ;
        Date d = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (filters.size() > 0){
            for (FilterTable f : filters){
                if (where.length() == 0)
                    where = " WHERE ("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
                 else
                    where = where + " AND ("+f.getCampo()+" "+f.getOperador()+" "+f.getVariavel()+")" ;
                }
        }
        Cursor res =  db.rawQuery( "SELECT ID, UUID, CONTA_PEDIDO_ITEM_ID, CONTA_PEDIDO_ORIGEM_ID, " +
                "CONTA_PEDIDO_DESTINO_ID, DATA, QUANTIDADE, USUARIO_ID, STATUS, CAIXA_ID "+
                "FROM "+DB_TABLE+ " "+ where , null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                d = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
               // ContaPedido origem = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ORIGEM_ID"))) ;
               // ContaPedido destino = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_DESTINO_ID"))) ;
               // if (destino == null) destino = new ContaPedido("N", "N", new Date(), 1) ;
               // ContaPedidoItem item = DaoDbTabela.getContaPedidoItemInternoDAO(ctx).get(res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ITEM_ID"))) ;
                l.add(new Transferencia(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("UUID")),
                    d,
                    res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ORIGEM_ID")),
                    res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_DESTINO_ID")),
                    res.getInt(res.getColumnIndexOrThrow("CONTA_PEDIDO_ITEM_ID")),
                    res.getDouble(res.getColumnIndexOrThrow("QUANTIDADE")),
                    res.getInt(res.getColumnIndexOrThrow("USUARIO_ID")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                    UtilSet.getTerminalId(ctx)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public void transferir(Transferencia transferencia){
        ContaPedidoItem it = DaoDbTabela.getContaPedidoItemInternoDAO(ctx).get(transferencia.getContaPedidoItem_id()) ;
        ContaPedido destino = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(transferencia.getContaPedido_destino_id()) ;
        ContaPedido cp = DaoDbTabela.getContaPedidoInternoDAO(ctx).store(destino);
        transferencia.setContaPedido_destino_id(destino.getId());
        if (transferencia.getQuantidade() == it.getQuantidade()) {
            incluir(transferencia);
            //DaoDbTabela.getContaPedidoItemInternoDAO(ctx).transferir(transferencia.getContaPedidoItem(), transferencia.getContaPedido_destino());
            DaoDbTabela.getContaPedidoItemInternoDAO(ctx).transferir(it, destino);
        } else {
            Double qtransf = transferencia.getQuantidade() ;
            Double qrest  = it.getQuantidade() - qtransf ;
            it.setQuantidade(qrest);
            it.setValor(qrest * it.getPreco());
            it.setComissao(it.getValor() * (it.getProduto().getComissao() / 100));
            DaoDbTabela.getContaPedidoItemInternoDAO(ctx).alterar(it);
            ContaPedidoItem itt = it ;
            itt.setQuantidade(qtransf);
            itt.setValor(qtransf * itt.getPreco());
            itt.setComissao(itt.getValor() * (itt.getProduto().getComissao() / 100));
            DaoDbTabela.getContaPedidoItemInternoDAO(ctx).inserir(cp, itt);
            incluir(transferencia);

        }
    }

    private void incluir(Transferencia t){
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", t.getUuid());
        contentValues.put("CONTA_PEDIDO_ITEM_ID", t.getContaPedidoItem_id());
        contentValues.put("CONTA_PEDIDO_ORIGEM_ID", t.getContaPedido_origem_id());
        contentValues.put("CONTA_PEDIDO_DESTINO_ID", t.getContaPedido_destino_id());
        contentValues.put("DATA", fdate.format(t.getData()));
        contentValues.put("USUARIO_ID", t.getUsuario_id());
        contentValues.put("QUANTIDADE", t.getQuantidade());
        contentValues.put("STATUS", t.getStatus());
        //contentValues.put("CAIXA_ID", t.getCaixa_id());
        t.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }

    public void excluir() {
        db.delete(DB_TABLE, "ID >= ?", new String[]{"0"}) ;
    }
}
