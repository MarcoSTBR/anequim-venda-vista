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
import com.anequimplus.entity.Modalidade;

import java.util.List;

public class ModalidadeSupSanAdapter extends ArrayAdapter<Modalidade> {

    private Context ctx ;
    private List<Modalidade> list ;
    private LayoutInflater inflater ;

    public ModalidadeSupSanAdapter(Context ctx, List<Modalidade> list) {
        super(ctx, R.layout.layout_grade_modalidade, list);
        this.ctx = ctx ;
        this.list = list ;
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

        View row = inflater.inflate(R.layout.layout_grade_modalidade, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.textViewModalidade);

        textView.setText(list.get(position).getDescricao());

        return row;
    }
}
