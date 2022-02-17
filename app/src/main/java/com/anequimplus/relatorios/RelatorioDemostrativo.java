package com.anequimplus.relatorios;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.ado.DBHelper;
import com.anequimplus.ado.Dao;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.tipos.TipoEstiloFont;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDemostrativo implements IImpressaoRelatorio {

    private Context ctx;
    private SQLiteDatabase db = null;
    private Impressora impressora ;
    private Caixa caixa ;
    private Double totalDinheiro = 0.0 ;
    private Double totalTroco = 0.0 ;
    private Double totalDesconto = 0.0 ;
    private List<RowImpressao> l ;
    //private final String DB_TABLE = "PRODUTO";

    public RelatorioDemostrativo(Context ctx, Caixa caixa, Impressora impressora) {
        this.ctx = ctx;
        this.impressora = impressora ;
        this.caixa = caixa ;
        db = DBHelper.getDB(ctx).getWritableDatabase();
    }

    @Override
    public List<RowImpressao> getRelatorio() {
        l = new ArrayList<RowImpressao>() ;
        setCabecalho("DEMOSTRATIVO", l) ;
        setModalidades() ;
        return l ;
    }

    private void setModalidades() {
        List<RegMod> listMod = new ArrayList<RegMod>() ;
        Cursor res = db.rawQuery("SELECT CAIXA_ID, MODALIDADE_ID, sum(VALOR) VALOR " +
                "FROM PEDIDO_PG_I WHERE CAIXA_ID = ? "+
                " GROUP BY CAIXA_ID, MODALIDADE_ID ", new String[]{String.valueOf(caixa.getId())});
        res.moveToFirst();
        while(res.isAfterLast()==false) {
            Modalidade m = Dao.getModalidadeADO(ctx).getModalidade(res.getInt(res.getColumnIndex("MODALIDADE_ID"))) ;
            switch (m.getTipoModalidade()){
                case pgTroco: totalTroco = res.getDouble(res.getColumnIndex("VALOR")) ;
                break;
                case pgDinheiro: totalDinheiro = res.getDouble(res.getColumnIndex("VALOR")) ;
                break;
                case pgDesconto: totalDesconto = res.getDouble(res.getColumnIndex("VALOR")) ;
                default:
                    listMod.add(new RegMod(m.getDescricao(), res.getDouble(res.getColumnIndex("VALOR"))));

            }
            res.moveToNext();
        }
        setLinhasMod(listMod) ;

    }

    private void setLinhasMod(List<RegMod> listMod) {
        DecimalFormat vlr = new DecimalFormat("R$ #0.00") ;
        String aux = "DINHEIRO" ;
        String v = vlr.format(totalDinheiro - totalTroco) ;
        Double total = totalDinheiro - totalTroco ;
        setLinha(aux, v) ;
        for (RegMod r : listMod){
            aux = r.getDescricao() ;
            v = vlr.format(r.getValor()) ;
            total = total + r.getValor() ;
            setLinha(aux, v) ;
        }
        aux = "TOTAL" ;
        v = vlr.format(total) ;
        setLinha(aux, v) ;

        aux = "DESCONTO" ;
        v = vlr.format(totalDesconto) ;
        setLinha(aux, v) ;
    }

    private void setLinha(String aux , String v){
        l.add(new RowImpressao(aux.trim() + repetir(" ",impressora.getTamColuna() - (aux.length() + v.length())) + v, TipoAlinhamento.ptLeft, 10, TipoEstiloFont.ptsNormal)) ;
    }

    private class RegMod {
        private String descricao ;
        private Double valor ;

        public RegMod(String descricao, Double valor) {
            this.descricao = descricao;
            this.valor = valor;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public Double getValor() {
            return valor;
        }

        public void setValor(Double valor) {
            this.valor = valor;
        }
    }

    private void setCabecalho(String titulo, List<RowImpressao> l){
       SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss") ;
       l.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
       l.add(new RowImpressao(titulo, TipoAlinhamento.ptCenter,10)) ;
       l.add(new RowImpressao("CAIXA    : "+ UtilSet.getUsuarioNome(ctx), TipoAlinhamento.ptLeft,10)) ;
       l.add(new RowImpressao("ABERTURA : "+ dt.format(caixa.getData()), TipoAlinhamento.ptLeft,10)) ;
       l.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
       l.add(new RowImpressao("Modalidade       Valor", TipoAlinhamento.ptCenter,10)) ;
    }

    private String repetir(String s, int ii){
        String aux = "" ;
        for (int i=0 ; i < ii ; i++ ){
            aux = aux + s ;
        }
        return aux ;
    }
}
