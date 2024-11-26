package com.anequimplus.nfce;

import android.app.Activity;

import com.anequimplus.conexoes.ConexaoServer;
import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.utilitarios.Configuracao;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoImprimirNFCeServerLocal extends ConexaoServer {

    private ContaPedidoNFCe contaPedidoNFCe ;
    private ListenerImprimirNFCe listenerImprimirNFCe ;

    public ConexaoImprimirNFCeServerLocal(Activity ctx, ContaPedidoNFCe contaPedidoNFCe, ListenerImprimirNFCe listenerImprimirNFCe)  throws MalformedURLException , JSONException {
        super(ctx);
        this.contaPedidoNFCe = contaPedidoNFCe;
        this.listenerImprimirNFCe = listenerImprimirNFCe;
        maps.put("data", getJSON()) ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/emitir_nfce") ;
    }

    private JSONObject getJSON() throws JSONException{
        JSONObject j = new JSONObject();
        j.put("REQ", "LER_XML_NFCE_API");
        j.put("CHAVE", contaPedidoNFCe.getChave());
        return j ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                JSONObject js = j.getJSONObject("data");
                if (js.getString("RESP").equals("OK")) {
                    listenerImprimirNFCe.ok(js.getString("XML"));
                } else listenerImprimirNFCe.erro(js.getString("MENSAGEM"));
            } else listenerImprimirNFCe.erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerImprimirNFCe.erro(e.getMessage()+"-"+s);
        }
    }
}
