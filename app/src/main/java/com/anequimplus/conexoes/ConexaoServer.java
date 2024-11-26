package com.anequimplus.conexoes;

import android.app.Activity;

import com.anequimplus.utilitarios.TokenSet;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;


public class ConexaoServer implements ConexaoListener  {

    public Activity ctx;
    public String msg;
    public int codInt = 0;
    public Map<String, Object> maps;
    public URL url;
    public String token;
    public String method;
    private int tipoConexao = 0 ;
    private Activity activity;

    public ConexaoServer(Activity activity) {
        //super(ctx);
        this.activity = activity ;
        this.ctx = activity;
        //this.maps = new HashMap<String, Object>();
        maps = new LinkedHashMap<>();
        msg = "Aguarde...";
        token = TokenSet.getToken(this.ctx);
        method = "GET";

        tipoConexao = 0 ;//TipoConexao.httpOkHttp;
    }

    public void execute() {
        if (tipoConexao == 0)
            new ConexaoOkHttp(activity, msg, maps, url, token, method, this).executar();
        else
            resultado(-1, "ConexaoAsyncTask");
        //    new ConexaoAsyncTask(ctx, msg, maps, url, token, method, this).execute();
        //Context ctx, String msg, int codInt, Map<String, Object> maps, URL url, String token, String method, ProgressDialog progressDialog, ConexaoListener conexaoListener
    }
    public void resultado(int cod, String s){
        codInt = cod ;
        onPostExecute(s);
    }

    protected void onPostExecute(String s) {
        //Toast.makeText(ctx, "stup onPostExecute", Toast.LENGTH_SHORT).show();
    }


}
