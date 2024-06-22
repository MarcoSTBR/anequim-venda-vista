package com.anequimplus.relatorios;

import android.content.Context;

import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildContaPedidoTransferencia;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerTransferencia;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelatorioTransferencias  {

    private Context ctx;
    private Impressora impressora ;
    private Caixa caixa ;
    private List<RowImpressao> lRow ;
    private ListenerRelatorio listenerRelatorio ;
    private List<Transferencia> trans ;
    private List<ContaPedido> listContaPedidos ;

    public RelatorioTransferencias(Context ctx, Caixa caixa, Impressora impressora, ListenerRelatorio listenerRelatorio) {
        this.ctx = ctx;
        this.impressora = impressora;
        this.caixa = caixa;
        this.listenerRelatorio = listenerRelatorio ;
    }

    public void executar(){
        lRow = new ArrayList<RowImpressao>() ;
        setCabecalho("TRANSFERÊNCIAS") ;
        //setTransferencias(l) ;
        getListTransferencias();
    }


    private void getListTransferencias(){
        Date ini = caixa.getData();
        Date fim = new Date();
        SimpleDateFormat dfF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FilterTables filterTables = new FilterTables();
        filterTables.add(new FilterTable("DATA", ">=", dfF.format(ini) ));
        new BuildContaPedidoTransferencia(ctx, filterTables, "", new ListenerTransferencia() {
            @Override
            public void ok(List<Transferencia> t) {
                if (t.size()>0) {
                    trans = t;
                    getPedidosTrans();
                } else listenerRelatorio.erroMensagem("Nenhuma Transferencia Encontrada!");
              //  setTransferencias(t) ;
            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);
            }
        }).executar();
    }

    private void getPedidosTrans() {
        String in = "" ;
        for (Transferencia t : trans){
            if (in.length() == 0){
                in = " ("+t.getContaPedido_destino_id()+","+t.getContaPedido_origem_id() ;
            } else {
                in = in + ","+t.getContaPedido_destino_id()+","+t.getContaPedido_origem_id();
            }
        }
        in = in + ")" ;
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("ID", "IN", in));
        new BuildContaPedido(ctx, filters.getList(), "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                listContaPedidos = l ;
                setTransferencias() ;
            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);

            }
        }).executar();

    }


    private void setTransferencias() {
        List<FilterTable> filterTables = new ArrayList<FilterTable>() ;
        filterTables.add(new FilterTable("CAIXA_ID", "=", String.valueOf(caixa.getId()))) ;
        //List<Transferencia> l = DaoDbTabela.getTransferenciaDAO(ctx).getList(filterTables);

        SimpleDateFormat pedDt = new SimpleDateFormat("dd/MM HH:mm");
        DecimalFormat qtd = new DecimalFormat("#0.###") ;
        DecimalFormat vlr = new DecimalFormat("R$ #0.00") ;
        String pedO = "" ; String dtaO = "" ;
        String pedD = "" ; String dtaD = "" ;
        String aux = "" ;

        for (Transferencia t : trans) {

            ContaPedido origem    = getContaPedido(t.getContaPedido_origem_id()) ;//DaoDbTabela.getContaPedidoInternoDAO(ctx).get(t.getContaPedido_origem_id());
            ContaPedido destino   = getContaPedido(t.getContaPedido_destino_id()) ; //DaoDbTabela.getContaPedidoInternoDAO(ctx).get(t.getContaPedido_destino_id());
            ContaPedidoItem  item = getContaPedidoItem(t.getContaPedidoItem_id()) ;// DaoDbTabela.getContaPedidoItemInternoDAO(ctx).get(t.getContaPedidoItem_id());

            pedO = repetir("0", 7- origem.getPedido().length()) + origem.getPedido() ;
            dtaO = pedDt.format(origem.getData()) ;
            pedD = repetir("0", 7- destino.getPedido().length())+destino.getPedido() ;
            dtaD = pedDt.format(destino.getData()) ;
            aux = pedO+" => "+pedD ;
            lRow.add(new RowImpressao(aux, TipoAlinhamento.ptCenter, 10)) ;
            aux = dtaO+" => "+dtaD ;
            lRow.add(new RowImpressao(aux, TipoAlinhamento.ptCenter, 10)) ;
            aux = qtd.format(t.getQuantidade())+" "+item.getProduto().getDescricao() ;
            lRow.add(new RowImpressao(aux, TipoAlinhamento.ptCenter, 10)) ;
            lRow.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        }
        lRow.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        listenerRelatorio.ok(lRow);

    }

    private void setCabecalho(String titulo){
        SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss") ;
        lRow.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao(titulo, TipoAlinhamento.ptCenter,10)) ;
        lRow.add(new RowImpressao("CAIXA    : "+ UtilSet.getUsuarioNome(ctx), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao("ABERTURA : "+ dt.format(caixa.getData()), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
       // l.add(new RowImpressao("Conta    Data    Valor (R$)  PGTS (R$)" , TipoAlinhamento.ptCenter,10)) ;
    }

    private String repetir(String s, int ii){
        String aux = "" ;
        for (int i=0 ; i < ii ; i++ ){
            aux = aux + s ;
        }
        return aux ;
    }

    private ContaPedido getContaPedido(int id){
        ContaPedido c = null ;
        for (ContaPedido cp : listContaPedidos){
            if (cp.getId() == id) c = cp ;
        }
        return c ;
    }

    private ContaPedidoItem getContaPedidoItem(int id){
        ContaPedidoItem it = null ;
        for (ContaPedido cp : listContaPedidos){
            for (ContaPedidoItem i : cp.getListContaPedidoItem()) {
                if (i.getId() == id) it = i;
            }
        }
        return it ;
    }

}
