package com.anequimplus.relatorios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.builds.BuildContaPedidoPagamento;
import com.anequimplus.builds.BuildSangria;
import com.anequimplus.builds.BuildSuprimento;
import com.anequimplus.listeners.ListenerContaPedidoPagamento;
import com.anequimplus.listeners.ListenerSangria;
import com.anequimplus.listeners.ListenerSuprimento;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.Sangria;
import com.anequimplus.entity.Suprimento;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.tipos.TipoEstiloFont;
import com.anequimplus.tipos.TipoModalidade;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDemostrativo {

    private Context ctx;
    private Impressora impressora ;
    private Caixa caixa ;
    private Double totalDinheiro = 0.0 ;
    private Double totalTroco = 0.0 ;
    private Double totalDesconto = 0.0 ;
    private List<RowImpressao> lRow ;
    private List<Suprimento> lSuprimento ;
    private List<Sangria> lSangria ;
    private ListenerRelatorio listenerRelatorio ;
    private SQLiteDatabase db = null;
    private List<RegMod> listMod ;

    public RelatorioDemostrativo(Context ctx, Caixa caixa, Impressora impressora, ListenerRelatorio listenerRelatorio) {
        this.ctx = ctx;
        this.impressora = impressora ;
        this.caixa = caixa ;
        this.listenerRelatorio = listenerRelatorio ;
      //  db = DBHelper.getDB(ctx).getWritableDatabase();
    }

/*


    @Override
    public List<RowImpressao> getRelatorio() {
        lSuprimento  = DaoDbTabela.getSuprimentoADO(ctx).getList(caixa) ;
        lSangria     = DaoDbTabela.getSangriaADO(ctx).getList(caixa) ;
        l = new ArrayList<RowImpressao>() ;
        setCabecalho("DEMOSTRATIVO", l) ;
        setModalidades() ;
        return l ;
    }
*/

    public void executar(){
        lSuprimento  = DaoDbTabela.getSuprimentoADO(ctx).getList(caixa) ;
        lSangria     = DaoDbTabela.getSangriaADO(ctx).getList(caixa) ;
        lRow = new ArrayList<RowImpressao>() ;
        setCabecalho("DEMOSTRATIVO") ;
        // getModalidade() ;
        // setModalidades() ;
        getListSuprimento() ;
    }

    private void getListSuprimento(){
        String order = "MODALIDADE_ID" ;
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("CAIXA_ID", "=", String.valueOf(caixa.getId())));
        filters.add(new FilterTable("STATUS", "=", "1"));
        new BuildSuprimento(ctx, filters, order, new ListenerSuprimento() {
            @Override
            public void ok(List<Suprimento> l) {
                lSuprimento = l ;
                getListSangria() ;
            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);

            }
        }).executar();
    }

    private void getListSangria(){
        String order = "MODALIDADE_ID" ;
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("CAIXA_ID", "=", String.valueOf(caixa.getId())));
        filters.add(new FilterTable("STATUS", "=", "1"));
        new BuildSangria(ctx, filters, order, new ListenerSangria() {
            @Override
            public void ok(List<Sangria> l) {
                lSangria = l ;
                getModalidade() ;
            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);

            }
        }).executar();
    }


    private Double getSuprimento() {
        Double v = 0.0 ;
        for (Suprimento it : lSuprimento){
                v = v + it.getValor() ;
        }
        return v ;
    }

    private Double getSuprimento(int modalidade_id) {
        Double v = 0.0 ;
        for (Suprimento it : lSuprimento){
            if (it.getModalidade_id() == modalidade_id)
                v = v + it.getValor() ;
        }
        return v ;
    }

    private Double getSangria() {
        Double v = 0.0 ;
        for (Sangria it : lSangria){
                v = v + it.getValor() ;
        }
        return v ;
    }

    private Double getSangria(int modalidade_id) {
        Double v = 0.0 ;
        for (Sangria it : lSangria){
            if (it.getModalidade_id() == modalidade_id)
                v = v + it.getValor() ;
        }
        return v ;
    }

    private void getModalidade(){
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("CAIXA_ID", "=", String.valueOf(caixa.getId())));
        filters.add(new FilterTable("STATUS", "=", "1"));
        new BuildContaPedidoPagamento(ctx, filters, "MODALIDADE_ID", new ListenerContaPedidoPagamento() {
            @Override
            public void ok(List<ContaPedidoPagamento> l) {
                setlistMod(l);
            }

            @Override
            public void erro(String msg) {
                listenerRelatorio.erroMensagem(msg);
            }
        }).executar();
    }

    private void setlistMod(List<ContaPedidoPagamento> l){
        List<ContaPedidoPagamento> grupos = new ArrayList<ContaPedidoPagamento>() ;
        ContaPedidoPagamento obj = null ;
        for (ContaPedidoPagamento cp : l){
            obj = getObjList(cp.getModalidade(), grupos) ;
            if (obj == null){
                grupos.add(new ContaPedidoPagamento(cp.getId(), cp.getContaPedido_id(), cp.getUuid(),
                        cp.getData(), cp.getCaixa_id(), cp.getModalidade(), cp.getValor(), cp.getStatus(), UtilSet.getTerminalId(ctx))) ;
            } else {
                Double v = cp.getValor() + obj.getValor() ;
                obj.setValor(v);
            }
        }
        setPagamentos(grupos) ;
    }

    private ContaPedidoPagamento getObjList(Modalidade m, List<ContaPedidoPagamento> grupos) {
        ContaPedidoPagamento r = null ;
        for (ContaPedidoPagamento cp : grupos){
            if (cp.getModalidade().getId() == m.getId()){
                r = cp ;
            }
        }
        return r ;
    }

    private void setPagamentos(List<ContaPedidoPagamento> l){
        totalDinheiro = 0.0 ;
        totalTroco    = 0.0 ;
        totalDesconto = 0.0 ;
        Modalidade modDinheiro = null ;
        List<RegMod> listMod = new ArrayList<RegMod>() ;
        for (ContaPedidoPagamento c : l) {
            switch (c.getModalidade().getTipoModalidade()) {
                case pgTroco:
                    totalTroco = totalTroco + c.getValor() ;//res.getDouble(res.getColumnIndexOrThrow("VALOR"));
                    break;
                case pgDinheiro:
                    totalDinheiro = totalDinheiro + c.getValor();//res.getDouble(res.getColumnIndexOrThrow("VALOR"));
                    modDinheiro = c.getModalidade();
                    break;
                case pgDesconto:
                    totalDesconto = totalDesconto + c.getValor(); //res.getDouble(res.getColumnIndexOrThrow("VALOR"));
                default:
                    if (c.getModalidade().getTipoModalidade() != TipoModalidade.pgDesconto)
                        listMod.add(new RegMod(c.getModalidade(), c.getModalidade().getDescricao(), c.getValor()));
            }
        }
        if (modDinheiro != null)
            totalDinheiro = totalDinheiro - getSangria(modDinheiro.getId()) + + getSuprimento(modDinheiro.getId());
        setSuprimentoSangria(listMod) ;
        setLinhasMod(listMod) ;
        listenerRelatorio.ok(lRow);
    }

/*

    private void setModalidades() {
        totalDinheiro = 0.0 ;
        totalTroco    = 0.0 ;
        totalDesconto = 0.0 ;
        List<RegMod> listMod = new ArrayList<RegMod>() ;
        Cursor res = db.rawQuery("SELECT CAIXA_ID, MODALIDADE_ID, sum(VALOR) VALOR " +
                "FROM PEDIDO_PG_I WHERE CAIXA_ID = ? "+
                " GROUP BY CAIXA_ID, MODALIDADE_ID ", new String[]{String.valueOf(caixa.getId())});
        res.moveToFirst();
        Modalidade modDinheiro = null ;
        while(res.isAfterLast()==false) {
            Modalidade m = DaoDbTabela.getModalidadeADO(ctx).getModalidade(res.getInt(res.getColumnIndexOrThrow("MODALIDADE_ID"))) ;
            switch (m.getTipoModalidade()){
                case pgTroco: totalTroco = totalTroco + res.getDouble(res.getColumnIndexOrThrow("VALOR")) ;
                break;
                case pgDinheiro: totalDinheiro = totalDinheiro + res.getDouble(res.getColumnIndexOrThrow("VALOR")) ;
                    modDinheiro = m ;
                break;
                case pgDesconto: totalDesconto = totalDesconto + res.getDouble(res.getColumnIndexOrThrow("VALOR")) ;
                default:
                    if (m.getTipoModalidade() != TipoModalidade.pgDesconto)
                    listMod.add(new RegMod(m, m.getDescricao(), res.getDouble(res.getColumnIndexOrThrow("VALOR"))));

            }
            res.moveToNext();
        }
        if (modDinheiro != null)
            totalDinheiro = totalDinheiro - getSangria(modDinheiro.getId()) + + getSuprimento(modDinheiro.getId());
        setSuprimentoSangria(listMod) ;
        setLinhasMod(listMod) ;

    }
*/

    private void setSuprimentoSangria(List<RegMod> listMod){
        for (RegMod rg : listMod){
            Double v = rg.getValor();
            v = v - getSangria(rg.modalidade.getId()) + getSuprimento(rg.modalidade.getId());
            rg.setValor(v);
        }
    }

    private void setLinhasMod(List<RegMod> listMod) {
        DecimalFormat vlr = new DecimalFormat("R$ #0.00") ;

        String Faux = "FUNDO DE TROCO" ;
        String Fv = "("+vlr.format(caixa.getValor())+")" ;
        setLinha(Faux, Fv) ;


        String aux = "DINHEIRO" ;
        String v = vlr.format(totalDinheiro - totalTroco + caixa.getValor() );
        Double total = totalDinheiro - totalTroco + caixa.getValor() ;
        setLinha(aux, v) ;
        for (RegMod r : listMod){
            aux = r.getDescricao() ;
            v = vlr.format(r.getValor()) ;
            total = total + r.getValor() ;
            setLinha(aux, v) ;
        }

        aux = "DESCONTO" ;
        v = "("+vlr.format(totalDesconto)+")" ;
        setLinha(aux, v) ;

        aux = "TOTAL" ;
        v = vlr.format(total) ;
        setLinha(aux, v) ;

        aux = "TOTAL DE VENDAS" ;
        v = vlr.format(total - caixa.getValor()) ;
        setLinha(aux, v) ;

        aux = "TOTAL DE SUPRIMENTO" ;
        v = vlr.format(getSuprimento()) ;
        setLinha(aux, "("+v+")") ;

        aux = "TOTAL DE SANGRIA" ;
        v = vlr.format(getSangria()) ;
        setLinha(aux, "("+v+")") ;

    }

    private void setLinha(String aux , String v){
        lRow.add(new RowImpressao(aux.trim() + repetir(".",impressora.getTamColuna() - (aux.length() + v.length())) + v, TipoAlinhamento.ptLeft, 10, TipoEstiloFont.ptsNormal)) ;
    }

    private class RegMod {
        private Modalidade modalidade ;
        private String descricao ;
        private Double valor ;

        public RegMod(Modalidade modalidade ,String descricao, Double valor) {
            this.modalidade = modalidade ;
            this.descricao = descricao;
            this.valor = valor;
        }

        public Modalidade getModalidade() {
            return modalidade;
        }

        public void setModalidade(Modalidade modalidade) {
            this.modalidade = modalidade;
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

    private void setCabecalho(String titulo){
       SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss") ;
        lRow.add(new RowImpressao(repetir("=", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao(titulo, TipoAlinhamento.ptCenter,10)) ;
        lRow.add(new RowImpressao("CAIXA    : "+ UtilSet.getUsuarioNome(ctx), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao("ABERTURA : "+ dt.format(caixa.getData()), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao(repetir("-", impressora.getTamColuna()), TipoAlinhamento.ptLeft,10)) ;
        lRow.add(new RowImpressao("Modalidade       Valor", TipoAlinhamento.ptCenter,10)) ;
    }

    private String repetir(String s, int ii){
        String aux = "" ;
        for (int i=0 ; i < ii ; i++ ){
            aux = aux + s ;
        }
        return aux ;
    }
}
