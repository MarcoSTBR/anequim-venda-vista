package com.anequimplus.relatorios;

import android.content.Context;

import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildContaPedidoItem;
import com.anequimplus.builds.BuildContaPedidoItemCancelamento;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerContaPedidoItem;
import com.anequimplus.listeners.ListenerItemCancelamento;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Impressora;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelatorioCancelamentos  {


    private Context ctx;
    private Impressora impressora ;
    private Caixa caixa ;
    private List<RowImpressao> lRow ;
    private ListenerRelatorio listenerRelatorio ;
    private List<ContaPedidoItemCancelamento> listCancelamento ;
    private List<ContaPedidoItem> listContaPedidoItens ;
    private List<ContaPedido> listContaPedidos ;

    public RelatorioCancelamentos(Context ctx,  Caixa caixa, Impressora impressora, ListenerRelatorio listenerRelatorio) {
        this.ctx = ctx;
        this.impressora = impressora;
        this.caixa = caixa;
        this.listenerRelatorio = listenerRelatorio ;
    }


    public void executar(){
        lRow = new ArrayList<RowImpressao>() ;
        setCabecalho("CANCELAMENTOS") ;
        //setCancelamentos() ;
        getListCancelamentos() ;
    }


    private void getListCancelamentos(){
        Date ini = caixa.getData();
        Date fim = new Date();
        SimpleDateFormat dfF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FilterTables filterTables = new FilterTables();
//        String txt = '"' +dfF.format(ini)+'"'+" AND "+'"'+ dfF.format(fim)+ '"';
       // String txt = '"' + dfF.format(ini) + '"';
        filterTables.add(new FilterTable("DATA", ">=",  dfF.format(ini)));
        new BuildContaPedidoItemCancelamento(ctx, filterTables, "", new ListenerItemCancelamento() {
            @Override
            public void ok(List<ContaPedidoItemCancelamento> l) {
                if (l.size()>0) {
                    listCancelamento = l;
                    getPedidoItens();
                } else listenerRelatorio.erroMensagem("Nenhum cancelamento encontrado!");

            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);
            }
        }).executar();
    }

    private void getPedidoItens() {
        String in = "" ;
        for (ContaPedidoItemCancelamento t : listCancelamento){
            if (in.length() == 0){
                in = " ("+t.getContaPedidoItem_id() ;
            } else {
                in = in + ","+t.getContaPedidoItem_id();
            }
        }
        in = in + ")" ;
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("ID", "IN", in));
        new BuildContaPedidoItem(ctx, filters, "", new ListenerContaPedidoItem() {
            @Override
            public void ok(List<ContaPedidoItem> l) {
                listContaPedidoItens = l ;
                setPedidos() ;
            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);

            }
        }).executar();
    }

    private void setPedidos() {
        String in = "" ;
        for (ContaPedidoItem t : listContaPedidoItens){
            if (in.length() == 0){
                in = " ("+t.getContaPedido_id() ;
            } else {
                in = in + ","+t.getContaPedido_id();
            }
        }
        in = in + ")" ;
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("ID", "IN", in));
        new BuildContaPedido(ctx, filters.getList(), "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                listContaPedidos = l ;
                setCancelamentos();
            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);

            }
        }).executar();

    }


    private void setCancelamentos() {
       // List<FilterTable> filterTables = new ArrayList<FilterTable>() ;
       // filterTables.add(new FilterTable("CAIXA_ID", "=", String.valueOf(caixa.getId()))) ;
       // List<ContaPedidoItemCancelamento> l = DaoDbTabela.getContaPedidoItemCancelamentoDAO(ctx).getList(filterTables);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        SimpleDateFormat pedDt = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        DecimalFormat qtd = new DecimalFormat("#0.###") ;
        DecimalFormat vlr = new DecimalFormat("R$ #0.00") ;
        String ped = "" ;
        String pedD = "" ;
        String dt = "" ;
        String aux = "" ;
        for (ContaPedidoItemCancelamento cp : listCancelamento){
            ContaPedido contaPedido = getContaPedido(cp.getContaPedidoItem_id()) ;//DaoDbTabela.getContaPedidoInternoDAO(ctx).get(cp.getContaPedidoItem_id()) ;
            if (contaPedido == null) {
                String uuid = "" ;
                String pp = "N-" + cp.getContaPedidoItem_id() ;
                contaPedido = new ContaPedido(uuid, pp, new Date(), 0 ) ;
                contaPedido.setListContaPedidoItem(new ArrayList<ContaPedidoItem>());
            }
            ped = repetir("0", 7 - contaPedido.getPedido().length()) + contaPedido.getPedido() ;
            pedD = df.format(contaPedido.getData()) ;
            dt = df.format(cp.getData()) ;
            aux = ped+" "+pedD+" "+dt ;
            lRow.add(new RowImpressao(aux, TipoAlinhamento.ptLeft,  10)) ;
            aux = qtd.format(cp.getQuantidade())+" "+ getContaPedidoItem(cp.getContaPedidoItem_id()).getProduto().getDescricao() ;// DaoDbTabela.getContaPedidoItemInternoDAO(ctx).get(cp.getContaPedidoItem_id()).getProduto().getDescricao() ;
            lRow.add(new RowImpressao(aux, TipoAlinhamento.ptLeft,  10)) ;
            lRow.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        }
        lRow.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        listenerRelatorio.ok(lRow);
    }

    private void setCabecalho(String titulo) {
        SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss") ;
        lRow.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao(titulo, TipoAlinhamento.ptCenter,10)) ;
        lRow.add(new RowImpressao("CAIXA    : "+ UtilSet.getUsuarioNome(ctx), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao("ABERTURA : "+ dt.format(caixa.getData()), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
    }

    private String repetir(String s, int ii){
        String aux = "" ;
        for (int i=0 ; i < ii ; i++ ){
            aux = aux + s ;
        }
        return aux ;
    }

    private ContaPedido getContaPedido(int item_id){
        ContaPedido c = null ;
        for (ContaPedido cp : listContaPedidos){
            for (ContaPedidoItem i : cp.getListContaPedidoItem()) {
                if (i.getId() == item_id) c = cp ;
            //    if (i.getId() == id) it = i;
            }
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
