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

public abstract class ComexaoImpressoraRemota extends ConexaoServer {

    public ComexaoImpressoraRemota(Context ctx, Link link, JSONArray jr) throws Exception {
        super(ctx);

        switch(link) {
            case fImpressoraRemotaPedido: maps.put("class", "AfoodImpressoraRemotaPedido") ;
                                          maps.put("method", "impressoes") ;
                                          maps.put("pedidos", jr);
                                          msg = "Impressão Remota" ;
                                          break;
            default: throw new Exception("Link não encontrado!") ;
        }
        maps.put("chave", UtilSet.getChave(ctx)) ;
        maps.put("loja_id", UtilSet.getLojaId(ctx)) ;
        maps.put("MAC", UtilSet.getMAC(ctx)) ;
        maps.put("system_user_id", UtilSet.getId_Usuario(ctx)) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(link).getUrl() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("ConexaoImp", s.toString()) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                verificaImpressao(j.getJSONArray("data")) ;
                oK(j.getJSONArray("data")) ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    private void verificaImpressao(JSONArray its) throws JSONException
    {
        Log.e("verificaImpressao", its.toString());
        for (int i = 0 ; i < its.length() ; i++) {
            JSONArray itss = its.getJSONArray(i);
            for (int ii = 0; ii < itss.length(); ii++) {
                JSONArray itsss = its.getJSONArray(ii);
                for (int x = 0; x < itsss.length(); x++) {
                    JSONObject j = itsss.getJSONObject(x);
                    String pedido = j.getString("PEDIDO");
//                JSONArray jImp = it.getJSONArray("IMPRESSAO") ;
                    imprimir(new Impressora(j.getJSONObject("IMPRESSORA")), getList(j.getJSONArray("RELATORIO")));
            }
          }
        }
    }

    private void imprimir(Impressora imp, List<RowImpressao> list){
        ListenerImpressao l = new ListenerImpressao() {
            @Override
            public void onImpressao(int status) {
                //            ErroImpressao(1,"Impressão OK") ;
                oK(null);



            }

            @Override
            public void onError(int status, String messagem) {
                //              ErroImpressao(status,messagem) ;

            }
        };
        if (imp.getTipoImpressora() == TipoImpressora.tpIV7) {
            new RelatorioA7(ctx, list, l).executar();
        }
    }



    private List<RowImpressao> getList(JSONArray ja) throws JSONException {
        List<RowImpressao> row = new ArrayList<RowImpressao>() ;
         for (int x = 0 ; x < ja.length() ; x++) {
                row.add(new RowImpressao(ja.getJSONObject(x)));
            }
        return row ;
    }

    public abstract void oK(JSONArray jrr) ;
    public abstract void erro(String mgg) ;

}
