package com.anequimplus.exportacao;

import org.json.JSONArray;

public interface ParamExportacao {
    String getClasse() ;
    String getMethod() ;
    JSONArray getDados() ;
    String getNomeParam() ;
    void executar() ;
    RegExport getRegExport() ;

}
