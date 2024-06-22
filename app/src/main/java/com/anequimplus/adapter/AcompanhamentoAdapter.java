package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.ProdutoAcompanhamento;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AcompanhamentoAdapter extends RecyclerView.Adapter<AcompanhamentoAdapter.AcompanhamentoHolder>{

    private Context ctx ;
    private final List<ProdutoAcompanhamento> list ;

    public AcompanhamentoAdapter(Context ctx, List<ProdutoAcompanhamento> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AcompanhamentoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_acompanhamento, null);
        return new AcompanhamentoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcompanhamentoHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class AcompanhamentoHolder extends RecyclerView.ViewHolder{
        RecyclerView acomp_itens ;
        TextView descricao ;
        TextView obs ;

        public AcompanhamentoHolder(@NonNull View itemView) {
            super(itemView);
            descricao   = itemView.findViewById(R.id.descricao_acomp);
            obs = itemView.findViewById(R.id.obs_acomp);
            acomp_itens = itemView.findViewById(R.id.acomp_itens);

        }

        public void bind(ProdutoAcompanhamento pa) {
            DecimalFormat frmQ = new DecimalFormat("R$ #0.00");
            String desc = pa.getAcompanhamento().getDescricao() ;
            if (pa.getAcompanhamento().getPreco() > 0)
                desc = desc +"\n"+frmQ.format(pa.getAcompanhamento().getPreco()) ;
            descricao.setText(desc);
            String m = "("+pa.getAcompanhamento().getMin()+"/"+pa.getAcompanhamento().getMax()+")" ;
            if (pa.getAcompanhamento().getObs().length() > 0)
                m = m +" "+ pa.getAcompanhamento().getObs() ;
            obs.setText(m);
            int ncolum = 1 ;//DisplaySet.getNumeroDeColunasGrade(ctx) ;
            StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(ncolum, StaggeredGridLayoutManager.VERTICAL);
            acomp_itens.setLayoutManager(layoutManager);
            acomp_itens.setAdapter(new AcompanhamentoItemAdapter(ctx, pa.getAcompanhamento(), pa.getAcompanhamento_items()) {
                @Override
                public void onChange() {
                    onSetChange();
                }
            });
        }
    }

    public abstract void onSetChange() ;
}
