package com.anequimplus.anequimdroid.fragment_config;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.conexoes.ConexaoTeste;
import com.anequimplus.utilitarios.Configuracao;

import java.net.MalformedURLException;

public class FragmentConfigConexao extends Fragment {


    private CheckBox checkedContaCompartilhada ;
    private EditText linkContaCompartilhada ;
    private Button testar_conexao ;
    private RadioButton api_v13 ;
    private RadioButton api_v14 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config_conexao, container, false);
        linkContaCompartilhada = view.findViewById(R.id.linkContaCompartilhada) ;
        linkContaCompartilhada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Configuracao.setLinkContaCompartilhada(getContext(), editable.toString()) ;

            }
        });
        checkedContaCompartilhada = (CheckBox) view.findViewById(R.id.checkedContaCompartilhada);
        checkedContaCompartilhada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.checkedContaCompartilhada:
                        Configuracao.setPedidoCompartilhado(getContext(), checkedContaCompartilhada.isChecked());
                        break;
                    default:
                        Toast.makeText(getContext(), "Nenhum Cheched Setado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        testar_conexao = view.findViewById(R.id.testar_conexao) ;
        testar_conexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new ConexaoTeste(getContext()) {
                        @Override
                        public void retorno(String msg) {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }.execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erro "+e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

        });
        api_v13 = view.findViewById(R.id.api_v13) ;
        api_v13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.api_v13)
                  Configuracao.setApiVersao(getContext(), "V13") ;
            }
        });
        api_v14 = view.findViewById(R.id.api_v14) ;
        api_v14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.api_v14)
                    Configuracao.setApiVersao(getContext(), "V14") ;
            }
        });

        getValores();
        return view ;
    }

    private void getValores(){
        linkContaCompartilhada.setText(Configuracao.getLinkContaCompartilhada(getContext()));
        checkedContaCompartilhada.setChecked(Configuracao.getPedidoCompartilhado(getContext()));
        setVersao() ;
    }

    private void setVersao(){
        String v = Configuracao.getApiVersao(getContext());
        switch (v) {
          case "V13": api_v13.setChecked(true);
          break;
          case "V14": api_v14.setChecked(true);
          break;
          default: Toast.makeText(getContext(), "Opção de Versão Inválida", Toast.LENGTH_SHORT).show();
        }

    }

}