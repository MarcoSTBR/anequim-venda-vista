package com.anequimplus.impressao;

import android.content.Context;
import android.widget.Toast;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.Impressora;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.tipos.TipoAlinhamento;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cielo.orders.domain.PrinterAttributes;
import cielo.sdk.order.PrinterListener;
import cielo.sdk.printer.PrinterManager;

public class ContaPedidoLIO extends CorpoRelatorio {

    private Context ctx ;
    private PrinterManager printerManager ;
    private PrinterListener printerListener ;
    private ListenerImpressao listenerImpressao;
    private ContaPedido contaPedido ;
    private int status ;
    private String mensagem ;
    private int tamColunas = 38 ;
    private Impressora impressora ;

    public ContaPedidoLIO(Context ctx, ContaPedido contaPedido, String link, String param, Impressora impressora, ListenerImpressao listenerImpressao) {
        super(ctx, link, param);
        this.ctx = ctx;
        this.listenerImpressao = listenerImpressao;
        this.contaPedido = contaPedido;
        printerManager = new PrinterManager(ctx) ;
    }

    public void executar(){
        printerListener = new PrinterListener() {
            @Override
            public void onPrintSuccess() {
                status = 1 ;
                mensagem = "OK" ;

            }

            @Override
            public void onError(Throwable throwable) {
                status = 2 ;
                mensagem = "Erro" ;
                listenerImpressao.onError(status, mensagem);

            }

            @Override
            public void onWithoutPaper() {
                status = 3 ;
                mensagem = "Sem papel" ;
                listenerImpressao.onError(status, mensagem);

            }
        };
        //imprimirContaPedido() ;
     execute() ;
    }

    private void imprimirContaPedido() {
        status = 1 ;
        String linha ;
        SimpleDateFormat sdate = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
        sendString(new RowImpressao(getLinha(tamColunas, "-"), TipoAlinhamento.ptLeft, 20)) ;
        sendString(new RowImpressao(sdate.format(new Date()), TipoAlinhamento.ptRight, 20)) ;
        linha = "Pedido "+ contaPedido.getPedido() ;
        sendString(new RowImpressao(linha, TipoAlinhamento.ptLeft, 20)) ;
        sendString(new RowImpressao(getLinha(tamColunas, "-"), TipoAlinhamento.ptLeft, 20)) ;
        for (ContaPedidoItem it : contaPedido.getListContaPedidoItem()){
          if (status == 1) {
              sendItem(it);
          }
        }
        if (status == 1){
            sendRodape() ;
        }
        if (status ==1) {
            listenerImpressao.onImpressao(1);
        } else {

            Toast.makeText(ctx,mensagem, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRodape() {
        sendString(new RowImpressao(getLinha(tamColunas, "-"), TipoAlinhamento.ptLeft, 20)) ;
    }

    private void sendItem(ContaPedidoItem it){
        DecimalFormat frmQ = new DecimalFormat("#0.###");
        DecimalFormat frmV = new DecimalFormat("#0.00");

        String litd = it.getProduto().getDescricao() ;
        String litp = "( "+frmQ.format(it.getQuantidade())+" ) "+frmV.format(it.getValor()) ;
        String[] linha = {litd, litp}  ;
        HashMap<String, Integer> alDesc =  new HashMap<>() ;
        alDesc.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        alDesc.put(PrinterAttributes.KEY_TYPEFACE, 0);
        alDesc.put(PrinterAttributes.KEY_TEXT_SIZE, 10);
        alDesc.put(PrinterAttributes.KEY_WEIGHT, 2);
        HashMap<String, Integer> alVal =  new HashMap<>() ;
        alVal.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        alVal.put(PrinterAttributes.KEY_TYPEFACE, 1);
        alVal.put(PrinterAttributes.KEY_TEXT_SIZE, 10);
        alVal.put(PrinterAttributes.KEY_WEIGHT, 1);
        List<Map<String, Integer>> stylesMap =  new ArrayList<>();
        stylesMap.add(alDesc);
        stylesMap.add(alVal);
        printerManager.printMultipleColumnText(linha, stylesMap, printerListener);
    }

    private void sendString(RowImpressao l){
        if (status == 1) {
            HashMap<String, Integer> alinhamento = new HashMap<>();
            switch (l.getTipoAlinhamento().valor) {
                case 0:
                    alinhamento.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
                    alinhamento.put(PrinterAttributes.KEY_TYPEFACE, 0);
                    alinhamento.put(PrinterAttributes.KEY_TEXT_SIZE, l.getTamFont() /*20*/);
                    break;
                case 1:
                    alinhamento.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
                    alinhamento.put(PrinterAttributes.KEY_TYPEFACE, 0);
                    alinhamento.put(PrinterAttributes.KEY_TEXT_SIZE, l.getTamFont());
                    break;

                case 2:
                    alinhamento.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
                    alinhamento.put(PrinterAttributes.KEY_TYPEFACE, 0);
                    alinhamento.put(PrinterAttributes.KEY_TEXT_SIZE, l.getTamFont());
                    break;
            }
            ;
            printerManager.printText(l.getLinha(), alinhamento, printerListener);
        }
    }

    private String getLinha(int tam, String s){
        String t = "" ;
        for (int i=1 ; i<=tam ; i++){
            t = t + "s" ;
        }
        return t ;
    }


    @Override
    public void setErro(String msg) {
        listenerImpressao.onError(2, msg);
    }

    @Override
    public void setRelatorio(List<RowImpressao> listRow) {
      for (RowImpressao l : listRow) {
          sendString(l);
      }

    }
}
