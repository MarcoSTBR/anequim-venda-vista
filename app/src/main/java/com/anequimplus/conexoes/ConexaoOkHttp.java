package com.anequimplus.conexoes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConexaoOkHttp {

    private Context ctx;
    private String msg;
    private Map<String, Object> maps;
    private URL url;
    private String token;
    private String method;
    private ProgressDialog progressDialog = null;
    private OkHttpClient httpClient;
    private Activity activity;

    private ConexaoListener conexaoListener;

    public ConexaoOkHttp(Activity ctx, String msg, Map<String, Object> maps, URL url, String token, String method, ConexaoListener conexaoListener) {
        this.activity = ctx;
        this.ctx = ctx;
        this.msg = msg;
        this.maps = maps;
        this.url = url;
        this.token = token;
        this.method = method;
        this.conexaoListener = conexaoListener;
    }

    private void setPreExecute() {
        if (msg.length() > 0) {
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(msg);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void executar() {
        Log.i("okhttp", "executar url "+url.toString()+" method "+method) ;
        setPreExecute();
        try {
            httpClient = getUnsafeOkHttpClient();
            if (httpClient != null) {
                if (method.equals("GET")) {
                    get();
                }
                if (method.equals("POST")) {
                    post();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            responder(0, e.getMessage());
        }
    }

    private void post() throws UnsupportedEncodingException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry m : maps.entrySet()) {
            formBuilder.addEncoded(String.valueOf(m.getKey()) ,String.valueOf(m.getValue())) ;
/*
            postDataBytes += URLEncoder.encode(String.valueOf(m.getKey()), "UTF-8")
                             +"="+
                             URLEncoder.encode(String.valueOf(m.getValue()),"UTF-8") ;
*/
        }
        Log.i("body", formBuilder.toString()) ;
        Request request = new Request.Builder()
                .addHeader("Authorization", token)
                //.addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(url.toString())
                .post(formBuilder.build())
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        Log.i("okhttp", "exception " + e.getMessage());
                        responder(0, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String s = response.body().string();
                Log.i("okhttp", "response " + s);
                //   resultado(s);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        responder(response.code(), s);
                        // resultado(response.code() ,s);
                    }
                });
            }
        });
    }

    private void get() throws UnsupportedEncodingException {
        String urlget = "";
        for (Map.Entry m : maps.entrySet()) {
            if (urlget.length() != 0) urlget = urlget + "&";
            urlget = urlget + m.getKey() + "=" + URLEncoder.encode(String.valueOf(m.getValue()), "UTF-8");
         //   urlget = urlget + m.getKey() + "=" + String.valueOf(m.getValue());
        }
        String surl = url.toString() + "?" + urlget;

        Log.i("okhttp", "url " + surl);
        Log.i("okhttp", "token " + token);

        //  MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("Content-Type","application/json")
                .url(surl)
                .build();
        Log.i("okhttp", "header " + request.headers().toString());

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        Log.i("okhttp", "exception " + e.getMessage());
                        responder(0, e.getMessage());
                        // resultado(0, e.getMessage());
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String s = response.body().string();
                Log.i("okhttp", "response " + s);
                //   resultado(s);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        responder(response.code(), s);
                        // resultado(response.code() ,s);
                    }
                });
            }
        });

    }

    private void responder(int cod, String s) {
        if (msg.length() > 0) progressDialog.dismiss();
        conexaoListener.resultado(cod, s);
    }

    public static OkHttpClient getUnsafeOkHttpClient()  {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder.build();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return new OkHttpClient.Builder().build();

        } catch (KeyManagementException e){
            e.printStackTrace();
            return new OkHttpClient.Builder().build();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return new OkHttpClient.Builder().build();
        }

    }


}
