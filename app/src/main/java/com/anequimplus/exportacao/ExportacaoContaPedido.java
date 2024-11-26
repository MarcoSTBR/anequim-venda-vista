package com.anequimplus.exportacao;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExportacaoContaPedido extends ConexaoExportacao implements ParamExportacao{


    private List<ContaPedido> list ;
    private ListenerExportacao listenerExportacao ;

    public ExportacaoContaPedido(Activity ctx, RegExport regExport, ListenerExportacao listenerExportacao) {
        super(ctx, regExport, listenerExportacao);
        this.listenerExportacao = listenerExportacao ;
    }


    @Override
    public String getClasse() {
        return "AfoodContaPedido";
    }

    @Override
    public String getMethod() {
        return "atualizar";
    }

    @Override
    public String getNomeParam() {
        return "contas" ;
    }

    @Override
    public void executar() {
        super.execute() ;
    }

    @Override
    public JSONArray getDados() {
        list = DaoDbTabela.getContaPedidoInternoDAO(ctx).getList(new FilterTables().getList(), "") ;
        JSONArray j = new JSONArray() ;
        SimpleDateFormat ds = new SimpleDateFormat("yyyyMMddHHmmss") ;
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //"yyyy-MM-dd HH:mm:ss"
        try {
            for (ContaPedido c : list){
                JSONObject js = c.getExportarJSON() ;
                js.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
                js.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
                for (int i = 0 ; i < js.getJSONArray("ITENS").length() ; i++) {
                    js.getJSONArray("ITENS").getJSONObject(i).put("CONTA_PEDIDO_UUID", c.getUuid()) ;
                }
                for (int i = 0 ; i < js.getJSONArray("PAGAMENTOS").length() ; i++) {
                    js.getJSONArray("PAGAMENTOS").getJSONObject(i).put("CONTA_PEDIDO_UUID", c.getUuid()) ;
                    int id = js.getJSONArray("PAGAMENTOS").getJSONObject(i).getInt("CAIXA_ID") ;
                    Caixa cx = DaoDbTabela.getCaixaADO(ctx).getUUID(id) ;
                    if (cx == null)
                        js.getJSONArray("PAGAMENTOS").getJSONObject(i).put("CAIXA_UUID", String.valueOf(cx.getId())) ;
                     else js.getJSONArray("PAGAMENTOS").getJSONObject(i).put("CAIXA_UUID", cx.getUuid()) ;
                }
                j.put(js) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerExportacao.erro(getRegExport(), 0, e.getMessage());
        }
        return j ;
    }

}
