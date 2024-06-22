package com.anequimplus.relatorios;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.impressao.ContaPedidoI9;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImpressaoContaPedidoI9 implements IImpressaoContaPedido {

    private ContaPedido contaPedido ;
    private int tamColuna  ;

    public ImpressaoContaPedidoI9(int tamColuna) {
        this.tamColuna = tamColuna;
    }

    @Override
    public void setContaPedido(ContaPedido contaPedido) {
        this.contaPedido = contaPedido;
    }

    @Override
    public List<ContaPedidoI9.LinhaImpressao> getListLinhas() {

        List<ContaPedidoI9.LinhaImpressao> list = new ArrayList<ContaPedidoI9.LinhaImpressao>() ;
        addCabecalho(list) ;
        addItens(list) ;
        addPagamento(list) ;
        return list;
    }

    private void addCabecalho(List<ContaPedidoI9.LinhaImpressao> list){
        // yyyy-MM-dd HH:mm:ss
        String separador = "-" ;
        String vias = "Via "+contaPedido.getNum_impressao();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
        list.add(new ContaPedidoI9.LinhaImpressao("CONTA : "+contaPedido.getPedido(), ContaPedidoI9.EstiloM8_M10.Reverso, ContaPedidoI9.TamanhoM8_M10.A3, ContaPedidoI9.AlinhamentoM8_M10.Center)) ;
        list.add(new ContaPedidoI9.LinhaImpressao("ABERTURA : "+dt.format(contaPedido.getData()),ContaPedidoI9.EstiloM8_M10.FonteB, ContaPedidoI9.TamanhoM8_M10.A2, ContaPedidoI9.AlinhamentoM8_M10.Left));
        list.add(new ContaPedidoI9.LinhaImpressao(repetir(separador, tamColuna - vias.length())+vias, ContaPedidoI9.EstiloM8_M10.FonteA, ContaPedidoI9.TamanhoM8_M10.A1, ContaPedidoI9.AlinhamentoM8_M10.Center));

    }

    private void addItens(List<ContaPedidoI9.LinhaImpressao> list) {
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
                list.add(new ContaPedidoI9.LinhaImpressao(txt, ContaPedidoI9.EstiloM8_M10.FonteA , ContaPedidoI9.TamanhoM8_M10.A1, ContaPedidoI9.AlinhamentoM8_M10.Left));
            } else {
                list.add(new ContaPedidoI9.LinhaImpressao(txt.substring(1, tamColuna), ContaPedidoI9.EstiloM8_M10.FonteA, ContaPedidoI9.TamanhoM8_M10.A1, ContaPedidoI9.AlinhamentoM8_M10.Left));
                list.add(new ContaPedidoI9.LinhaImpressao(repetir(" ", tamColuna - qv.length())+ qv, ContaPedidoI9.EstiloM8_M10.FonteA, ContaPedidoI9.TamanhoM8_M10.A1, ContaPedidoI9.AlinhamentoM8_M10.Left));
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
    public List<ContaPedidoI9.LinhaImpressao> getRecibo() {
        List<ContaPedidoI9.LinhaImpressao> list = new ArrayList<ContaPedidoI9.LinhaImpressao>() ;
        String separador = "-" ;
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
        list.add(new ContaPedidoI9.LinhaImpressao("RECIBO DA CONTA : "+contaPedido.getPedido(), ContaPedidoI9.EstiloM8_M10.Reverso, ContaPedidoI9.TamanhoM8_M10.A3, ContaPedidoI9.AlinhamentoM8_M10.Center)) ;
        list.add(new ContaPedidoI9.LinhaImpressao("ABERTURA : "+dt.format(contaPedido.getData()),ContaPedidoI9.EstiloM8_M10.FonteB, ContaPedidoI9.TamanhoM8_M10.A2, ContaPedidoI9.AlinhamentoM8_M10.Left));
        list.add(new ContaPedidoI9.LinhaImpressao("FECHAMENTO : "+dt.format(contaPedido.getData_fechamento()),ContaPedidoI9.EstiloM8_M10.FonteB, ContaPedidoI9.TamanhoM8_M10.A2, ContaPedidoI9.AlinhamentoM8_M10.Left));
        addPagamento(list) ;
        return list;
    }

    private void addPagamento(List<ContaPedidoI9.LinhaImpressao> list){
        String separador = "-" ;
        list.add(new ContaPedidoI9.LinhaImpressao(repetir(separador, tamColuna), ContaPedidoI9.EstiloM8_M10.FonteA, ContaPedidoI9.TamanhoM8_M10.A1, ContaPedidoI9.AlinhamentoM8_M10.Center));
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


        ContaPedidoI9.AlinhamentoM8_M10 aling = ContaPedidoI9.AlinhamentoM8_M10.Center ;
        ContaPedidoI9.TamanhoM8_M10 tam = ContaPedidoI9.TamanhoM8_M10.L1 ;
        ContaPedidoI9.TamanhoM8_M10 tamMod = ContaPedidoI9.TamanhoM8_M10.A2 ;

        ContaPedidoI9.EstiloM8_M10 estilo = ContaPedidoI9.EstiloM8_M10.FonteB ;
        ContaPedidoI9.EstiloM8_M10 estilototal = ContaPedidoI9.EstiloM8_M10.Reverso ;

        if (contaPedido.getTotalPagamentos() > 0){
            if (contaPedido.getTotalItens() == contaPedido.getTotal()) {
                list.add(new ContaPedidoI9.LinhaImpressao(auxTotal, estilo, tam, aling));
            }
            for (ContaPedidoPagamento pg : contaPedido.getListPagamento()){
                aux =   pg.getModalidade().getDescricao() ;
                valor = frm.format(pg.getValor()) ;
                auxTotal = aux+repetir(" ", recuo-valor.length())+valor ;
                list.add(new ContaPedidoI9.LinhaImpressao(auxTotal, estilo, tamMod, aling));
            }
            if (contaPedido.getTotalaPagar() > 0) {
                aux = "A PAGAR ";
                valor = frm.format(contaPedido.getTotalaPagar());
                auxTotal = aux + repetir(" ", recuo - valor.length()) + valor;
                list.add(new ContaPedidoI9.LinhaImpressao(auxTotal, estilototal, tam, aling));
            } else {

                aux = "TROCO ";
                valor = frm.format(-1 * contaPedido.getTotalaPagar());
                auxTotal = aux + repetir(" ", recuo - valor.length()) + valor;
                list.add(new ContaPedidoI9.LinhaImpressao(auxTotal, estilototal, tam, aling));
            }


        } else {
            if (contaPedido.getTotalItens() == contaPedido.getTotal()) {
                list.add(new ContaPedidoI9.LinhaImpressao(auxTotal, estilototal, tam, aling));

            } else {
                list.add(new ContaPedidoI9.LinhaImpressao(auxSubtotal, estilo, tam, aling));
                if (contaPedido.getDesconto() > 0)
                    list.add(new ContaPedidoI9.LinhaImpressao(auxDesconto, estilo, tam, aling));
                if (contaPedido.getTotalComissao() > 0)
                    list.add(new ContaPedidoI9.LinhaImpressao(auxServico, estilo, tam, aling));
                list.add(new ContaPedidoI9.LinhaImpressao(auxTotal, estilototal, tam, aling));
            }
        }

    }

}
