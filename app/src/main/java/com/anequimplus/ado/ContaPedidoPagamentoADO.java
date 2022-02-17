package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.Modalidade;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoPagamentoADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private final String BANCO = "PEDIDO_PG_I" ;

    public ContaPedidoPagamentoADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<ContaPedidoPagamento> getList(ContaPedido cp) {
        List<ContaPedidoPagamento> l = new ArrayList<ContaPedidoPagamento>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, DATA, PEDIDO_ID, CAIXA_ID, MODALIDADE_ID, VALOR, STATUS " +
                " FROM PEDIDO_PG_I WHERE PEDIDO_ID = ? ", new String[]{String.valueOf(cp.getId())});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                l.add(new ContaPedidoPagamento(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("PEDIDO_ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        dt,
                        res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                        Dao.getModalidadeADO(ctx).getModalidade(res.getInt(res.getColumnIndexOrThrow("MODALIDADE_ID"))),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")))) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

    public List<ContaPedidoPagamento> getPagamentos(int id_conta) {
        List<ContaPedidoPagamento> l = new ArrayList<ContaPedidoPagamento>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, DATA, PEDIDO_ID, CAIXA_ID, MODALIDADE_ID, VALOR, STATUS " +
                " FROM PEDIDO_PG_I WHERE PEDIDO_ID = ? ", new String[]{String.valueOf(id_conta)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                l.add(new ContaPedidoPagamento(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("PEDIDO_ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        dt,
                        res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                        Dao.getModalidadeADO(ctx).getModalidade(res.getInt(res.getColumnIndexOrThrow("MODALIDADE_ID"))),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")))) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }


    public List<ContaPedidoPagamento> getCaixaList(Caixa c) {
        List<ContaPedidoPagamento> l = new ArrayList<ContaPedidoPagamento>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, UUID, DATA, PEDIDO_ID, CAIXA_ID, MODALIDADE_ID, VALOR, STATUS " +
                " FROM PEDIDO_PG_I WHERE CAIXA_ID = ? ", new String[]{String.valueOf(c.getId())});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                l.add(new ContaPedidoPagamento(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getInt(res.getColumnIndexOrThrow("PEDIDO_ID")),
                        res.getString(res.getColumnIndexOrThrow("UUID")),
                        dt,
                        res.getInt(res.getColumnIndexOrThrow("CAIXA_ID")),
                        Dao.getModalidadeADO(ctx).getModalidade(res.getInt(res.getColumnIndexOrThrow("MODALIDADE_ID"))),
                        res.getDouble(res.getColumnIndexOrThrow("VALOR")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")))) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return l ;
    }

/*
    public ContaPedidoPagamento pagamentoADD(ContaPedido c, JSONObject j) throws JSONException {
        ContaPedidoPagamento p = new ContaPedidoPagamento(j.getInt("id"),
                new Modalidade(j.getJSONObject("MODALIDADE")),
                j.getDouble("VALOR"));
        return p ;
    }
*/


    public void incluir(ContaPedidoPagamento contaPedidoPagamento) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", contaPedidoPagamento.getUuid());
        contentValues.put("DATA", df.format(contaPedidoPagamento.getData()));
        contentValues.put("PEDIDO_ID", contaPedidoPagamento.getContaPedido_id());
        contentValues.put("CAIXA_ID", contaPedidoPagamento.getCaixa_id());
        contentValues.put("MODALIDADE_ID", contaPedidoPagamento.getModalidade().getId());
        contentValues.put("VALOR", contaPedidoPagamento.getValor());
        contentValues.put("STATUS", contaPedidoPagamento.getStatus());
        contaPedidoPagamento.setId( (int) db.insert(BANCO, null, contentValues));
    }

    public void alterar(ContaPedidoPagamento contaPedidoPagamento) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UUID", contaPedidoPagamento.getUuid());
        contentValues.put("DATA", df.format(contaPedidoPagamento.getData()));
        contentValues.put("PEDIDO_ID", contaPedidoPagamento.getContaPedido_id());
        contentValues.put("CAIXA_ID", contaPedidoPagamento.getCaixa_id());
        contentValues.put("MODALIDADE_ID", contaPedidoPagamento.getModalidade().getId());
        contentValues.put("VALOR", contaPedidoPagamento.getValor());
        contentValues.put("STATUS", contaPedidoPagamento.getStatus());
        db.update(BANCO, contentValues, "ID = ?", new String[] {String.valueOf(contaPedidoPagamento.getId())});
    }

    public List<ContaPedidoPagamento> getList(JSONArray pags) throws JSONException{
        List<ContaPedidoPagamento> l = new ArrayList<ContaPedidoPagamento>() ;
        for (int i=0 ; i < pags.length() ; i++){
            Modalidade modalidade = Dao.getModalidadeADO(ctx).getModalidade(pags.getJSONObject(i).getInt("MODALIDADE_ID")) ;
            l.add(new ContaPedidoPagamento(pags.getJSONObject(i), modalidade)) ;
        }
        return l ;
    }

}
