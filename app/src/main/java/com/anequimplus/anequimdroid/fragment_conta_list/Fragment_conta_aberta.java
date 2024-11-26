package com.anequimplus.anequimdroid.fragment_conta_list;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.anequimplus.adapter.ContaPedidoAdapterListAberto;
import com.anequimplus.anequimdroid.ActivityConta;
import com.anequimplus.anequimdroid.ActivityImprimirConta;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.builds.BuildContaPedido;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.listeners.ListenerContaPedido;
import com.anequimplus.utilitarios.DisplaySet;

import java.util.ArrayList;
import java.util.List;

public class Fragment_conta_aberta extends Fragment {

    private RecyclerView grade_conta_list ;
    private View view ;
    private EditText filtro ;
    private ImageButton contasAbertasBusca ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_conta_aberta, container, false) ;
        grade_conta_list = view.findViewById(R.id.grade_conta_list) ;
        filtro = view.findViewById(R.id.filtro_conta_aberta) ;
        contasAbertasBusca = view.findViewById(R.id.contasAbertasBusca) ;
        contasAbertasBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregar(getContext()) ;
            }
        });
        filtro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                carregar(getContext());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("pedido", "OnResume contas abertas") ;
       // carregar(getContext());
    }

    private void carregar(Context ctx){
        List<FilterTable> filters = new ArrayList<FilterTable>() ;
        if (filtro.getText().length() > 0)
            filters.add(new FilterTable("PEDIDO", "=", filtro.getText().toString())) ;
        filters.add(new FilterTable("STATUS", "=", "1")) ;
        new BuildContaPedido(getActivity(), filters, "", new ListenerContaPedido() {
            @Override
            public void ok(List<ContaPedido> l) {
                StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(DisplaySet.getNumeroDeColunasGrade(ctx), StaggeredGridLayoutManager.VERTICAL);
                grade_conta_list.setLayoutManager(layoutManager);
                grade_conta_list.setAdapter(new ContaPedidoAdapterListAberto(getContext(), l) {
                    @Override
                    public void onManutencao(ContaPedido cp) {
                        Intent intent = new Intent(getContext(), ActivityConta.class);
                        Bundle params = new Bundle();
                        params.putInt("CONTA_ID", cp.getId());
                        intent.putExtras(params);
                        startActivity(intent);
                    }

                    @Override
                    public void onConta(ContaPedido cp) {
                        Intent intent = new Intent(getContext(), ActivityImprimirConta.class);
                        Bundle params = new Bundle();
                        params.putInt("CONTA_ID", cp.getId());
                        intent.putExtras(params);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        }).executar();
/*

        try {
            new ConexaoContaPedido(getContext(), filters, "") {
                @Override
                public void oK(List<ContaPedido> l) {
                    StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(DisplaySet.getNumeroDeColunasGrade(ctx), StaggeredGridLayoutManager.VERTICAL);
                    grade_conta_list.setLayoutManager(layoutManager);
                    grade_conta_list.setAdapter(new ContaPedidoAdapterListAberto(getContext(), l) {
                        @Override
                        public void onManutencao(ContaPedido cp) {
                            Intent intent = new Intent(getContext(), ActivityConta.class);
                            Bundle params = new Bundle();
                            params.putInt("CONTA_ID", cp.getId());
                            intent.putExtras(params);
                            startActivity(intent);
                        }

                        @Override
                        public void onConta(ContaPedido cp) {
                            Intent intent = new Intent(getContext(), ActivityImprimirConta.class);
                            Bundle params = new Bundle();
                            params.putInt("CONTA_ID", cp.getId());
                            intent.putExtras(params);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void erro(String mgg) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
*/
    }

    private void alert(String msg){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Alerta")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
    }

/*
    public void getImpressora(){
        ImpressoraAdapter impAdp = new ImpressoraAdapter(getContext(), DaoDbTabela.getImpressoraADO(getContext()).getList()) ;
 //       impressoraPadrao = DaoDbTabela.getImpressoraADO(getContext()).getImpressora(UtilSet.getImpPadraoContaPedido(getContext())) ;
        impressoraPadrao = DaoDbTabela.getImpressoraADO(getContext()).getList().get(0);
        spinnerImp.setAdapter(impAdp);
        spinnerImp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                impressoraPadrao = DaoDbTabela.getImpressoraADO(getContext()).getList().get(i) ;
                UtilSet.setImpPadraoContaPedido(getContext(), impressoraPadrao.getDescricao()) ;
                setImpressoraPadrao() ;
                //UtilSet.setImpPadraoFechamento(getBaseContext(), impressoraPadrao.getDescricao()) ;
                //carregaRelatorio() ;
            }
        });
        setImpressoraPadrao();
    }

    private void setImpressoraPadrao() {
        String descImp = UtilSet.getImpPadraoContaPedido(getContext());
        for (int i = 0 ; i<spinnerImp.getCount() ; i++){
            spinnerImp.getItemAtPosition(i);
            Impressora impSp = (Impressora)spinnerImp.getItemAtPosition(i) ;
            // Log.i("List Imp","i "+impSp.getDescricao()+" padrao "+descImp) ;
            if (descImp.equals(impSp.getDescricao())) {
                spinnerImp.setSelection(i);
                impressoraPadrao = impSp ;
            }
        }
        instanciarImp(impressoraPadrao) ;
    }

    private void instanciarImp(Impressora impressoraPadrao) {
        if (impressoraPadrao != null) {
            controlImp = BuilderControleImp.getImpressora(getContext(), impressoraPadrao.getTipoImpressora());
            ListenerImpressao limp = new ListenerImpressao() {
                @Override
                public void onImpressao(int status) {
                    Toast.makeText(getContext(), "Impressão OK", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(int status, String messagem) {
                    Toast.makeText(getContext(), messagem, Toast.LENGTH_SHORT).show();

                }
            };
            controlImp.setListenerImpressao(limp);
            controlImp.open();
        }
    }
*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        carregar(getContext());
    }
}