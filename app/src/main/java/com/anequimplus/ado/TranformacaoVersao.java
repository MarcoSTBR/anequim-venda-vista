package com.anequimplus.ado;

import com.anequimplus.entity.ContaPedido;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public interface TranformacaoVersao {

   public List<ContaPedido> getContas(JSONArray data) throws Exception;


}
