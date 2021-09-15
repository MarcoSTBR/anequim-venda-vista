package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Loja;

import java.util.List;

public class LojaAdapter extends ArrayAdapter<Loja> {

    private Context ctx ;
    private List<Loja> list ;
    private LayoutInflater inflater ;

    public LojaAdapter(Context ctx, List<Loja> list) {
        super(ctx, R.layout.layout_grade_lojas, list);
        this.ctx = ctx;
        this.list = list;
        inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE) ;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustonView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustonView(position, convertView, parent);
    }

    private View getCustonView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.layout_grade_lojas, parent, false);

        TextView textNomeLoja = (TextView) row.findViewById(R.id.textNomeLoja);

        textNomeLoja.setText(list.get(position).getNome());

        return row;
    }



}
