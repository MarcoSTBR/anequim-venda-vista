package com.anequimplus.nfce;

import org.json.JSONObject;

public interface ListenerNFCeLocal {
    void ok(JSONObject j) ;
    void erro(String msg) ;
}
