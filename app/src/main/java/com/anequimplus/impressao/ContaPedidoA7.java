package com.anequimplus.impressao;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.utilitarios.RowImpressao;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import inputservice.NfePrinter.ReceiptPrinterA7;


public class ContaPedidoA7  extends CorpoRelatorio {

    private ReceiptPrinterA7  a7;
    private Context ctx ;
    private boolean imprimiu ;
    private static boolean connected ;
    private ContaPedido contaPedido ;
    private ListenerImpressao listenerImpressao;
    private int tamColuna = 48 ;


    public ContaPedidoA7(Context ctx, ContaPedido contaPedido, String link, String param, ListenerImpressao listenerImpressao) {
        super(ctx, link, param);
        this.ctx = ctx;
        this.contaPedido = contaPedido;
        this.listenerImpressao = listenerImpressao;
        a7 = new ReceiptPrinterA7() ;
    }


    public void execute(){
        // imprimirExecutar() ;
        execute() ;
    }

    private void imprimirExecutar(){
        imprimiu = false ;
        Log.i("ContaPedidoA7", "execute");
        a7.getMobilePrinter().Reset() ;
        if (a7.connect(false)) {
            imprimiu = true;
            List<String> texto = getTemplate();
            for (int linha = 0; linha < texto.size(); linha++) {
                try {
                    Log.i("ContaPedidoA7", linha + " " + texto.get(linha));
                    a7.getMobilePrinter().SendString(texto.get(linha));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    listenerImpressao.onError(2, e.getMessage());
                    imprimiu = false;
                    break;
                }
            }
            if (imprimiu) {
                a7.getMobilePrinter().CloseConnect();
                a7.disconnect();
                listenerImpressao.onImpressao(1);
            } else {
                a7.getMobilePrinter().CloseConnect();
                a7.disconnect();
            }
        } else {
            listenerImpressao.onError(1, "Sem conexão com a Impressora");
        }

    }


    private List<String> getTemplate(){
        SimpleDateFormat fd1 = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss") ;
        SimpleDateFormat fd2 = new SimpleDateFormat("E, dd/MM HH:mm") ;
        String d = fd2.format(new Date()) ;
        List<String> t = new ArrayList<String>() ;
        t.add(replaceLinha("=",tamColuna-d.length())+d+"\n");
        t.add("Pedido: "+contaPedido.getPedido()+"\n");
        t.add("Data  : "+fd1.format(contaPedido.getData())+"\n");
        t.add(getLinha("="));
        t.add("Produto                     Quant. Preço Valor"+"\n");
        setProdutos(t);
        t.add(getLinha("="));
        insertTotais(t,"SUBTOTAL:",contaPedido.getTotalItens(), true);
        insertTotais(t,"DESCONTO:",contaPedido.getDesconto(), false);
        insertTotais(t,"TAXA:",contaPedido.getTotalComissao(), false);
        insertTotais(t,"TOTAL:",contaPedido.getTotal(), true);
        insertTotais(t,"PAGAMENTO:",contaPedido.getTotalPagamentos(), false);
        insertTotais(t,"TOTAL A PAGAR:",contaPedido.getTotalaPagar(), false);
        lineFeed(t,5) ;
        return t ;
    }

    private void insertTotais(List<String> t, String titulo, double valor, boolean fixo){
        if (fixo || (valor > 0)){
            String frmV = new DecimalFormat("#0.00").format(valor);
            String tt = titulo + replaceLinha(" ",14-titulo.length())+" R$" ;
            t.add(tt +replaceLinha(" ",tamColuna-tt.length()-frmV.length()-9)+frmV+"\n");
        }
    }

    private void lineFeed(List<String> t, int pulo){
        for (int i=1 ; i <= pulo ; i++) {
            t.add(" \n");
        }
    }

    private void setProdutos(List<String> t){
      DecimalFormat frmQ = new DecimalFormat("#0.###");
      DecimalFormat frmP = new DecimalFormat("#0.00");
      DecimalFormat frmV = new DecimalFormat("R$ #0.00");

      for (ContaPedidoItem it : contaPedido.getListContaPedidoItem()) {
          String ds = it.getProduto().getDescricao() ;
          String qv = frmQ.format(it.getQuantidade())+" x "+frmP.format(it.getPreco())+" = "+frmV.format(it.getValor()) ;
          if ((qv.length() + ds.length()) >= tamColuna){
              t.add(ds+"\n");
              t.add(replaceLinha(" ",tamColuna-qv.length())+qv+"\n");
          } else {
              t.add(ds+replaceLinha(" ",tamColuna-(qv.length()+ds.length()))+qv+"\n");
          }
      }
    }

    private String getLinha(String p){
        String l = "";
        for (int i=1 ; i<=tamColuna ; i++){
            l = l + p ;
        }
        return l ;
    }

    private String replaceLinha(String p , int q){
        String l = "";
        for (int i=1 ; i<=q ; i++){
            l = l + p ;
        }
        return l ;
    }


    @Override
    public void setErro(String msg) {
        listenerImpressao.onError(2, msg);

    }

    @Override
    public void setRelatorio(List<RowImpressao> listRow) {
        imprimiu = false ;
        Log.i("ContaPedidoA7", "setRelatorio");
        a7.getMobilePrinter().Reset() ;
        if (a7.connect(false)) {
            imprimiu = true;
            for (RowImpressao l : listRow){
                Log.i("ContaPedidoA7", l.getLinha());
                try {
                    a7.getMobilePrinter().SendString(l.getLinha());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("ContaPedidoA7", e.getMessage());
                    imprimiu = false;
                    listenerImpressao.onError(2,  e.getMessage());
                    break;
                }
            }
            a7.getMobilePrinter().CloseConnect();
            a7.disconnect();
            if (imprimiu) {
                listenerImpressao.onImpressao(1);
            }
        } else
            listenerImpressao.onError(1, "Sem conexão com a Impressora");


    }
}
