package com.anequimplus.impressao;

import android.content.Context;

import com.anequimplus.utilitarios.RowImpressao;

import java.util.List;

/*
import cielo.orders.domain.PrinterAttributes;
import cielo.sdk.order.PrinterListener;
import cielo.sdk.printer.PrinterManager;
*/

public class ImpressaoLio {
    private Context ctx ;
    private ListenerImpressao listenerImpressao ;
    private List<RowImpressao> listLinhas ;
/*
    private PrinterListener printerListener ;
    private PrinterManager printerManager ;
*/
    private int status ;

    public ImpressaoLio(Context ctx, ListenerImpressao listenerImpressao, List<RowImpressao> listLinhas) {
        this.ctx = ctx ;
        this.listenerImpressao = listenerImpressao ;
        this.listLinhas = listLinhas ;
        imprimir() ;
    }

    private void imprimir(){
/*
        printerManager = new PrinterManager(ctx) ;
        status = 1 ;

        printerListener = new PrinterListener() {
            @Override
            public void onPrintSuccess() {
                status = 1 ;

            }

            @Override
            public void onError(Throwable throwable) {
                status = 0 ;
                listenerImpressao.onError(status, "Impressora sem papel!");

            }

            @Override
            public void onWithoutPaper() {
                status = 2 ;
                listenerImpressao.onError(status, "Impressora sem papel!");
            }
        };

        for (RowImpressao l : listLinhas){
            sendString(l, printerListener) ;
            if (status != 1) break;
        }

*/
    }

/*
    private void sendString(RowImpressao linha, PrinterListener printerListener){
        HashMap<String, Integer> alinhamento =  new HashMap<>() ;
        //TipoAlinhamento.ptCenter
        switch (linha.getTipoAlinhamento().valor){
            case 0 :
                alinhamento.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
                alinhamento.put(PrinterAttributes.KEY_TYPEFACE, 0);
                alinhamento.put(PrinterAttributes.KEY_TEXT_SIZE, linha.getTamFont() */
/*20*//*
);
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
*/
}
