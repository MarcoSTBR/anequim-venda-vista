package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Sangria;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class SangriaAdapter extends RecyclerView.Adapter<SangriaAdapter.SangriaHolder>{

    private Context ctx ;
    private List<Sangria> list ;

    public SangriaAdapter(Context ctx, List<Sangria> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public SangriaAdapter.SangriaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_sangria, null);
        return new SangriaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SangriaHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SangriaHolder extends RecyclerView.ViewHolder{

        private TextView titulo ;
        private TextView descricao;
        private TextView modalidade ;
        private TextView valor ;
        private TextView data ;
        private ImageView supsan_imprimir ;

        public SangriaHolder(@NonNull View itemView) {
            super(itemView);
            titulo     = itemView.findViewById(R.id.titulo) ;
            descricao  = itemView.findViewById(R.id.supsan_descricao) ;
            data       = itemView.findViewById(R.id.supsan_data) ;
            modalidade = itemView.findViewById(R.id.supsan_modalidade) ;
            valor      = itemView.findViewById(R.id.supsan_valor) ;
            supsan_imprimir  = itemView.findViewById(R.id.supsan_imprimir) ;
            supsan_imprimir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(list.get(getAdapterPosition())) ;
                }
            });
        }

        public void bind(Sangria it){
            DecimalFormat vf    = new DecimalFormat("R$ #0.00") ;
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
            titulo.setText("SANGRIA");
            descricao.setText(it.getDescricao());
            data.setText(dt.format(it.getData()));
            modalidade.setText(DaoDbTabela.getModalidadeADO(ctx).getModalidade(it.getModalidade_id()).getDescricao());
            valor.setText(vf.format(it.getValor()));
        }
    }

    public abstract void select(Sangria it) ;


}

