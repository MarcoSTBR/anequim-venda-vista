package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.OpcaoMenuPrincipal;

import java.util.List;

public class MenuPrincipalAdapter extends BaseAdapter {

    private List<OpcaoMenuPrincipal> list ;
    private Context ctx ;

    public MenuPrincipalAdapter(Context ctx, List<OpcaoMenuPrincipal> list){
        this.ctx = ctx ;
        this.list = list ;
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
        view  = inflater.inflate(R.layout.layout_grade_principal, null) ;
        ImageView ig  = view.findViewById(R.id.imageViewGradePrincipal);

        TextView txt = view.findViewById(R.id.textViewGradePrincipal);
        TextView obs = view.findViewById(R.id.textViewGradePrincipalObs);
        ig.setImageResource(list.get(i).getImagem()) ;
        txt.setText(list.get(i).getDescricao());
        obs.setText(list.get(i).getObs());

        return view;
    }
}
