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
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;
import com.anequimplus.entity.ContaPedidoPagamento;
import com.anequimplus.tipos.TipoModalidade;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class ContaPedidoAdapterListFechada extends RecyclerView.Adapter<ContaPedidoAdapterListFechada.ContaPedidoHolder>{

    private Context ctx ;
    private List<ContaPedido> list ;

    public ContaPedidoAdapterListFechada(Context ctx, List<ContaPedido> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public ContaPedidoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_conta_fechada_list, null);
        return new ContaPedidoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContaPedidoHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContaPedidoHolder extends RecyclerView.ViewHolder{

        TextView textContaPedido ;
        TextView listItensPedido ;
        TextView textContaTotal ;
        TextView textPagamento ;
        ImageButton recibo ;
        int tamcol = 40 ;

        public ContaPedidoHolder(@NonNull View itemView) {
            super(itemView);
            textContaPedido = itemView.findViewById(R.id.textContaPedido) ;
            listItensPedido = itemView.findViewById(R.id.listItensPedido) ;
            textContaTotal  = itemView.findViewById(R.id.textContaTotal) ;
            textPagamento   = itemView.findViewById(R.id.textPagamento) ;
            recibo = itemView.findViewById(R.id.rebibo_conta) ;
            recibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecibo(list.get(getAdapterPosition())) ;
                }
            });
        }

        public void bind(ContaPedido c){
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm:ss") ;
            textContaPedido.setText("Conta : "+c.getPedido()+"\n"+
                            "ABERTURA...: "+ UtilSet.getDiaSemana(c.getData())+" "+dt.format(c.getData())+"\n"+
                            "FECHAMENTO.: "+ UtilSet.getDiaSemana(c.getData_fechamento())+" "+dt.format(c.getData_fechamento()));
            listItensPedido.setText(getItens(c));
            textContaTotal.setText(getTotais(c));
            textPagamento.setText(getPagamentos(c));
            if (c.getTotalPagamentos() > 0){
                textPagamento.setVisibility(View.VISIBLE);
            } else {
                textPagamento.setVisibility(View.GONE);
            }
        }

        private String getTotais(ContaPedido cp){
            String txt = reperir("=", tamcol) ;
            txt = txt + "\n" + getTotais("SUB-TOTAL R$", cp.getTotalItens()) ;
            txt = txt + "\n" + getTotais("DESCONTO  R$", cp.getDesconto()) ;
            txt = txt + "\n" + getTotais("COMISSÃO  R$", cp.getTotalComissao()) ;
            txt = txt + "\n" + getTotais("TOTAL  R$", cp.getTotal()) ;
            txt = txt + "\n" + reperir("=", tamcol) ;
            return txt ;
        }

        private String getTotais(String t, Double v){
            DecimalFormat vf = new DecimalFormat("#0.00") ;
            return t + reperir(" ", tamcol - (t.length() + vf.format(v).length())) + vf.format(v) ;
        }

        private String getPagamentos(ContaPedido cp){
            String txt = "" ;
            txt = reperir("=", tamcol) ;
            Double vls = 0.0 ;
            Double vlTroco = 0.0 ;
            Double vlDesconto = 0.0 ;
            for (ContaPedidoPagamento it : cp.getListPagamento()){
                if (it.getStatus() == 1) {
                    if ((it.getModalidade().getTipoModalidade() != TipoModalidade.pgTroco)) {
                        String ds = it.getModalidade().getDescricao() + " R$";
                        txt = txt + "\n" + getTotais(ds, it.getValor());
                        vls = vls + it.getValor();
                    }
                    if (it.getModalidade().getTipoModalidade() == TipoModalidade.pgTroco) {
                        vlTroco = vlTroco + it.getValor() ;
                    }
                    if (it.getModalidade().getTipoModalidade() == TipoModalidade.pgDesconto) {
                        vlDesconto = vlDesconto + it.getValor() ;
                    }
                }
            }
            if (vlTroco > 0) {
                txt = txt + "\n" + getTotais("TROCO", vlTroco) ;
            }
            txt = txt + "\n" + reperir("-", tamcol) ;
            txt = txt + "\n" + getTotais("TOTAL", vls - vlTroco - vlDesconto) ;
            txt = txt + "\n" + reperir("=", tamcol) ;
            return txt ;
        }

        private String getItens(ContaPedido cp){
            DecimalFormat qf = new DecimalFormat("#0.###") ;
            DecimalFormat vf = new DecimalFormat("R$ #0.00") ;
            String txt = "" ;
            String ds = "" ;
            String vl = "" ;
            for (ContaPedidoItem it : cp.getListContaPedidoItemAtivosAgrupados()){
                ds  = it.getProduto().getDescricao().trim() ;
                vl  = qf.format(it.getQuantidade())+" X "+ vf.format(it.getPreco())+" = "+vf.format(it.getValor()) ;
                if ((ds.length() + vl.length()) < tamcol) {
                    txt = txt + ds + reperir(" ", tamcol - (vl.length() + ds.length())) + vl + "\n";
                } else {
                    ds = ds + reperir(" ", tamcol - ds.length()) ;
                    txt = txt + ds + "\n" + reperir(" ", tamcol - vl.length()) + vl + "\n";
                }
            }
            return  txt ;
        }


        private String reperir(String s, int v){
            String x = "" ;
            for (int i = 0 ; i < v ; i++){
                x = x + s ;
            }
            return x ;
        }


    }

    public abstract void onRecibo(ContaPedido cp) ;

}
