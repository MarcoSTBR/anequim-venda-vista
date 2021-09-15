package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Caixa;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ConexaoCaixa extends ConexaoServer {

    private Link link = null ;
    private ListerConexao listerConexao ;
    private Caixa caixa ;


    public ConexaoCaixa(Context ctx, Link link, double valor) throws LinkAcessoADO.ExceptionLinkNaoEncontrado, MalformedURLException {
        super(ctx);
        this.link = link ;
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        maps.put("class","AfoodCaixas") ;
        switch(link) {
            case fAberturaCaixa   : maps.put("method","abertura") ;
                                    maps.put("valor", valor) ;
                                    maps.put("data", fdate.format(new Date())) ;
                                    maps.put("modalidade_id", 1) ;
                                    url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAberturaCaixa).getUrl() ;
                                    msg = "Abertura Caixa";
                                    setParametrosAbertura();
                                    break;
            case fConsultaCaixa   : maps.put("method","consultar") ;
                                    url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaCaixa).getUrl() ;
                                    msg = "Consultando Caixa";
                                    setParametrosConsulta();
                                    break;
            case fFechamentoCaixa : maps.put("method","fechamento") ;
                                    url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fFechamentoCaixa).getUrl() ;
                                    msg = "Fechando o Caixa";
                                    setParametrosFechamento();
                                    break ;
            default: throw new LinkAcessoADO.ExceptionLinkNaoEncontrado(link);
        }
        maps.put("chave",UtilSet.getChave(ctx)) ;
        maps.put("system_user_id",UtilSet.getId_Usuario(ctx)) ;
        maps.put("loja_id", UtilSet.getLojaId(ctx)) ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;

    }


    private void setParametrosConsulta()  {
        listerConexao = new ListerConexao() {
            @Override
            public void resposta(JSONObject dados) {
                try {
                    if (dados.getJSONArray("data").length() > 0) {
                        JSONObject j = (JSONObject) dados.getJSONArray("data").get(0) ;
                        caixa = new Caixa(j) ;
                        Dao.getCaixaADO(ctx).setCaixa(caixa);
                        caixaAberto(Dao.getCaixaADO(ctx).getCaixa());
                    } else {
                        caixaFechado("Nenhum Caixa Aberto!");
                    }

                } catch (ParseException | JSONException e) {
                    e.printStackTrace();
                    erro(e.getMessage());
                }
            }

            @Override
            public void erroMsg(String msg) {
                Dao.getCaixaADO(ctx).setCaixa(null);
                erro(msg);
            }
        };
    };

    private void setParametrosAbertura() {
        listerConexao = new ListerConexao() {
            @Override
            public void resposta(JSONObject dados) {
                try {
                    caixa = new Caixa(dados.getJSONObject("data")) ;
                    Dao.getCaixaADO(ctx).setCaixa(caixa);
                    caixaAberto(Dao.getCaixaADO(ctx).getCaixa());
                } catch (ParseException | JSONException e) {
                    e.printStackTrace();
                    erro(e.getMessage());
                }
            }

            @Override
            public void erroMsg(String msg) {
                Dao.getCaixaADO(ctx).setCaixa(null);
                erro(msg);
            }
        };
    }

    private void setParametrosFechamento()  {
        listerConexao = new ListerConexao() {
            @Override
            public void resposta(JSONObject dados) {
                Dao.getCaixaADO(ctx).setCaixa(null);
                try {
                    JSONObject j = dados.getJSONObject("data") ;
                    caixaFechado("Caixa Fechado");
                } catch (JSONException e) {
                    e.printStackTrace();
                    erro(e.getMessage());
                }
            }

            @Override
            public void erroMsg(String msg) {
                Dao.getCaixaADO(ctx).setCaixa(null);
                erro(msg);

            }
        };

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Resp_Caixa", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listerConexao.resposta(j);
            } else {
                listerConexao.erroMsg(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listerConexao.erroMsg(e.getMessage());
        }

    }
    public abstract void caixaAberto(Caixa caixa);
    public abstract void caixaFechado(String msg);
    public abstract void erro(String msg);

}
