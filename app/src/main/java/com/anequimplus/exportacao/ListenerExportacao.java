package com.anequimplus.exportacao;

public interface ListenerExportacao {
    void ok(RegExport regExport) ;
    void erro(RegExport regExport, int cod, String msg);

}
