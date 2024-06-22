package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.OpcoesFechamento;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class ConexaoOpFechamento extends ConexaoServer {

    private Caixa caixa ;

    public ConexaoOpFechamento(Context ctx, Caixa caixa) throws  MalformedURLException {
        super(ctx);
        msg = "Consultando Opçoes" ;

        //setParamentros() ;
        this.caixa = caixa ;
        maps.put("class", "AfoodOpcoesFechamento") ;
        maps.put("method", "consultar") ;
        maps.put("system_user_id", UtilSet.getUsuarioId(ctx)) ;
        maps.put("loja_id", UtilSet.getLojaId(ctx)) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaOpFechamento).getUrl() ;
    }

    public void execute(){
/*
      if (Configuracao.getPedidoCompartilhado(ctx)){
          this.execute();
      } else {
*/
        DaoDbTabela.getOpcoesFechamentoADO(ctx).setMenuInterno();
        oK(caixa, DaoDbTabela.getOpcoesFechamentoADO(ctx).getList()) ;
/*
    }

*/

   }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.i("OPCOES", s) ;
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                DaoDbTabela.getOpcoesFechamentoADO(ctx).opcoesADD(j.getJSONArray("data"));
                oK(caixa, DaoDbTabela.getOpcoesFechamentoADO(ctx).getList()) ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public abstract void oK(Caixa caixa, List<OpcoesFechamento> list) ;
    public abstract void erro(String msg) ;
}
