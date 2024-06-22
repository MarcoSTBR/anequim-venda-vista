package com.anequimplus.exportacao;

public enum TipoExportacao {
    teCaixaAfoodV13("Caixa Anequim V13"),
    teCaixaAfoodV14("Caixa Anequim V14"),
    teNotas13("Notas V13") ,
    tePedidoV13("Pedidos V13"),
    tePedidoV14("Pedidos V14"),
    teContaPedidoV14("Conta Pedido V14"),
    teTransferenciaV13("Transferências V13"),
    teTransferenciaV14("Transferências V14"),
    teCancelamento14("Cancelamentos V14")
    ;

    public String valor;
    TipoExportacao(String v) {
        valor = v;
    }
}
