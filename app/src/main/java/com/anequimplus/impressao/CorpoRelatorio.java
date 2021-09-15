package com.anequimplus.impressao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.anequimplus.utilitarios.RowImpressao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class CorpoRelatorio  extends AsyncTask<String, Void, String>  {

    private Context ctx ;
    private List<RowImpressao> listRow ;
    private String link ;
    private String param ;

    public CorpoRelatorio(Context ctx, String link, String param) {
        this.ctx = ctx ;
        this.link = link;
        this.param = param;
        List<RowImpressao> listRow = new ArrayList<RowImpressao>();
    }


    @Override
    protected String doInBackground(String... strings) {
        String resposta = "";
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  //abre conexao
            connection.setRequestMethod("POST"); //fala que quer um post
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded"); //fala o que vai mandar
            connection.setRequestProperty("charset", "utf-8");
            connection.setDoOutput(true);
            String parameters = "dados="+param ;
            Log.i("CorpoRelatorio link",url.toString());
            Log.i("CorpoRelatorio param",parameters);
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream());
            out.write(parameters);
            out.flush();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            resposta = "";
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                resposta = resposta + decodedString;
            }

            connection.disconnect();

        } catch (ProtocolException e) {
            e.printStackTrace();
            resposta = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            resposta = e.getMessage();
        }
        Log.i("CorpoRelatorio resposta",resposta) ;
        return resposta;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getInt("CODRETORNO") == 1) {
                JSONArray ja = j.getJSONArray("RELATORIO") ;
                for (int i = 0 ; i < ja.length() ; i++) {
                    listRow.add(new RowImpressao(ja.getJSONObject(i)));
                }
                setRelatorio(listRow) ;
            } else setErro(j.getString("MENSAGEM"));
        } catch (JSONException e) {
            e.printStackTrace();
            setErro(e.getMessage());
        }

    }

    public abstract void setErro(String msg) ;

    public abstract void setRelatorio(List<RowImpressao> listRow) ;

}
