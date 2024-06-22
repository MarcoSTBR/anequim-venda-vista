package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.Suprimento;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoExportarSuprimentos extends ConexaoServer{

    private List<Caixa> caixaList ;
    private List<Suprimento> listSuprimentos ;
    private List<Suprimento> getListSuprimentosEnviados ;


    public ConexaoExportarSuprimentos(Context ctx, List<Suprimento> listSuprimentos, List<Caixa> caixaList) throws MalformedURLException {
        super(ctx);
        this.listSuprimentos = listSuprimentos ;
        this.caixaList = caixaList ;
        msg = "Enviando Suprimentos...." ;
        maps.put("class","AfoodSuprimento") ;
        maps.put("method","atualizar") ;
        maps.put("MAC", UtilSet.getMAC(ctx)) ;
        getListSuprimentosEnviados = new ArrayList<Suprimento>() ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fTransferencia).getUrl() ;
    }

    public void executar(){
        try {
            maps.put("suprimentos", getJson()) ;
            super.execute() ;
        } catch (JSONException e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }
    }


    private JSONArray getJson() throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (Suprimento s : listSuprimentos){
            JSONObject obj = s.getExportacaoJSON() ;
            obj.put("LOJA_ID", UtilSet.getLojaId(ctx));
            Caixa caixa = getCaixa(obj.getInt("CAIXA_ID")) ;
            if (caixa != null) {
                obj.put("CAIXA_UUID", caixa.getUuid()) ;
                jaa.put(obj);
                getListSuprimentosEnviados.add(s) ;
            } else mensagem("Caixa do suprimento não encontrado!");
        }
        Log.i("suprimento", jaa.toString()) ;
        return jaa;
    }


    private Caixa getCaixa(int id){
        Caixa c = null ;
        for (Caixa cx : caixaList){
            if (cx.getId() == id){
              c = cx ;
            }
        }
        return c ;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("supromentos", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                ok(getListSuprimentosEnviados) ;
            } else erro(codInt, j.getString("data"));

        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, s) ;
            //  Toast.makeText(ctx, "JSON "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void ok(List<Suprimento> l) ;
    public abstract void erro(int cod, String msg) ;
    public abstract void mensagem(String msg) ;
}
