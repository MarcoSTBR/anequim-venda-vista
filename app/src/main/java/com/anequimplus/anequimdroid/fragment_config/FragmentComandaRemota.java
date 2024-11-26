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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.builds.BuildSetorImp;
import com.anequimplus.builds.BuildSetorImpItens;
import com.anequimplus.builds.BuildSetorImpTerminal;
import com.anequimplus.conexoes.ConexaoServerSetorImp;
import com.anequimplus.conexoes.ConexaoServerSetorImpItens;
import com.anequimplus.conexoes.ConexaoServerSetorImpTerminal;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.SetorImp;
import com.anequimplus.entity.SetorImpItens;
import com.anequimplus.entity.SetorImpTerminal;
import com.anequimplus.listeners.ListenerSetorImp;
import com.anequimplus.listeners.ListenerSetorImpItens;
import com.anequimplus.listeners.ListenerSetorImpTerminal;
import com.anequimplus.utilitarios.Configuracao;

import java.net.MalformedURLException;
import java.util.List;

public class FragmentComandaRemota extends Fragment {

    private EditText servidor_imp_remoto ;
    private CheckBox ativar_comanda_remota ;
    private Button atualizar_setor_imp ;
    private List<SetorImp> lSetorImp ;
    private List<SetorImpItens> lSetorImpItens ;
    private List<SetorImpTerminal> lSetorImpTerminais ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comanda_remota, container, false);
        // Inflate the layout for this fragment
        servidor_imp_remoto = view.findViewById(R.id.servidor_imp_remoto) ;
        servidor_imp_remoto.setText(Configuracao.getLinkComandaRemota(getContext()));
        servidor_imp_remoto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Configuracao.setLinkComandaRemota(getContext(), s.toString()) ;
            }
        });
        ativar_comanda_remota = view.findViewById(R.id.ativar_comanda_remota);
        ativar_comanda_remota.setChecked(Configuracao.getSeComandaRemota(getContext()));
        ativar_comanda_remota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.ativar_comanda_remota)
                    Configuracao.setSeComandaRemota(getContext(), ativar_comanda_remota.isChecked());
            }
        });

        atualizar_setor_imp = view.findViewById(R.id.atualizar_setor_imp) ;
        atualizar_setor_imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSetorImp() ;
            }
        });

        return view ;
    }

    private void getSetorImp(){
        FilterTables filter = new FilterTables() ;
        filter.add(new FilterTable("STATUS", "=", "1"));
        try {
            new ConexaoServerSetorImp(getActivity(), filter, "", new ListenerSetorImp() {
                @Override
                public void ok(List<SetorImp> l) {
                    lSetorImp = l ;
                    setExcluirImpLocal() ;
                  //  getSetorImpItens() ;
                }

                @Override
                public void erro(String msg) {
                    alert(msg) ;
                }
            }).execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }

    }

    private void setExcluirImpLocal() {
        FilterTables filters = new FilterTables() ;
        new BuildSetorImp(getActivity(), filters, new ListenerSetorImp() {
            @Override
            public void ok(List<SetorImp> l) {
                setSetorImpLocal() ;
            }

            @Override
            public void erro(String msg) {
                alert(msg) ;
            }
        }).executar();

    }

    private void setSetorImpLocal() {
        new BuildSetorImp(getActivity(), lSetorImp, new ListenerSetorImp() {
            @Override
            public void ok(List<SetorImp> l) {
                getSetorImpItens();
            }

            @Override
            public void erro(String msg) {
                alert(msg) ;
            }
        }).executar();
    }

    private void getSetorImpItens(){
        FilterTables filter = new FilterTables() ;
        filter.add(new FilterTable("STATUS", "=", "1"));
        try {
            new ConexaoServerSetorImpItens(getActivity(), filter, "", new ListenerSetorImpItens() {
                @Override
                public void ok(List<SetorImpItens> l) {
                    lSetorImpItens = l ;
                    setExcluirImpItensLocal();
                }

                @Override
                public void erro(String msg) {
                    alert(msg) ;
                }
            }).execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }

    }

    private void setExcluirImpItensLocal() {
        FilterTables filters = new FilterTables() ;
        new BuildSetorImpItens(getActivity(), filters, new ListenerSetorImpItens() {
            @Override
            public void ok(List<SetorImpItens> l) {
                setSetorImpItensLocal();
            }

            @Override
            public void erro(String msg) {
                alert(msg) ;
            }
        }).executar();

    }

    private void setSetorImpItensLocal() {
        new BuildSetorImpItens(getActivity(), lSetorImpItens, new ListenerSetorImpItens() {
            @Override
            public void ok(List<SetorImpItens> l) {
                getSetorImpTerminais() ;
               // alert("Importação OK") ;
            }

            @Override
            public void erro(String msg) {
                alert(msg) ;
            }
        }).executar();
    }

    private void getSetorImpTerminais(){
        FilterTables filter = new FilterTables() ;
        filter.add(new FilterTable("STATUS", "=", "1"));
        try {
            new ConexaoServerSetorImpTerminal(getActivity(), filter, "", new ListenerSetorImpTerminal() {
                @Override
                public void ok(List<SetorImpTerminal> l) {
                    lSetorImpTerminais = l ;
                    setExcluirSetorImpTerminal();
                }

                @Override
                public void erro(String msg) {
                    alert(msg);
                }
            }).execute() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }

    }

    private void setExcluirSetorImpTerminal(){
        FilterTables filters = new FilterTables() ;
        new BuildSetorImpTerminal(getActivity(), filters, new ListenerSetorImpTerminal() {
            @Override
            public void ok(List<SetorImpTerminal> l) {
                setSetorImpTerminalLocal() ;
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();

    }

    private void setSetorImpTerminalLocal(){
        new BuildSetorImpTerminal(getActivity(), lSetorImpTerminais, new ListenerSetorImpTerminal() {
            @Override
            public void ok(List<SetorImpTerminal> l) {
                alert("Importação OK") ;
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }).executar();
    }

    private void alert(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void alertToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

}