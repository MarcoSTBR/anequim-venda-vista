package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoAutenticacao extends ConexaoServer {

    private String cnpj ;

    public ConexaoAutenticacao(Context ctx ,String cnpj) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        this.cnpj = cnpj ;
        maps.put("class","AfoodAutenticacao") ;
        maps.put("method","autenticacao") ;
        maps.put("cnpj",cnpj) ;
        msg = "Autenticação" ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAutenticacao).getUrl();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("ConexaoAutenticacarest",s) ;
        try {
            JSONObject dados = new JSONObject(s);
            if (dados.getString("status").equals("success"))
                {
                    if (dados.getJSONArray("data").length() > 0) {
                        String chv = dados.getJSONArray("data").getJSONObject(0).getString("CHAVE");
                        int status = dados.getJSONArray("data").getJSONObject(0).getInt("STATUS");
                        UtilSet.setChave(ctx, chv);
                          if (status == 1) {
                            UtilSet.setCnpj(ctx, cnpj);
                            oK(codInt);
                          } else erro(codInt,"Entre em contato com o administrador!");
                        } else {
                            UtilSet.setChave(ctx, "");
                            erro(codInt,"Autenticação não encontrada!");
                        }
                } else erro(codInt, dados.getString("data"));
            } catch (JSONException e) {
                e.printStackTrace();
                erro(codInt, e.getMessage());
            }
    }

    public abstract void oK(int cod) ;
    public abstract void erro(int cod, String msg) ;

}
