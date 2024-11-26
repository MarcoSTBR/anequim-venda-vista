package com.anequimplus.nfce;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.builds.BuildContaPedidoNFCe;
import com.anequimplus.conexoes.ConexaoLojas;
import com.anequimplus.conexoes.ConexaoServer;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoDest;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.entity.Loja;
import com.anequimplus.entity.Produto;
import com.anequimplus.entity.ProdutoImposto;
import com.anequimplus.listeners.ListenerContaPedidoNfce;
import com.anequimplus.listeners.ListenerLoja;
import com.anequimplus.tipos.TipoConexao;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConexaoEmitirNFCeServerLocal extends ConexaoServer {

    private ListenerEmitirNFce listenerEmitirNFce;
    private ContaPedido contaPedido ;
    private List<ProdutoImposto> listImpostos ;
    private ContaPedidoDest contaPedidoDest ;
    private Loja loja ;
    private Date data_emit ;

    public ConexaoEmitirNFCeServerLocal(Activity ctx, ContaPedido contaPedido, ContaPedidoDest contaPedidoDest, List<ProdutoImposto> listImpostos, ListenerEmitirNFce listenerEmitirNFce) throws MalformedURLException {
        super(ctx);
        msg = "Emitindo NFce...";
        this.listenerEmitirNFce = listenerEmitirNFce;
        this.contaPedido = contaPedido;
        this.contaPedidoDest = contaPedidoDest ;
        this.listImpostos = listImpostos;
        url =  new URL(Configuracao.getLinkContaCompartilhada(ctx)+"/emitir_nfce") ;
    }

    public void executar() throws MalformedURLException{
        //FilterTables fiters = new FilterTables();
       // fiters.add(new FilterTable("id", "=", String.valueOf(UtilSet.getLojaId(ctx))));
        try {
            new ConexaoLojas(ctx,UtilSet.getLojaId(ctx), new ListenerLoja() {
                @Override
                public void ok(List<Loja> l) {
                    if (l.size()>0) {
                        loja = l.get(0) ;
                        emitirNfce();
                    } else
                        listenerEmitirNFce.erro("Loja Não Encontrada!");
                }

                @Override
                public void erro(String msg) {
                    listenerEmitirNFce.erro(msg);
                }
            }).execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
    }

    private JSONObject getStatusInternet(){
        JSONObject j = new JSONObject();
        try {
            j.put("REQ","INTERNET_NFCE") ;
            return j ;
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEmitirNFce.erro(e.getMessage());
            return null ;
        }
    }

    private JSONObject getJSONREQ(){
        JSONObject req = new JSONObject() ;
        try {
            req.put("REQ","EMITIR_NFCE_API")  ;
            req.put("NF",getJSON())  ;
            return req ;
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEmitirNFce.erro(e.getMessage());
            return null ;
        }
    }

    private void enviaREQ(JSONObject j){
        Log.i("data_nfce", j.toString());
        //listenerEmitirNFce.erro(j.toString());
        JSONObject req = getJSONREQ() ;
        maps.put("data", req) ;
        Log.i("data_nfce", req.toString());
        //listenerEmitirNFce.erro(j.toString());
        super.execute();
    }


    private void emitirNfce() {
        try {
            new ConexaoNfCeLocal(ctx, getStatusInternet(), new ListenerNFCeLocal() {
                @Override
                public void ok(JSONObject j) {
                    enviaREQ(j);
                }

                @Override
                public void erro(String msg) {
                    listenerEmitirNFce.erro(msg);
                }
            }).execute();
        } catch (MalformedURLException e){
            e.printStackTrace();
            listenerEmitirNFce.erro(e.getMessage());
        }
    }

    private JSONObject getJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        JSONObject jg = new JSONObject();

        data_emit = new Date() ;
        j.put("Ide_cNF",contaPedido.getId());
        j.put("Ide_dEmi", df.format(data_emit));

        j.put("Emit_CNPJCPF", loja.getCnpj()) ;
        j.put("Emit_IE", loja.getInsc_estadual()) ;
        j.put("Emit_xNome", loja.getLoja()) ;
        j.put("Emit_xFant", loja.getAlias()) ;
        j.put("Emit_EnderEmit_fone", loja.getTelefone() ) ;
        j.put("Emit_EnderEmit_CEP" , loja.getCep()) ;
        j.put("Emit_EnderEmit_xLgr" , loja.getLogradouro()) ;
        j.put("Emit_EnderEmit_nro" , loja.getNumero()) ;
        j.put("Emit_EnderEmit_xCpl" , loja.getComplemento()) ;
        j.put("Emit_EnderEmit_xBairro" , loja.getBairro()) ;
        j.put("Emit_EnderEmit_cMun" , loja.getMunicipio_ibge()) ;
        j.put("Emit_EnderEmit_xMun" , loja.getMunicipio()) ;
        j.put("Emit_EnderEmit_UF" , loja.getUf()) ;

        if (contaPedidoDest == null) {
            j.put("Dest_CNPJCPF", "");
            j.put("Dest_xNome", "");
        } else {
            j.put("Dest_CNPJCPF", contaPedidoDest.getCpfcnpj());
            j.put("Dest_xNome", contaPedidoDest.getNome());
        }
        j.put("InfAdic_infCpl" , "CONTA "+contaPedido.getPedido()) ;
        addItens(j) ;
        addPagamentos(j) ;
        jg.put("REQ","EMITIR_NFCE_API")  ;
        jg.put("NF",j)  ;
        return j;
    }

    private void addItens(JSONObject j) throws JSONException {
        JSONArray itens = new JSONArray() ;
        for (ContaPedidoItem item : contaPedido.getListContaPedidoItemAtivosAgrupados()){
            JSONObject jIt = new JSONObject() ;
            ProdutoImposto pImposto = getImposto(item.getProduto()) ;
            jIt.put("Codigo", item.getProduto().getCodBarra()) ;
            jIt.put("cEAN", pImposto.getEAN()) ;
            jIt.put("cEANTrib",pImposto.getEANTRIB()) ;
            jIt.put("Descricao",item.getProduto().getDescricao()) ;
            jIt.put("Unidade",item.getProduto().getUnidade()) ;
            jIt.put("NCM", pImposto.getNCM()) ;
            jIt.put("CFOP", pImposto.getCFOP()) ;
            jIt.put("CEST", pImposto.getCEST()) ;

            jIt.put("Quantidade", formataDecimal("####0.000",item.getQuantidade())) ;
            jIt.put("PrecoUnitario",formataDecimal("####0.000",item.getPreco())) ;
            jIt.put("ValorTotal",formataDecimal("####0.00", item.getValor())) ;
            jIt.put("Desconto",formataDecimal("####0.00",item.getDesconto())) ;
            jIt.put("vTributoF",formataDecimal("####0.00",0.0)) ;
            jIt.put("vTributoE",formataDecimal("####0.00",0.0)) ;
            jIt.put("vTributoM",formataDecimal("####0.00",0.0)) ;
            jIt.put("vTotTrib",formataDecimal("####0.00",0.0)) ;

            jIt.put("vBCST", formataDecimal("####0.00",0.0)) ;
            jIt.put("pICMSST", formataDecimal("####0.00",0.0)) ;
            jIt.put("vICMSST", formataDecimal("####0.00",0.0)) ;

            jIt.put("vBC", formataDecimal("####0.00",item.getValor())) ;

            jIt.put("CST",pImposto.getICMS_CST()) ;
            jIt.put("pICMS",formataDecimal("####0.00",pImposto.getICMS_ALIQUOTA())) ;
           // jIt.put("vICMS",formataDecimal("####0.00",it.vICMS)) ;

            jIt.put("CST_PIS", pImposto.getPIS_CST()) ;
            jIt.put("pALIQ_PIS", formataDecimal("####0.00",pImposto.getPIS_ALIQUOTA())) ;
           // jIt.put("vALIQ_PIS", formataDecimal("####0.00",it.vALIQ_PIS )) ;

            jIt.put("CST_COFINS", pImposto.getCOFINS_CST()) ;
            jIt.put("pALIQ_COFINS", formataDecimal("####0.00",pImposto.getCOFINS_ALIQUOTA())) ;
          //  jIt.put("vALIQ_COFINS", formataDecimal("####0.00",it.vALIQ_COFINS)) ;
            jIt.put("pRedBC", formataDecimal("####0.00", pImposto.getPREDBC())) ;

            jIt.put("pFCP", formataDecimal("####0.00", pImposto.getPFCP())) ;
            if (pImposto.getCBENEF() == null)
                jIt.put("cBenef", "") ;
             else
            if (pImposto.getCBENEF().equals("null"))
                jIt.put("cBenef", "") ;
              else
                jIt.put("cBenef", pImposto.getCBENEF()) ;
            jIt.put("motDesICMS", pImposto.getMOTDESICMS()) ;

            itens.put(jIt);
        }
        j.put("ITENS", itens);
    }

    private void addPagamentos(JSONObject j) throws JSONException {
        JSONArray itens = new JSONArray() ;
        for (ContaPedidoPagamento pg : contaPedido.getListPagamento()){
            JSONObject it = new JSONObject();
            if ((pg.getStatus() == 1) && !(pg.getModalidade().getCodigo().equals("99")) && !(pg.getModalidade().getCodigo().equals("00"))){
                it.put("CodModalidade",pg.getModalidade().getCod_recebimento());
                it.put("Modalidade",pg.getModalidade().getDescricao());
                it.put("Valor",formataDecimal("####0.00", pg.getValor()));
                it.put("CnpjPg","");
                it.put("BandeiraPg","");
                it.put("AutPg","");
                itens.put(it) ;
            }
        }
        j.put("PAGAMENTO", itens) ;
    }


    private String formataDecimal(String frm, Double valor){
        DecimalFormat d = new DecimalFormat(frm) ;
        return d.format(valor * 1000) ;
    }

    private ProdutoImposto getImposto(Produto p){
        ProdutoImposto prd = null ;
        for (ProdutoImposto pp : listImpostos){
            if (pp.getID() == p.getId()){
                prd = pp ;
            }
        }
        return  prd ;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("executar", s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s.trim()) ;
            if (j.getString("status").equals("success")) {
                JSONObject js = j.getJSONObject("data");
                if (js.getString("RESP").equals("OK")) {
                    String chave = js.getString("CHAVE") ;
                    ContaPedidoNFCe cNfce = new ContaPedidoNFCe(
                            UtilSet.getUUID(),
                            contaPedido.getId(),
                            data_emit,
                            chave,
                            js.getString("PROTOCOLO"),
                            js.getString("XMOTIVO"),
                            js.getInt("CSTAT"),
                            0,
                            js.getInt("STATUS_CONTINGENCIA"),
                            1,
                            0);
                    setContaPedidoNFCe(cNfce);
                } else listenerEmitirNFce.erro(js.getString("MENSAGEM"));
            } else listenerEmitirNFce.erro(j.getString("data"));
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
