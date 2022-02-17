package com.anequimplus.relatorios;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.Impressora;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RelatorioCancelamentos implements IImpressaoRelatorio{


    private Context ctx;
    private Impressora impressora ;
    private Caixa caixa ;
    private List<RowImpressao> l ;

    public RelatorioCancelamentos(Context ctx,  Caixa caixa, Impressora impressora) {
        this.ctx = ctx;
        this.impressora = impressora;
        this.caixa = caixa;
    }

    @Override
    public List<RowImpressao> getRelatorio() {
        l = new ArrayList<RowImpressao>() ;
        setCabecalho("CANCELAMENTOS", l) ;
        setCancelamentos(l) ;
        return l ;
    }

    private void setCancelamentos(List<RowImpressao> linhas) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        filters.add(new FilterTable("DATA", ">=", df.format(caixa.getData()))) ;
        List<ContaPedidoItemCancelamento> l = Dao.getContaPedidoItemCancelamentoDAO(ctx).getList(filters);

        SimpleDateFormat pedDt = new SimpleDateFormat("dd/MM HH:mm");
        DecimalFormat qtd = new DecimalFormat("#0.###") ;
        DecimalFormat vlr = new DecimalFormat("R$ #0.00") ;
        String ped = "" ;
        String pedD = "" ;
        String dt = "" ;
        String aux = "" ;
        for (ContaPedidoItemCancelamento cp : l){
            ContaPedido contaPedido = Dao.getContaPedidoInternoDAO(ctx).get(cp.getContaPedidoItem().getContaPedido_id()) ;
            ped = repetir("0", 7 - contaPedido.getPedido().length()) + contaPedido.getPedido() ;
            pedD = df.format(contaPedido.getData()) ;
            dt = df.format(cp.getData()) ;
            aux = ped+" "+pedD+" "+dt ;
            linhas.add(new RowImpressao(aux, TipoAlinhamento.ptLeft,  10)) ;
            aux = qtd.format(cp.getQuantidade())+" "+ cp.getContaPedidoItem().getProduto().getDescricao() ;
            linhas.add(new RowImpressao(aux, TipoAlinhamento.ptLeft,  10)) ;
            linhas.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;


        }
        linhas.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
    }

    private void setCabecalho(String titulo, List<RowImpressao> l) {
        SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss") ;
        l.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao(titulo, TipoAlinhamento.ptCenter,10)) ;
        l.add(new RowImpressao("CAIXA    : "+ UtilSet.getUsuarioNome(ctx), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao("ABERTURA : "+ dt.format(caixa.getData()), TipoAlinhamento.ptLeft,10)) ;
        l.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
    }

    private String repetir(String s, int ii){
        String aux = "" ;
        for (int i=0 ; i < ii ; i++ ){
            aux = aux + s ;
        }
        return aux ;
    }

}
