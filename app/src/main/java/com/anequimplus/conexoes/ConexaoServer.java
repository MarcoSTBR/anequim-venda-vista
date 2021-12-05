package com.anequimplus.conexoes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.anequimplus.utilitarios.TokenSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConexaoServer  extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog ;
    public String msg ;
    public Context ctx ;
    public int codInt = 0;
//    public String nParm = "" ;
    public Map<String, Object>  maps ;
    public URL url ;
    public String token ;


    public ConexaoServer(Context ctx) {
        this.ctx = ctx ;
        //this.maps = new HashMap<String, Object>();
        maps = new LinkedHashMap<>();
        msg = "Aguarde" ;
        token = TokenSet.getToken(this.ctx) ;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle(msg);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String resposta = "";
        try {
            StringBuilder postData = new StringBuilder();
//            String param = "" ;
            for(Map.Entry m:maps.entrySet())
            {
 //               if (param.length() != 0)  param = param +"&" ;
//                param = param + m.getKey()+"="+URLEncoder.encode(String.valueOf(m.getValue())) ;
                if (postData.length() != 0) postData.append("&");
                postData.append(URLEncoder.encode(String.valueOf(m.getKey()), "UTF-8"));
                postData.append("=");
                postData.append(URLEncoder.encode(String.valueOf(m.getValue()), "UTF-8"));
                Log.i("parameters", m.getKey()+"="+m.getValue());
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            Log.i("parameters", postData.toString());
//            URL u = new URL(url.toString()+"&"+param) ;
//            Log.i("url_u", u.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  //abre conexao
            connection.setRequestMethod("POST"); //fala que quer um post
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //fala o que vai mandar
           // connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

            if (!token.equals("")) {
                connection.setRequestProperty("Authorization", String.valueOf(token));
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(postDataBytes);
            //connection.getOutputStream().flush();

            Log.i("Url", url.toString());
            Log.e("Url", url.toString());

            codInt = connection.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                        connection.getInputStream(), "UTF-8"));
//            connection.getInputStream(), "UTF-8"));
            String decodedString = "";
            //Log.e("decodedString",decodedString) ;
            while ((decodedString = in.readLine()) != null) {
                resposta = resposta + decodedString;
                Log.e("decodedString",connection.getResponseCode()+" "+decodedString) ;
            }
            in.close();
           // Log.e("decodedString",decodedString) ;



            connection.disconnect();

        } catch (ProtocolException e) {
            e.printStackTrace();
            resposta = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            resposta = e.getMessage();
        }
        Log.i("decodedString",codInt+" "+resposta) ;
        return resposta;
    }

    @Override
    protected void onCancelled() {
        progressDialog.dismiss();
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String s) {
        progressDialog.dismiss();
        super.onPostExecute(s);
    }
    /*
    @Override
    protected void onPostExecute(String s) {
      //  super.onPostExecute(s);
        Log.e("onPostExecute",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getInt("CODRETORNO") == 1) {
                listerConexao.resposta(j);
            } else listerConexao.erroMsg(j.getString("MENSAGEM"));
        } catch (JSONException e) {
            e.printStackTrace();
            listerConexao.erroMsg(e.getMessage());
        }

    }

 */
}
