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
import com.anequimplus.entity.Impressora;

import java.util.List;

public class ImpressoraAdapter extends ArrayAdapter<Impressora> {
    private Context ctx ;
    private List<Impressora> listImp ;
    private LayoutInflater inflater ;

    public ImpressoraAdapter(Context ctx, List<Impressora> listImp) {
        super(ctx, R.layout.layout_grade_impressora, listImp);
        this.ctx = ctx;
        this.listImp = listImp;
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

        View row = inflater.inflate(R.layout.layout_grade_impressora, parent, false);

        TextView textViewImpressora = (TextView) row.findViewById(R.id.textViewImpressora);

        textViewImpressora.setText(listImp.get(position).getDescricao());

        return row;
    }
}
