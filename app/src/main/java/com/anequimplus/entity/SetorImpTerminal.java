package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class SetorImpTerminal {
    private int id ;
    private int setor_imp_id ;
    private int conf_terminal_id  ;
    private int impressora_id ;
    private int status ;

    public SetorImpTerminal(JSONObject j) throws JSONException {
      if (j.isNull("id"))
          id = j.getInt("ID") ;
       else id = j.getInt("id") ;
      setor_imp_id     = j.getInt("SETOR_IMP_ID" );
      conf_terminal_id = j.getInt("CONF_TERMINAL_ID" );
      impressora_id    = j.getInt("IMPRESSORA_ID" );
      status           = j.getInt("STATUS" );
    }

    public JSONObject getJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("ID", id) ;
        j.put("SETOR_IMP_ID", setor_imp_id) ;
        j.put("CONF_TERMINAL_ID", conf_terminal_id) ;
        j.put("IMPRESSORA_ID", impressora_id) ;
        j.put("STATUS", status) ;
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSetor_imp_id() {
        return setor_imp_id;
    }

    public void setSetor_imp_id(int setor_imp_id) {
        this.setor_imp_id = setor_imp_id;
    }

    public int getConf_terminal_id() {
        return conf_terminal_id;
    }

    public void setConf_terminal_id(int conf_terminal_id) {
        this.conf_terminal_id = conf_terminal_id;
    }

    public int getImpressora_id() {
        return impressora_id;
    }

    public void setImpressora_id(int impressora_id) {
        this.impressora_id = impressora_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
