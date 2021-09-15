package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Grupo;

import java.util.List;

public class GrupoAdapter extends BaseAdapter {

    private Context ctx ;
    private List<Grupo> lisGrupo ;

    public GrupoAdapter(Context ctx, List<Grupo> lisGrupo){
        this.ctx = ctx ;
        this.lisGrupo = lisGrupo ;
    }

    @Override
    public int getCount() {
        return lisGrupo.size();
    }

    @Override
    public Object getItem(int i) {
        return lisGrupo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lisGrupo.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View  row = inflater.inflate(R.layout.layout_grade_grupo, null) ;
        Grupo g = lisGrupo.get(i) ;
        TextView txt = row.findViewById(R.id.textView_grupo_descricao);
        ImageView img = row.findViewById(R.id.imageViewGrupoProduto) ;
        img.setImageResource(R.mipmap.ic_grupo_produto_foreground);
        txt.setText(g.getDescricao());
        return row;
    }
}
