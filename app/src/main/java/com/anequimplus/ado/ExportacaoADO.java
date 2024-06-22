package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.utilitarios.ProgressDisplay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExportacaoADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;
    private ProgressDisplay progress ;

    public ExportacaoADO(Context ctx) {
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public void setProgress(ProgressDisplay progress) {
        this.progress = progress;
    }

    public List<Caixa> getCaixas(){
        progress.titulo("Caixa");
        List<Caixa> l = new ArrayList<Caixa>() ;
        List<Caixa> list = DaoDbTabela.getCaixaADO(ctx).getList(new ArrayList<FilterTable>());
        List<RegExp> lregs = getRegs("EXP_CAIXA") ;
        Log.i("getCaixas", "caixa getList " +list.size()+" getRegs "+ lregs.size());
        int count = 0 ;
        for (Caixa c : list) {
            progress.progress(++count, list.size());
            if (!ifImportado(c.getId(),lregs)){
                Log.i("getCaixas", "id " +c.getId());
                l.add(c) ;
            }
        }
        return l;
    }

    public void setCaixas(List<Caixa> l){
        for (Caixa c : l){
            incluir("EXP_CAIXA", new RegExp(c.getId(), new Date()));
        }
    }

    public List<ContaPedido> getContas(){
        progress.titulo("Conta");
        List<ContaPedido> l = new ArrayList<ContaPedido>() ;
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("STATUS", "<>", "1")) ;
        List<ContaPedido> list = DaoDbTabela.getContaPedidoInternoDAO(ctx).getList(filters, "ID");
        List<RegExp> lregs = getRegs("EXP_CONTA") ;
        Log.i("getContas", "contas getList " +list.size()+" getRegs "+ lregs.size());
        int count = 0 ;
        for (ContaPedido it : list) {
            progress.progress(++count, list.size());
            if (!ifImportado(it.getId(),lregs)){
                Log.i("getContas", "id " +it.getId());
                l.add(it) ;
            }
        }
        return l;
    }

    public void setContas(List<ContaPedido> contas){
        List<RegExp> regsItem = getRegs("EXP_CONTA_ITEM") ;
        List<RegExp> regsPag = getRegs("EXP_CONTA_PAG") ;
        for (ContaPedido conta : contas){

          for (ContaPedidoItem item : conta.getListContaPedidoItem()) {
              if (!ifImportado(item.getId(),regsItem)) {
                  incluir("EXP_CONTA_ITEM", new RegExp(item.getId(), new Date()));
              }
          }
          for (ContaPedidoPagamento pg : conta.getListPagamento()) {
              if (!ifImportado(pg.getId(),regsPag)) {
                  incluir("EXP_CONTA_PAG", new RegExp(pg.getId(), new Date()));
              }
          }
          incluir("EXP_CONTA", new RegExp(conta.getId(), new Date()));
        }
    }

    public List<Transferencia> getTrasferencias(){
        progress.titulo("Transferências");
        List<Transferencia> l = new ArrayList<Transferencia>() ;
        List<Transferencia> list = DaoDbTabela.getTransferenciaDAO(ctx).getList(new ArrayList<FilterTable>());
        List<RegExp> lregs = getRegs("EXP_CONTA_TRANS") ;
        Log.i("getTrasnferencias", "Trans getList " +list.size()+" getRegs "+ lregs.size());
        int count = 0 ;
        for (Transferencia t : list) {
            progress.progress(++count, list.size());
            if (!ifImportado(t.getId(),lregs)){
                ContaPedido destino = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(t.getContaPedido_destino_id()) ;
                ContaPedido origem  = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(t.getContaPedido_origem_id()) ;
                if ((destino.getStatus() != 1) &&  (origem.getStatus() != 1)){
                    Log.i("getTransferencias", "id " +t.getId());
                    l.add(t) ;
                }
            }
        }
        return l;
    }

    public void setTransferencia(List<Transferencia> l){
        for (Transferencia t : l){
            incluir("EXP_CONTA_TRANS", new RegExp(t.getId(), new Date()));
        }
    }

    public List<ContaPedidoItemCancelamento> getContaCancel(){
        progress.titulo("Cancelamentos");
        List<ContaPedidoItemCancelamento> l = new ArrayList<ContaPedidoItemCancelamento>() ;
        List<ContaPedidoItemCancelamento> list = DaoDbTabela.getContaPedidoItemCancelamentoDAO(ctx).getList(new ArrayList<FilterTable>());
        List<RegExp> lregs = getRegs("EXP_CONTA_CANCEL") ;
        Log.i("getCancelamento", "Cancelamento " +list.size()+" getRegs "+ lregs.size());
        int count = 0 ;
        for (ContaPedidoItemCancelamento t : list) {
            progress.progress(++count, list.size());
            if (!ifImportado(t.getId(),lregs)) {
                if (DaoDbTabela.getContaPedidoInternoDAO(ctx).get(t.getContaPedidoItem_id()).getStatus() != 1)
                {
                    Log.i("getCancelamento", "id " + t.getId());
                    l.add(t);
                }
            }
        }
        return l;
    }

    public void setContaCancel(List<ContaPedidoItemCancelamento> l){
        for (ContaPedidoItemCancelamento t : l){
            incluir("EXP_CONTA_CANCEL", new RegExp(t.getId(), new Date()));
        }
    }

    private List<RegExp> getRegs(String tabela) {
        List<RegExp> l = new ArrayList<RegExp>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null ;
        Cursor res =  db.rawQuery( "SELECT ID, DATA FROM "+tabela+" ", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            try {
                    dt = (Date) df.parse(res.getString(res.getColumnIndexOrThrow("DATA")));
                    l.add(new RegExp(res.getInt(res.getColumnIndexOrThrow("ID")), dt)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            res.moveToNext();
        }
        return l;
    }

    private void incluir(String tabela, RegExp reg){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", reg.getId());
        contentValues.put("DATA", df.format(reg.getData()));
        db.insert(tabela, null, contentValues) ;
    }

    public void limpar() {
        excluir("EXP_CAIXA");
        excluir("EXP_CONTA");
        excluir("EXP_CONTA_ITEM");
        excluir("EXP_CONTA_PAG");
        excluir("EXP_CONTA_CANCEL");
        excluir("EXP_CONTA_TRANS");
    }

    private void excluir(String tabela){
        db.delete(tabela, null, null) ;
    }

    private Boolean ifImportado(int id, List<RegExp> l){
        Boolean flag = false ;
        for (RegExp reg : l){
            if (reg.getId() == id) flag = true ;
        }
        return flag ;
    }

    class RegExp {
        private int id ;
        private Date data ;

        public RegExp(int id, Date data) {
            this.id = id;
            this.data = data;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Date getData() {
            return data;
        }

        public void setData(Date data) {
            this.data = data;
        }
    }

}
