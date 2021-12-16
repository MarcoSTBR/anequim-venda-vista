package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.GradeVendas;

import java.util.List;

public abstract class GradeVendasAdapter extends RecyclerView.Adapter<GradeVendasAdapter.GradeVendasAdapterHolder> {

    private Context ctx ;
    private List<GradeVendas> list ;


    public GradeVendasAdapter(Context ctx, List<GradeVendas> list){
        this.ctx = ctx ;
        this.list = list ;
    }



    @NonNull
    @Override
    public GradeVendasAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_grade_grade_vendas, null);

        //  LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        //  View view  = inflater.inflate(R.layout.layout_grade_pedido, null) ;

        return new GradeVendasAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeVendasAdapterHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GradeVendasAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt ;
        ImageView bt ;

        public GradeVendasAdapterHolder(@NonNull View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.descricaoGradeVendas);
            bt =  (ImageView) itemView.findViewById(R.id.fotoGradeVendas);
            itemView.setOnClickListener(this);
        }

        public void bind(GradeVendas g){
            txt.setText(g.getDescricao()) ;
        }

        @Override
        public void onClick(View v) {
            selecionado(list.get(getAdapterPosition())) ;
        }
    }

    public abstract void selecionado(GradeVendas g) ;

}
