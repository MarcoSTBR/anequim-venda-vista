package com.anequimplus.entity;

import com.anequimplus.tipos.TipoModalidade;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContaPedido extends Entidade implements Serializable {
    private int id ;
    private String uuid ;
    private String pedido ;
    private Date data ;
    private double desconto ;
    private List<ContaPedidoItem> listContaPedidoItem;
    private List<ContaPedidoPagamento> listPagamento;
    private int status ;
    private int status_comissao ;
    private int num_impressao ;
    private Date data_fechamento ;
    private int num_pessoas ;
    private int system_user_id ;

    public ContaPedido(String uuid, String pedido, Date data, int system_user_id){
        this.id = 0;
        this.uuid = uuid;
        this.pedido = pedido;
        this.data = data;
        this.desconto = 0;
        this.listContaPedidoItem = null ;
        this.listPagamento = null;
        this.status = 1;
        this.status_comissao = 1;
        this.num_impressao = 0;
        this.data_fechamento = data;
        this.num_pessoas = 1;
        this.system_user_id = system_user_id ;
    }

    public ContaPedido(int id, String uuid, String pedido, Date data, double desconto, List<ContaPedidoItem> listContaPedidoItem, List<ContaPedidoPagamento> listPagamento, int status, int status_comissao, int num_impressao, Date data_fechamento, int num_pessoas, int system_user_id) {
        this.id = id;
        this.uuid = uuid;
        this.pedido = pedido;
        this.data = data;
        this.desconto = desconto;
        this.listContaPedidoItem = listContaPedidoItem;
        this.listPagamento = listPagamento;
        this.status = status;
        this.status_comissao = status_comissao;
        this.num_impressao = num_impressao;
        this.data_fechamento = data_fechamento;
        this.num_pessoas = num_pessoas;
        this.system_user_id = system_user_id ;
    }

    public ContaPedido(JSONObject j) throws JSONException {
        if (j.isNull("id"))
            this.id = j.getInt("ID") ;
          else this.id = j.getInt("id") ;
        this.uuid = j.getString("UUID") ;
        this.pedido = j.getString("PEDIDO") ;
        this.data = UtilSet.getData(j.getString("DATA"));
        this.desconto = j.getDouble("DESCONTO");
        this.status = j.getInt("STATUS") ;
        this.status_comissao = j.getInt("STATUS_COMISSAO") ;
        this.num_impressao = j.getInt("NUM_IMPRESSAO") ;
        this.data_fechamento = UtilSet.getData(j.getString("DATA_FECHAMENTO"));;
        this.num_pessoas = j.getInt("NUM_PESSOAS") ;
        this.system_user_id = j.getInt("SYSTEM_USER_ID") ;

        this.listContaPedidoItem = new ArrayList<ContaPedidoItem>() ;
        this.listPagamento = new ArrayList<ContaPedidoPagamento>();

    }

    public JSONObject getAlterarJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        j.put("PEDIDO", pedido) ;
        j.put("DATA", df.format(data)) ;
        j.put("DESCONTO", desconto) ;
        j.put("STATUS", status) ;
        j.put("STATUS_COMISSAO", status_comissao) ;
        j.put("NUM_IMPRESSAO", num_impressao) ;
        j.put("DATA_FECHAMENTO", df.format(data_fechamento)) ;
        j.put("NUM_PESSOAS", num_pessoas) ;
        j.put("UUID", uuid) ;
        return j ;
    }


    public JSONObject getExportarJSON() throws JSONException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        JSONArray jitens = new JSONArray() ;
        JSONArray jpags = new JSONArray() ;
        //j.put("id", id) ;
        j.put("UUID", uuid) ;
        j.put("PEDIDO", pedido) ;
        j.put("DATA", df.format(data)) ;
        j.put("DESCONTO", desconto) ;
        j.put("STATUS", status) ;
        j.put("STATUS_COMISSAO", status_comissao) ;
        j.put("NUM_IMPRESSAO", num_impressao) ;
        j.put("DATA_FECHAMENTO", df.format(data_fechamento)) ;
        j.put("NUM_PESSOAS", num_pessoas) ;
        j.put("SYSTEM_USER_ID", system_user_id) ;
        for (ContaPedidoItem it : listContaPedidoItem){
            jitens.put(it.getExportacaoJSON()) ;
        }
        for (ContaPedidoPagamento it : listPagamento){
            jpags.put(it.getExportacaoJSON()) ;
        }
        j.put("ITENS", jitens) ;
        j.put("PAGAMENTOS", jpags) ;
        return j ;
    }

    public ContaPedido(JSONObject j, List<ContaPedidoItem> listContaPedidoItem, List<ContaPedidoPagamento> listPagamento) throws JSONException {
        if (j.isNull("id"))
            this.id = j.getInt("ID");
          else
           this.id = j.getInt("id");
        this.uuid = j.getString("UUID");
        this.pedido = j.getString("PEDIDO");
        this.data =  UtilSet.getData(j.getString("DATA")) ;
        this.desconto = j.getDouble("DESCONTO");
        this.status = j.getInt("STATUS") ;
        this.status_comissao = j.getInt("STATUS_COMISSAO") ;
        this.num_impressao = j.getInt("NUM_IMPRESSAO") ;
        this.data_fechamento = UtilSet.getData(j.getString("DATA_FECHAMENTO")) ;
        this.num_pessoas = j.getInt("NUM_PESSOAS") ;
        this.system_user_id = j.getInt("SYSTEM_USER_ID") ; ;
        this.listContaPedidoItem = listContaPedidoItem ;
        this.listPagamento = listPagamento ;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        if (j.isNull("id"))
            this.id = j.getInt("ID");
        else
            this.id = j.getInt("id");
        this.uuid = j.getString("UUID");
        this.pedido = j.getString("PEDIDO");
        this.data =  UtilSet.getData(j.getString("DATA")) ;
        this.desconto = j.getDouble("DESCONTO");
        this.status = j.getInt("STATUS") ;
        this.status_comissao = j.getInt("STATUS_COMISSAO") ;
        this.num_impressao = j.getInt("NUM_IMPRESSAO") ;
        this.data_fechamento = UtilSet.getData(j.getString("DATA_FECHAMENTO")) ;
        this.num_pessoas = j.getInt("NUM_PESSOAS") ;
        this.system_user_id = j.getInt("SYSTEM_USER_ID") ; ;
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public List<ContaPedidoItem> getListContaPedidoItem() {
        return listContaPedidoItem;
    }

    public void setListContaPedidoItem(List<ContaPedidoItem> listContaPedidoItem) {
        this.listContaPedidoItem = listContaPedidoItem;
    }

    public List<ContaPedidoPagamento> getListPagamento() {
        return listPagamento;
    }

    public void setListPagamento(List<ContaPedidoPagamento> listPagamento) {
        this.listPagamento = listPagamento;
    }

    public int getStatus_comissao() {
        return status_comissao;
    }

    public void setStatus_comissao(int status_comissao) {
        this.status_comissao = status_comissao;
    }

    public int getNum_impressao() {
        return num_impressao;
    }

    public void setNum_impressao(int num_impressao) {
        this.num_impressao = num_impressao;
    }

    public Date getData_fechamento() {
        return data_fechamento;
    }

    public void setData_fechamento(Date data_fechamento) {
        this.data_fechamento = data_fechamento;
    }

    public int getNum_pessoas() {
        return num_pessoas;
    }

    public void setNum_pessoas(int num_pessoas) {
        this.num_pessoas = num_pessoas;
    }

    public int getSystem_user_id() {
        return system_user_id;
    }

    public void setSystem_user_id(int system_user_id) {
        this.system_user_id = system_user_id;
    }

    public double getTotalQuantidadeItens(){
        double tot = 0 ;
        for (ContaPedidoItem it : getListContaPedidoItemAtivos()){
            if (it.getStatus() == 1)
                tot = tot + it.getQuantidade();
        }
        return tot ;
    }

    public double getTotalItens(){
        double tot = 0 ;
        for (ContaPedidoItem it : getListContaPedidoItemAtivos()){
            if (it.getStatus() == 1)
                tot = tot + it.getValor();
        }
        return tot ;
    }

    public double getTotalPagamentos(){
        double tot = 0 ;
        for (ContaPedidoPagamento it : listPagamento){
            if (it.getStatus() == 1)
              if (it.getModalidade().getTipoModalidade() != TipoModalidade.pgTroco)
                 tot = tot + it.getValor();
        }
        return tot ;
    }

    public double getTotalComissao(){
        double tot = 0 ;
        for (ContaPedidoItem it : getListContaPedidoItemAtivos()){
            if (status_comissao == 1) {
                if (desconto > 0) {
                    double rat =  desconto / getTotalItens();
                    double com = it.getComissao() - (rat * it.getComissao()) ;
                    tot = tot +  com ;
                } else {
                    tot = tot + it.getComissao();
                }
            }
        }
        return tot ;
    }

    public double getTotal(){
        return getTotalItens() - getDesconto() + getTotalComissao() ;
    }

    public double getTotalSemDesconto(){
        return getTotalItens() + getTotalComissao() ;
    }

    public double getTotalaPagar(){
        return getTotal() - getTotalPagamentos();
    }

    public double getTotalPagamentoDesconto(){
        double vl = 0 ;
        for (ContaPedidoPagamento p : getListPagamentoAtivos()){
                vl = vl + p.getValor() ;
        }
        return vl ;
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ContaPedidoItem> getListContaPedidoItemAtivos(){
        List<ContaPedidoItem> l = new ArrayList<ContaPedidoItem>() ;
        for (ContaPedidoItem it : listContaPedidoItem){
            if (it.getStatus() == 1)
                l.add(it) ;
        }
        return  l ;
    }

    public List<ContaPedidoItem> getListContaPedidoItemAtivosAgrupados(){
        List<ContaPedidoItem> l = new ArrayList<ContaPedidoItem>() ;
        for (ContaPedidoItem it : getListContaPedidoItemAtivos()){
                ContaPedidoItem item = getProdItemlist(l, it) ;
                if (item == null) {
                    item = new ContaPedidoItem(it.getId(), it.getData(), it.getContaPedido_id(), it.getUUID(), it.getProduto(), it.getQuantidade(),
                            it.getPreco(), it.getDesconto(), it.getComissao(), it.getValor(), it.getObs(), it.getStatus(), it.getUsuario_id()) ;
                    l.add(item);
                } else {
                    Double quatidade = item.getQuantidade() + it.getQuantidade() ;
                    Double valor     = item.getValor() + it.getValor() ;
                    Double comissao  = item.getComissao() + it.getComissao() ;
                    Double desconto  = item.getDesconto() + it.getDesconto() ;

                    item.setQuantidade(quatidade);
                    item.setValor(valor);
                    item.setComissao(comissao);
                    item.setDesconto(desconto);
                }
        }
        return  l ;
    }

    private ContaPedidoItem getProdItemlist(List<ContaPedidoItem> l, ContaPedidoItem item){
        ContaPedidoItem it = null ;
        for (ContaPedidoItem i : l) {
            if ((i.getProduto().getId() == item.getProduto().getId())
                 && (i.getPreco() == item.getPreco()))
                it = i ;
        }
        return it ;
    }

    public List<ContaPedidoPagamento> getListPagamentoAtivos() {
        List<ContaPedidoPagamento> l = new ArrayList<ContaPedidoPagamento>() ;
        for (ContaPedidoPagamento it : listPagamento){
            if (it.getStatus() == 1)
                l.add(it);
        }
        return l;
    }

}
