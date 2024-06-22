package com.anequimplus.nfce;

import java.util.Date;

public interface ListenerCloudNFCeCertificado {
    void ok(String msg, Date validade) ;
    void erro(int cod, String msg) ;
}
