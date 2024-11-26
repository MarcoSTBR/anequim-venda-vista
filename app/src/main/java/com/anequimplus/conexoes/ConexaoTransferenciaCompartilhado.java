package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildContaPedidoItem;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Transferencia;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerContaPedidoItem;
import com.anequimplus.listeners.ListenerTransferencia;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConexaoTransferenciaCompartilhado extends ConexaoServer{

    private Transferencia transferencia = null ;
    private ListenerTransferencia listenerTransferencia ;
    private String pedido ;
    private TipoConexao tipoConexao ;
    private ContaPedido contaPedidoDestino ;
    private ContaPedidoItem contaPedidoItem ;
    private ContaPedidoItem contaPedidoItemDestino ;

    public ConexaoTransferenciaCompartilhado(Activity ctx, FilterTables filterTables, String order, ListenerTransferencia listenerTransferencia) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Transfencias ...." ;
        method = "GET" ;
        this.listenerTransferencia = listenerTransferencia ;
        maps.put("filters", filterTables.getJSON()) ;
        maps.put("order", order) ;
        tipoConexao = TipoConexao.cxConsultar ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido_transferencia") ;

    }

    public ConexaoTransferenciaCompartilhado(Activity ctx, String pedido, Transferencia transferencia, ListenerTransferencia listenerTransferencia) throws MalformedURLException{
        super(ctx);
        method = "POST" ;
        msg = "Transferindo ...." ;
        this.pedido = pedido ;
        this.transferencia = transferencia ;
        this.listenerTransferencia = listenerTransferencia ;
        tipoConexao = TipoConexao.cxIncluir ;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/conta_pedido_transferencia") ;
    }

    private JSONObject getTransJSON() {
        JSONObject j = null;
        try {
            j = transferencia.getExportacaoJSON();
            j.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
            j.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return j;
    }


    public void executar(){
       if (tipoConexao == TipoConexao.cxIncluir) {
           getItem();
       } else
       super.execute() ;
    }

    private void getItem(){
        FilterTables l = new FilterTables() ;
        String id = String.valueOf(transferencia.getContaPedidoItem_id()) ;
        l.add(new FilterTable("ID", "=", id )) ;
        new BuildContaPedidoItem(ctx, l, "ID",new ListenerContaPedidoItem() {
            @Override
            public void ok(List<ContaPedidoItem> l) {
                if (l.size()>0) {
                    contaPedidoItem = l.get(0);
                    getContaDestino() ;
                }
                else listenerTransferencia.erro("Item "+id+" não encontrado!");
            }

            @Override
            public void erro(String msg) {
                listenerTransferencia.erro(msg);

            }
        }).executar();
    }

    private void getContaDestino() {
        List<FilterTable> l = new ArrayList<FilterTable>() ;
        String id = String.valueOf(transferencia.getContaPedidoItem_id()) ;
        l.add(new FilterTable("PEDIDO", "=", pedido )) ;
        l.add(new FilterTable("STATUS", "=", "1")) ;
        new BuildContaPedido(ctx, l, "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                Log.i("transferencia", "N item "+pedido+" "+l.size());
                if (l.size() > 0){
                    contaPedidoDestino = l.get(0);
                    transferencia.setContaPedido_destino_id(contaPedidoDestino.getId());
                    iniciarTransferencia() ;
                } else addPedido() ;
            }

            @Override
            public void erro(String msg) {

            }
        }).executar();
    }

    private void addPedido(){
       ContaPedido cp = new ContaPedido(0, UtilSet.getUUID(), pedido, new Date(),0, new ArrayList<ContaPedidoItem>(), new ArrayList<ContaPedidoPagamento>(), 1,1,0, new Date(),1, UtilSet.getUsuarioId(ctx)) ;
       new BuildContaPedido(ctx, cp, new ListenerContaPedido() {
           @Override
           public void ok(List<ContaPedido> l) {
               getContaDestino() ;
//               contaPedidoDestino = l.get(0) ;
//               iniciarTransferencia() ;
           }

           @Override
           public void erro(String msg) {
               listenerTransferencia.erro(msg);

           }
       }).executar();
    }

    private void iniciarTransferencia(){
        if (transferencia.getQuantidade() == contaPedidoItem.getQuantidade()) {
            contaPedidoItem.setContaPedido_id(contaPedidoDestino.getId());
            alterarDestino(contaPedidoItem) ;
        } else {
            Double qtransf = transferencia.getQuantidade() ;
            Double qrest  =  contaPedidoItem.getQuantidade() - qtransf ;
            Double comissao = contaPedidoItem.getProduto().getComissao() ;

            contaPedidoItemDestino = new ContaPedidoItem(0, new Date(), contaPedidoDestino.getId(), UtilSet.getUUID(), contaPedidoItem.getProduto(),
                    qtransf, contaPedidoItem.getPreco(), 0, (qtransf * contaPedidoItem.getPreco()) * (comissao / 100), qtransf * contaPedidoItem.getPreco(),
                    contaPedidoItem.getObs(), 1, UtilSet.getUsuarioId(ctx)) ;
            contaPedidoItem.setQuantidade(qrest);
            contaPedidoItem.setValor(qrest * contaPedidoItem.getPreco());
            contaPedidoItem.setDesconto(0);
            contaPedidoItem.setComissao(contaPedidoItem.getValor() * (comissao / 100));
            transferencia.setContaPedido_destino_id(contaPedidoDestino.getId());
            incluirItem() ;
        }

    }

    private void incluirItem(){
        new BuildContaPedidoItem(ctx, contaPedidoItemDestino, new ListenerContaPedidoItem() {
            @Override
            public void ok(List<ContaPedidoItem> l) {
                alterarDestino(contaPedidoItem) ;
            }

            @Override
            public void erro(String msg) {
                listenerTransferencia.erro(msg);
            }
        }).executar();
    }

    private void alterarDestino(ContaPedidoItem item){
        new BuildContaPedidoItem(ctx, item, new ListenerContaPedidoItem() {
            @Override
            public void ok(List<ContaPedidoItem> l) {
                incluirTransferencia() ;
            }

            @Override
            public void erro(String msg) {
                listenerTransferencia.erro(msg);
            }
        }).executar();
    }

    private void incluirTransferencia(){
       maps.put("data", getTransJSON()) ;
       super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("transferencia", " trans "+ codInt +" [ "+ s+ " ] ") ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerTransferencia.ok(getList(j.getJSONArray("data")));
            } else {
                listenerTransferencia.erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerTransferencia.erro(e.getMessage()) ;
        }
    }

    private List<Transferencia> getList(JSONArray j) throws JSONException {
        List<Transferencia> t = new ArrayList<Transferencia>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            t.add(new Transferencia(j.getJSONObject(i))) ;
        }
        return t ;
    }
}
