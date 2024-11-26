package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.entity.Caixa;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class ConexaoExportarCaixa extends ConexaoServer {

    private List<Caixa> list ;

    public ConexaoExportarCaixa(Activity ctx, List<Caixa> caixas) throws MalformedURLException {
        super(ctx);
        this.list = caixas ;
        msg = "Atualizar Caixa";
        maps.put("class","AfoodCaixas") ;
        maps.put("method","atualizar") ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("caixas", getJson(caixas)) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ;//DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAtualizarCaixa).getUrl() ;
    }

    private JSONArray getJson(List<Caixa> caixas){
        JSONArray jaa = new JSONArray();
        for (Caixa c : caixas){
            try {
                JSONObject obj = c.getExportacaoJson() ;
                obj.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
                obj.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
                obj.put("MODALIDADE_ID", 2) ;
                jaa.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("caixas", jaa.toString()) ;
        return jaa ;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Resp_Caixa", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
               ok(list);
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage()) ;
        }
    }

    public abstract void ok(List<Caixa> l);
    public abstract void erro(String msg);

}
