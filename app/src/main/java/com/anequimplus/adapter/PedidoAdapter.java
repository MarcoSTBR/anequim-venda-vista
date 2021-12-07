package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;

import java.text.DecimalFormat;
import java.util.List;

public abstract class PedidoAdapter extends BaseAdapter {

    private Context ctx ;
    private List<Pedido> pedidos ;


    public PedidoAdapter(Context ctx, List<Pedido> pedidos) {
        this.ctx = ctx;
        this.pedidos = pedidos;
    }


    @Override
    public int getCount() {
        return pedidos.size();
    }

    @Override
    public Object getItem(int i) {
        return pedidos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return pedidos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        view  = inflater.inflate(R.layout.layout_grade_pedido, null) ;

        DecimalFormat frmV = new DecimalFormat("#0.00");
        DecimalFormat frmQ = new DecimalFormat("#0.###");

        final Pedido p = pedidos.get(i) ;
        TextView txt = view.findViewById(R.id.textViewLabelPed);
        TextView obs = view.findViewById(R.id.textViewGradePedidoQ);
        ImageButton bt = (ImageButton) view.findViewById(R.id.pedidoconta);
        ImageButton Abrir = (ImageButton) view.findViewById(R.id.pedidocontaabrir);
        txt.setText(" Pedido: "+p.getPedido());
        if (p.getStatus() == 2) {
            bt.setVisibility(View.VISIBLE);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setConta(p) ;
                }
            });
            Abrir.setVisibility(View.VISIBLE);
            Abrir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContaAbrir(p) ;
                }
            });


        } else {
            Abrir.setVisibility(View.GONE);
            bt.setVisibility(View.GONE);

        }
        double q = 0 ;
        double vl= 0 ;
        for (PedidoItem it : p.getListPedidoItem()){
            q = q + it.getItenSelect().getQuantidade();
            vl= vl + it.getItenSelect().getValor();
        }
        obs.setText("Iten's ("+frmQ.format(q)+") no valor de R$ "+frmV.format(vl));
        return view;
    }

    public abstract void setConta(Pedido p) ;
    public abstract void setContaAbrir(Pedido p) ;
}
