package com.anequimplus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ModalidadeAdapter extends BaseAdapter {

    private Context ctx ;
    private List<Modalidade> list ;
    private Handler handler = new Handler() ;

    public ModalidadeAdapter(Context ctx, List<Modalidade> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId() ;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        final View row  = inflater.inflate(R.layout.layout_grade_modalidade, null) ;
        Modalidade m = list.get(i) ;
        TextView txt = row.findViewById(R.id.textViewModalidade);
        txt.setText(m.getDescricao());
        ImageView imageViewModalidade = row.findViewById(R.id.imageViewModalidade);
        imageViewModalidade.setImageResource(R.mipmap.menu_caixa);
        final String nUrl = m.getFoto().getUrl();
        final String nParam = m.getFoto().getParam() ;
        if (!nUrl.equals("")) {

            new Thread() {
                @Override
                public void run() {
                    try{
                        URL url2 = new URL(nUrl);
                        HttpURLConnection conexao = (HttpURLConnection) url2.openConnection();
                        conexao.setRequestMethod("POST"); //fala que quer um post
                        if (!nParam.equals("")) {
                            String parameters = "dados="+nParam ;
                            OutputStreamWriter out = new OutputStreamWriter(
                                    conexao.getOutputStream());
                            out.write(parameters);
                            out.flush();
                        }
                        /*
                        String parameters = "dados="+j.toString() ;
                        OutputStreamWriter out = new OutputStreamWriter(
                                conexao.getOutputStream());
                        out.write(parameters);
                        out.flush();
                         */
                        InputStream input = conexao.getInputStream();
                        final Bitmap img = BitmapFactory.decodeStream(input);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ImageView imageViewModalidade = row.findViewById(R.id.imageViewModalidade);
                                imageViewModalidade.setImageBitmap(img);

                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        return row;
    }


}
