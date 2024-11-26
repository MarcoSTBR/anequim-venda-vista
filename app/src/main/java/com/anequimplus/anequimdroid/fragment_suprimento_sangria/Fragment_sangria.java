package com.anequimplus.anequimdroid.fragment_suprimento_sangria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.ImpressoraAdapter;
import com.anequimplus.adapter.ModalidadeSupSanAdapter;
import com.anequimplus.adapter.SangriaAdapter;
import com.anequimplus.anequimdroid.ActivityValor;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.builds.BuildCaixa;
import com.anequimplus.builds.BuildControleAcesso;
import com.anequimplus.builds.BuildSangria;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.Impressora;
import com.anequimplus.entity.Modalidade;
import com.anequimplus.entity.Sangria;
import com.anequimplus.impressao.BuilderControleImp;
import com.anequimplus.impressao.ControleImpressora;
import com.anequimplus.impressao.ListenerImpressao;
import com.anequimplus.listeners.ListenerCaixa;
import com.anequimplus.listeners.ListenerControleAcesso;
import com.anequimplus.listeners.ListenerSangria;
import com.anequimplus.tipos.TipoAlinhamento;
import com.anequimplus.utilitarios.DisplaySet;
import com.anequimplus.utilitarios.RowImpressao;
import com.anequimplus.utilitarios.UtilSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_sangria extends Fragment {

    private Spinner gradeModalidade;
    private Spinner sansun_impressora ;
    private EditText edit ;
    private Button button ;
    private Modalidade modalidade = null ;
    private Caixa caixa = null ;
    private Double valor = 0.0 ;
    private RecyclerView gradeSangria ;
    private final int VALOR = 1 ;
    private ControleImpressora controlImp = null ;
    private Impressora impressoraPadrao ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sangria, container, false);
        gradeModalidade = (Spinner) view.findViewById(R.id.grade_modalidade_sangria) ;
        gradeModalidade.setAdapter(new ModalidadeSupSanAdapter(getContext(), DaoDbTabela.getModalidadeADO(getContext()).getGradeList()));
        gradeModalidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modalidade = DaoDbTabela.getModalidadeADO(getContext()).getGradeList().get(position) ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edit   = view.findViewById(R.id.edit_desc_sangria) ;
        button = view.findViewById(R.id.botao_adicionar_san) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSangria() ;
            }
        });
        gradeSangria = view.findViewById(R.id.list_grade_sangria) ;
        sansun_impressora = view.findViewById(R.id.sansun_impressora) ;
        getImpressora() ;
        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        //caixa = DaoDbTabela.getCaixaADO(getContext()).getCaixaAberto(UtilSet.getUsuarioId(getContext())) ;
        getCaixa() ;
        //display() ;
    }

    private void getCaixa(){
        FilterTables f = new FilterTables() ;
        f.add(new FilterTable("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(getContext())))) ;
        f.add(new FilterTable("STATUS", "=", "1")) ;
        new BuildCaixa(getActivity(), f, "", new ListenerCaixa() {
            @Override
            public void ok(List<Caixa> l) {
                if (l.size()>0){
                    caixa = l.get(0) ;
                } else caixa = null ;
                display() ;
            }

            @Override
            public void erro(String msg) {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_notifications_black_24dp)
                        .setTitle("Atenção")
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("Ok", null).show();
            }
        }).executar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        controlImp.close();
    }

    private void getImpressora() {
        ImpressoraAdapter impAdp = new ImpressoraAdapter(getContext(), DaoDbTabela.getImpressoraADO(getContext()).getList()) ;
        impressoraPadrao = DaoDbTabela.getImpressoraADO(getContext()).getImpressora(UtilSet.getImpPadraoSuprimentoSangria(getContext())) ;
        sansun_impressora.setAdapter(impAdp);
        sansun_impressora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                impressoraPadrao = DaoDbTabela.getImpressoraADO(getContext()).getList().get(position) ;
                UtilSet.setImpPadraoSuprimentoSangria(getContext(), impressoraPadrao.getDescricao()) ;
                setImpressoraPadrao() ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setImpressoraPadrao();
    }

    private void setImpressoraPadrao() {
        String descImp = UtilSet.getImpPadraoSuprimentoSangria(getContext());
        impressoraPadrao = null ;
        for (int i = 0 ; i<sansun_impressora.getCount() ; i++){
            sansun_impressora.getItemAtPosition(i);
            Impressora impSp = (Impressora)sansun_impressora.getItemAtPosition(i) ;
            // Log.i("List Imp","i "+impSp.getDescricao()+" padrao "+descImp) ;
            if (descImp.equals(impSp.getDescricao())) {
                sansun_impressora.setSelection(i);
                impressoraPadrao = impSp ;
            }
        }
        instanciarImp(impressoraPadrao) ;
    }

    private void instanciarImp(Impressora imp){
        if (impressoraPadrao != null) {
            controlImp = BuilderControleImp.getImpressora(getContext(), impressoraPadrao);
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

    private void display(){
        FilterTables f = new FilterTables() ;
        String order = "ID" ;
        Log.i("sangria", "id caixa "+caixa.getId());
        f.add(new FilterTable("CAIXA_ID", "=", String.valueOf(caixa.getId())));
        new BuildSangria(getActivity(), f, order, new ListenerSangria() {
            @Override
            public void ok(List<Sangria> l) {
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(DisplaySet.getNumeroDeColunasGrade(getContext()), StaggeredGridLayoutManager.VERTICAL);
                gradeSangria.setLayoutManager(layoutManager);
                gradeSangria.setAdapter(new SangriaAdapter(getContext(), l) {
                    @Override
                    public void select(Sangria it) {
                        imprimir(it);
                    }
                }) ;
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }).executar();
    }

    private void setSangria() {
        new BuildControleAcesso(getContext(), 2, new ListenerControleAcesso() {
            @Override
            public void ok(int usuario_id) {
                modalidade = DaoDbTabela.getModalidadeADO(getContext()).getGradeList().get(gradeModalidade.getSelectedItemPosition()) ;
                String descricao = edit.getText().toString() ;
                if (descricao.length() == 0) edit.setText("Nenhuma") ;
                Intent intent = new Intent(getContext(), ActivityValor.class);
                Bundle params = new Bundle();
                params.putDouble("VALOR", 0.0);
                intent.putExtras(params);
                startActivityForResult(intent, VALOR);
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }).executar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VALOR) {
            if (resultCode == -1) {
                valor = data.getDoubleExtra("VALOR", 0.0) ;
                if (valor > 0){
                    addSangria() ;
                } else Toast.makeText(getContext(), "Valor Inválido!", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void addSangria(){
        Sangria it = new Sangria(0, edit.getText().toString(), new Date(), caixa.getId(), modalidade.getId(), valor, UtilSet.getUUID(), 1) ;
        new BuildSangria(getActivity(), it, new ListenerSangria() {
            @Override
            public void ok(List<Sangria> l) {
                imprimir(it) ;
                display() ;
            }

            @Override
            public void erro(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }).executar();
        /*
        DaoDbTabela.getSangriaADO(getContext()).incluir(it);
        imprimir(it) ;
        display() ;*/
    }

    private void imprimir(Sangria it){
        List<RowImpressao> lp = new ArrayList<RowImpressao>() ;
        DecimalFormat vf    = new DecimalFormat("R$ #0.00") ;
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm") ;

        int tamcol = impressoraPadrao.getTamColuna() ;
        lp.add(new RowImpressao(UtilSet.repetir("=", tamcol), TipoAlinhamento.ptLeft, 10)) ;
        lp.add(new RowImpressao("SANGRIA", TipoAlinhamento.ptCenter, 10)) ;
        lp.add(new RowImpressao(dt.format(it.getData()), TipoAlinhamento.ptCenter, 10)) ;
        lp.add(new RowImpressao(it.getDescricao(), TipoAlinhamento.ptCenter, 10)) ;
        lp.add(new RowImpressao(vf.format(it.getValor()), TipoAlinhamento.ptCenter, 10)) ;
        lp.add(new RowImpressao(UtilSet.repetir("=", tamcol), TipoAlinhamento.ptLeft, 10)) ;
        controlImp.impressaoLivre(lp);
    }
}