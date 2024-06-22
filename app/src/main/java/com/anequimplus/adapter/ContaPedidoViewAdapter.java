package com.anequimplus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.ContaPedidoItem;

import java.text.DecimalFormat;
import java.util.List;

public abstract class ContaPedidoViewAdapter extends RecyclerView.Adapter<ContaPedidoViewAdapter.ContaPedidoViewHolder>{

    private List<ContaPedidoItem> list ;

    public ContaPedidoViewAdapter(List<ContaPedidoItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ContaPedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_conta_view, null);

        return new ContaPedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContaPedidoViewHolder holder, int position) {
        holder.bind(list.get(position)) ;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContaPedidoViewHolder extends RecyclerView.ViewHolder{

        TextView txtDescricao ;
        TextView txtQPreco ;
        ImageButton bCancelar ;
        ImageButton bTransferencia ;

        public ContaPedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.TextViewContaView) ;
            txtQPreco = itemView.findViewById(R.id.textQuantidadeValorContaItem) ;
            bCancelar  = itemView.findViewById(R.id.cancelarItemConta) ;
            bTransferencia = itemView.findViewById(R.id.transferenciaItemConta) ;
        }

        public void bind(final ContaPedidoItem it){

            DecimalFormat q = new DecimalFormat("#0.###") ;
            DecimalFormat v = new DecimalFormat("#0.00") ;
            txtDescricao.setText(it.getProduto().getDescricao());
            String pvl = q.format(it.getQuantidade())+" X "+v.format(it.getPreco())+" = "+v.format(it.getValor()) ;
            txtQPreco.setText(pvl);
            bCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelar(it) ;
                }
            });
            bTransferencia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferir(it) ;
                }
            });

        }
    }

    public abstract void cancelar(ContaPedidoItem item) ;
    public abstract void transferir(ContaPedidoItem item) ;

}
