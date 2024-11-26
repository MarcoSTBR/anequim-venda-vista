package com.anequimplus.conexoes;

import android.app.Activity;
import android.util.Log;

import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.Sangria;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoExportarSangrias extends ConexaoServer{

    private List<Caixa> caixaList ;
    private List<Sangria> listSangrias ;
    private List<Sangria> listSangriaEnviadas ;


    public ConexaoExportarSangrias(Activity ctx, List<Sangria> listSangrias, List<Caixa> caixaList) throws MalformedURLException {
        super(ctx);
        this.listSangrias = listSangrias ;
        this.caixaList = caixaList ;
        msg = "Enviando Sangrias...." ;
        maps.put("class","AfoodSangria") ;
        maps.put("method","atualizar") ;
        maps.put("MAC", UtilSet.getMAC(ctx)) ;
        listSangriaEnviadas = new ArrayList<Sangria>() ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //DaoDbTabela.getLinkAcessoADO(ctx).getLinkAcesso(Link.fTransferencia).getUrl() ;
    }

    public void executar(){
        try {
            maps.put("sangrias", getJson()) ;
            super.execute() ;
        } catch (JSONException e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }
    }


    private JSONArray getJson() throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (Sangria s : listSangrias){
            JSONObject obj = s.getExportacaoJSON() ;
            obj.put("LOJA_ID", UtilSet.getLojaId(ctx));
            Caixa caixa = getCaixa(obj.getInt("CAIXA_ID")) ;
            if (caixa != null) {
                obj.put("CAIXA_UUID", caixa.getUuid()) ;
                jaa.put(obj);
                listSangriaEnviadas.add(s) ;
            } else mensagem("Caixa da sangria não encontrado!");
        }
        Log.i("sangria", jaa.toString()) ;
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
        Log.i("suprimentos", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                ok(listSangriaEnviadas) ;
            } else erro(codInt, j.getString("data"));

        } catch (JSONException e) {
            e.printStackTrace();
            erro(codInt, s) ;
            //  Toast.makeText(ctx, "JSON "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void ok(List<Sangria> l) ;
    public abstract void erro(int cod, String msg) ;
    public abstract void mensagem(String msg) ;
}
