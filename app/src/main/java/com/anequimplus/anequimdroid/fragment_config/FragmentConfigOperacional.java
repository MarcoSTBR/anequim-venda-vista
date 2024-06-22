package com.anequimplus.anequimdroid.fragment_config;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.utilitarios.Configuracao;
import com.anequimplus.utilitarios.UtilSet;

public class FragmentConfigOperacional extends Fragment{


    TextView mac ;
    CheckBox checked_acumular_pedidos ;
    CheckBox conf_conta_cliente_pergunta_nfce ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config_operacional, container, false);
        mac = view.findViewById(R.id.textViewMac) ;
        mac.setText("MAC : "+UtilSet.getMAC(getContext()));

        checked_acumular_pedidos = view.findViewById(R.id.checked_acumular_pedidos) ;
        checked_acumular_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.checked_acumular_pedidos) {
                    String a = "Desativado" ;
                    if (checked_acumular_pedidos.isChecked())
                        a = "Ativado" ;
                    Toast.makeText(getContext(), a, Toast.LENGTH_SHORT).show();
                    Configuracao.setAcumulaPedidos(getContext(), checked_acumular_pedidos.isChecked());
                }
            }
        });
        conf_conta_cliente_pergunta_nfce = view.findViewById(R.id.conf_conta_cliente_pergunta_nfce) ;
        conf_conta_cliente_pergunta_nfce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.conf_conta_cliente_pergunta_nfce) {
                    String a = "Desativado" ;
                    if (conf_conta_cliente_pergunta_nfce.isChecked())
                        a = "Ativado" ;
                    Toast.makeText(getContext(), a, Toast.LENGTH_SHORT).show();
                    Configuracao.setConf_conta_cliente_pergunta_nfce(getContext(), conf_conta_cliente_pergunta_nfce.isChecked());
                }
            }
        });
        setParam() ;
        return view ;
    }

    private void setParam(){
        checked_acumular_pedidos.setChecked(Configuracao.getAcumulaPedidos(getContext()));
        conf_conta_cliente_pergunta_nfce.setChecked(Configuracao.getConf_conta_cliente_pergunta_nfce(getContext()));
    }

}