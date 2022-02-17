package com.anequimplus.conexoes;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.Impressora;
import com.anequimplus.relatorios.RelatorioCancelamentos;
import com.anequimplus.relatorios.RelatorioContas;
import com.anequimplus.relatorios.RelatorioDemostrativo;
import com.anequimplus.relatorios.RelatorioTransferencias;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoRelatorios extends ConexaoServer {

    private List<RowImpressao> list ;
    private Impressora impressora ;
    private Caixa caixa ;
    private int opcao_id ;

    public ConexaoRelatorios(Context ctx, Impressora impressora, Caixa caixa, int opcao_id) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Relatório" ;
        this.impressora = impressora ;
        this.caixa = caixa ;
        this.opcao_id =opcao_id ;

        maps.put("class","AfoodOpcoesFechamento") ;
        maps.put("method","obterOpRelatorio") ;
        maps.put("caixa_id",caixa.getId()) ;
        maps.put("opcao_id",opcao_id) ;
        maps.put("impressora_id",impressora.getId()) ;

        maps.put("chave", UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("impressora_id",impressora.getId()) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fImprimirOpFechamento).getUrl() ;
        list = new ArrayList<RowImpressao>() ;
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

    public void execute(){
        if (Configuracao.getPedidoCompartilhado(ctx)){
            this.execute();
        } else {
            switch (opcao_id){
                case 1 : ok(new RelatorioDemostrativo(ctx, caixa, impressora).getRelatorio()) ;
                break;
                case 2 : ok(new RelatorioContas(ctx, caixa, impressora).getRelatorio()) ;
                break;
                case 3 : ok(new RelatorioTransferencias(ctx, caixa, impressora).getRelatorio()) ;
                break;
                case 4 : ok(new RelatorioCancelamentos(ctx, caixa, impressora).getRelatorio()) ;
                break;
                default: erroMensagem("Opção ["+opcao_id+"] Não Encontrada!");
            }

        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                formarLinhas(j.getJSONArray("data"));
                ok(list) ;
            } else erroMensagem(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        }

    }


    public abstract void ok(List<RowImpressao> l) ;
    public abstract void erroMensagem(String msg) ;
}
