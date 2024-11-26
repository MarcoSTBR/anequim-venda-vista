package com.anequimplus.exportacao;

import android.app.Activity;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExportacaoCabNotaSaida extends ConexaoExportacao implements ParamExportacao {

    private List<ContaPedido> list ;
    private ListenerExportacao listenerExportacao ;


    public ExportacaoCabNotaSaida(Activity ctx, RegExport regExport, ListenerExportacao listenerExportacao) {
        super(ctx, regExport, listenerExportacao);
        this.listenerExportacao = listenerExportacao ;
    }

    @Override
    public String getClasse() {
      return "AfoodGCabecalhoNotaSaida";
    }

    @Override
    public String getMethod() {
        return "atualizar";
    }

    @Override
    public String getNomeParam() {
        return "contas";
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
                JSONObject js = new JSONObject() ;
                js.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
                js.put("UUID", c.getUuid()) ;
                js.put("NOTACUPOM", ds.format(c.getData())) ;
                js.put("CLASSIFICACAOENTRADASAIDA_ID", 2) ;
                js.put("DATA", dt.format(c.getData())) ;
                js.put("TIPONOTA", "") ;
                js.put("CHV_NFE", "") ;
                js.put("CLIENTE_ID", 1) ;
                js.put("ESTORNO", null) ;
                js.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
                js.put("PESSOAS", c.getNum_pessoas()) ;
                js.put("USUARIO_ID", c.getSystem_user_id()) ;
                js.put("VENDEDOR_ID", c.getSystem_user_id()) ;
                js.put("ITENS", getItens(c)) ;
                js.put("PAGAMENTOS", getPagamento(c)) ;

                j.put(js) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerExportacao.erro(getRegExport(), 0, e.getMessage());
        }
        return j ;
    }

    private JSONArray getItens(ContaPedido c) throws JSONException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray jaa = new JSONArray();
        for (ContaPedidoItem i : c.getListContaPedidoItemAtivos()) {
            JSONObject obj = new JSONObject() ;
            obj.put("UUID", i.getUUID()) ;
            obj.put("DATA", dt.format(i.getData())) ;
            obj.put("PRODUTO_ID", i.getProduto().getId()) ;
            obj.put("QUANTIDADE", i.getQuantidade()) ;
            obj.put("PRECOUNITARIO", i.getPreco()) ;
            obj.put("PRECOTOTAL", i.getValor()) ;
            obj.put("DESCONTO", i.getDesconto()) ;
            obj.put("COMISSAO", i.getComissao()) ;
            obj.put("ACRESCIMO", 0) ;
            obj.put("USUARIO", i.getUsuario_id()) ;
            obj.put("USUARIO_ID", i.getUsuario_id()) ;
            obj.put("VENDEDOR_ID", i.getUsuario_id()) ;
            jaa.put(obj) ;
        }
        return jaa ;
    }

    private JSONArray getPagamento(ContaPedido c) throws JSONException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray jaa = new JSONArray();
        for (ContaPedidoPagamento i : c.getListPagamentoAtivos()) {
            JSONObject obj = new JSONObject() ;
            obj.put("CONTA_PEDIDO_UUID", c.getUuid()) ;
            obj.put("CAIXA_UUID", getCaixaUUID(i)) ;
            obj.put("UUID", i.getUuid()) ;
            obj.put("DATA", dt.format(i.getData())) ;
            obj.put("NOTA", "") ;
            obj.put("nCFe", "0") ;
            obj.put("SERIE", "0") ;
            obj.put("MODALIDADE_ID", i.getModalidade().getId()) ;
            obj.put("VALOR", i.getValor()) ;
            obj.put("STATUS", i.getStatus()) ;
            obj.put("STATUS_PG", i.getStatus()) ;
            obj.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
            jaa.put(obj) ;
        }
        return jaa ;
    }

    private String getCaixaUUID(ContaPedidoPagamento i){
        String uuid = "" ;
        Caixa c = DaoDbTabela.getCaixaADO(ctx).get(i.getCaixa_id()) ;
        if (c != null)
            uuid = c.getUuid() ;
        return uuid ;
    }


}
