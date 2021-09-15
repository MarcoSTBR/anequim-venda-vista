package com.anequimplus.impressao;

public interface ListenerImpressao {

    public void onImpressao(int status) ;

    public void onError(int status, String messagem) ;

}
