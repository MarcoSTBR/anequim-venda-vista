package com.anequimplus.impressao;

import android.content.Context;

import com.anequimplus.conexoes.ConexaoRelatorios;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.utilitarios.RowImpressao;

import java.util.HashMap;
import java.util.List;

import cielo.orders.domain.PrinterAttributes;
import cielo.sdk.order.PrinterListener;
import cielo.sdk.printer.PrinterManager;

public class RelatorioLIO {
    private Context ctx ;
    private PrinterManager printerManager ;
    private PrinterListener printerListener ;
    private ListenerImpressao listenerImpressao;
    private int status ;
    private String mensagem ;
    private List<RowImpressao> listImpressao ;


    public RelatorioLIO(Context ctx, ConexaoRelatorios relatorio, ListenerImpressao listenerImpressao) {
        this.ctx = ctx;
        this.listenerImpressao = listenerImpressao ;
        this.listImpressao = relatorio.getLinhas();

    }

    public void executar(){
        printerManager = new PrinterManager(ctx) ;
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
                mensagem = "Sem papel";
                listenerImpressao.onError(status, mensagem);
            }
        };
        status = 1 ;
        for (RowImpressao l : listImpressao){
            if (status == 1) {
                sendString(l);
            }
        }
        if (status == 1){
            listenerImpressao.onImpressao(status);
        }
    }



    private void sendString(RowImpressao linha){
        HashMap<String, Integer> alinhamento =  new HashMap<>() ;
        //TipoAlinhamento.ptCenter
        switch (linha.getTipoAlinhamento().valor){
            case 0 :
                alinhamento.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
                alinhamento.put(PrinterAttributes.KEY_TYPEFACE, 0);
                alinhamento.put(PrinterAttributes.KEY_TEXT_SIZE, linha.getTamFont() /*20*/);
                break ;
            case 1 :
                alinhamento.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
                alinhamento.put(PrinterAttributes.KEY_TYPEFACE, 0);
                alinhamento.put(PrinterAttributes.KEY_TEXT_SIZE, linha.getTamFont());
                break ;

            case 2 :
                alinhamento.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
                alinhamento.put(PrinterAttributes.KEY_TYPEFACE, 0);
                alinhamento.put(PrinterAttributes.KEY_TEXT_SIZE, linha.getTamFont());
                break ;
        };

        printerManager.printText(linha.getLinha(), alinhamento, printerListener) ;

    }


    //public void setListenerImpressao(ListenerImpressao listenerImpressao) {
    //    this.listenerImpressao = listenerImpressao;
   // }


}
