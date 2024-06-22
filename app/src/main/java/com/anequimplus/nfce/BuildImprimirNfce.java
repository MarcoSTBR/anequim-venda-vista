package com.anequimplus.nfce;

import android.content.Context;

import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;

import java.net.MalformedURLException;

public class BuildImprimirNfce {

    private Context ctx;
    private ContaPedidoNFCe contaPedidoNFCe ;
    private ListenerImprimirNFCe listenerImprimirNFCe ;

    public BuildImprimirNfce(Context ctx, ContaPedidoNFCe contaPedidoNFCe, ListenerImprimirNFCe listenerImprimirNFCe) {
        this.ctx = ctx;
        this.contaPedidoNFCe = contaPedidoNFCe;
        this.listenerImprimirNFCe = listenerImprimirNFCe;
    }


    public void executar(){
        try {
            switch (Configuracao.getOpcaoNFCE(ctx)) {
                case 1 : new ConexaoImprimirNFCeServerLocal(ctx, contaPedidoNFCe, listenerImprimirNFCe).execute();
                break;
                case 2 : new ConexaoImprimirNFCeCloudNFCe(ctx, contaPedidoNFCe, listenerImprimirNFCe).execute();
                break;
                default:listenerImprimirNFCe.erro("Nenhuma opção de NFCe encontrada!");
            }
        } catch (JSONException e){
            e.printStackTrace();
            listenerImprimirNFCe.erro(e.getMessage());
        } catch (MalformedURLException e){
            e.printStackTrace();
            listenerImprimirNFCe.erro(e.getMessage());
        }
    }


}
