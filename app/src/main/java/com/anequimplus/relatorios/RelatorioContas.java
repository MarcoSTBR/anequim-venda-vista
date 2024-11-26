package com.anequimplus.relatorios;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Impressora;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelatorioContas {

    private Activity ctx;
    private Impressora impressora ;
    private Caixa caixa ;
    private List<RowImpressao> rel ;
    private ListenerRelatorio listenerRelatorio ;

    public RelatorioContas(Activity ctx, Caixa caixa, Impressora impressora, ListenerRelatorio listenerRelatorio) {
        this.ctx = ctx;
        this.impressora = impressora;
        this.caixa = caixa;
        this.listenerRelatorio = listenerRelatorio;
    }

    public void executar(){
        rel = new ArrayList<RowImpressao>() ;
        setCabecalho("CONTAS") ;
        getPedidos();
    }

    private void getPedidos(){
        Date ini = caixa.getData();
        Date fim = new Date();
        SimpleDateFormat dfF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<FilterTable> filterTables = new ArrayList<FilterTable>() ;
        String txt = '"'+dfF.format(ini)+  '"';
        filterTables.add(new FilterTable("DATA", ">=", txt)) ;
        new BuildContaPedido(ctx, filterTables, "DATA", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                setPedidos(l);
            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);


            }
        }).executar();

    }

    private void setPedidos(List<ContaPedido> list) {
       SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yy HH:mm");
       DecimalFormat dc = new DecimalFormat("#0.00") ;
       DecimalFormat dct = new DecimalFormat("R$ #0.00") ;
       Log.i("relatorio_contas", "list "+list.size()) ;

/*
       SimpleDateFormat dfF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       Date ini = caixa.getData();
       Date fim = new Date();
       List<FilterTable> filterTables = new ArrayList<FilterTable>() ;
       filterTables.add(new FilterTable("DATA", ">=", "'"+dfF.format(ini)+"'")) ;
*/

       String t = "" ;
       String vl = "" ;
       String pg = "" ;
       String d = "" ;
       String texto = "" ;
       String texto2 = "" ;
       Double vlr = 0.0 ;
       Double pgs = 0.0 ;
       Double comissao = 0.0 ;
       for (ContaPedido cp : list) {
          Log.i("relatorio_contas", "cp "+cp.getPedido()) ;
          t = repetir("0",7-cp.getPedido().length())+cp.getPedido();
          Log.i("relatorio_contas", "cp pedido "+t) ;
          d = dt.format(cp.getData());
          vl = dc.format(cp.getTotal() - cp.getDesconto());
          pg = dc.format(cp.getTotalPagamentos() - cp.getDesconto());
          texto  = t+" "+d ;
          texto2 = vl + repetir(" ",10-pg.length()) + pg ;
          texto = texto + repetir(" ",impressora.getTamColuna()-(texto.length() + texto2.length())) + texto2;
           rel.add(new RowImpressao(texto,TipoAlinhamento.ptLeft, 10)) ;
          vlr = vlr + cp.getTotal() - cp.getDesconto() ;
          pgs  =  pgs + cp.getTotalPagamentos() - cp.getDesconto() ;
          comissao = comissao + cp.getTotalComissao() ;
       }
        rel.add(new RowImpressao(repetir("=",impressora.getTamColuna()),TipoAlinhamento.ptLeft, 10)) ;

       texto = "VALOR TOTAL    : " ;
       vl = dct.format(vlr);
        rel.add(new RowImpressao(texto + repetir(" ",impressora.getTamColuna()-(texto.length()+vl.length())) + vl, TipoAlinhamento.ptLeft, 10)) ;
       texto = "VALOR PAGO     : " ;
       vl = dct.format(pgs);
        rel.add(new RowImpressao(texto + repetir(" ",impressora.getTamColuna()-(texto.length()+vl.length())) + vl, TipoAlinhamento.ptLeft, 10)) ;
       texto = "VALOR COMISSÃO : " ;
       vl = dct.format(comissao);
        rel.add(new RowImpressao(texto + repetir(" ",impressora.getTamColuna()-(texto.length()+vl.length())) + vl, TipoAlinhamento.ptLeft, 10)) ;
        listenerRelatorio.ok(rel);
    }

    private void setCabecalho(String titulo){
        SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss") ;
        rel.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        rel.add(new RowImpressao(titulo, TipoAlinhamento.ptCenter,10)) ;
        rel.add(new RowImpressao("CAIXA    : "+ UtilSet.getUsuarioNome(ctx), TipoAlinhamento.ptLeft,10)) ;
        rel.add(new RowImpressao("ABERTURA : "+ dt.format(caixa.getData()), TipoAlinhamento.ptLeft,10)) ;
        rel.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        rel.add(new RowImpressao("Conta    Data    Valor (R$)  PGTS (R$)" , TipoAlinhamento.ptCenter,10)) ;
    }

    private String repetir(String s, int ii){
        String aux = "" ;
        for (int i=0 ; i < ii ; i++ ){
            aux = aux + s ;
        }
        return aux ;
    }
}
