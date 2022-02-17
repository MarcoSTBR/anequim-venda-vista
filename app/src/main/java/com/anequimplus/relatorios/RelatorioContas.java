package com.anequimplus.relatorios;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.Impressora;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RelatorioContas implements IImpressaoRelatorio {

    private Context ctx;
    private Impressora impressora ;
    private Caixa caixa ;
    private List<RowImpressao> l ;

    public RelatorioContas(Context ctx, Caixa caixa, Impressora impressora) {
        this.ctx = ctx;
        this.impressora = impressora;
        this.caixa = caixa;
    }

    @Override
    public List<RowImpressao> getRelatorio() {
        l = new ArrayList<RowImpressao>() ;
        setCabecalho("CONTAS", l) ;
        setPedidos() ;
        return l ;
    }

    private void setPedidos() {
       SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yy HH:mm");
       DecimalFormat dc = new DecimalFormat("#0.00") ;
       DecimalFormat dct = new DecimalFormat("R$ #0.00") ;
       List<ContaPedido> list = Dao.getContaPedidoInternoDAO(ctx).getList() ;
       String t = "" ;
       String vl = "" ;
       String pg = "" ;
       String d = "" ;
       String texto = "" ;
       String texto2 = "" ;
       Double vlr = 0.0 ;
       Double pgs = 0.0 ;
       for (ContaPedido cp : list) {
          t = repetir("0",7-cp.getPedido().length())+cp.getPedido();
          d = dt.format(cp.getData());
          vl = dc.format(cp.getTotal());
          pg = dc.format(cp.getTotalPagamentos());
          texto  = t+" "+d ;
          texto2 = vl + repetir(" ",10-pg.length()) + pg ;
          texto = texto + repetir(" ",impressora.getTamColuna()-(texto.length() + texto2.length())) + texto2;
          l.add(new RowImpressao(texto,TipoAlinhamento.ptLeft, 10)) ;
          vlr = vlr + cp.getTotal() ;
          pgs  =  pgs + cp.getTotalPagamentos() ;
       }
       l.add(new RowImpressao(repetir("=",impressora.getTamColuna()),TipoAlinhamento.ptLeft, 10)) ;

       texto = "VALOR TOTAL: " ;
       vl = dct.format(vlr);
       l.add(new RowImpressao(texto + repetir(" ",impressora.getTamColuna()-(texto.length()+vl.length())) + vl, TipoAlinhamento.ptLeft, 10)) ;
       texto = "VALOR PAGO : " ;
       vl = dct.format(pgs);
       l.add(new RowImpressao(texto + repetir(" ",impressora.getTamColuna()-(texto.length()+vl.length())) + vl, TipoAlinhamento.ptLeft, 10)) ;
    }

    private void setCabecalho(String titulo, List<RowImpressao> l){
        SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss") ;
        l.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao(titulo, TipoAlinhamento.ptCenter,10)) ;
        l.add(new RowImpressao("CAIXA    : "+ UtilSet.getUsuarioNome(ctx), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao("ABERTURA : "+ dt.format(caixa.getData()), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao("Conta    Data    Valor (R$)  PGTS (R$)" , TipoAlinhamento.ptCenter,10)) ;
    }

    private String repetir(String s, int ii){
        String aux = "" ;
        for (int i=0 ; i < ii ; i++ ){
            aux = aux + s ;
        }
        return aux ;
    }
}
