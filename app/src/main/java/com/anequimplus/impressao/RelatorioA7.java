package com.anequimplus.impressao;

import android.content.Context;

import com.anequimplus.conexoes.ConexaoRelatorios;
import com.anequimplus.utilitarios.RowImpressao;

import java.io.UnsupportedEncodingException;
import java.util.List;

import inputservice.NfePrinter.ReceiptPrinterA7;

public class RelatorioA7 extends ReceiptPrinterA7 {

    private Context ctx ;
    private boolean imprimiu ;
    private static boolean connected ;
    private ListenerImpressao listenerImpressao;
    //private ConexaoRelatorios relatorio ;
    private List<RowImpressao> listImpressao ;


    public RelatorioA7(Context ctx, ConexaoRelatorios relatorio, ListenerImpressao listenerImpressao) {
        this.ctx = ctx;
        //this.relatorio = relatorio;
        this.listImpressao = relatorio.getLinhas() ;
        this.listenerImpressao = listenerImpressao;
    }

    public RelatorioA7(Context ctx, List<RowImpressao> relatorio, ListenerImpressao listenerImpressao) {
        this.ctx = ctx;
        this.listImpressao = relatorio ;
        this.listenerImpressao = listenerImpressao;
    }

    public void executar(){
        imprimiu = false ;
        this.mobileprint.Reset() ;
        if (connect(false)){
            imprimiu = true ;
            try {
                for (RowImpressao l : listImpressao){
                        this.mobileprint.SendString(l.getLinha()+"\n") ;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                listenerImpressao.onError(2, e.getMessage());
            }

            this.mobileprint.CloseConnect() ;
            disconnect();
            listenerImpressao.onImpressao(1);
        } else {
            listenerImpressao.onError(3, "Sem conexão com a Impressora");
        }
    }



}
