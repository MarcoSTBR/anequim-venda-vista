package com.anequimplus.relatorios;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RelatorioTransferencias implements IImpressaoRelatorio{

    private Context ctx;
    private Impressora impressora ;
    private Caixa caixa ;
    private List<RowImpressao> l ;

    public RelatorioTransferencias(Context ctx, Caixa caixa, Impressora impressora) {
        this.ctx = ctx;
        this.impressora = impressora;
        this.caixa = caixa;
    }

    @Override
    public List<RowImpressao> getRelatorio() {
        l = new ArrayList<RowImpressao>() ;
        setCabecalho("TRANSFERÊNCIAS", l) ;
        setTransferencias(l) ;
        return l ;
    }

    private void setTransferencias(List<RowImpressao> linhas) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<FilterTable> filters = new ArrayList<FilterTable>() ;

    //    filters.add(new FilterTable("1", "=", "1")) ;
        filters.add(new FilterTable("DATA", ">=", df.format(caixa.getData()))) ;
        List<Transferencia> l = Dao.getTransferenciaDAO(ctx).getList(filters);

        SimpleDateFormat pedDt = new SimpleDateFormat("dd/MM HH:mm");
        DecimalFormat qtd = new DecimalFormat("#0.###") ;
        DecimalFormat vlr = new DecimalFormat("R$ #0.00") ;
        String pedO = "" ; String dtaO = "" ;
        String pedD = "" ; String dtaD = "" ;
        String aux = "" ;

        for (Transferencia t : l) {
            pedO = repetir("0", 7- t.getContaPedido_origem().getPedido().length()) + t.getContaPedido_origem().getPedido() ;
            dtaO = pedDt.format(t.getContaPedido_origem().getData()) ;
            pedD = repetir("0", 7-t.getContaPedido_destino().getPedido().length())+t.getContaPedido_destino().getPedido() ;
            dtaD = pedDt.format(t.getContaPedido_destino().getData()) ;
            aux = pedO+" => "+pedD ;
            linhas.add(new RowImpressao(aux, TipoAlinhamento.ptCenter, 10)) ;
            aux = dtaO+" => "+dtaD ;
            linhas.add(new RowImpressao(aux, TipoAlinhamento.ptCenter, 10)) ;
            aux = qtd.format(t.getQuantidade())+" "+t.getContaPedidoItem().getProduto().getDescricao() ;
            linhas.add(new RowImpressao(aux, TipoAlinhamento.ptCenter, 10)) ;
            linhas.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        }
        linhas.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;

    }

    private void setCabecalho(String titulo, List<RowImpressao> l){
        SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss") ;
        l.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao(titulo, TipoAlinhamento.ptCenter,10)) ;
        l.add(new RowImpressao("CAIXA    : "+ UtilSet.getUsuarioNome(ctx), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao("ABERTURA : "+ dt.format(caixa.getData()), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
       // l.add(new RowImpressao("Conta    Data    Valor (R$)  PGTS (R$)" , TipoAlinhamento.ptCenter,10)) ;
    }

    private String repetir(String s, int ii){
        String aux = "" ;
        for (int i=0 ; i < ii ; i++ ){
            aux = aux + s ;
        }
        return aux ;
    }

}
