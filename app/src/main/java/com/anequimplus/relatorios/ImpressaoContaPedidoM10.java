package com.anequimplus.relatorios;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.impressao.ContaPedidoM10;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImpressaoContaPedidoM10 implements IImpressaoContaPedido {

    private ContaPedido contaPedido ;
    private int tamColuna  ;

    public ImpressaoContaPedidoM10(int tamColuna) {
        this.tamColuna = tamColuna;
    }

    @Override
    public void setContaPedido(ContaPedido contaPedido) {
        this.contaPedido = contaPedido;
    }

    @Override
    public List<ContaPedidoM10.LinhaImpressao> getListLinhas() {
        List<ContaPedidoM10.LinhaImpressao> list = new ArrayList<ContaPedidoM10.LinhaImpressao>() ;
        addCabecalho(list) ;
        addItens(list) ;

        return list;
    }

    private void addCabecalho(List<ContaPedidoM10.LinhaImpressao> list){
       // yyyy-MM-dd HH:mm:ss
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
        list.add(new ContaPedidoM10.LinhaImpressao("CONTA : "+contaPedido.getPedido(), ContaPedidoM10.EstiloM8_M10.Reverso, ContaPedidoM10.TamanhoM8_M10.A3, ContaPedidoM10.AlinhamentoM8_M10.Center)) ;
        list.add(new ContaPedidoM10.LinhaImpressao("ABERTURA : "+dt.format(contaPedido.getData()),ContaPedidoM10.EstiloM8_M10.FonteB, ContaPedidoM10.TamanhoM8_M10.A2, ContaPedidoM10.AlinhamentoM8_M10.Left));
    }

    private void addItens(List<ContaPedidoM10.LinhaImpressao> list) {
        String separador = "-" ;
        list.add(new ContaPedidoM10.LinhaImpressao(repetir(separador, tamColuna), ContaPedidoM10.EstiloM8_M10.FonteB, ContaPedidoM10.TamanhoM8_M10.A2, ContaPedidoM10.AlinhamentoM8_M10.Left));
        DecimalFormat frm = new DecimalFormat("R$ #0.00");
        DecimalFormat qrm = new DecimalFormat("#0.###");
        for (ContaPedidoItem it : contaPedido.getListContaPedidoItem()){
            String txt = it.getProduto().getDescricao() ;
            String qv = qrm.format(it.getQuantidade())+"   X R$ "+frm.format(it.getPreco())+" = R$ "+frm.format(it.getValor()) ;
            list.add(new ContaPedidoM10.LinhaImpressao(txt, ContaPedidoM10.EstiloM8_M10.FonteA, ContaPedidoM10.TamanhoM8_M10.A2, ContaPedidoM10.AlinhamentoM8_M10.Left));
            list.add(new ContaPedidoM10.LinhaImpressao(qv, ContaPedidoM10.EstiloM8_M10.FonteB, ContaPedidoM10.TamanhoM8_M10.A2, ContaPedidoM10.AlinhamentoM8_M10.Right));
        }
        list.add(new ContaPedidoM10.LinhaImpressao(repetir(separador, tamColuna), ContaPedidoM10.EstiloM8_M10.FonteB, ContaPedidoM10.TamanhoM8_M10.A2, ContaPedidoM10.AlinhamentoM8_M10.Left));
        String aux = "" ;
        String valor = "" ;
        String auxSubtotal = "" ;
        String auxServico = "" ;
        String auxTotal = "" ;
        int recuo = 8 ;
        aux =   "Subtotal " ; valor = frm.format(contaPedido.getTotalItens()) ; auxSubtotal = aux+repetir(" ", recuo-valor.length())+valor ;
        aux =   "Serviço  " ; valor = frm.format(contaPedido.getTotalComissao()) ; auxServico = aux+repetir(" ", recuo-valor.length())+valor ;
        aux =   "TOTAL    " ; valor = frm.format(contaPedido.getTotal()) ; auxTotal = aux+repetir(" ", recuo-valor.length())+valor ;


        ContaPedidoM10.AlinhamentoM8_M10 aling = ContaPedidoM10.AlinhamentoM8_M10.Center ;
        ContaPedidoM10.TamanhoM8_M10 tam = ContaPedidoM10.TamanhoM8_M10.A3 ;
        ContaPedidoM10.TamanhoM8_M10 tamMod = ContaPedidoM10.TamanhoM8_M10.A2 ;

        ContaPedidoM10.EstiloM8_M10 estilo = ContaPedidoM10.EstiloM8_M10.FonteB ;
        ContaPedidoM10.EstiloM8_M10 estilototal = ContaPedidoM10.EstiloM8_M10.Reverso ;

        if (contaPedido.getTotalPagamentos() > 0){
            if (contaPedido.getTotalItens() == contaPedido.getTotal()) {
                list.add(new ContaPedidoM10.LinhaImpressao(auxTotal, estilo, tam, aling));
            }
            for (ContaPedidoPagamento pg : contaPedido.getListPagamento()){
                aux =   pg.getModalidade().getDescricao() ;
                valor = frm.format(pg.getValor()) ;
                auxTotal = aux+repetir(" ", recuo-valor.length())+valor ;
                list.add(new ContaPedidoM10.LinhaImpressao(auxTotal, estilo, tamMod, aling));
            }
            aux = "A PAGAR "   ;
            valor = frm.format(contaPedido.getTotalaPagar()) ;
            auxTotal = aux+repetir(" ", recuo-valor.length())+valor ;
            list.add(new ContaPedidoM10.LinhaImpressao(auxTotal, estilototal, tam, aling));


        } else {
            if (contaPedido.getTotalItens() == contaPedido.getTotal()) {
                list.add(new ContaPedidoM10.LinhaImpressao(auxTotal, estilototal, tam, aling));

            } else {
                list.add(new ContaPedidoM10.LinhaImpressao(auxSubtotal, estilo, tam, aling));
                if (contaPedido.getTotalComissao() > 0)
                    list.add(new ContaPedidoM10.LinhaImpressao(auxServico, estilo, tam, aling));
                list.add(new ContaPedidoM10.LinhaImpressao(auxTotal, estilototal, tam, aling));
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

}
