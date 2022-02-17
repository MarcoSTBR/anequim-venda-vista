package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.Produto;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoDAO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String DB_TABLE = "PEDIDO_I" ;

    public ContaPedidoDAO(Context ctx){
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public void atualizar(List<Pedido> l){

        ContaPedido cp = null ;
        for (Pedido p : l) {
            Log.i("atualizar", p.getPedido()+" "+p.getListPedidoItem().size());
            cp = new ContaPedido(UtilSet.getUUID(), p.getPedido(), p.getData(), UtilSet.getUsuarioId(ctx));
            cp = store(cp) ;
            for (PedidoItem it : p.getListPedidoItem()) {
                Produto prd = it.getItenSelect().getProduto() ;
                double comissao = prd.getComissao() / 100 * it.getItenSelect().getValor() ;
                ContaPedidoItem cpi = new ContaPedidoItem(0, cp.getId(), UtilSet.getUUID(), prd, it.getItenSelect().getQuantidade(),
                        it.getItenSelect().getPreco(), it.getItenSelect().getDesconto(), comissao,
                        it.getItenSelect().getValor(), it.getItenSelect().getObs(), 1) ;
                Dao.getContaPedidoItemInternoDAO(ctx).inserir(cp, cpi);
            }
        }
    }

    public ContaPedido store(ContaPedido c){
        ContaPedido cp = null ;
        cp = consultarSimples(c.getPedido()) ;
        if (cp == null) {
            inserir(c);
            cp = c ;
        }
        return cp ;
    }

    public List<ContaPedido> getList(){
        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Date dtf = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS, DESCONTO,   " +
                "STATUS_COMISSAO, NUM_IMPRESSAO, DATA_FECHAMENTO, NUM_PESSOAS, SYSTEM_USER_ID "+
                " FROM PEDIDO_I ORDER BY PEDIDO ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt  = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FECHAMENTO")));
                l.add(new ContaPedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        Dao.getContaPedidoItemInternoDAO(ctx).listItens(res.getInt(res.getColumnIndexOrThrow("ID"))) ,
                        Dao.getContaPedidoPagamentoADO(ctx).getPagamentos(res.getInt(res.getColumnIndexOrThrow("ID"))),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS_COMISSAO")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        dtf,
                        res.getInt(res.getColumnIndexOrThrow("NUM_PESSOAS")),
                        res.getInt(res.getColumnIndexOrThrow("SYSTEM_USER_ID")))) ;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public List<ContaPedido> getList(List<FilterTable> filterTables , String order){
        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Date dtf = null ;

        String ord = "" ;
        if (order.length() > 0) ord = " ORDER BY "+order ;

        String where[] = null ;
        if (filterTables.size() > 0) where = new String[]{} ;
        String select = "" ;

        int i = -1 ;
        for (FilterTable f : filterTables){
            if (select == "") {
                select = " WHERE ("+f.getCampo()+" "+f.getOperador() + " "+ f.getVariavel() +") "  ;

            } else {
                select = select  + " AND ("+f.getCampo()+" "+f.getOperador() + " "+ f.getVariavel() +") " ;
            }
        }

        Log.i("select", select) ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS, DESCONTO, " +
                "STATUS_COMISSAO, NUM_IMPRESSAO, DATA_FECHAMENTO, NUM_PESSOAS, SYSTEM_USER_ID "+
                " FROM PEDIDO_I "+
                select +
                ord , null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt  = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FECHAMENTO")));
                l.add(new ContaPedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        Dao.getContaPedidoItemInternoDAO(ctx).listItens(res.getInt(res.getColumnIndexOrThrow("ID"))) ,
                        Dao.getContaPedidoPagamentoADO(ctx).getPagamentos(res.getInt(res.getColumnIndexOrThrow("ID"))),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS_COMISSAO")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        dtf,
                        res.getInt(res.getColumnIndexOrThrow("NUM_PESSOAS")),
                        res.getInt(res.getColumnIndexOrThrow("SYSTEM_USER_ID")))) ;

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
        Date dt  = null ;
        Date dtf = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS, DESCONTO,   " +
                "STATUS_COMISSAO, NUM_IMPRESSAO, DATA_FECHAMENTO, NUM_PESSOAS, SYSTEM_USER_ID "+
                " FROM PEDIDO_I  WHERE ID = ? ", new String[]{String.valueOf(id)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt  = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FECHAMENTO")));
                l.add(new ContaPedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        Dao.getContaPedidoItemInternoDAO(ctx).listItens(res.getInt(res.getColumnIndexOrThrow("ID"))) ,
                        Dao.getContaPedidoPagamentoADO(ctx).getPagamentos(res.getInt(res.getColumnIndexOrThrow("ID"))),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS_COMISSAO")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        dtf,
                        res.getInt(res.getColumnIndexOrThrow("NUM_PESSOAS")),
                        res.getInt(res.getColumnIndexOrThrow("SYSTEM_USER_ID")))) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public ContaPedido get(int id){
        ContaPedido cp = null ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt  = null ;
        Date dtf = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS, DESCONTO,   " +
                "STATUS_COMISSAO, NUM_IMPRESSAO, DATA_FECHAMENTO, NUM_PESSOAS, SYSTEM_USER_ID "+
                " FROM PEDIDO_I  WHERE ID = ? ", new String[]{String.valueOf(id)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt  = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FECHAMENTO")));
                cp = new ContaPedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        Dao.getContaPedidoItemInternoDAO(ctx).listItens(res.getInt(res.getColumnIndexOrThrow("ID"))) ,
                        Dao.getContaPedidoPagamentoADO(ctx).getPagamentos(res.getInt(res.getColumnIndexOrThrow("ID"))),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS_COMISSAO")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        dtf,
                        res.getInt(res.getColumnIndexOrThrow("NUM_PESSOAS")),
                        res.getInt(res.getColumnIndexOrThrow("SYSTEM_USER_ID"))) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return cp ;
    }

    public List<ContaPedido> getList(Date d){

        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Date dtf = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS, DESCONTO,   " +
                "STATUS_COMISSAO, NUM_IMPRESSAO, DATA_FECHAMENTO, NUM_PESSOAS, SYSTEM_USER_ID  "+
                " FROM PEDIDO_I  WHERE DATA >= ? ORDER BY PEDIDO ", new String[]{df.format(d)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt  = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FECHAMENTO")));
                l.add(new ContaPedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        Dao.getContaPedidoItemInternoDAO(ctx).listItens(res.getInt(res.getColumnIndexOrThrow("ID"))) ,
                        Dao.getContaPedidoPagamentoADO(ctx).getPagamentos(res.getInt(res.getColumnIndexOrThrow("ID"))),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS_COMISSAO")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        dtf,
                        res.getInt(res.getColumnIndexOrThrow("NUM_PESSOAS")),
                        res.getInt(res.getColumnIndexOrThrow("SYSTEM_USER_ID")))) ;

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
        Date dtf = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS, DESCONTO,   " +
                "STATUS_COMISSAO, NUM_IMPRESSAO, DATA_FECHAMENTO, NUM_PESSOAS, SYSTEM_USER_ID "+
                " FROM PEDIDO_I WHERE STATUS = 1 ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt  = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FECHAMENTO")));
                l.add(new ContaPedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        Dao.getContaPedidoItemInternoDAO(ctx).listItens(res.getInt(res.getColumnIndexOrThrow("ID"))) ,
                        Dao.getContaPedidoPagamentoADO(ctx).getPagamentos(res.getInt(res.getColumnIndexOrThrow("ID"))),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS_COMISSAO")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        dtf,
                        res.getInt(res.getColumnIndexOrThrow("NUM_PESSOAS")),
                        res.getInt(res.getColumnIndexOrThrow("SYSTEM_USER_ID")))) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public List<ContaPedido> getListFechados() {
        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Date dtf = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS, DESCONTO,   " +
                "STATUS_COMISSAO, NUM_IMPRESSAO, DATA_FECHAMENTO, NUM_PESSOAS, SYSTEM_USER_ID "+
                " FROM PEDIDO_I WHERE STATUS = 2 ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt  = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FECHAMENTO")));
                l.add(new ContaPedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                        dt,
                        res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                        Dao.getContaPedidoItemInternoDAO(ctx).listItens(res.getInt(res.getColumnIndexOrThrow("ID"))) ,
                        Dao.getContaPedidoPagamentoADO(ctx).getPagamentos(res.getInt(res.getColumnIndexOrThrow("ID"))),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS_COMISSAO")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        dtf,
                        res.getInt(res.getColumnIndexOrThrow("NUM_PESSOAS")),
                        res.getInt(res.getColumnIndexOrThrow("SYSTEM_USER_ID")))) ;
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
        Date dtf = null ;
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, UUID, DATA, STATUS, DESCONTO,   " +
                "STATUS_COMISSAO, NUM_IMPRESSAO, DATA_FECHAMENTO, NUM_PESSOAS, SYSTEM_USER_ID "+
        "FROM PEDIDO_I WHERE PEDIDO = ? AND STATUS = 1 ", new String[]{pedido});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt  = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                dtf = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA_FECHAMENTO")));
                cp = new ContaPedido(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("UUID")),
                    res.getString(res.getColumnIndexOrThrow("PEDIDO")),
                    dt,
                    res.getDouble(res.getColumnIndexOrThrow("DESCONTO")),
                    Dao.getContaPedidoItemInternoDAO(ctx).listItens(res.getInt(res.getColumnIndexOrThrow("ID"))) ,
                    Dao.getContaPedidoPagamentoADO(ctx).getPagamentos(res.getInt(res.getColumnIndexOrThrow("ID"))),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS_COMISSAO")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    dtf,
                    res.getInt(res.getColumnIndexOrThrow("NUM_PESSOAS")),
                    res.getInt(res.getColumnIndexOrThrow("SYSTEM_USER_ID"))) ;
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
        contentValues.put("DESCONTO", p.getDesconto());
        contentValues.put("STATUS_COMISSAO", p.getStatus_comissao());
        contentValues.put("NUM_IMPRESSAO", p.getNum_impressao());
        contentValues.put("DATA_FECHAMENTO", df.format(p.getData_fechamento()));
        contentValues.put("NUM_PESSOAS", p.getNum_pessoas());
        contentValues.put("SYSTEM_USER_ID", p.getSystem_user_id());
        p.setId((int)db.insert(DB_TABLE, null, contentValues)) ;
    }

    public void alterar(ContaPedido p){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO", p.getPedido());
        contentValues.put("UUID", p.getUuid());
        contentValues.put("STATUS", p.getStatus());
        contentValues.put("DATA", df.format(p.getData()));
        contentValues.put("DESCONTO", p.getDesconto());
        contentValues.put("STATUS_COMISSAO", p.getStatus_comissao());
        contentValues.put("NUM_IMPRESSAO", p.getNum_impressao());
        contentValues.put("DATA_FECHAMENTO", df.format(p.getData_fechamento()));
        contentValues.put("NUM_PESSOAS", p.getNum_pessoas());
        contentValues.put("SYSTEM_USER_ID", p.getSystem_user_id());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[]{String.valueOf(p.getId())});

    }

    public void excluir(Pedido p){
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(p.getId())}) ;
    }

    public void excluir(){
        db.delete(DB_TABLE, null, null) ;
    }

    public List<ContaPedido> getJSON(JSONArray data) throws JSONException {
        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        for (int i = 0 ; i < data.length() ; i++){
            ContaPedido cp = new ContaPedido(data.getJSONObject(i), new ArrayList<ContaPedidoItem>(), new ArrayList<ContaPedidoPagamento>()) ;
            for (int ii=0 ; ii < data.getJSONObject(i).getJSONArray("ITENS").length() ; i++){
                JSONObject itj = data.getJSONObject(i).getJSONArray("ITENS").getJSONObject(ii) ;
                Produto prd = Dao.getProdutoADO(ctx).getProdutoId(itj.getInt("PRODUTO_ID")) ;
                cp.getListContaPedidoItem().add(new ContaPedidoItem(itj, prd)) ;
            }
            for (int ii=0 ; ii < data.getJSONObject(i).getJSONArray("PAGAMENTOS").length() ; i++){
                JSONObject itj = data.getJSONObject(i).getJSONArray("PAGAMENTOS").getJSONObject(ii) ;
                Modalidade m = Dao.getModalidadeADO(ctx).getModalidade(itj.getInt("MODALIDADE_ID")) ;
                cp.getListPagamento().add(new ContaPedidoPagamento(itj, m)) ;
            }
        }
        return l ;
    }
}
