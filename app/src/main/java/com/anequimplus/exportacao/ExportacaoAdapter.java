package com.anequimplus.exportacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.utilitarios.ProgressDisplay;

import java.util.List;

public class ExportacaoAdapter extends RecyclerView.Adapter<ExportacaoAdapter.ExportacaoAdapterHolder>{


    private Context ctx ;
    private List<ParamExportacao> list ;

    public ExportacaoAdapter(Context ctx, List<ParamExportacao> list){
        this.ctx = ctx ;
        this.list = list ;
    }

    @NonNull
    @Override
    public ExportacaoAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_exportacao, null);
        return new ExportacaoAdapter.ExportacaoAdapterHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ExportacaoAdapter.ExportacaoAdapterHolder holder, int position) {
        holder.bind(list.get(position)) ;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ExportacaoAdapterHolder extends RecyclerView.ViewHolder {

        private TextView textExpTabela ;
        private TextView textExpTexto ;
        private ProgressBar progressBar ;
        private ProgressDisplay progressDisplay ;


        public ExportacaoAdapterHolder(@NonNull View itemView) {
            super(itemView);
            textExpTabela   = itemView.findViewById(R.id.textExpTabela);
            textExpTexto    = itemView.findViewById(R.id.textExpTexto);
            progressBar     = itemView.findViewById(R.id.progressBar);

        }


        public void bind(ParamExportacao it){
            textExpTabela.setText(it.getRegExport().getTipoExportacao().valor);
            textExpTexto.setText(it.getRegExport().getExpTexto());
            it.getRegExport().setProgressDisplay(new ProgressDisplay() {
                @Override
                public void titulo(String titulo) {
                    textExpTabela.setText(titulo);
                }

                @Override
                public void progress(int position, int max) {
                    textExpTexto.setText(" "+ position+" / "+max);
                    progressBar.setMax(max);
                    progressBar.setProgress(position);
                }
            });
        }
    }

}
