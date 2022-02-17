package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Caixa;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoCaixa extends ConexaoServer {

   // private Caixa caixa ;

    public ConexaoCaixa(Context ctx, List<Caixa> caixas) throws MalformedURLException, LinkAcessoADO.ExceptionLinkNaoEncontrado {
        super(ctx);
        msg = "Atualizar Caixa";
        maps.put("class","AfoodCaixas") ;
        maps.put("method","atualizar") ;
        maps.put("MAC",UtilSet.getMAC(ctx)) ;
        maps.put("caixas", getJson(caixas)) ;
        url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAtualizarCaixa).getUrl() ;
    }

    private JSONArray getJson(List<Caixa> caixas){
        JSONArray jaa = new JSONArray();
        for (Caixa c : caixas){
            jaa.put(c.getJson());
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
               List<Caixa> l = new ArrayList<Caixa>() ;
               for (int i=0; i < j.getJSONArray("data").length() ; i++)
                 l.add(new Caixa(j.getJSONArray("data").getJSONObject(i))) ;
               Ok(l);
            } else erro(j.getString("data"));

        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage()) ;
          //  Toast.makeText(ctx, "JSON "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
            erro(e.getMessage()) ;
        //    Toast.makeText(ctx, "PARSE "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void Ok(List<Caixa> l);
    public abstract void erro(String msg);

}
