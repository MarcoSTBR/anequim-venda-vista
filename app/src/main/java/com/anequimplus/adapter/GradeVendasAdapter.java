package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.GradeVendas;

import java.util.List;

public class GradeVendasAdapter extends BaseAdapter {

    private Context ctx ;
    private List<GradeVendas> list ;

    public GradeVendasAdapter(Context ctx, List<GradeVendas> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  list.get(position).getId() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View  row = inflater.inflate(R.layout.activity_grade_grade_vendas, null) ;
        GradeVendas g = list.get(position) ;

        TextView txt = row.findViewById(R.id.descricaoGradeVendas);
        ImageView img = row.findViewById(R.id.fotoGradeVendas) ;
        img.setImageResource(R.mipmap.ic_grupo_produto_foreground);
        txt.setText(g.getDescricao());
        return row;
    }
}
