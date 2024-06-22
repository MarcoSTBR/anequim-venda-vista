package com.anequimplus.nfce;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.anequimplus.utilitarios.ConfiguracaoCloudNFceNFCe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexaoCloudNFCe extends AsyncTask<String, Void, String> {

    protected Context ctx ;
    protected URL url ;
    protected String method = "GET" ;
    protected String token  ;
    protected String data = "";
    protected int codigoStatus = 0 ;


    public ConexaoCloudNFCe(Context ctx){
        this.ctx = ctx;
        token = ConfiguracaoCloudNFceNFCe.getCloudNfce_token(ctx) ;
    }

    @Override
    protected String doInBackground(String... strings) {
        String resposta = "" ;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  //abre conexao
            connection.setRequestProperty("Authorization", String.valueOf(token));
            if (method.equals("POST")) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                StringBuilder postData = new StringBuilder(data);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                //postData.append(data);
                connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                connection.getOutputStream().write(postDataBytes);
                connection.getOutputStream().close();
                Log.i("cloudnfce", "data "+data) ;
            } else {
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
            }
            connection.setRequestMethod(method);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream(), "UTF-8"));
            String decodedString = "";
            while ((decodedString = in.readLine()) != null) {
                resposta = resposta + decodedString;
                Log.e("decodedString",connection.getResponseCode()+" "+decodedString) ;
            }
            in.close();
            connection.disconnect();
            codigoStatus = connection.getResponseCode() ;
            Log.i("cloudnfce", "codigo "+connection.getResponseCode()) ;

        } catch (IOException e) {
            e.printStackTrace();
            resposta = e.getMessage() ;
        }
        Log.i("cloudnfce", "resposta "+resposta) ;
        return resposta;
    }
}
