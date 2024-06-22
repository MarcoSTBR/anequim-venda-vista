package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.conexoes.ConexaoEnvioContaPedidoComandaRemota;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.listeners.ListenerEnvioComandaRemota;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BuildEnvioComandaRemota {

    private Context ctx ;
    private List<Pedido> list ;
    private ListenerEnvioComandaRemota listenerEnvioComandaRemota ;
    private int cancelamento ;

    public BuildEnvioComandaRemota(Context ctx, List<Pedido> list, int cancelamento, ListenerEnvioComandaRemota listenerEnvioComandaRemota) {
        this.ctx = ctx;
        this.list = list;
        this.cancelamento = cancelamento ;
        this.listenerEnvioComandaRemota = listenerEnvioComandaRemota ;
    }

    public void executar(){
        try {
           if (Configuracao.getSeComandaRemota(ctx)) {
               new ConexaoEnvioContaPedidoComandaRemota(ctx, getJSONArray(), listenerEnvioComandaRemota).execute();
           } listenerEnvioComandaRemota.ok("OK");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerEnvioComandaRemota.erro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEnvioComandaRemota.erro(e.getMessage());
        }
    }

    private JSONArray getJSONArray() throws JSONException {
        JSONArray jaa = new JSONArray();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        Date d = new Date() ;
        int idterm = UtilSet.getTerminalId(ctx) ;
        String garcon = UtilSet.getUsuarioNome(ctx);//UtilSet.getLogin(ctx) ;
        for (Pedido p : list){
            JSONObject item = new JSONObject();
            item.put("PEDIDO", p.getPedido()) ;
            item.put("DATA", dt.format(d)) ;
            item.put("GARCON", garcon) ;
            item.put("STATUS_IMP_REMOTA", 1) ;
            item.put("STATUS_PAINEL", 1) ;
            item.put("CONF_TERMINAL_ID", idterm) ;
            item.put("TIPO_PEDIDO", 1) ;
            item.put("CANCELAMENTO", cancelamento) ;
            item.put("UUID", UtilSet.getUUID()) ;
            JSONArray itens = new JSONArray() ;
            for (PedidoItem it : p.getListPedidoItem()){
                JSONObject itped = new JSONObject() ;
                JSONArray acomp = new JSONArray() ;
                itped.put("PRODUTO", it.getItemSelect().getProduto().getJSONRemoto()) ;
                itped.put("PEDIDO", p.getPedido()) ;
                itped.put("DATA",dt.format(d)) ;
                itped.put("QUANTIDADE", it.getItemSelect().getQuantidade()) ;
                itped.put("PRECO", it.getItemSelect().getPreco()) ;
                itped.put("OBS", it.getItemSelect().getObs()) ;
                itped.put("UUID", UtilSet.getUUID()) ;
                for (PedidoItemAcomp itac : it.getAcompanhamentos()){
                    JSONObject ac = new JSONObject() ;
                    ac.put("PEDIDO_ITEM", it.getId()) ;
                    ac.put("PRODUTO", itac.getItemSelect().getProduto().getJSONRemoto()) ;
                    ac.put("DATA",dt.format(d)) ;
                    ac.put("QUANTIDADE", itac.getItemSelect().getQuantidade()) ;
                    ac.put("PRECO", itac.getItemSelect().getPreco()) ;
                    ac.put("OBS", itac.getItemSelect().getObs()) ;
                    ac.put("UUID", UtilSet.getUUID()) ;
                    acomp.put(ac) ;
                }
                itped.put("ACOMPANHAMENTOS", acomp) ;
                itens.put(itped) ;
            }
            item.put("JSON", itens.toString()) ;
            jaa.put(item) ;
        }
        return jaa ;
    }
}
