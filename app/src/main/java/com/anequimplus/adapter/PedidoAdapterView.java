package com.anequimplus.adapter;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Pedido;

import java.text.DecimalFormat;
import java.util.List;

public abstract class PedidoAdapterView extends RecyclerView.Adapter<PedidoAdapterView.PedidoAdapterHolder>{

    private final List<Pedido> list ;

    public PedidoAdapterView(List<Pedido> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PedidoAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_pedido, null);

      //  LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
      //  View view  = inflater.inflate(R.layout.layout_grade_pedido, null) ;
        return new PedidoAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapterHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class PedidoAdapterHolder extends RecyclerView.ViewHolder{
        TextView txt ;
        ImageButton bt ;
        ImageButton Abrir ;
        TextView obs ;

        DecimalFormat frmV = new DecimalFormat("#0.00");
        DecimalFormat frmQ = new DecimalFormat("#0.###");

        public PedidoAdapterHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.textViewLabelPed);
            bt = (ImageButton) itemView.findViewById(R.id.pedidoconta);
            Abrir = (ImageButton) itemView.findViewById(R.id.pedidocontaabrir);
            obs = itemView.findViewById(R.id.textViewGradePedidoQ);
        }

        public void bind(Pedido p){
            Log.i("RecyclerView", p.getPedido()) ;
            txt.setText(" Conta: "+p.getPedido());
            obs.setText("Iten(s) ("+frmQ.format(p.getQuantidadeTotalItens())+")\nValor de R$ "+frmV.format(p.getValorTotalItens()));

            if (p.getStatus() == 1) {
                Abrir.setBackground(getNegativo(Color.GREEN, bt));
            }
            Abrir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContaAbrir(p) ;
                }
            });

            bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setConta(p) ;
                    }
            });
        }

        private ShapeDrawable getNegativo(@ColorInt int color, View v) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.setIntrinsicHeight(v.getHeight());
            shapeDrawable.setIntrinsicWidth(v.getWidth());
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }

    }

    public abstract void setConta(Pedido p) ;
    public abstract void setContaAbrir(Pedido p) ;

}
