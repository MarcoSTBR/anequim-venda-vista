package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.entity.Caixa;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class CaixaADO {

    private Context ctx ;
    private  Caixa caixa = null;

    public CaixaADO(Context ctx) {
        this.ctx = ctx;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public void setCaixaJson(JSONObject j) throws JSONException, ParseException {
        this.caixa = new Caixa(j);
    }
}
