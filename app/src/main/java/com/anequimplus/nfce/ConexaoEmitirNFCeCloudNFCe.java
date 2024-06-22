package com.anequimplus.nfce;

import android.content.Context;
import android.util.Log;

import com.anequimplus.builds.BuildContaPedidoNFCe;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoDest;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.ProdutoImposto;
import com.anequimplus.listeners.ListenerContaPedidoNfce;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.ConfiguracaoCloudNFceNFCe;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConexaoEmitirNFCeCloudNFCe extends ConexaoCloudNFCe{

    private ListenerEmitirNFce listenerEmitirNFce;
    private ContaPedido contaPedido ;
    private List<ProdutoImposto> listImpostos ;
    private ContaPedidoDest contaPedidoDest ;
    private Date data_emit ;

    public ConexaoEmitirNFCeCloudNFCe(Context ctx, ContaPedido contaPedido, ContaPedidoDest contaPedidoDest, List<ProdutoImposto> listImpostos, ListenerEmitirNFce listenerEmitirNFce) throws MalformedURLException {
        super(ctx);
        //msg = "Emitindo NFce...";
        method = "POST" ;
        this.listenerEmitirNFce = listenerEmitirNFce;
        this.contaPedido = contaPedido;
        this.contaPedidoDest = contaPedidoDest ;
        this.listImpostos = listImpostos;
        url =  new URL(ConfiguracaoCloudNFceNFCe.getLinkCloudNFce(ctx)+"nfce") ;
    }

    public void executar(){

        try {
            data = getBodyNFCe() ;
            this.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEmitirNFce.erro(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            listenerEmitirNFce.erro(e.getMessage());
        }
    }

    private String getBodyNFCe() throws Exception {
        data_emit = new Date() ;
        String a = "yyyy-MM-dd'T'HH:mm:ssXXX" ;
        SimpleDateFormat sd = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sd = new SimpleDateFormat(a);
        }
        String datae = sd.format(data_emit) ;
        JSONObject j = new JSONObject() ;
        JSONArray jitens = new JSONArray() ;
        j.put("natureza_operacao", "VENDA DENTRO DO ESTADO") ;
        j.put("serie", String.valueOf(ConfiguracaoCloudNFceNFCe.getCloudNfce_serie(ctx))) ;
        j.put("numero", String.valueOf(ConfiguracaoCloudNFceNFCe.getCloudNfce_numero(ctx)+1)) ;
        j.put("data_emissao", datae) ;
        j.put("presenca_comprador", "1") ;
        if (contaPedidoDest != null){
            if (contaPedidoDest.getCpfcnpj().length() == 12)
                j.put("cpf", contaPedidoDest.getCpfcnpj()) ;
             else j.put("cnpj", contaPedidoDest.getCpfcnpj()) ;
            j.put("nome", contaPedidoDest.getNome()) ;
            j.put("indicador_inscricao_estadual", "9") ;
        }
        int cont = 0 ;
        Double total = 0.0 ;
        for (ContaPedidoItem it : contaPedido.getListContaPedidoItemAtivosAgrupados()){
            ProdutoImposto imposto = getProdutoImposto(it.getProduto().getId()) ;
            JSONObject itm = new JSONObject() ;
            itm.put("numero_item", String.valueOf(++cont)) ;
            itm.put("origem", "0") ;
            itm.put("inclui_no_total", "1") ;
            itm.put("codigo_produto", it.getProduto().getCodBarra()) ;
            itm.put("descricao", it.getProduto().getDescricao()) ;
            itm.put("unidade_comercial", "RJ") ;
            itm.put("valor_unitario_comercial", it.getPreco()) ;
            itm.put("quantidade_comercial", it.getQuantidade()) ;
            itm.put("valor_bruto", it.getValor()) ;
            itm.put("valor_desconto", it.getDesconto()) ;

            itm.put("cest", imposto.getCEST()) ;
            if (imposto.getNCM() != null)
            itm.put("codigo_ncm", imposto.getNCM()) ;
            itm.put("cfop", imposto.getCFOP()) ;
            if (imposto.getCBENEF().equals("null") || (imposto.getCBENEF() == null))
                itm.put("codigo_beneficio_fiscal", "") ;
             else itm.put("codigo_beneficio_fiscal", imposto.getCBENEF()) ;

            JSONObject impostos = new JSONObject() ;
            impostos.put("valor_aproximado_tributos", 0) ;
            JSONObject icms = new JSONObject() ;
                if (imposto.getICMS_CST().equals("null") || (imposto.getICMS_CST() == null))
                    icms.put("situacao_tributaria", "01") ;
                  else  icms.put("situacao_tributaria", imposto.getICMS_CST()) ;
                icms.put("situacao_tributaria", imposto.getICMS_CST()) ;
                //icms.put("valor_base_calculo", it.getValor()) ;
                if (imposto.getICMS_MODBC().equals("null") || (imposto.getICMS_MODBC() == null))
                    icms.put("modalidade_base_calculo", "0") ;
                   else icms.put("modalidade_base_calculo", imposto.getICMS_MODBC()) ;
                 Log.i("data", "["+imposto.getICMS_MODBC()+"]") ;
                if (imposto.getPREDBC() > 0)
                  icms.put("aliquota_reducao_base_calculo", imposto.getPREDBC()) ;
                icms.put("aliquota", imposto.getICMS_ALIQUOTA()) ;
                icms.put("valor", (imposto.getICMS_ALIQUOTA() * it.getValor()) /100) ;
            impostos.put("icms", icms) ;

            JSONObject pis = new JSONObject() ;
                if (imposto.getPIS_CST().equals("null") || (imposto.getPIS_CST() == null))
                    pis.put("situacao_tributaria", "01") ;
                 else pis.put("situacao_tributaria", imposto.getPIS_CST()) ;
                pis.put("aliquota", imposto.getPIS_ALIQUOTA()) ;
                pis.put("valor", (imposto.getPIS_ALIQUOTA() * it.getValor()) / 100) ;
                pis.put("valor_base_calculo", (imposto.getPIS_ALIQUOTA() * it.getValor()) / 100) ;

            impostos.put("pis", pis) ;

            JSONObject cofins = new JSONObject() ;
                if (imposto.getCOFINS_CST().equals("null") || (imposto.getCOFINS_CST() == null))
                    cofins.put("situacao_tributaria", "01") ;
                 else cofins.put("situacao_tributaria", imposto.getCOFINS_CST()) ;
                cofins.put("aliquota", imposto.getCOFINS_ALIQUOTA()) ;
                cofins.put("valor", (imposto.getCOFINS_ALIQUOTA() * it.getValor()) / 100) ;
                cofins.put("valor_base_calculo", (imposto.getCOFINS_ALIQUOTA() * it.getValor()) / 100) ;

            impostos.put("cofins",cofins) ;

            itm.put("imposto", impostos );
            total = total + it.getValor() ;
            jitens.put(itm) ;
        }
        j.put("itens", jitens) ;

        JSONObject pagamento = new JSONObject() ;
        JSONArray pagamentos = new JSONArray() ;
        Double totPag = 0.0 ;
        for (ContaPedidoPagamento pag : contaPedido.getListPagamentoAtivos()){
            JSONObject pg = new JSONObject() ;
            pg.put("meio_pagamento", pag.getModalidade().getCod_recebimento()) ;
            pg.put("valor", pag.getValor()) ;
            pg.put("tipo_integracao", "2") ;
            pagamentos.put(pg) ;
            totPag = totPag + pag.getValor() ;
        }
        if (totPag > total){
            pagamento.put("valor_troco", totPag - total) ;
        }
        pagamento.put("formas_pagamento ", pagamentos) ;
        j.put("pagamento", pagamento) ;

        j.put("informacoes_adicionais_fisco", "") ;
        String txtAdic = "Caixa "+UtilSet.getUsuarioNome(ctx) ;
        j.put("informacoes_adicionais_contribuinte", txtAdic) ;

        JSONObject frete = new JSONObject() ;
        frete.put("modalidade_frete", "9") ;
        j.put("frete", frete) ;
        Log.i("data", j.toString()) ;
        return j.toString() ;
    }


    private ProdutoImposto getProdutoImposto(int id) throws Exception {
        for (ProdutoImposto prd : listImpostos){
            if (id == prd.getID()){
                return prd ;
            }
        }
        throw new Exception("Produto id = ["+id+"] não encontrado!") ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("onPostExecute", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getBoolean("sucesso")) {
                    ContaPedidoNFCe cNfce = new ContaPedidoNFCe(
                            UtilSet.getUUID(),
                            contaPedido.getId(),
                            data_emit,
                            j.getString("chave") ,
                            j.getString("protocolo"),
                            j.getString("mensagem"),
                            j.getInt("codigo"),
                            0,
                            0, //STATUS_CONTINGENCIA
                            1,
                            1);
                    ConfiguracaoCloudNFceNFCe.setCloudNfce_numero(ctx, j.getInt("numero"));
                    setContaPedidoNFCe(cNfce);
            } else {

                String msg = j.getString("mensagem") ;
                if (!j.isNull("erros")) {
                    msg = msg + " "+ j.getJSONObject("erros").getString("campo") + " " +
                            j.getJSONObject("erros").getString("erro") + " " +
                            j.getJSONObject("erros").getString("descricao");
                }
                listenerEmitirNFce.erro(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEmitirNFce.erro(s);
        }
    }

    private void setContaPedidoNFCe(ContaPedidoNFCe it){
        new BuildContaPedidoNFCe(ctx, it, TipoConexao.cxIncluir, new ListenerContaPedidoNfce() {
            @Override
            public void ok(List<ContaPedidoNFCe> l) {
                if (l.size() > 0)
                    listenerEmitirNFce.ok(it);
                else listenerEmitirNFce.erro("Erro na inclusão da NFCe");
            }

            @Override
            public void erro(String msg) {
                listenerEmitirNFce.erro(msg);
            }
        }).executar();
    }


}
