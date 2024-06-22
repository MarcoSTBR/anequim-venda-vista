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


public class ConexaoServer extends AsyncTask<String, Void, String> {

    public Context ctx;
    public String msg;
    public int codInt = 0;
    public Map<String, Object> maps;
    public URL url;
    public String token;
    public String method;
    public ProgressDialog progressDialog;


    public ConexaoServer(Context ctx) {
        this.ctx = ctx;
        //this.maps = new HashMap<String, Object>();
        maps = new LinkedHashMap<>();
        msg = "Aguarde...";
        token = TokenSet.getTokenUrl(this.ctx);
        method = "POST";
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
/*

    public void execute() {
        try {
            contruirUrl() ;
            executando();
        } catch (Exception e) {

        }
    }
*/
/*

    private void contruirUrl() {
        try {
            String urlget = getPosData();
            StringBuilder postData = new StringBuilder();
            Log.i("parameters", postData.toString());
            Log.i("url_u", url.toString());
            if (method.equals("GET")) {
                String nurl = null;
                if (urlget.length() > 0)
                    nurl = url.toString() + "?" + urlget;
                else nurl = url.toString();
                url = new URL(nurl);
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
*/
/*

    public void executando() {
        Request request = null ;
        if (method.equals("GET"))
                request = new Request.Builder()
                .header("Authorization", token)
                .url(url)
                .build();
        else
            request = new Request.Builder()
                    .header("Authorization", token)
                    .url(url)
                    .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                onPostExecute(response.body().string()) ;
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                onPostExecute(e.getMessage()) ;

            }
        });
    }

*/

/*
    private String getPosData() {
        String urlget = "";
        try {
            for (Map.Entry m : maps.entrySet()) {
                if (urlget.length() != 0) urlget = urlget + "&";
                urlget = urlget + m.getKey() + "=" + URLEncoder.encode(String.valueOf(m.getValue()), "UTF-8");
                Log.i("parameters", m.getKey() + "=" + m.getValue());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlget ;
    }

*/


    @Override
    protected String doInBackground(String... strings) {
        String resposta = "";
        String urlget = "";
        HttpURLConnection connection = null;
        try {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry m : maps.entrySet()) {
                if (urlget.length() != 0) urlget = urlget + "&";
                urlget = urlget + m.getKey() + "=" + URLEncoder.encode(String.valueOf(m.getValue()), "UTF-8");
                if (postData.length() != 0) postData.append("&");
                postData.append(URLEncoder.encode(String.valueOf(m.getKey()), "UTF-8"));
                postData.append("=");
                postData.append(URLEncoder.encode(String.valueOf(m.getValue()), "UTF-8"));
                Log.i("parameters", m.getKey() + "=" + m.getValue());
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            Log.i("parameters", postData.toString());
//            URL u = new URL(url.toString()+"&"+param) ;
            if (url == null) throw new Exception("Link inválido!");
            Log.i("url_u", url.toString());

            if (method.equals("GET")) {
                String nurl = null;
                if (urlget.length() > 0)
                    nurl = url.toString() + "?" + urlget;
                else nurl = url.toString();
                url = new URL(nurl);
            }

           /* SSLFactory sslFactory = SSLFactory.builder()
                    .withDefaultTrustMaterial()     // JDK trusted certificates
                    .withSystemTrustMaterial()      // Android system trusted certificates
                    .withTrustMaterial("/path/to/truststore.p12", "password".toCharArray()) // your custom list of trusted certificates
                    .build();*/

            connection = (HttpURLConnection) url.openConnection();  //abre conexao
/*
            connection.setSSLSocketFactory(sslFactory.getSslSocketFactory());
            connection.setHostnameVerifier(sslFactory.getHostnameVerifier());
*/

            //connection.setRequestMethod("POST"); //fala que quer um post
            //  if (method.equals("POST")) {
            //      connection.setRequestMethod(method);
            // }
            connection.setRequestMethod(method);

            if ((method.equals("GET")) || (method.equals("POST")))
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //fala o que vai mandar
            // connection.setRequestProperty("Accept-Encoding", "identity");

            if (!token.equals("")) {
                connection.setRequestProperty("Authorization", String.valueOf(token));
            }

            connection.setDoInput(true);
            if (method.equals("POST")) {
                if (postData.toString().length() > 0) {

                    connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    //  if (method.equals("POST"))
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(postDataBytes);
                    //connection.getOutputStream().flush();
                }
            }

            Log.i("Urlmethod", method);
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
                //  Log.e("decodedString",connection.getResponseCode()+" "+decodedString) ;
            }
            in.close();
            // Log.e("decodedString",decodedString) ;
            connection.disconnect();

        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.i("ProtocolException", codInt + " " + e.getMessage());
            resposta = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("IOException", codInt + " " + e.getMessage());
            resposta = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Exception", codInt + " " + e.getMessage());
            resposta = e.getMessage();
        }
        Log.i("decodedString", codInt + " " + resposta);
        try {
            codInt = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            if (codInt == 0) resposta = "Sem conexão com Servidor " + resposta;
        }

        return resposta;
    }



    @Override
    protected void onCancelled() {
        progressDialog.dismiss();
        Log.i("onPostExecute", msg + " cancelled ");
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        Log.i("onPostExecute", msg + " cod " + codInt + " " + s);
        //if (codInt == 0) s = "Sem Conexão com o Servidor!";
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
