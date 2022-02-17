package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.PedidoItem;

import java.text.DecimalFormat;
import java.util.List;

public class PedidoEnvioAdapter extends RecyclerView.Adapter<PedidoEnvioAdapter.PedidoEnvioHold>{

    private Context ctx ;
    private List<PedidoItem> list ;
    private EdicaoItem edicaoItem ;

    public PedidoEnvioAdapter(Context ctx, List<PedidoItem> list, EdicaoItem edicaoItem) {
        this.ctx = ctx;
        this.list = list;
        this.edicaoItem = edicaoItem ;
    }

    @NonNull
    @Override
    public PedidoEnvioHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_pedidoitem, null);
        return new PedidoEnvioHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoEnvioHold holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PedidoEnvioHold extends RecyclerView.ViewHolder{

        TextView textViewPedidoItem ;
        TextView textIemPrdObs ;
        TextView textQPrPedidoItem ;
        ImageButton ib_mais ;
        ImageButton ib_menos ;
        ImageButton ib_editar ;

        public PedidoEnvioHold(@NonNull View itemView) {
            super(itemView);
            textViewPedidoItem = itemView.findViewById(R.id.textViewPedidoItem) ;
            textIemPrdObs = itemView.findViewById(R.id.textIemPrdObs) ;
            textQPrPedidoItem = itemView.findViewById(R.id.textQPrPedidoItem) ;
            ib_mais = itemView.findViewById(R.id.ib_mais) ;
            ib_menos = itemView.findViewById(R.id.ib_menos) ;
            ib_editar = itemView.findViewById(R.id.ib_editar) ;
        }


        public void bind(PedidoItem pit){
            final PedidoItem item = pit;
            setValores(item);
            ib_mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Double q = item.getItenSelect().getQuantidade() + 1;
                    edicaoItem.mais(item, q);
                    setValores(item);
                }
            }); ;
            ib_menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Double q = item.getItenSelect().getQuantidade() - 1 ;
                    edicaoItem.menos(item, q);
                    setValores(item);
                }
            });
            ib_editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edicaoItem.editar(item);
                }
            });
        }

        private void setValores(PedidoItem p) {
            DecimalFormat frm = new DecimalFormat("R$ #0.00");
            DecimalFormat qrm = new DecimalFormat("#0.###");
            textViewPedidoItem.setText(p.getItenSelect().getProduto().getDescricao());
            String txt = qrm.format(p.getItenSelect().getQuantidade())+" X "+
                    frm.format(p.getItenSelect().getPreco())+" = "+
                    frm.format(p.getItenSelect().getValor()) ;
            textQPrPedidoItem.setText(txt);
            textIemPrdObs.setText(p.getItenSelect().getObs());

        }

    }
}
