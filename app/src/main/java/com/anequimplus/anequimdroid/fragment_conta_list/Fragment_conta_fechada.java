package com.anequimplus.anequimdroid.fragment_conta_list;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anequimplus.adapter.ContaPedidoAdapterListFechada;
import com.anequimplus.anequimdroid.ActivityDisplayNFce;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.builds.BuildCaixa;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.builds.BuildContaPedidoNFCe;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.listeners.ListenerContaPedidoNfce;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.ContaPedidoNFCe;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.nfce.BuildEmitirNFce;
import com.anequimplus.nfce.ListenerEmitirNFce;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.DisplaySet;
import com.anequimplus.utilitarios.UtilSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Fragment_conta_fechada extends Fragment {

    private RecyclerView grade_conta_list ;
    private ContaPedido contaPedido ;
    private ContaPedidoNFCe contaPedidoNFCe ;
    private Caixa caixa = null ;
    private EditText filtro ;
    private ImageButton contasFechadasBusca;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conta_fechada, container, false);
        grade_conta_list = view.findViewById(R.id.grade_conta_list_fechada) ;

        filtro = view.findViewById(R.id.filtro_conta_fechadas) ;
        contasFechadasBusca = view.findViewById(R.id.contasFechadasBusca) ;
        contasFechadasBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCaixa() ;
            }
        });


        return view ;
    }

    private void getCaixa() {
        FilterTables filter = new FilterTables() ;
        filter.add(new FilterTable("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(getContext()))));
        filter.add(new FilterTable("STATUS", "=", "1"));
        new BuildCaixa(getContext(), filter, "", new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                if (l.size()>0){
                    caixa = l.get(0) ;
                }
                carregar(getContext());
            }

            @Override
            public void erro(String msg) {

            }
        }).executar();
    }

    private void carregar(Context ctx){
        List<FilterTable> filterTables = new ArrayList<FilterTable>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date ini = new Date() ;
        if (caixa != null)
          ini = caixa.getData();
         else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date()) ;
            gc.add(Calendar.HOUR,-12) ;
            ini = gc.getTime() ;
        }
        Date fim = new Date();
        Log.i("filtrodata", df.format(ini)+" "+df.format(fim) ) ;
        if (filtro.getText().toString().length() > 0){
            filterTables.add(new FilterTable("PEDIDO", "=", filtro.getText().toString())) ;
        }
        filterTables.add(new FilterTable("STATUS", "=", "2")) ;
        filterTables.add(new FilterTable("DATA_FECHAMENTO BETWEEN ", "'"+df.format(ini)+"'"+" AND ", "'"+df.format(fim)+"'")) ;
        new BuildContaPedido(ctx, filterTables, "DATA_FECHAMENTO DESC", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(DisplaySet.getNumeroDeColunasGrade(ctx), StaggeredGridLayoutManager.VERTICAL);
                grade_conta_list.setLayoutManager(layoutManager);
                grade_conta_list.setAdapter(new ContaPedidoAdapterListFechada(getContext(), l) {
                    @Override
                    public void onRecibo(ContaPedido cp) {
                        contaPedido = cp ;
                        validaEmissaoRecibo() ;
                    }
                });
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        }).executar();

    }

    private void validaEmissaoRecibo(){
     if (Configuracao.getOpcaoNFCE(getContext()) == 0)
        alert("Recibo Já Emitido!");
      else reciboClick() ;

    }

    private void reciboClick(){
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("CONTA_PEDIDO_ID", "=", String.valueOf(contaPedido.getId())));
        filters.add(new FilterTable("STATUS", "=", "1"));
        new BuildContaPedidoNFCe(getContext(), filters, "CONTA_PEDIDO_ID", new ListenerContaPedidoNfce() {
            @Override
            public void ok(List<ContaPedidoNFCe> l) {
                if (l.size()>0){
                    contaPedidoNFCe = l.get(0) ;
                }  else contaPedidoNFCe =null ;
                emissaoRecibo() ;

            }

            @Override
            public void erro(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }).executar();
    }



    public void emissaoRecibo() {
        if (contaPedidoNFCe == null) {
            new BuildEmitirNFce(getContext(), contaPedido, new ListenerEmitirNFce() {
                @Override
                public void ok(ContaPedidoNFCe it) {
                   // contaPedidoNFCe = it ;
                    displayNfce(it.getUUID());
                }

                @Override
                public void erro(String msg) {
                    alert(msg) ;
                    //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }).executar();

        } else  displayNfce(contaPedidoNFCe.getUUID());
    }

    private void alert(String msg){
      new AlertDialog.Builder(getContext())
            .setTitle("Atenção")
            .setMessage(msg)
            .setPositiveButton("OK",null).show();
    }

    private void displayNfce(String uuid){
        Bundle params = new Bundle();
        Intent intent = new Intent(getContext(), ActivityDisplayNFce.class);
        params.putString("UUID", uuid);
        intent.putExtras(params);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        carregar(getContext());
    }
}