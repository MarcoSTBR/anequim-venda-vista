package com.anequimplus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.OpcoesFechamento;

import java.util.List;

public class OpcoesFechamentoAdapter extends RecyclerView.Adapter<OpcoesFechamentoAdapter.HolderOpcoesFechamento> {
    private List<OpcoesFechamento> list ;
    private OnItemClickListener mListener ;

    public interface OnItemClickListener {
        void onItemClick(int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }

    public static class HolderOpcoesFechamento extends RecyclerView.ViewHolder{
        public TextView tvOpFechamentoDesc ;

        public HolderOpcoesFechamento(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvOpFechamentoDesc = itemView.findViewById(R.id.tvOpFechamentoDesc) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition() ;
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }

    public OpcoesFechamentoAdapter(List<OpcoesFechamento> l) {
        list = l ;
    }

    @NonNull
    @Override
    public HolderOpcoesFechamento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_opcoes_fechamento, parent, false) ;
        HolderOpcoesFechamento holder = new HolderOpcoesFechamento(v, mListener) ;
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull HolderOpcoesFechamento holder, int position) {
       OpcoesFechamento op = list.get(position) ;
       holder.tvOpFechamentoDesc.setText(op.getDescricao());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

