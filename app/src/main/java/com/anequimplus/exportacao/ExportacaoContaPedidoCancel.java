package com.anequimplus.exportacao;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoItemCancelamento;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ExportacaoContaPedidoCancel extends ConexaoExportacao implements ParamExportacao{


    private List<ContaPedidoItemCancelamento> list ;
    private ListenerExportacao listenerExportacao ;

    public ExportacaoContaPedidoCancel(Context ctx, RegExport regExport, ListenerExportacao listenerExportacao) {
        super(ctx, regExport, listenerExportacao);
        this.listenerExportacao = listenerExportacao ;
    }


    @Override
    public String getClasse() {
        return "AfoodContaPedidoCancel";
    }

    @Override
    public String getMethod() {
        return "atualizar";
    }

    @Override
    public String getNomeParam() {
        return "cancelamentos" ;
    }

    @Override
    public void executar() {
        super.execute() ;
    }

    @Override
    public JSONArray getDados() {
        list = DaoDbTabela.getContaPedidoItemCancelamentoDAO(ctx).getList(new FilterTables(), "") ;
        JSONArray j = new JSONArray() ;
        //SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //"yyyy-MM-dd HH:mm:ss"
        try {
            for (ContaPedidoItemCancelamento it : list){
                JSONObject js = it.getExportacaoJSON() ;
                js.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
                ContaPedidoItem item = DaoDbTabela.getContaPedidoItemInternoDAO(ctx).get(it.getContaPedidoItem_id()) ;
                js.put("CONTA_PEDIDO_ITEM_UUID", item.getUUID()) ;
                j.put(js) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerExportacao.erro(getRegExport(), 0, e.getMessage());
        }
        return j ;
    }

}
