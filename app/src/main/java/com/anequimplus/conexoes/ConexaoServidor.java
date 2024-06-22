package com.anequimplus.conexoes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public abstract class ConexaoServidor extends AsyncTask<Context, String, Integer> {

    private Context ctx ;
    private String nParm ;
    private String jsonDeResposta = "" ;
    private URL url ;
    private int progess = 0 ;
    //private ProgressDialog progressDialog ;

    public ConexaoServidor(Context ctx, String nParm, URL url){
        this.ctx = ctx ;
        this.nParm = nParm ;
        this.url = url ;
        //progressDialog = new ProgressDialog(ctx) ;
    }


    @Override
    protected Integer doInBackground(Context... contexts) {
/*
        if (!UtilSet.seInternet(ctx)){
            jsonDeResposta = "Sem conexão com servidor!" ;
            return null ;
        }
*/
        //PrintStream printStream;
        try {
            //URL url2 = new URL(UtilSet.getServidor(ctx) + "/incluirpedido/" + nParm);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  //abre conexao
            connection.setRequestMethod("POST"); //fala que quer um post
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded"); //fala o que vai mandar
            connection.setRequestProperty("charset", "utf-8");
            connection.setDoOutput(true);
            String parameters = "dados="+ URLEncoder.encode(nParm,"UTF-8") ;
            Log.e("ConexaoServidor",parameters);
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream());
            out.write(parameters);
            out.flush();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            jsonDeResposta = "";
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                jsonDeResposta = jsonDeResposta + decodedString;
                Log.e("decodedString",decodedString) ;
            }
            Log.i("Conexao",jsonDeResposta) ;
            connection.disconnect();

            //jsonDeResposta = new Scanner(connection.getInputStream()).next(); //pega resposta
        } catch (ProtocolException e) {
            e.printStackTrace();
            jsonDeResposta = e.getMessage();
            Log.e("ProtocolException",e.getMessage()) ;
        } catch (IOException e) {
            e.printStackTrace();
            jsonDeResposta = e.getMessage();
            Log.e("IOException",e.getMessage()) ;
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        resposta(jsonDeResposta);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    public abstract void resposta(String resposta) ;

}
