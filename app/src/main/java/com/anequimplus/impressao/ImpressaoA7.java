package com.anequimplus.impressao;

import android.content.Context;

import com.anequimplus.utilitarios.RowImpressao;

import java.io.UnsupportedEncodingException;
import java.util.List;

import inputservice.NfePrinter.ReceiptPrinterA7;

public class ImpressaoA7 extends ReceiptPrinterA7 {

    private Context ctx ;
    private ListenerImpressao listenerImpressao ;
    private List<RowImpressao> listLinhas ;
    private int status ;

    public ImpressaoA7(Context ctx, ListenerImpressao listenerImpressao, List<RowImpressao> listLinhas){
        this.ctx = ctx ;
        this.listenerImpressao = listenerImpressao ;
        this.listLinhas = listLinhas ;
        imprimir() ;
    }

    private void imprimir() {
        this.getMobilePrinter().Reset() ;
        if (connect(false)){
           try {
                for (RowImpressao l : listLinhas){
                    getMobilePrinter().SendString(l.getLinha()+'\n');
                }
                listenerImpressao.onImpressao(1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                listenerImpressao.onError(2, e.getMessage());
            }
            this.getMobilePrinter().CloseConnect();
            this.disconnect();
        } else {
            this.getMobilePrinter().CloseConnect();
            this.disconnect();
            listenerImpressao.onError(2, "Sem conexão com a Impressora");
        }
    }


}
