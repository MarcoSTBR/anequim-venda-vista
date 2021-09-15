package com.anequimplus.conexoes;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ConexaoPagamentoConta extends ConexaoServer {

    public ConexaoPagamentoConta(Context ctx, ContaPedido contaPedido, Caixa caixa, Modalidade modalidade, double valor) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, MalformedURLException {
        super(ctx);
        msg = "Pagamento" ;
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

        maps.put("class","AfoodContaPedidoPag") ;
        maps.put("method","incluir") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        maps.put("pedido_id",contaPedido.getId()) ;
        maps.put("data_pag", fdate.format(new Date())) ;
        maps.put("modalidade_id",modalidade.getId()) ;
        maps.put("caixa_id",caixa.getId()) ;
        maps.put("valor",valor) ;
        maps.put("status",1) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fPagamentoPedido).getUrl();

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                oK();
            } else {
                erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public abstract void oK() ;
    public abstract void erro(String msg) ;
}
