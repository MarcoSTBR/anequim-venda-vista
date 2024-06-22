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
import com.anequimplus.entity.PedidoItemAcomp;

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
                    Double q = item.getItemSelect().getQuantidade() + 1;
                    edicaoItem.mais(item, q);
                    setValores(item);
                }
            }); ;
            ib_menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Double q = item.getItemSelect().getQuantidade() - 1 ;
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
            textViewPedidoItem.setText(p.getItemSelect().getProduto().getDescricao());
            String acomp = "" ;
            Double vacomp = 0.0 ;
            for (PedidoItemAcomp ac : p.getAcompanhamentos()){
                String aux = qrm.format(ac.getItemSelect().getQuantidade())+" "+
                        ac.getItemSelect().getProduto().getDescricao() ;
                if (ac.getItemSelect().getValor() > 0)
                    aux = aux + " - " + frm.format(ac.getItemSelect().getValor()) ;
                if (acomp.length() == 0)
                   acomp = acomp + aux  ;
                 else acomp = acomp + "\n" + aux  ;
                vacomp = vacomp + ac.getItemSelect().getValor() ;
            }

            String txt = qrm.format(p.getItemSelect().getQuantidade())+" X "+
                    frm.format(p.getItemSelect().getPreco())+" = "+
                    frm.format(p.getItemSelect().getValor()) ;
            if (vacomp > 0) {
                txt = txt +" Add " + frm.format(vacomp) ;
            }
            textQPrPedidoItem.setText(txt);
            String obs = "" ;
            if (p.getItemSelect().getObs().length() > 0)
                obs = p.getItemSelect().getObs() ;
            if (acomp.length()  > 0) {
                if (obs.length() == 0)
                    obs = obs + acomp ;
                 else obs = obs + "\n" + acomp ;
            }
            if ( obs.length() == 0) {
                textIemPrdObs.setVisibility(View.GONE);
            } else {
                textIemPrdObs.setVisibility(View.VISIBLE);
                textIemPrdObs.setText(obs);
            }

        }

    }
}
