package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Impressora;

import java.util.List;

public class ConfigImpressoraAdapter extends RecyclerView.Adapter<ConfigImpressoraAdapter.ConfigImpressoraHolper>{

    private Context ctx ;
    private List<Impressora> l ;

    public ConfigImpressoraAdapter(Context ctx, List<Impressora> l){
        this.ctx = ctx ;
        this.l = l ;
    }

    @NonNull
    @Override
    public ConfigImpressoraHolper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_grade_config_impressoras, null);
        return new ConfigImpressoraHolper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigImpressoraHolper holder, int position) {
        holder.bind(l.get(position));
    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    class ConfigImpressoraHolper extends RecyclerView.ViewHolder {

        private TextView descricao ;
        private TextView numCol ;
        private TextView tipoImp ;
        private TextView status ;

        public ConfigImpressoraHolper(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.textNomeImpressora) ;
            numCol = itemView.findViewById(R.id.textNumeroColunas) ;
            tipoImp = itemView.findViewById(R.id.textTipoImpressora) ;
            status = itemView.findViewById(R.id.textStatusImpressora) ;
        }

        public void bind(Impressora i){
            descricao.setText(i.getDescricao());
            numCol.setText("Colunas "+i.getTamColuna());
            tipoImp.setText("Tipo "+ i.getTipoImpressora().name()) ;
            if (i.getStatus() == 1) {
                status.setText("ATIVO");
            } else {
                status.setText("INATIVO");
            }
        }
    }
}
