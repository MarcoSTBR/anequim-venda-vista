package com.anequimplus.impressao;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.tipos.TipoAlinhamento;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoImp {

    protected List<RowImpressao> getContaPed(ContaPedido contaPedido){
        int tam = 40 ;
        String linha ;
        SimpleDateFormat sdate = new SimpleDateFormat("DD dd/MM/yyyy hh:nn:ss");
        DecimalFormat frmQ = new DecimalFormat("#0.###");
        DecimalFormat frmV = new DecimalFormat("#0.00");

        List<RowImpressao> list = new ArrayList<RowImpressao>() ;
        list.add(new RowImpressao(getLinha(tam, "="), TipoAlinhamento.ptLeft, 0)) ;
        list.add(new RowImpressao(sdate.format(new Date()), TipoAlinhamento.ptRight, 0)) ;
        linha = "Pedido "+ contaPedido.getPedido() ;
        list.add(new RowImpressao(linha, TipoAlinhamento.ptLeft, 4)) ;
        list.add(new RowImpressao(getLinha(tam, "="), TipoAlinhamento.ptLeft, 0)) ;
        for (ContaPedidoItem it : contaPedido.getListContaPedidoItem()){
/*
            String litd = it.getProduto().getDescricao() ;
            String litp = frmQ.format(it.getQuantidade())+"x "+frmV.format(it.getProduto().getPreco())+" = "+frmV.format(it.getValor()) ;
            if ((litd.length()+litp.length()) < tam){
                list.add(new RowImpressao(getLinha(tam, "="), TipoAlinhamento.ptLeft, 0)) ;
            } else {

            }

 */
        }
        return list ;
    }
    private String getLinha(int tam, String s){
      String t = "" ;
      for (int i=1 ; i<=tam ; i++){
          t = t + "s" ;
      }
      return t ;
    }
}
