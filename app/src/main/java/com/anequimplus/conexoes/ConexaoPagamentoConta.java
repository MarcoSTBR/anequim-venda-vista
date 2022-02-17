package com.anequimplus.conexoes;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Date;

public abstract class ConexaoPagamentoConta extends ConexaoServer {

    private ContaPedidoPagamento contaPedidoPagamento ;

    public ConexaoPagamentoConta(Context ctx, ContaPedidoPagamento contaPedidoPagamento)  {
        super(ctx);
        this.contaPedidoPagamento = contaPedidoPagamento ;
        msg = "Pagamento..." ;
       // SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        try {
            maps.put("class","AfoodContaPedidoPag") ;
            maps.put("method","incluir") ;
            maps.put("data",this.contaPedidoPagamento.getJSON()) ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fPagamentoPedido).getUrl();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }
    }

    public void execute(){
        if (Configuracao.getPedidoCompartilhado(ctx)) {
            this.execute() ;
        } else {
            Dao.getContaPedidoPagamentoADO(ctx).incluir(contaPedidoPagamento) ;
            ContaPedido c = Dao.getContaPedidoInternoDAO(ctx).get(contaPedidoPagamento.getContaPedido_id()) ;
            if (c.getTotalPagamentos() >= c.getTotal()){
                c.setStatus(2);
                c.setData_fechamento(new Date());
                Dao.getContaPedidoInternoDAO(ctx).store(c) ;
            }
            oK();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                oK();
            } else {
                erro(codInt, j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, e.getMessage());
        }
    }

    public abstract void oK() ;
    public abstract void erro(int cod, String msg) ;
}
