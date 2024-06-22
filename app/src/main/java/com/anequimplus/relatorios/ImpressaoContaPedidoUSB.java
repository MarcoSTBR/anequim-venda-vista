package com.anequimplus.relatorios;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.impressao.ContaPedidoUSB;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImpressaoContaPedidoUSB implements IImpressaoContaPedido {

    private ContaPedido contaPedido ;
    private int tamColuna  ;

    public ImpressaoContaPedidoUSB(int tamColuna) {
        this.tamColuna = tamColuna;
    }

    @Override
    public void setContaPedido(ContaPedido contaPedido) {
        this.contaPedido = contaPedido;
    }

    @Override
    public List<ContaPedidoUSB.LinhaImpressao> getListLinhas() {

        List<ContaPedidoUSB.LinhaImpressao> list = new ArrayList<ContaPedidoUSB.LinhaImpressao>() ;
        addCabecalho(list) ;
        addItens(list) ;
        addPagamento(list) ;
        return list;
    }

    private void addCabecalho(List<ContaPedidoUSB.LinhaImpressao> list){
        // yyyy-MM-dd HH:mm:ss
        String separador = "-" ;
        String vias = "Via "+contaPedido.getNum_impressao();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
        list.add(new ContaPedidoUSB.LinhaImpressao("CONTA : "+contaPedido.getPedido(), ContaPedidoUSB.EstiloM8_M10.Reverso, ContaPedidoUSB.TamanhoM8_M10.A3, ContaPedidoUSB.AlinhamentoM8_M10.Center)) ;
        list.add(new ContaPedidoUSB.LinhaImpressao("ABERTURA : "+dt.format(contaPedido.getData()),ContaPedidoUSB.EstiloM8_M10.FonteB, ContaPedidoUSB.TamanhoM8_M10.A2, ContaPedidoUSB.AlinhamentoM8_M10.Left));
        list.add(new ContaPedidoUSB.LinhaImpressao(repetir(separador, tamColuna - vias.length())+vias, ContaPedidoUSB.EstiloM8_M10.FonteA, ContaPedidoUSB.TamanhoM8_M10.A1, ContaPedidoUSB.AlinhamentoM8_M10.Center));

    }

    private void addItens(List<ContaPedidoUSB.LinhaImpressao> list) {
        DecimalFormat frm = new DecimalFormat("R$ #0.00");
        DecimalFormat qrm = new DecimalFormat("#0.###");
        for (ContaPedidoItem it : contaPedido.getListContaPedidoItemAtivosAgrupados()) {
            String txt = it.getProduto().getDescricao().trim();
            String qv  = qrm.format(it.getQuantidade()).trim() + " X " + frm.format(it.getPreco()).trim() + " = " + frm.format(it.getValor()).trim();
            int tqv  = qv.length() ;
            int ttxt = txt.length() ;
            int tamT   = ttxt + tqv ;
           /* Log.i("additens", "txt  "+txt.length()) ;
            Log.i("additens", "qv   "+qv.length()) ;
            Log.i("additens", "txt + qv   "+tamT) ;
            Log.i("additens", "tamcoluna "+tamColuna+" "+tamT) ;*/
            if (tamColuna > tamT){
                txt = txt.trim() + repetir(" ",tamColuna - tamT) + qv.trim() ;
                list.add(new ContaPedidoUSB.LinhaImpressao(txt, ContaPedidoUSB.EstiloM8_M10.FonteA , ContaPedidoUSB.TamanhoM8_M10.A1, ContaPedidoUSB.AlinhamentoM8_M10.Left));
            } else {
                list.add(new ContaPedidoUSB.LinhaImpressao(txt.substring(1, tamColuna), ContaPedidoUSB.EstiloM8_M10.FonteA, ContaPedidoUSB.TamanhoM8_M10.A1, ContaPedidoUSB.AlinhamentoM8_M10.Left));
                list.add(new ContaPedidoUSB.LinhaImpressao(repetir(" ", tamColuna - qv.length())+ qv, ContaPedidoUSB.EstiloM8_M10.FonteA, ContaPedidoUSB.TamanhoM8_M10.A1, ContaPedidoUSB.AlinhamentoM8_M10.Left));
            }
        }
    }

    private String repetir(String s, int count){
        String r = "" ;
        for (int i = 0; i < count; i++) {
            r = r + s ;
        }
        return r ;
    }

    @Override
    public List<ContaPedidoUSB.LinhaImpressao> getRecibo() {
        List<ContaPedidoUSB.LinhaImpressao> list = new ArrayList<ContaPedidoUSB.LinhaImpressao>() ;
        String separador = "-" ;
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
        list.add(new ContaPedidoUSB.LinhaImpressao("RECIBO DA CONTA : "+contaPedido.getPedido(), ContaPedidoUSB.EstiloM8_M10.Reverso, ContaPedidoUSB.TamanhoM8_M10.A3, ContaPedidoUSB.AlinhamentoM8_M10.Center)) ;
        list.add(new ContaPedidoUSB.LinhaImpressao("ABERTURA : "+dt.format(contaPedido.getData()),ContaPedidoUSB.EstiloM8_M10.FonteB, ContaPedidoUSB.TamanhoM8_M10.A2, ContaPedidoUSB.AlinhamentoM8_M10.Left));
        list.add(new ContaPedidoUSB.LinhaImpressao("FECHAMENTO : "+dt.format(contaPedido.getData_fechamento()),ContaPedidoUSB.EstiloM8_M10.FonteB, ContaPedidoUSB.TamanhoM8_M10.A2, ContaPedidoUSB.AlinhamentoM8_M10.Left));
        addPagamento(list) ;
        return list;
    }

    private void addPagamento(List<ContaPedidoUSB.LinhaImpressao> list){
        String separador = "-" ;
        list.add(new ContaPedidoUSB.LinhaImpressao(repetir(separador, tamColuna), ContaPedidoUSB.EstiloM8_M10.FonteA, ContaPedidoUSB.TamanhoM8_M10.A1, ContaPedidoUSB.AlinhamentoM8_M10.Center));
        DecimalFormat frm = new DecimalFormat("R$ #0.00");
        DecimalFormat qrm = new DecimalFormat("#0.###");
        String aux = "" ;
        String valor = "" ;
        String auxSubtotal = "" ;
        String auxDesconto = "" ;
        String auxServico = "" ;
        String auxTotal = "" ;
        int recuo = 8 ;
        aux =   "Subtotal " ; valor = frm.format(contaPedido.getTotalItens()) ; auxSubtotal = aux+repetir(" ", recuo-valor.length())+valor ;
        aux =   "Desconto " ; valor = frm.format(contaPedido.getDesconto()) ; auxDesconto = aux+repetir(" ", recuo-valor.length())+valor ;
        aux =   "Serviço  " ; valor = frm.format(contaPedido.getTotalComissao()) ; auxServico = aux+repetir(" ", recuo-valor.length())+valor ;
        aux =   "TOTAL    " ; valor = frm.format(contaPedido.getTotal()) ; auxTotal = aux+repetir(" ", recuo-valor.length())+valor ;


        ContaPedidoUSB.AlinhamentoM8_M10 aling = ContaPedidoUSB.AlinhamentoM8_M10.Center ;
        ContaPedidoUSB.TamanhoM8_M10 tam = ContaPedidoUSB.TamanhoM8_M10.L1 ;
        ContaPedidoUSB.TamanhoM8_M10 tamMod = ContaPedidoUSB.TamanhoM8_M10.A2 ;

        ContaPedidoUSB.EstiloM8_M10 estilo = ContaPedidoUSB.EstiloM8_M10.FonteB ;
        ContaPedidoUSB.EstiloM8_M10 estilototal = ContaPedidoUSB.EstiloM8_M10.Reverso ;

        if (contaPedido.getTotalPagamentos() > 0){
            if (contaPedido.getTotalItens() == contaPedido.getTotal()) {
                list.add(new ContaPedidoUSB.LinhaImpressao(auxTotal, estilo, tam, aling));
            }
            for (ContaPedidoPagamento pg : contaPedido.getListPagamento()){
                aux =   pg.getModalidade().getDescricao() ;
                valor = frm.format(pg.getValor()) ;
                auxTotal = aux+repetir(" ", recuo-valor.length())+valor ;
                list.add(new ContaPedidoUSB.LinhaImpressao(auxTotal, estilo, tamMod, aling));
            }
            if (contaPedido.getTotalaPagar() > 0) {
                aux = "A PAGAR ";
                valor = frm.format(contaPedido.getTotalaPagar());
                auxTotal = aux + repetir(" ", recuo - valor.length()) + valor;
                list.add(new ContaPedidoUSB.LinhaImpressao(auxTotal, estilototal, tam, aling));
            } else {

                aux = "TROCO ";
                valor = frm.format(-1 * contaPedido.getTotalaPagar());
                auxTotal = aux + repetir(" ", recuo - valor.length()) + valor;
                list.add(new ContaPedidoUSB.LinhaImpressao(auxTotal, estilototal, tam, aling));
            }


        } else {
            if (contaPedido.getTotalItens() == contaPedido.getTotal()) {
                list.add(new ContaPedidoUSB.LinhaImpressao(auxTotal, estilototal, tam, aling));

            } else {
                list.add(new ContaPedidoUSB.LinhaImpressao(auxSubtotal, estilo, tam, aling));
                if (contaPedido.getDesconto() > 0)
                    list.add(new ContaPedidoUSB.LinhaImpressao(auxDesconto, estilo, tam, aling));
                if (contaPedido.getTotalComissao() > 0)
                    list.add(new ContaPedidoUSB.LinhaImpressao(auxServico, estilo, tam, aling));
                list.add(new ContaPedidoUSB.LinhaImpressao(auxTotal, estilototal, tam, aling));
            }
        }

    }

}
