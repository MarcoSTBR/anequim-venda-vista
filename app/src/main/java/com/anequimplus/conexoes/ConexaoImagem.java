package com.anequimplus.conexoes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.anequimplus.utilitarios.LinksParaAcesso;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoImagem extends AsyncTask<String, Void, Bitmap> {

    private Context ctx ;
    private LinksParaAcesso link ;
    private String msg ;
    private URL url ;

    public ConexaoImagem(Context ctx, LinksParaAcesso link) throws MalformedURLException {
        this.ctx = ctx ;
        this.link = link ;
        url = new URL(link.getUrl()) ;
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
            if (!link.getParam().equals("")){

                String parameters = "dados="+link.getParam() ;
                Log.i("parameters",parameters);
                OutputStreamWriter out = new OutputStreamWriter(
                        connection.getOutputStream());
                out.write(parameters);
                out.flush();
            }
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
            erro(msg) ;
        } else {
            setBitmap(bitmap);
        }
    }

    public abstract void setBitmap(Bitmap bt) ;
    public abstract void erro(String msg) ;
}
