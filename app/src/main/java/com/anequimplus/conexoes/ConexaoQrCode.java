package com.anequimplus.conexoes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoQrCode extends AsyncTask<String, Void, Bitmap> {

    private Context ctx ;
    private String msg ;
    private String chave ;
    private URL url ;
    private String param ;

    public ConexaoQrCode(Context ctx, String chave) {
        this.ctx = ctx ;
        this.chave = chave ;
        setParametros() ;

    }

    private void setParametros()  {
        JSONObject j = new JSONObject();
        try {
            UtilSet.getAutenticacao(ctx, j);
            j.put("CHAVE", chave) ;
            url = new URL(UtilSet.getServidor(ctx)+"/QRCODE") ;
            param = j.toString() ;
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap img = null ;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  //abre conexao
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded"); //fala o que vai mandar
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestMethod("POST"); //
            connection.setDoOutput(true);
            String parameters = "dados="+param ;
            Log.i("parameters",parameters);
            OutputStreamWriter out = new OutputStreamWriter(
            connection.getOutputStream());
            out.write(parameters);
            out.flush();
            InputStream input = connection.getInputStream();
            img = BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return img;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap == null){
            erro(msg);
        } else {
            setBitmap(bitmap);
        }
    }

    public abstract void setBitmap(Bitmap bt) ;
    public abstract void erro(String msg) ;

}
