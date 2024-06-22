package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoExportarContas extends ConexaoServer{

    private List<ContaPedido> contaPedidoList ;
    private List<ContaPedido> contaPedidoEnviado ;
    private List<Caixa> caixaList ;


    public ConexaoExportarContas(Context ctx, List<ContaPedido> contaPedidoList, List<Caixa> caixaList) throws MalformedURLException {
        super(ctx);
        this.contaPedidoList = contaPedidoList ;
        this.caixaList = caixaList ;
        msg = "Exportando Contas";
        maps.put("class","AfoodContaPedido") ;
        maps.put("method","atualizar") ;
        maps.put("MAC", UtilSet.getMAC(ctx)) ;
        contaPedidoEnviado = new ArrayList<ContaPedido>() ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAtualizarCaixa).getUrl() ;
    }

    private JSONArray jsonArray() throws Exception {
        JSONArray jaa = new JSONArray() ;
        for (ContaPedido cp : contaPedidoList){
            JSONObject jobj = cp.getExportarJSON() ;
            jobj.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
            for (int i=0 ; i < jobj.getJSONArray("PAGAMENTOS").length() ; i++){
                JSONObject pg = jobj.getJSONArray("PAGAMENTOS").getJSONObject(i) ;
                Log.i("Exportar", pg.toString()) ;
                int cid = pg.getInt("CAIXA_ID") ;
                JSONObject caixa = getCaixa(cid) ;
                if (caixa == null) throw new Exception("ID Caixa "+cid+" não encontrado!") ;
                 else pg.put("CAIXA", caixa) ;
            }
            jaa.put(jobj);
            contaPedidoEnviado.add(cp) ;
        }
        return jaa;
    }
    private JSONObject getCaixa(int id){
        JSONObject obj = null ;
        for (Caixa c : caixaList){
            if (c.getId() == id)
                obj = c.getExportacaoJson() ;
        }
        return obj ;
    }


    public void executar(){
        try {
            maps.put("contas", jsonArray()) ;
            super.execute();
        } catch (Exception e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("contas", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")){
                ok(contaPedidoEnviado) ;
            } else erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, e.getMessage()) ;
        }
    }

    protected abstract void ok(List<ContaPedido> l) ;
    protected abstract void erro(int cod, String msg) ;
}
