package com.anequimplus.conexoes;

import android.content.Context;

import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.Impressora;
import com.anequimplus.relatorios.ListenerRelatorio;
import com.anequimplus.relatorios.RelatorioCancelamentos;
import com.anequimplus.relatorios.RelatorioContas;
import com.anequimplus.relatorios.RelatorioDemostrativo;
import com.anequimplus.relatorios.RelatorioTransferencias;
import com.anequimplus.utilitarios.RowImpressao;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class ConexaoRelatorios  {

    private Context ctx ;
    private List<RowImpressao> list ;
    private Impressora impressora ;
    private Caixa caixa ;
    private int opcao_id ;
    private ListenerRelatorio listenerRelatorio ;

    public ConexaoRelatorios(Context ctx, Impressora impressora, Caixa caixa, int opcao_id, ListenerRelatorio listenerRelatorio)  {
        this.ctx = ctx;
        this.impressora = impressora ;
        this.caixa = caixa ;
        this.opcao_id =opcao_id ;
        this.listenerRelatorio = listenerRelatorio ;
    }


    private void formarLinhas(JSONArray j) throws JSONException {
            for (int i=0 ; i < j.length() ; i++ ){
                list.add(new RowImpressao(j.getJSONObject(i)));
            }
    }

    public List<RowImpressao> getLinhas(){
        return list ;
    }


    public String getStringLinhas(){
        String txt = "" ;
        for (RowImpressao l : list){
            txt = txt + l.getLinha() + "\n" ;
        }
        return  txt ;
    }

    public void executar(){

            switch (opcao_id){
                case 1 : new RelatorioDemostrativo(ctx, caixa, impressora, listenerRelatorio).executar() ;
                break;
                case 2 : new RelatorioContas(ctx, caixa, impressora, listenerRelatorio).executar() ;
                break;
                case 3 : new RelatorioTransferencias(ctx, caixa, impressora, listenerRelatorio).executar() ;
                break;
                case 4 : new RelatorioCancelamentos(ctx, caixa, impressora, listenerRelatorio).executar() ;
                break;
                default: listenerRelatorio.erroMensagem("Opção ["+opcao_id+"] Não Encontrada!");
            }
    }

}
