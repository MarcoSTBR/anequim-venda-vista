package com.anequimplus.anequimdroid.fragment_config;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.nfce.ConexaoCloudNFceCertificado;
import com.anequimplus.nfce.ConexaoConfiguracaoCloudNFCe;
import com.anequimplus.nfce.ListenerCloudNFCeCertificado;
import com.anequimplus.nfce.ListenerConfiguracaoCloudNFCe;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.ConfiguracaoCloudNFceNFCe;
import com.anequimplus.utilitarios.UtilSet;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentNFCe extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup nfce_opcao ;
    private RadioButton nfce_nenhum ;
    private RadioButton nfce_console ;
    private RadioButton nfce_cloud_nfce ;
    private LinearLayout layout_cloud_nfce ;
    private Button recuperar_token ;
    private TextView cloud_nfce_token ;
    private EditText editCloudNFeSerie ;
    private EditText editCloudNFeNumero ;
    private RadioGroup cloudNFe_ambiente ;
    private RadioButton cloudNFe_ambiente_homologacao ;
    private RadioButton cloudNFe_ambiente_producao ;
    private Button teste_certificado_cloudnfce ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_n_f_ce, container, false);
        layout_cloud_nfce = view.findViewById(R.id.layout_cloud_nfce) ;
        cloud_nfce_token  = view.findViewById(R.id.cloud_nfce_token) ;
        nfce_opcao        = view.findViewById(R.id.nfce_opcao) ;
        nfce_opcao.setOnCheckedChangeListener(this);
        cloudNFe_ambiente = view.findViewById(R.id.cloudNFe_ambiente) ;
        cloudNFe_ambiente.setOnCheckedChangeListener(this);
        nfce_nenhum     = view.findViewById(R.id.nfce_nenhum) ;
        nfce_console    = view.findViewById(R.id.nfce_console) ;
        nfce_cloud_nfce = view.findViewById(R.id.nfce_cloud_nfce) ;
        cloudNFe_ambiente_homologacao  = view.findViewById(R.id.cloudNFe_ambiente_homologacao) ;
        cloudNFe_ambiente_producao     = view.findViewById(R.id.cloudNFe_ambiente_producao) ;
        recuperar_token = view.findViewById(R.id.recuperar_token) ;
        recuperar_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterTables f = new FilterTables() ;
                f.add(new FilterTable("CNPJ", "=", UtilSet.getCnpj(getContext())));
                try {
                    new ConexaoConfiguracaoCloudNFCe(getContext(), f, new ListenerConfiguracaoCloudNFCe() {
                        @Override
                        public void ok(String token) {
                            //Toast.makeText(getContext(), token, Toast.LENGTH_LONG).show();
                            ConfiguracaoCloudNFceNFCe.setCloudNfce_token(getContext(), token);
                            setToken(token) ;
                            getCertificado() ;
                        }

                        @Override
                        public void erro(int cod, String msg) {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }).getToken();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        editCloudNFeSerie  = view.findViewById(R.id.editCloudNFeSerie) ;
        editCloudNFeSerie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")){
                    ConfiguracaoCloudNFceNFCe.setCloudNfce_serie(getContext(), Integer.valueOf(s.toString()));
                }

            }
        });
        editCloudNFeNumero = view.findViewById(R.id.editCloudNFeNumero) ;
        editCloudNFeNumero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")){
                    ConfiguracaoCloudNFceNFCe.setCloudNfce_numero(getContext(), Integer.valueOf(s.toString()));
                }
            }
        });
        setSerieNumero() ;
        setOpcoes() ;
        setToken(ConfiguracaoCloudNFceNFCe.getCloudNfce_token(getContext())) ;
        teste_certificado_cloudnfce = view.findViewById(R.id.teste_certificado_cloudnfce) ;
        teste_certificado_cloudnfce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCertificado() ;
            }
        });
        return view ;
    }

    private void getCertificado() {
        try {
            new ConexaoCloudNFceCertificado(getContext(), new ListenerCloudNFCeCertificado() {
                @Override
                public void ok(String msg, Date validade) {
                    ConfiguracaoCloudNFceNFCe.setCertificadoValidade(getContext(), validade) ;
                    SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy") ;
                    Toast.makeText(getContext(), msg+" validade "+sd.format(ConfiguracaoCloudNFceNFCe.getCertificadoValidade(getContext())), Toast.LENGTH_LONG).show(); ;
                }


                @Override
                public void erro(int cod, String msg) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                }
            }).execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setSerieNumero() {
        editCloudNFeSerie.setText(ConfiguracaoCloudNFceNFCe.getCloudNfce_serie(getContext()).toString()) ;
        editCloudNFeNumero.setText(ConfiguracaoCloudNFceNFCe.getCloudNfce_numero(getContext()).toString()) ;
    }

    private void setToken(String token){
        cloud_nfce_token.setText(token);
    }

    private void setOpcoes() {
        int op = Configuracao.getOpcaoNFCE(getContext()) ;
        switch (op){
            case 1 : nfce_console.setChecked(true);
            break;
            case 2 : nfce_cloud_nfce.setChecked(true);
            break;
            default: nfce_nenhum.setChecked(true);
        }
        if (ConfiguracaoCloudNFceNFCe.getOpcaoAmbiente(getContext()) == 0){
            cloudNFe_ambiente_homologacao.setChecked(true);
        } else {
            cloudNFe_ambiente_producao.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.nfce_console : Configuracao.setOpcaoNFCE(getContext(), 1);
                layout_cloud_nfce.setVisibility(View.GONE);
                break;

            case R.id.nfce_cloud_nfce : Configuracao.setOpcaoNFCE(getContext(), 2);
                layout_cloud_nfce.setVisibility(View.VISIBLE);
                break;

            case R.id.nfce_nenhum: Configuracao.setOpcaoNFCE(getContext(), 0);
                layout_cloud_nfce.setVisibility(View.GONE);
                break;

            case R.id.cloudNFe_ambiente_homologacao: ConfiguracaoCloudNFceNFCe.setOpcaoAmbiente(getContext(), 0);
                break;

            case R.id.cloudNFe_ambiente_producao:  ConfiguracaoCloudNFceNFCe.setOpcaoAmbiente(getContext(), 1);
                break;

            default: Toast.makeText(getContext(), "Nenhuma Opção Selecionada", Toast.LENGTH_SHORT).show();
        }
    }
}