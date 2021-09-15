package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.entity.Impressora;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.impressao.RelatorioA7;
import com.anequimplus.tipos.Link;
import com.anequimplus.tipos.TipoImpressora;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoEnvioPedido extends ConexaoServer {

    private ListerConexao listerConexao ;


    public ConexaoEnvioPedido(Context ctx) throws Exception {
        super(ctx);
        msg = "Enviando Pedido" ;
        JSONArray jr = Dao.getContaPedidoADO(ctx).getListEnvio() ;
        if (jr.length() == 0) throw new Exception("Nenhum Pedido Para Enviar");
        maps.put("class","AfoodContaPedidoItem") ;
        maps.put("method","incluir") ;
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        maps.put("pedidos", jr) ;
        Log.e("pedidos", jr.toString());
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fIncluirPedido).getUrl();

        //setParametros() ;
        //setListeners();
    }

    private void setListeners() {
       listerConexao = new ListerConexao() {
           @Override
           public void resposta(JSONObject dados) {
               Dao.getPedidoADO(ctx).delete();
      //         ErroImpressao(1,"Impressão OK") ;
               //verificaImpressao(dados) ;
           }

           @Override
           public void erroMsg(String msg) {
               ErroEnvio(msg) ;
           }
       };
    }

    private void verificaImpressao(JSONObject dados) {
        try {
            JSONArray ja = dados.getJSONArray("PEDIDOS") ;
            for (int i = 0 ; i < ja.length() ; i++) {
                JSONObject it = ja.getJSONObject(i);
                String pedido = it.getString("PEDIDO") ;
                JSONArray jImp = it.getJSONArray("IMPRESSAO") ;
                for (int x = 0 ; x < jImp.length() ; x++) {
                    JSONObject itemImpressao = jImp.getJSONObject(x);
                    imprimir(new Impressora(itemImpressao.getJSONObject("IMPRESSORA")), getList(itemImpressao.getJSONArray("RELATORIO"))) ;
                    Log.i("verificaImpressao "+x, jImp.getJSONObject(x).toString()) ;
                }
            }
          //  EnvioOK(1,"Impressão OK") ;
        } catch (JSONException e) {
            e.printStackTrace();
          //  EnvioOK(2,"Envio OK, Porém Erro na Impressão") ;

        }
    }

    private void imprimir(Impressora imp, List<RowImpressao> list){
        ListenerImpressao l = new ListenerImpressao() {
            @Override
            public void onImpressao(int status) {
    //            ErroImpressao(1,"Impressão OK") ;

            }

            @Override
            public void onError(int status, String messagem) {
  //              ErroImpressao(status,messagem) ;

            }
        };
       if (imp.getTipoImpressora() == TipoImpressora.tpIV7){
           new RelatorioA7(ctx,list,l).executar();
       }

    }

    private List<RowImpressao> getList(JSONArray ja){
        List<RowImpressao> row = new ArrayList<RowImpressao>() ;
        try {
        for (int x = 0 ; x < ja.length() ; x++) {
                row.add(new RowImpressao(ja.getJSONObject(x)));
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return row ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExecute_Envio",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                Dao.getPedidoADO(ctx).delete();
                EnvioOK(j.getJSONArray("data")) ;
            } else ErroEnvio(j.getString("data")) ;
        } catch (Exception e) {
            e.printStackTrace();
            ErroEnvio(e.getMessage()) ;
        }
    }
    public abstract void EnvioOK(JSONArray jr) throws Exception;
    public abstract void ErroEnvio(String msg) ;
//    public abstract void ErroImpressao(int status, String msg) ;
}
