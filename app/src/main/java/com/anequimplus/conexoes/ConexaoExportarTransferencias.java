package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoExportarTransferencias extends ConexaoServer{

    private List<ContaPedido> contaPedidoList ;
    private List<Transferencia> listTrasnferencias ;
    private List<Transferencia> listTrasnferenciaEnviados ;


    public ConexaoExportarTransferencias(Activity ctx, List<Transferencia> listTrasnferencias, List<ContaPedido> contaPedidoList) throws MalformedURLException {
        super(ctx);
        this.listTrasnferencias = listTrasnferencias ;
        this.contaPedidoList = contaPedidoList ;
        msg = "Transferencias...." ;
        maps.put("class","AfoodContaPedidoTrans") ;
        maps.put("method","atualizar") ;
        maps.put("MAC", UtilSet.getMAC(ctx)) ;
        listTrasnferenciaEnviados = new ArrayList<Transferencia>() ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fTransferencia).getUrl() ;
    }

    public void executar(){
        try {
            maps.put("transferencias", getTransferencias()) ;
            super.execute() ;
        } catch (JSONException e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }
    }


    private JSONArray getTransferencias() throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (Transferencia t : listTrasnferencias){
            JSONObject obj = t.getEXPJSON() ;
            obj.put("LOJA_ID", UtilSet.getLojaId(ctx));
            ContaPedidoItem item = getItem(t.getContaPedidoItem_id()) ;
            ContaPedido origem   = getConta(t.getContaPedido_origem_id()) ;
            ContaPedido destino  = getConta(t.getContaPedido_destino_id()) ;
            if (verificar(item, origem, destino)) {
                obj.put("CONTA_PEDIDO_ITEM_UUID", item.getUUID()) ;
                obj.put("CONTA_PEDIDO_ORIGEM_UUID", origem.getUuid()) ;
                obj.put("CONTA_PEDIDO_DESTINO_UUID", destino.getUuid()) ;
                listTrasnferenciaEnviados.add(t);
                jaa.put(obj);
            }
        }
        Log.i("transferencia", jaa.toString()) ;
        return jaa;
    }

    private boolean verificar(ContaPedidoItem item, ContaPedido origem, ContaPedido destino) {
        Boolean flag = true ;

        if (item == null){
            flag = false ;
            mensagem("Item não Encontrado!") ;
        }
        if (origem == null){
            flag = false ;
            mensagem("Conta de Origem não Encontrada!") ;
        }
        if (destino == null){
            flag = false ;
            mensagem("Conta de Destino não Encontrada!") ;
        }

        return flag ;
    }

    private ContaPedidoItem getItem(int id){
        ContaPedidoItem it = null ;
        for (ContaPedido cp : contaPedidoList){
            for (ContaPedidoItem i : cp.getListContaPedidoItem()){
                if (i.getId() == id)
                    it = i ;
            }
        }
        return it ;
    }

    private ContaPedido getConta(int id){
        ContaPedido it = null ;
        for (ContaPedido cp : contaPedidoList){
            if (cp.getId() == id)
                it = cp ;
        }
        return it ;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("transferencias", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                ok(listTrasnferenciaEnviados) ;
            } else erro(codInt, j.getString("data"));

        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, s) ;
            //  Toast.makeText(ctx, "JSON "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void ok(List<Transferencia> l) ;
    public abstract void erro(int cod, String msg) ;
    public abstract void mensagem(String msg) ;
}
