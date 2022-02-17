package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoItem;

import java.text.DecimalFormat;
import java.util.List;

public abstract class ContaPedidoAdapter extends BaseAdapter {

    private Context ctx ;
    private List<ContaPedido> list ;
    private List<String> listSelect ;
    private int orientation ;

    public ContaPedidoAdapter(Context ctx, List<ContaPedido> list, List<String> listSelect, int orientation) {
        this.orientation = orientation ;
        this.ctx = ctx;
        this.list = list;
        this.listSelect = listSelect ;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View row  = inflater.inflate(R.layout.layout_grade_conta, null) ;
        final ContaPedido cp = list.get(i);
        TextView ped = row.findViewById(R.id.TextViewConta);
        ped.setText("Pedido "+cp.getPedido()+" orientation "+ orientation) ;

        DecimalFormat frmQ = new DecimalFormat("#0.###");
        DecimalFormat frmV = new DecimalFormat("#0.00");
        String corpoDesc  = "" ;
        String corpoValor = "" ;
        for (ContaPedidoItem it : cp.getListContaPedidoItem()){

            String qv = frmQ.format(it.getQuantidade())+" x "+frmV.format(it.getPreco())+" = "+frmV.format(it.getValor()) ;
            corpoValor = corpoValor+qv+"\n" ;
            corpoDesc  = corpoDesc + it.getProduto().getDescricao()+"\n";
        }
        //final String txtCorpo = corpoDesc ;
        TextView corpDesc = row.findViewById(R.id.textViewContaCorpoDesc);
        TextView corpValor = row.findViewById(R.id.textViewContaCorpoValor);
        corpDesc.setText(corpoDesc);
        corpValor.setText(corpoValor);
        final LinearLayout gradedaconta = row.findViewById(R.id.gradedaconta) ;
        if (ifChecked(cp)) {
          //  corp.setText(txtCorpo);
            gradedaconta.setVisibility(row.VISIBLE);

        } else {
          //  corp.setText("");
            gradedaconta.setVisibility(row.GONE);
        }

        final CheckBox checkBox = row.findViewById(R.id.checkBoxConta);
        checkBox.setChecked(ifChecked(cp));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){
                    listSelect.add(cp.getPedido()) ;
                    gradedaconta.setVisibility(View.VISIBLE);
                    //corp.setText(txtCorpo);
                } else {
                    listSelect.remove(cp.getPedido());
                    gradedaconta.setVisibility(View.GONE);
                    //corp.setText("");
                }
                chagoCheckBox() ;
            }
        });
        String subtotal = frmV.format(cp.getTotalItens()) ;
        String comissao = frmV.format(cp.getTotalComissao()) ;
        String desconto = frmV.format(cp.getDesconto());
        String total = frmV.format(cp.getTotal()) ;
        String pagamento = frmV.format(cp.getTotalPagamentos());
        String tot = "" ;
        if (orientation == 1) {
              tot = "SUBTOTAL  : R$ " + stringOfChar(" ", 10 - subtotal.length()) + subtotal + "\n" +
                    "COMISSAO  : R$ " + stringOfChar(" ", 10 - comissao.length()) + comissao + "\n" +
                    "DESCONTO  : R$ " + stringOfChar(" ", 10 - desconto.length()) + desconto + "\n" +
                    "TOTAL     : R$ " + stringOfChar(" ", 10 - total.length()) + total + "\n" +
                    "PAGAMENTO : R$ " + stringOfChar(" ", 10 - pagamento.length()) + pagamento;
        } else {
              tot = "SUBTOTAL  : R$ " + stringOfChar(" ", 10 - subtotal.length()) + subtotal + " " +
                    "COMISSAO  : R$ " + stringOfChar(" ", 10 - comissao.length()) + comissao + "\n" +
                    "DESCONTO  : R$ " + stringOfChar(" ", 10 - desconto.length()) + desconto + " " +
                    "TOTAL     : R$ " + stringOfChar(" ", 10 - total.length()) + total + "\n" +
                    "PAGAMENTO : R$ " + stringOfChar(" ", 10 - pagamento.length()) + pagamento;

        }
        TextView totais = row.findViewById(R.id.textViewContaTotais);
        totais.setText(tot);


        return row;
    }

    private boolean ifChecked(ContaPedido c1){
        boolean r = false ;
        for (String c : listSelect){
            if (c.equals(c1.getPedido())) r = true ;
        }
        return r ;
    }

    private String stringOfChar(String f, int q){
        String t = "" ;
        for (int i=1 ; i <= q ; i++)
            t = t + f ;
        return t ;
    }
    protected abstract void chagoCheckBox();
}
