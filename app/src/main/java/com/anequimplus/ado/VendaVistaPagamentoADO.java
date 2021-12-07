package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.VendaVista;
import com.anequimplus.entity.VendaVistaPagamento;

import java.util.ArrayList;
import java.util.List;

public class VendaVistaPagamentoADO {

    private Context ctx ;
    private final String DB_TABLE = "VENDA_VISTA_PAGAMENTO" ;
    private SQLiteDatabase db ;

    public VendaVistaPagamentoADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<VendaVistaPagamento> getList(VendaVista vendaVista){
        List<VendaVistaPagamento> list = new ArrayList<VendaVistaPagamento>() ;
        Cursor res =  db.rawQuery( "SELECT ID, VENDA_VISTA_ID, MODALIDADE_ID, VALOR, STATUS " +
                "FROM VENDA_VISTA_PAGAMENTO " +
                "WHERE VENDA_VISTA_ID = ?", new String[] {String.valueOf(vendaVista.getId())} );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Modalidade modalidade = Dao.getModalidadeADO(ctx).getModalidade(res.getInt(res.getColumnIndex("MODALIDADE_ID"))) ;
            list.add(new VendaVistaPagamento(
                    res.getInt(res.getColumnIndex("ID")),
                    vendaVista,
                    modalidade,
                    res.getDouble(res.getColumnIndex("VALOR")),
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

    public List<VendaVistaPagamento> getListStatusOk(VendaVista vendaVista){
        List<VendaVistaPagamento> list = new ArrayList<VendaVistaPagamento>() ;
        Cursor res =  db.rawQuery( "SELECT ID, VENDA_VISTA_ID, MODALIDADE_ID, VALOR, STATUS " +
                "FROM VENDA_VISTA_PAGAMENTO " +
                "WHERE (VENDA_VISTA_ID = ?) AND (STATUS = 1) ", new String[] {String.valueOf(vendaVista.getId())} );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Modalidade modalidade = Dao.getModalidadeADO(ctx).getModalidade(res.getInt(res.getColumnIndex("MODALIDADE_ID"))) ;
            list.add(new VendaVistaPagamento(
                    res.getInt(res.getColumnIndex("ID")),
                    vendaVista,
                    modalidade,
                    res.getDouble(res.getColumnIndex("VALOR")),
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

    public void inserir(VendaVistaPagamento vendaVistaPagamento){
        ContentValues contentValues = new ContentValues();
        contentValues.put("VENDA_VISTA_ID", vendaVistaPagamento.getVendaVista().getId());
        contentValues.put("MODALIDADE_ID", vendaVistaPagamento.getModalidade().getId());
        contentValues.put("VALOR", vendaVistaPagamento.getValor());
        contentValues.put("STATUS", vendaVistaPagamento.getStatus());
        vendaVistaPagamento.setId((int) db.insert(DB_TABLE, null, contentValues)) ;
    }

    public void alterar(VendaVistaPagamento vendaVistaPagamento){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", vendaVistaPagamento.getId());
        contentValues.put("VENDA_VISTA_ID", vendaVistaPagamento.getVendaVista().getId());
        contentValues.put("MODALIDADE_ID", vendaVistaPagamento.getModalidade().getId());
        contentValues.put("VALOR", vendaVistaPagamento.getValor());
        contentValues.put("STATUS", vendaVistaPagamento.getStatus());
        db.update(DB_TABLE, contentValues, "ID = ?", new String[] {Integer.toString(vendaVistaPagamento.getId())});
    }

    public void excluir(VendaVista vendaVista){
        ContentValues contentValues = new ContentValues();
        contentValues.put("STATUS", 0);
        db.update(DB_TABLE, contentValues, "VENDA_VISTA_ID = "+vendaVista.getId(),null);
    }

    public void excluir(){
        db.delete(DB_TABLE, null, null);
    }

    public void excluir(VendaVistaPagamento vendaVistaPagamento){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", vendaVistaPagamento.getId());
        contentValues.put("VENDA_VISTA_ID", vendaVistaPagamento.getVendaVista().getId());
        contentValues.put("MODALIDADE_ID", vendaVistaPagamento.getModalidade().getId());
        contentValues.put("VALOR", vendaVistaPagamento.getValor());
        contentValues.put("STATUS", 0);
        db.update(DB_TABLE, contentValues, "ID=?", new String[] {Integer.toString(vendaVistaPagamento.getId())});
    }

}
