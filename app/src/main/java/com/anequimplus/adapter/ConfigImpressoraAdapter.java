package com.anequimplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.ConfiguracaoImpressora;
import com.anequimplus.entity.Impressora;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ConfigImpressoraAdapter extends RecyclerView.Adapter<ConfigImpressoraAdapter.ConfigImpressoraHolper>{

    private Context ctx ;
    private List<Impressora> l ;

    public ConfigImpressoraAdapter(Context ctx, List<Impressora> l){
        this.ctx = ctx ;
        this.l = l ;
    }

    @NonNull
    @Override
    public ConfigImpressoraHolper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_grade_config_impressoras, null);
        return new ConfigImpressoraHolper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigImpressoraHolper holder, int position) {
        holder.bind(l.get(position));
    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    class ConfigImpressoraHolper extends RecyclerView.ViewHolder {

        private TextView descricao ;
        private TextView numCol ;
        private TextView tipoImp ;
        private TextView status ;
        private EditText endereco_ip ;
        private EditText porta ;
        private ImageButton save ;

        public ConfigImpressoraHolper(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.textNomeImpressora) ;
            numCol = itemView.findViewById(R.id.textNumeroColunas) ;
            tipoImp = itemView.findViewById(R.id.textTipoImpressora) ;
            status = itemView.findViewById(R.id.textStatusImpressora) ;
            endereco_ip = itemView.findViewById(R.id.endereco_ip) ;
            porta       = itemView.findViewById(R.id.porta_impressora) ;
            save       = itemView.findViewById(R.id.gravar_configuracao_imp) ;
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getAdapterPosition() ;
                    ConfiguracaoImpressora imp = DaoDbTabela.getConfiguracaoImpressoraADO(ctx).get(l.get(id).getId()) ;
                    JSONObject j = new JSONObject() ;
                    try {
                        j.put("ENDERECO_IP", endereco_ip.getText()) ;
                        if (!porta.getText().equals(""))
                        j.put("PORTA", Integer.valueOf(porta.getText().toString())) ;
                        if (imp == null)
                        {
                            imp = new ConfiguracaoImpressora(l.get(id).getId(), j.toString()) ;
                            DaoDbTabela.getConfiguracaoImpressoraADO(ctx).incluir(imp);
                        } else {
                            imp.setConfig(j.toString());
                            DaoDbTabela.getConfiguracaoImpressoraADO(ctx).alterar(imp);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        }

        public void bind(Impressora i){
            descricao.setText(i.getDescricao());
            numCol.setText("Colunas "+i.getTamColuna());
            tipoImp.setText("Tipo "+ i.getTipoImpressora().name()) ;
            if (i.getStatus() == 1) {
                status.setText("ATIVO");
            } else {
                status.setText("INATIVO");
            }

            ConfiguracaoImpressora imp = DaoDbTabela.getConfiguracaoImpressoraADO(ctx).get(i.getId()) ;
            if (imp != null){
                try {
                    JSONObject j = new JSONObject(imp.getConfig()) ;
                    endereco_ip.setText(j.getString("ENDERECO_IP"));
                    porta.setText(j.getString("PORTA"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
