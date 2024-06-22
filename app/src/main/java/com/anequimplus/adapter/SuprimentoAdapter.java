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
import com.anequimplus.entity.Suprimento;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class SuprimentoAdapter extends RecyclerView.Adapter<SuprimentoAdapter.SuprimentoHolder>{

    private Context ctx ;
    private List<Suprimento> list ;

    public SuprimentoAdapter(Context ctx, List<Suprimento> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public SuprimentoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_suprimento, null);
        return new SuprimentoHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SuprimentoHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SuprimentoHolder extends RecyclerView.ViewHolder{

        private TextView titulo ;
        private TextView descricao;
        private TextView modalidade ;
        private TextView valor ;
        private TextView data ;
        private ImageView supsan_imprimir ;

        public SuprimentoHolder(@NonNull View itemView) {
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

        public void bind(Suprimento it){
            DecimalFormat vf    = new DecimalFormat("R$ #0.00") ;
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;
            titulo.setText("SUPRIMENTO");
            descricao.setText(it.getDescricao());
            data.setText(dt.format(it.getData()));
            modalidade.setText(DaoDbTabela.getModalidadeADO(ctx).getModalidade(it.getModalidade_id()).getDescricao());
            valor.setText(vf.format(it.getValor()));
        }
    }

    public abstract void select(Suprimento it) ;
}
