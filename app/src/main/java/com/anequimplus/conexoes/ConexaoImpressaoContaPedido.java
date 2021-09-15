package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.Impressora;
import com.anequimplus.impressao.ImpressaoA7;
import com.anequimplus.impressao.ImpressaoLio;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.tipos.Link;
import com.anequimplus.tipos.TipoImpressora;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoImpressaoContaPedido extends ConexaoServer {

    private Impressora impressora ;
//    private ListenerImpressao listenerImpressao ;

    public ConexaoImpressaoContaPedido(Context ctx, Impressora impressora, List<ContaPedido> listContapedido) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, JSONException, MalformedURLException {
        super(ctx);
        //this.listenerImpressao = listenerImpressao ;
        msg = "Impressão Conta Pedido" ;

        this.impressora = impressora ;

        maps.put("class", "AfoodContaPedido") ;
        maps.put("method", "imprimir") ;
        maps.put("chave", UtilSet.getChave(ctx)) ;
        maps.put("loja_id", UtilSet.getLojaId(ctx)) ;
        maps.put("system_user_id", UtilSet.getId_Usuario(ctx)) ;
        maps.put("impressora_id", impressora.getId()) ;
        maps.put("pedidos", getPedidos(listContapedido)) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fImprimirConta).getUrl() ;
    }

    private JSONArray getPedidos(List<ContaPedido> listContapedido) throws JSONException {
        JSONArray ja = new JSONArray();
        for (ContaPedido c : listContapedido){
            //JSONObject j = new JSONObject() ;
            //j.put("PEDIDO",c.getPedido()) ;
            ja.put(c.getId());
        }
        return ja ;
    }

    private void imprimirRelatorio(List<RowImpressao> list) {
     ListenerImpressao listenerImpressao = new ListenerImpressao() {
           @Override
           public void onImpressao(int status) {
               oK();
           }

           @Override
           public void onError(int status, String messagem) {
              erro(messagem);
           }
       };
       if (impressora.getTipoImpressora() != TipoImpressora.tpILocal) {
           if (impressora.getTipoImpressora() == TipoImpressora.tpILio) {
               new ImpressaoLio(ctx, listenerImpressao, list) ;
           }
           if (impressora.getTipoImpressora() == TipoImpressora.tpIV7) {
               new ImpressaoA7(ctx, listenerImpressao,list) ;
           }
       } else listenerImpressao.onImpressao(1); ;
    }

    private List<RowImpressao> formarLinhas(JSONArray j) throws JSONException {
        List<RowImpressao> list = new ArrayList<RowImpressao>() ;
        for (int i=0 ; i < j.length() ; i++ ){
                list.add(new RowImpressao(j.getJSONObject(i)));
        }
        return list ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExeImpressaoConta",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                imprimirRelatorio(formarLinhas(j.getJSONArray("data"))) ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }

    }

    public abstract void oK() ;
    public abstract void erro(String mgg) ;

}
