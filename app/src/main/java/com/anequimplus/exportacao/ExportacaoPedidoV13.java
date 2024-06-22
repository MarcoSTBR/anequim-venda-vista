package com.anequimplus.exportacao;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExportacaoPedidoV13 extends ConexaoExportacao implements ParamExportacao{


    private List<ContaPedido> list ;
    private ListenerExportacao listenerExportacao ;

    public ExportacaoPedidoV13(Context ctx, RegExport regExport, ListenerExportacao listenerExportacao) {
        super(ctx, regExport, listenerExportacao);
        this.listenerExportacao = listenerExportacao ;
    }


    @Override
    public String getClasse() {
        return "AfoodGPedido";
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
                JSONObject js = new JSONObject() ;
                js.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
                js.put("UUID", c.getUuid()) ;
                js.put("PEDIDO", c.getPedido()) ;
                js.put("DATASEQUENCIAL", dt.format(c.getData())) ;
                js.put("DATAFECHAMENTO", dt.format(c.getData_fechamento())) ;
                js.put("DATAATENDIMENTO", dt.format(c.getData())) ;
                js.put("STATUSRECEBIMENTO", c.getStatus()) ;
                js.put("USUARIO", UtilSet.getLogin(ctx)) ;
                js.put("VENDEDOR", UtilSet.getLogin(ctx)) ;
                js.put("CLIENTE", "0") ;
                js.put("LIBERACAOPARARECEBIMENTO", "1") ;
                js.put("AMBIENTE_ID", "1") ;
                js.put("VENDA_ID", "1") ;
                js.put("USUARIO_ID", c.getSystem_user_id()) ;
                js.put("VENDEDOR_ID", c.getSystem_user_id()) ;
                js.put("STATUS", c.getStatus()) ;
                js.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
                js.put("PESSOAS", c.getNum_pessoas()) ;
                js.put("ITENS", getItens(c)) ;
               // js.put("PAGAMENTOS", getPagamento(c)) ;
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
            obj.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
            obj.put("PEDIDO_UUID", c.getUuid()) ;
            obj.put("PEDIDO", c.getPedido()) ;
            obj.put("DATASEQUENCIAL", dt.format(c.getData())) ;
            obj.put("PRODUTO_ID", i.getProduto().getId()) ;
            obj.put("CODIGOPRODUTO", i.getProduto().getCodBarra()) ;
            obj.put("DATAENTRADAITEM", dt.format(i.getData())) ;
            obj.put("DIFERENCIAL", i.getId()) ;
            obj.put("USUARIO_ID", i.getUsuario_id()) ;
            obj.put("VENDEDOR_ID", i.getUsuario_id()) ;
            obj.put("VENDEDOR", UtilSet.getLogin(ctx)) ;
            obj.put("QUANTIDADE", i.getQuantidade()) ;
            obj.put("PRECOUNITARIO", i.getPreco()) ;
            obj.put("PRECOFINAL", i.getValor()) ;
            obj.put("COMISSAO", i.getComissao()) ;
            obj.put("DESCONTO", i.getDesconto()) ;
            obj.put("STATUS", i.getStatus()) ;
            jaa.put(obj) ;
        }
        return jaa ;
    }
}
