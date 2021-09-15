package com.anequimplus.anequimdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.ado.Dao;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.VendaVista;
import com.anequimplus.entity.VendaVistaPagamento;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ActivityVendaVistaPagamento extends AppCompatActivity {

    private Toolbar toolbar  ;
    private VendaVista vendaVista ;
    private List<VendaVistaPagamento> vendaVistaPagamentoList ;
    private TextView textViewDisplayPagamento ;
    private TextView textViewTotalVendaVista ;
    private ListView listViewModVendaVista ;
    private DecimalFormat frm ;
    private final int VALOR_PARCIAL = 6 ;
    private final int SELECAO_MODALIDADE = 7 ;

    private Button bparcial ;
    private Button btotal ;
    private double pago  ;
    private double total ;
    private double valorPagamento ;
    private Modalidade modalidade ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_vista_pagamento);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Pagamento Venda Vista");
//        vendaVista = Dao.getVendaVistaADO(this).getVendaVista(getIntent().getIntExtra("VENDA_VISTA_ID",0)) ;
        frm = new DecimalFormat("R$ #0.00");
        textViewDisplayPagamento = (TextView) findViewById(R.id.textViewDisplayPagamento) ;
        textViewTotalVendaVista = (TextView) findViewById(R.id.textViewTotalVendaVista) ;
        listViewModVendaVista = (ListView) findViewById(R.id.listViewModVendaVista) ;
        listViewModVendaVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        bparcial = (Button) findViewById(R.id.buttonParcial) ;
        bparcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValorParcial() ;
            }
        });
        btotal   = (Button) findViewById(R.id.buttonTotal) ;
        btotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValorTotal() ;
            }
        });

        listViewModVendaVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                excluir(vendaVistaPagamentoList.get(i)) ;
            }
        });

    }

    private void excluir(VendaVistaPagamento vendaVistaPg) {
        final VendaVistaPagamento vpg = vendaVistaPg ;
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle(vpg.getModalidade().getDescricao())
                .setMessage("Deseja Cancelar?")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("PAGAMENTO", vpg.getModalidade().getId()+" "+vpg.getModalidade().getDescricao()+" "+vpg.getStatus());
                        Dao.getVendaVistaPagamentoADO(getBaseContext()).excluir(vpg); ;
                        display();
                    }
                }).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            vendaVista = Dao.getVendaVistaADO(this).getVendaVista(getIntent().getIntExtra("VENDA_VISTA_ID",0)) ;
            toolbar.setTitle("Pagamento Venda a Vista");
            display() ;
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        /*
        if (item.getItemId() == R.id.action_modalidade){
        }
         */
        return true;
    }


    private void display() {
        vendaVistaPagamentoList = Dao.getVendaVistaPagamentoADO(this).getList(vendaVista) ;
        pago  = getTotalPago() ;
        total = vendaVista.getTotalVenda() ;

        textViewTotalVendaVista.setText(frm.format(total)) ;
        String txt = "" ;
        if (total > pago){
            txt = "RESTA "+frm.format(total-pago) ;
            btotal.setText(frm.format(total-pago));
        } else {
            txt = "TROCO "+frm.format(pago - total) ; ;
        }

        textViewDisplayPagamento.setText(txt);
        //listViewModVendaVista.setAdapter(new ModalidadeAdapter(getBaseContext(),Dao.getModalidadeADO(getBaseContext()).getList()));
        listViewModVendaVista.setAdapter(new RecebimentoAdapter(this, getOk(vendaVistaPagamentoList)));
    }

    private List<VendaVistaPagamento> getOk(List<VendaVistaPagamento> list){
        List<VendaVistaPagamento> ls = new ArrayList<VendaVistaPagamento>() ;
        for (VendaVistaPagamento iTp : list){
            if (iTp.getStatus() == 1){
                ls.add(iTp) ;
            }
        }
        return ls ;

    }

    private double getTotalPago(){
      double pg = 0 ;
      for (VendaVistaPagamento p : vendaVistaPagamentoList) {
          Log.i("PAGAMENTO", p.getModalidade().getId()+" "+p.getModalidade().getDescricao()+" "+p.getStatus());
            if (p.getStatus() == 1) {
                pg = pg + p.getValor() ;
            }
        }
      return pg ;
    }

    private void setValorParcial(){
        if (total > pago) {
            Intent intent = new Intent(getBaseContext(), ActivityValor.class);
            Bundle params = new Bundle();
            double v = total - pago ;
            params.putString("TITULO", "Pagamento: ");
            params.putString("SUBTITULO", frm.format(v));
            params.putDouble("VALOR", v);
            intent.putExtras(params);
            startActivityForResult(intent, VALOR_PARCIAL);
        }
    }

    private void setValorTotal(){
        if (total > pago) {
            valorPagamento = total - pago ;
            setModalidade();
        }
    }

    private void setModalidade(){
        double v = total - pago;
        Intent intent = new Intent(getBaseContext(), ActivityPagamento.class);
        Bundle params = new Bundle();
        params.putDouble("VALOR", v);
        intent.putExtras(params);
        startActivityForResult(intent, SELECAO_MODALIDADE);
    }

    private void setRecebimento(){
        VendaVistaPagamento it = new VendaVistaPagamento(0,vendaVista, modalidade, valorPagamento, 1) ;
        Dao.getVendaVistaPagamentoADO(this).inserir(it);
        display();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == VALOR_PARCIAL){
            if (resultCode == RESULT_OK){
                valorPagamento = data.getDoubleExtra("VALOR",0) ;
               // Toast.makeText(this, "VALOR_PARCIAL RESULT_OK "+data.getDoubleExtra("VALOR",0), Toast.LENGTH_LONG).show();
                setModalidade();
            } else {
                display();
            }
        }

        if (requestCode == SELECAO_MODALIDADE){
            if (resultCode == RESULT_OK){
                modalidade = Dao.getModalidadeADO(this).getModalidade(data.getIntExtra("MODALIDADE_ID",0)) ;
                setRecebimento() ;
            } else {
                display();
            }
        }

       // super.onActivityResult(requestCode, resultCode, data);
    }

    private class RecebimentoAdapter extends BaseAdapter{
        private Context ctx ;
        private List<VendaVistaPagamento> list ;

        public RecebimentoAdapter(Context ctx, List<VendaVistaPagamento> list) {
            this.ctx = ctx ;
            this.list = list ;
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
            View  row = inflater.inflate(R.layout.layout_grade_pagamento_venda, null) ;
            VendaVistaPagamento v = list.get(i) ;
            TextView modalidade = row.findViewById(R.id.textViewVendaModalidade);
            TextView valor = row.findViewById(R.id.textViewVendaValor);
            modalidade.setText(v.getModalidade().getDescricao());
            DecimalFormat frm = new DecimalFormat("R$  #0.00") ;
            valor.setText(frm.format(v.getValor()));
            return row;
        }
    }
}
