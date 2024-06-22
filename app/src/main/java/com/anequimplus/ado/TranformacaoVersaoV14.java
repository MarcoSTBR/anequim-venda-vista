package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedido;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class TranformacaoVersaoV14 implements TranformacaoVersao {

   private Context ctx ;

   public TranformacaoVersaoV14(Context ctx) {
      this.ctx = ctx;
   }

   public List<ContaPedido> getContas(JSONArray data) throws JSONException {
      return DaoDbTabela.getContaPedidoInternoDAO(ctx).getJSON(data) ;
   }


}
