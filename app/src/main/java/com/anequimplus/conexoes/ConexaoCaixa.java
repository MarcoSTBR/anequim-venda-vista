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

public abstract class ConexaoCaixa extends ConexaoServer {

   // private Caixa caixa ;

    public ConexaoCaixa(Context ctx, Caixa caixa)  {
        super(ctx);
        msg = "Atualizar Caixa";
//        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
          maps.put("class","AfoodCaixas") ;
          maps.put("method","atualizar") ;
          maps.put("MAC",UtilSet.getMAC(ctx)) ;
          maps.put("caixa", caixa.getJson().toString()) ;
          url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAtualizarCaixa).getUrl() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro("inicio "+e.getMessage());

        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Resp_Caixa", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                Caixa caixa = new Caixa(j.getJSONObject("data")) ;
                Dao.getCaixaADO(ctx).caixa_recebido(caixa.getId(), caixa.getGerezim_id());
                Ok(caixa) ;
            } else {
                erro(j.getString("data"));
            }
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

    public abstract void Ok(Caixa caixa);
    public abstract void erro(String msg);

}
