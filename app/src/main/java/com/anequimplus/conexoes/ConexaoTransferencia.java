package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoTransferencia extends ConexaoServer{

    private Transferencia transferencia ;

    public ConexaoTransferencia(Context ctx, Transferencia transferencia) {
        super(ctx);
        this.transferencia = transferencia ;
        msg = "Transferindo ...." ;
        try {
            maps.put("class","AfoodTransferencia") ;
            maps.put("method","transferir") ;
            maps.put("MAC", UtilSet.getMAC(ctx)) ;
            maps.put("transferencia", transferencia.getJSON()) ;
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fTransferencia).getUrl() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erro(0,e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(0,"Url erro "+e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            erro(0,e.getMessage());
        }
    }

    public void execute(){
        if (Configuracao.getPedidoCompartilhado(ctx)) {
            super.execute() ;
        } else {
            Dao.getTransferenciaDAO(ctx).transferir(transferencia);
            ok() ;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("transferencia", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                ok() ;
            } else {
                erro(codInt, j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, s) ;
        }
    }

    public abstract void ok() ;
    public abstract void erro(int cod, String msg) ;

}
