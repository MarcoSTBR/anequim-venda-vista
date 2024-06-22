package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Acompanhamento;
import com.anequimplus.entity.Acompanhamento_Item;
import com.anequimplus.entity.Produto;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AcompanhamentoItemAdapter extends RecyclerView.Adapter<AcompanhamentoItemAdapter.AcompanhamentoItemHolder>{

    private Context ctx ;
    private Acompanhamento acompanhamento ;
    private final List<Acompanhamento_Item> list ;

    public AcompanhamentoItemAdapter(Context ctx, Acompanhamento acompanhamento, List<Acompanhamento_Item> list) {
        this.ctx = ctx;
        this.list = list;
        this.acompanhamento = acompanhamento ;
    }

    @NonNull
    @Override
    public AcompanhamentoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_acompanhamento_item, null);
        return new AcompanhamentoItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcompanhamentoItemHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class AcompanhamentoItemHolder extends RecyclerView.ViewHolder{

        TextView descricao ;
        TextView obs ;
        TextView preco ;
        ImageButton mais ;
        ImageButton menos ;
        TextView quant ;


        public AcompanhamentoItemHolder(@NonNull View itemView) {
            super(itemView);
            descricao   = itemView.findViewById(R.id.textProdutoAcompItem);
            preco   = itemView.findViewById(R.id.preco_acomp_item);
            obs = itemView.findViewById(R.id.textProdutoAcompObsItem);
            quant = itemView.findViewById(R.id.acomp_item_quant);
            mais = itemView.findViewById(R.id.mais_acomp_item);
            menos = itemView.findViewById(R.id.menos_acomp_item);
        }

        public void bind(final Acompanhamento_Item it) {
            Produto p = DaoDbTabela.getProdutoADO(ctx).getProdutoId(it.getProduto_id()) ;
            descricao.setText(p.getDescricao());
            if (it.getObs().length() > 0){
                obs.setVisibility(View.VISIBLE);
                obs.setText(it.getObs());
            } else {
                obs.setVisibility(View.GONE);
            }
            setValores(it) ;
            mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (verificarLimite(it, 1.0)) {
                        it.setQuantidade(it.getQuantidade() + 1);
                        setValores(it);

                    }
                }
            });
            menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (it.getQuantidade() > 0){
                        it.setQuantidade(it.getQuantidade() - 1);
                        setValores(it);
                    }
                }
            });
        }

        private void setValores(Acompanhamento_Item it){
            DecimalFormat frmQ = new DecimalFormat("#0.###");
            DecimalFormat frmv = new DecimalFormat("R$ #0.00");
            preco.setText(frmv.format(it.getPreco()));
            if (it.getPreco() != 0) preco.setVisibility(View.VISIBLE);
             else preco.setVisibility(View.GONE);
            quant.setText(frmQ.format(it.getQuantidade()));
            onChange() ;

        }

        private boolean verificarLimite(Acompanhamento_Item it, Double q){
            DecimalFormat frmQ = new DecimalFormat("#0.###");
            if ((it.getQuantidade() + q) <= it.getMax()) {
                if ((totalQuant() + q) > acompanhamento.getMax()) {
                    Toast.makeText(ctx, "limitado a "+ frmQ.format(acompanhamento.getMax()) +" itens!", Toast.LENGTH_SHORT).show();
                    return false;
                } else return true;
            } else {
                Toast.makeText(ctx, "Quantidade Limitada a "+frmQ.format(it.getMax())+" itens!", Toast.LENGTH_SHORT).show();
                return false ;
            }
        }

        private Double totalQuant(){
            Double lq = 0.0 ;
            for (Acompanhamento_Item it : list){
                lq = lq + it.getQuantidade() ;
            }
            return lq ;
        }
    }

    public abstract void onChange() ;
}
