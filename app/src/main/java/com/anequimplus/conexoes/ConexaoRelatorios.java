package com.anequimplus.conexoes;

import android.content.Context;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Impressora;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoRelatorios extends ConexaoServer {

    private List<RowImpressao> list ;

    public ConexaoRelatorios(Context ctx, Impressora impressora, int caixa_id, int opcao_id) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Relatório" ;

        maps.put("class","AfoodOpcoesFechamento") ;
        maps.put("method","obterOpRelatorio") ;
        maps.put("chave", UtilSet.getChave(ctx)) ;
        maps.put("loja_id",UtilSet.getLojaId(ctx)) ;
        maps.put("impressora_id",impressora.getId()) ;
        maps.put("caixa_id",caixa_id) ;
        maps.put("opcao_id",opcao_id) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fImprimirOpFechamento).getUrl() ;
        list = new ArrayList<RowImpressao>() ;
    }


    public List<RowImpressao> getLinhas(){
        return list ;
    }


    private void formarLinhas(JSONArray j) {
        try {
            for (int i=0 ; i < j.length() ; i++ ){
                list.add(new RowImpressao(j.getJSONObject(i)));
            }
            Ok(list) ;
        } catch (JSONException e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        }
    }

    public String getStringLinhas(){
        String txt = "" ;
        for (RowImpressao l : list){
            txt = txt + l.getLinha() + "\n" ;
        }
        return  txt ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                formarLinhas(j.getJSONArray("data"));
            } else erroMensagem(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erroMensagem(e.getMessage());
        }

    }


    public abstract void Ok(List<RowImpressao> l) ;
    public abstract void erroMensagem(String msg) ;
}
