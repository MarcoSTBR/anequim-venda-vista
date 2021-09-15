package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.OpcoesFechamento;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

public abstract class ConexaoOpFechamento extends ConexaoServer {

    private Caixa caixa ;

    public ConexaoOpFechamento(Context ctx, Caixa caixa) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, MalformedURLException {
        super(ctx);
        msg = "Cosultar Opçoes" ;

        //setParamentros() ;
        this.caixa = caixa ;
        maps.put("class", "AfoodOpcoesFechamento") ;
        maps.put("method", "consultar") ;
        maps.put("system_user_id", UtilSet.getId_Usuario(ctx)) ;
        maps.put("chave", UtilSet.getChave(ctx)) ;
        maps.put("loja_id", UtilSet.getLojaId(ctx)) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaOpFechamento).getUrl() ;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.i("OPCOES", s) ;
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                Dao.getOpcoesFechamentoADO(ctx).opcoesADD(j.getJSONArray("data"));
                oK(caixa, Dao.getOpcoesFechamentoADO(ctx).getList()) ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public abstract void oK(Caixa caixa, List<OpcoesFechamento> list) ;
    public abstract void erro(String msg) ;
}
