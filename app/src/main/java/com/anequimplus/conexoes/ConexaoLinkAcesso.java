package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;


public abstract class ConexaoLinkAcesso extends ConexaoServer {


    public ConexaoLinkAcesso(Context ctx) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Links" ;
        String loja_id = Integer.toString(UtilSet.getLojaId(ctx)) ;
        String chave = UtilSet.getChave(ctx) ;
        maps.put("class","AfoodLinks") ;
        maps.put("method","loadAll") ;
        maps.put("chave",UtilSet.getChave(ctx));
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC", UtilSet.getMAC(ctx)) ;

        // url = new URL("http://10.0.0.103/design/rest.php?class=AfoodLinks&method=loadAll&chave="+chave+"&loja_id="+loja_id);
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fLinkAcesso).getUrl();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExecute_Liks",s) ;
        try {
            JSONObject j = new JSONObject(s);
            Log.e("onPostExecute_Liks_j",j.toString()) ;
            if (j.getString("status").equals("success")) {
                Dao.getLinkAcessoADO(ctx).setLinkAcesso(j.getJSONArray("data")) ;
                oK();
            } else erro(j.getString("data"));
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }


    }

/*
    private void setList(){
        list = new ArrayList<LinkAcesso>() ;
        try {
            list.add(new LinkAcesso(0, Link.fLinkAcesso, new URL(UtilSet.getServidor(ctx) + "/LinkAcesso")));
            list.add(new LinkAcesso(1, Link.fLogar, new URL(UtilSet.getServidor(ctx) + "/Login")));
            list.add(new LinkAcesso(2, Link.fConsultaProduto, new URL(UtilSet.getServidor(ctx) + "/ConsultaProduto")));
            list.add(new LinkAcesso(3, Link.fIncluirPedido, new URL(UtilSet.getServidor(ctx) + "/IncluirPedido")));
            list.add(new LinkAcesso(4, Link.fConsultaPedido, new URL(UtilSet.getServidor(ctx) + "/fConsultarPedido")));
            list.add(new LinkAcesso(5, Link.fPagamentoPedido, new URL(UtilSet.getServidor(ctx) + "/PagamentoPedido")));
            list.add(new LinkAcesso(6, Link.fTesteConexao, new URL(UtilSet.getServidor(ctx) + "/TesteConexao")));
            list.add(new LinkAcesso(7, Link.fConsultaModalidade, new URL(UtilSet.getServidor(ctx) + "/ConsultaModalidade")));
            list.add(new LinkAcesso(8, Link.fImagem, new URL(UtilSet.getServidor(ctx) + "/Imagem")));
            list.add(new LinkAcesso(9, Link.fpdf, new URL(UtilSet.getServidor(ctx) + "/pdf")));
            list.add(new LinkAcesso(10, Link.fAberturaCaixa, new URL(UtilSet.getServidor(ctx) + "/AberturaCaixa")));
            list.add(new LinkAcesso(11, Link.fConsultaCaixa, new URL(UtilSet.getServidor(ctx) + "/ConsultaCaixa")));
            list.add(new LinkAcesso(12, Link.fConsultaOpFechamento, new URL(UtilSet.getServidor(ctx) + "/ConsultaOpFechamento")));
            list.add(new LinkAcesso(13, Link.fExecutaOpFechamento, new URL(UtilSet.getServidor(ctx) + "/ExecutaOpFechamento")));
            list.add(new LinkAcesso(14, Link.fImprimirOpFechamento, new URL(UtilSet.getServidor(ctx) + "/ImprimirOpFechamento")));
            list.add(new LinkAcesso(15, Link.fFechamentoCaixa, new URL(UtilSet.getServidor(ctx) + "/FechamentoCaixa")));
            list.add(new LinkAcesso(16, Link.fImpressoras, new URL(UtilSet.getServidor(ctx) + "/Impressoras")));
            list.add(new LinkAcesso(17, Link.fConfiguracaoLIO, new URL(UtilSet.getServidor(ctx) + "/ConfiguracaoLio")));
            list.add(new LinkAcesso(18, Link.fImprimirConta, new URL(UtilSet.getServidor(ctx) + "/ImprimirConta")));
            list.add(new LinkAcesso(19, Link.fNFCeGetContaPedido, new URL(UtilSet.getServidor(ctx) + "/NFCeGetContaPedido")));
            list.add(new LinkAcesso(20, Link.fNFCeSetContaPedido, new URL(UtilSet.getServidor(ctx) + "/NFCeSetContaPedido")));
            list.add(new LinkAcesso(21, Link.fConsultaProduto, new URL(UtilSet.getServidor(ctx) + "/QRCode")));
            list.add(new LinkAcesso(22, Link.fGrupos, new URL(UtilSet.getServidor(ctx) + "/Grupos")));
            list.add(new LinkAcesso(23, Link.fGradeVendas, new URL(UtilSet.getServidor(ctx) + "/GradeVendas")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro("setList() "+e.getMessage()) ;
        }
    }

*/

    public abstract void oK() ;
    public abstract void erro(String msg) ;
}
