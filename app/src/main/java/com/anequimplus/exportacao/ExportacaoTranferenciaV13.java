package com.anequimplus.exportacao;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExportacaoTranferenciaV13 extends ConexaoExportacao implements ParamExportacao{


    private List<Transferencia> list ;
    private ListenerExportacao listenerExportacao ;

    public ExportacaoTranferenciaV13(Activity ctx, RegExport regExport, ListenerExportacao listenerExportacao) {
        super(ctx, regExport, listenerExportacao);
        this.listenerExportacao = listenerExportacao ;
    }


    @Override
    public String getClasse() {
        return "AfoodGPedidoTransferencia";
    }

    @Override
    public String getMethod() {
        return "atualizar";
    }

    @Override
    public String getNomeParam() {
        return "transferencias" ;
    }

    @Override
    public void executar() {
        super.execute() ;
    }

    @Override
    public JSONArray getDados() {
        list = DaoDbTabela.getTransferenciaDAO(ctx).getList(new FilterTables(), "") ;
        JSONArray j = new JSONArray() ;
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //"yyyy-MM-dd HH:mm:ss"
        try {
            for (Transferencia t : list){
                JSONObject js = t.getExportacaoJSON() ;
                js.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
                js.put("LOCAL_ID", UtilSet.getLojaId(ctx)) ;
                //js.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
                ContaPedido origem  = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(t.getContaPedido_origem_id()) ;
                ContaPedido destino = DaoDbTabela.getContaPedidoInternoDAO(ctx).get(t.getContaPedido_destino_id()) ;
                ContaPedidoItem item = DaoDbTabela.getContaPedidoItemInternoDAO(ctx).get(t.getContaPedidoItem_id()) ;
                js.put("PEDIDO_ORIGEM", origem.getPedido()) ;
                js.put("DATASEQ_ORIGEM", dt.format(origem.getData())) ;
                js.put("PEDIDO", destino.getPedido()) ;
                js.put("DATASEQUENCIAL", dt.format(destino.getData())) ;
                js.put("PRODUTO_ID", item.getProduto().getId()) ;
                js.put("DESCRICAO", item.getProduto().getDescricao()) ;
                js.put("USUARIO", UtilSet.getLogin(ctx)) ;
                js.put("AUTORIZACAO", UtilSet.getLogin(ctx)) ;
                js.put("CONTA_PEDIDO_ORIGEM_UUID", origem.getUuid()) ;
                js.put("CONTA_PEDIDO_DESTINO_UUID", destino.getUuid()) ;
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
