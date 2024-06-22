package com.anequimplus.exportacao;

import com.anequimplus.utilitarios.ProgressDisplay;

public interface RegExport {
    TipoExportacao getTipoExportacao();
    String getExpTexto();
    ProgressDisplay getProgressDisplay();
    void setProgressDisplay(ProgressDisplay progressDisplay) ;
}
