package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoContaPedidoItemCancelar extends ConexaoServer {

    private ContaPedidoItemCancelamento contaPedidoItemCancelamento ;

    public ConexaoContaPedidoItemCancelar(Context ctx, ContaPedidoItemCancelamento contaPedidoItemCancelamento) {
        super(ctx);
        this.contaPedidoItemCancelamento = contaPedidoItemCancelamento ;
        msg = "Cancelando Item...";
//        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Log.i("jsoncancelar", contaPedidoItemCancelamento.getJSON().toString()) ;
            maps.put("class","AfoodContaPedidoCancel") ;
            maps.put("method","cancelar") ;
            maps.put("MAC", UtilSet.getMAC(ctx)) ;
            maps.put("cancelamento", contaPedidoItemCancelamento.getJSON().toString()) ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fCancelarPedidoItem).getUrl() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erro(0, e.getMessage()) ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(0, e.getMessage()) ;
        } catch (JSONException e) {
            e.printStackTrace();
            erro(0, e.getMessage()) ;
        }
    }

    public void execute(){
        if (Configuracao.getPedidoCompartilhado(ctx)) {
            this.execute() ;
        } else {
            Dao.getContaPedidoItemCancelamentoDAO(ctx).incluir(contaPedidoItemCancelamento);
            Log.i("execute", contaPedidoItemCancelamento.toString());
            Dao.getContaPedidoItemInternoDAO(ctx).cancelar(contaPedidoItemCancelamento.getContaPedidoItem().getId());
            Ok();
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("cancelamento", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
               // Caixa caixa = new Caixa(j.getJSONObject("data")) ;
               //  Dao.getCaixaADO(ctx).caixa_recebido(caixa.getId(), caixa.getGerezim_id());
                Ok() ;
            } else {
                erro(codInt, j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, s) ;
            //  Toast.makeText(ctx, "JSON "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    public abstract void Ok() ;
    public abstract void erro(int cod, String msg) ;
}
