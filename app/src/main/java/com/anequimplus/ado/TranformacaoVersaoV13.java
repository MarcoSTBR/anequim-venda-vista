package com.anequimplus.ado;

import android.content.Context;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.Produto;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TranformacaoVersaoV13 implements TranformacaoVersao {

   private Context ctx ;

   public TranformacaoVersaoV13(Context ctx) {
      this.ctx = ctx;
   }

   public List<ContaPedido> getContas(JSONArray data) throws Exception {
      List<ContaPedido> resp = new ArrayList<ContaPedido>();

      Log.i("V13",data.toString()) ;
      for (int i = 0 ; i < data.length() ; i++){
         JSONObject cp = data.getJSONObject(i) ;
         ContaPedido contaPedido = new ContaPedido(cp.getInt("ID"), UtilSet.getUUID(), cp.getString("PEDIDO"), UtilSet.getData(cp.getString("DATASEQUENCIAL")),
                 getValorJSON(cp.getString("EMISSAOCOMDESCONTO")),
                 new ArrayList<ContaPedidoItem>(), new ArrayList<ContaPedidoPagamento>(),1, cp.getInt("INCLACRESCIMO"),
                 cp.getInt("LIBERACAOPARARECEBIMENTO"), UtilSet.getData(cp.getString("DATAFECHAMENTO")), cp.getInt("PESSOAS") , cp.getInt("USUARIO")) ;
         for (int y = 0 ; y < cp.getJSONArray("ITENS").length() ; y++){
            JSONObject item = cp.getJSONArray("ITENS").getJSONObject(y) ;
            Produto prd  =  DaoDbTabela.getProdutoADO(ctx).getCodigo(item.getString("CODIGOPRODUTO")) ;
            if (prd == null) throw new Exception("Produto Código "+item.getString("CODIGOPRODUTO")+" Não Encontrado") ;
            ContaPedidoItem contaPedidoItem = new ContaPedidoItem(item.getInt("ID"), UtilSet.getData(cp.getString("DATASEQUENCIAL")), contaPedido.getId(),
                    item.getString("UUID"), prd, getValorJSON(item.getString("QUANTIDADE")),
                    getValorJSON(item.getString("PRECOUNITARIO")), getValorJSON(item.getString("DESCONTO")), getValorJSON(item.getString("COMISSAO")),
                    getValorJSON(item.getString("PRECOFINAL")),
                    item.getString("OBS"), 1, item.getInt("VENDEDOR")) ;
            contaPedido.getListContaPedidoItem().add(contaPedidoItem);
         }
         Log.i("getContas", contaPedido.getExportarJSON().toString()) ;
         resp.add(contaPedido) ;
      }

      return resp ;
   }

   private Double getValorJSON(String data){
      return Double.valueOf(data.replace(",", ".")) ;
   }


}
