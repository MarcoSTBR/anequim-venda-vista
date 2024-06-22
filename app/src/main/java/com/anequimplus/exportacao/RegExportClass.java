package com.anequimplus.exportacao;

import com.anequimplus.utilitarios.ProgressDisplay;

public class RegExportClass implements RegExport{

    private String expTabela ;
    private String expTexto ;
    private ProgressDisplay progressDisplay ;
    private TipoExportacao tipoExportacao ;


    public RegExportClass(TipoExportacao tipoExportacao , String expTexto, ProgressDisplay progressDisplay){
        this.tipoExportacao = tipoExportacao ;
        this.expTexto  = expTexto ;
        this.progressDisplay = progressDisplay ;
    }

    @Override
    public TipoExportacao getTipoExportacao() {
        return tipoExportacao;
    }

    @Override
    public String getExpTexto() {
        return expTexto;
    }

    @Override
    public ProgressDisplay getProgressDisplay() {
        return progressDisplay;
    }

    @Override
    public void setProgressDisplay(ProgressDisplay progressDisplay) {
        this.progressDisplay = progressDisplay ;
    }

}
