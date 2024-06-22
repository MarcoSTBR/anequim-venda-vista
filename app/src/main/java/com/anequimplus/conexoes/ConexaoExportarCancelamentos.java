package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoExportarCancelamentos extends ConexaoServer{

   private List<ContaPedido> contaPedidoList ;
   private List<ContaPedidoItemCancelamento> listContaPedidocancelamento ;
   private List<ContaPedidoItemCancelamento> listCancelamentoEnviados ;

   public ConexaoExportarCancelamentos(Context ctx, List<ContaPedidoItemCancelamento> listContaPedidocancelamento, List<ContaPedido> contaPedidoList) throws MalformedURLException {
     super(ctx);
     this.listContaPedidocancelamento = listContaPedidocancelamento ;
     this.contaPedidoList = contaPedidoList ;
     msg = "Cancelamentos...";
     maps.put("class","AfoodContaPedidoCancel") ;
     maps.put("method","atualizar") ;
     maps.put("MAC", UtilSet.getMAC(ctx)) ;
     listCancelamentoEnviados = new ArrayList<ContaPedidoItemCancelamento>() ;
     url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAtualizarCancelamentoConta).getUrl() ;
    }

    public void executar(){
        try {
            maps.put("cancelamentos", getArrayJson(listContaPedidocancelamento)) ;
            super.execute() ;
        } catch (Exception e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }
    }


    private JSONArray getArrayJson(List<ContaPedidoItemCancelamento> l) throws Exception {
        JSONArray j = new JSONArray() ;
        for (ContaPedidoItemCancelamento it : l){
            JSONObject obj = it.getExpJSON() ;
            obj.put("LOJA_ID",UtilSet.getLojaId(ctx));
            ContaPedidoItem item = getContaPedidoItem(it.getContaPedidoItem_id()) ;
            if (item == null) throw new Exception("ID "+it.getContaPedidoItem_id()+" do Item não encontrado!") ;
            obj.put("CONTA_PEDIDO_ITEM_UUID", item.getUUID()) ;
            listCancelamentoEnviados.add(it) ;
            j.put(obj) ;
        }
        return j;
    }

    private ContaPedidoItem getContaPedidoItem(int id){
       ContaPedidoItem it = null ;
       for (ContaPedido cp : contaPedidoList){
           for (ContaPedidoItem i : cp.getListContaPedidoItem()){
               if (i.getId() == id)
                   it = i ;
           }
       }
       return it ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("cancelamento", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                ok(listCancelamentoEnviados) ;
            } else erro(codInt, j.getString("data"));

        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, s) ;
            //  Toast.makeText(ctx, "JSON "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void ok(List<ContaPedidoItemCancelamento> l) ;
    public abstract void erro(int cod, String msg) ;

}
