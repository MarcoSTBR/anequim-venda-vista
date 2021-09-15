package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.PagamentoConta;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.Produto;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedidoADO {

    private Context ctx ;
    private static List<ContaPedido> list ;

    public ContaPedidoADO(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<ContaPedido>() ;
    }

    public List<ContaPedido> getList(){
        return list ;
    }

    public void contaPedidoAdd(JSONArray contas) throws JSONException {
        list.clear();
        for (int i = 0 ; i < contas.length() ; i++){
            JSONObject j = contas.getJSONObject(i);
            ContaPedido c = null ;
            int id =  j.getInt("id");
            String ped = j.getString("PEDIDO");
            Date data = UtilSet.getData(j.getString("DATA")) ;
            c = new ContaPedido(id, ped, data, j.getDouble("COMISSAO"), j.getDouble("DESCONTO"), new ArrayList<ContaPedidoItem>(), new ArrayList<PagamentoConta>());
            list.add(c);
            for (int ii = 0 ; ii < j.getJSONArray("ITENS").length() ; ii++){
                JSONObject jIt = j.getJSONArray("ITENS").getJSONObject(ii);
                Produto p = new Produto(jIt.getJSONObject("PRODUTO")) ;//Dao.getProdutoADO(ctx).getProdtoId(jIt.getInt("PRODUTO_ID")) ;
                c.getListContaPedidoItem().add(Dao.getContaPedidoItemADO(ctx).contaPedidoItemAdd(c, p, jIt)) ;
            }
            for (int ii = 0 ; ii < j.getJSONArray("PAGAMENTOS").length() ; ii++) {
                JSONObject jItp = j.getJSONArray("PAGAMENTOS").getJSONObject(ii);
                c.getListPagamento().add(Dao.getPagamentoContaADO(ctx).pagamentoADD(c, jItp));
            }
        }
    }

    public ContaPedido getContaPedido(String pedido) {
        ContaPedido c = null ;
        for (ContaPedido cp : getList()){
            if (cp.getPedido().equals(pedido)){
                c = cp ;
            }
        }
        return c ;
    }

    public ContaPedido getContaPedido(int id) {
        ContaPedido c = null ;
        for (ContaPedido cp : getList()){
            if (cp.getId() == id){
                c = cp ;
            }
        }
        return c ;
    }

    public JSONArray getListEnvio() throws JSONException {
        JSONArray js = new JSONArray( );
        List<Pedido> ls = Dao.getPedidoADO(ctx).getList() ;
        for (Pedido p : ls) {
            js.put(p.getJSon()) ;
        }
        return js ;
    }
}
