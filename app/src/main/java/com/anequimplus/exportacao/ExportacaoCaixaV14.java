package com.anequimplus.exportacao;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExportacaoCaixaV14 extends ConexaoExportacao implements ParamExportacao {

    private List<Caixa> listCaixas ;
    private ListenerExportacao listenerExportacao ;


    public ExportacaoCaixaV14(Context ctx, RegExport regExport, ListenerExportacao listenerExportacao) {
        super(ctx, regExport, listenerExportacao);
        this.listenerExportacao = listenerExportacao ;
    }

    @Override
    public String getClasse() {
      return "AfoodCaixas";
    }

    @Override
    public String getMethod() {
        return "atualizar";
    }

    @Override
    public String getNomeParam() {
        return "caixas";
    }

    @Override
    public void executar() {
        super.execute() ;
    }

    @Override
    public JSONArray getDados() {
        listCaixas = DaoDbTabela.getCaixaADO(ctx).getList(new ArrayList<FilterTable>()) ;
        JSONArray j = new JSONArray() ;
        try {
            for (Caixa c : listCaixas){
                JSONObject js = c.getExportacaoJson() ;
                js.put("TIPO", 1) ;
                js.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
                js.put("CONF_TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
                js.put("MODALIDADE_ID", DaoDbTabela.getModalidadeADO(ctx).getModalidadeDinheiro().getId()) ;
                j.put(js) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerExportacao.erro(getRegExport(), 0, e.getMessage());
        }
        return j ;
    }


}
