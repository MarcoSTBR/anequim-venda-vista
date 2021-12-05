package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Terminal;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoConfTerminal extends ConexaoServer {

    private Link link ;

    public ConexaoConfTerminal(Context ctx, String mac) {
        super(ctx);
        link = Link.fConsultaTerminal ;
        try {
            msg = "Consultando Terminal..." ;
            maps.put("class","AfoodConf_Terminal") ;
            maps.put("method","consultar") ;
            maps.put("MAC",mac) ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(link).getUrl();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erroMsg(0, "Não encontrou o link do terminal");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erroMsg(0, "Url inválida!");
        }
    }

    public ConexaoConfTerminal(Context ctx, Terminal t)  {
        super(ctx);
        link = Link.fAtualizaTerminal ;
        try {
            msg = "Atualizando Terminal..." ;
            maps.put("class","AfoodConf_Terminal") ;
            maps.put("method","atualizar") ;
            maps.put("terminal",  t.getJson().toString()) ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(link).getUrl();
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erroMsg(0, "Não encontrou o link do terminal");
        } catch (MalformedURLException  e) {
            e.printStackTrace();
            erroMsg(0, "Url inválida!");
        } catch (JSONException e) {
            e.printStackTrace();
            erroMsg(0, "Erro ao converter dados!");
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("onPostExTerminal", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                Terminal t = new Terminal(j.getJSONObject("data")) ;
                UtilSet.setTerminalId(ctx, t.getId() );
                ok(t) ;
                //ok(j.getJSONObject("data").getInt("id"), j.getJSONObject("data").getString("DESCRICAO"));
            } else {
                erroMsg(codInt, j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erroMsg(codInt, s) ;
        }

    }

   public abstract void ok(Terminal t) ;
   public abstract void erroMsg(int cod, String msg) ;
}
