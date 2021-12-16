package com.anequimplus.tipos;

public enum Link {

    fAutenticacao("fAutenticacao"),
    fLogar("fLogar"),
    fAtualizarCaixa("fAtualizarCaixa"),
    fLinkAcesso("fLinkAcesso"),
    fCancelarPedidoItem("fCancelarPedidoItem"),
    fConsultaProduto("fConsultaProduto"),
    fIncluirPedido("fIncluirPedido"),
    fConsultaPedido("fConsultaPedido"),
    fPagamentoPedido("fPagamentoPedido"),
    fTesteConexao("fTesteConexao"),
    fConsultaModalidade("fConsultaModalidade"),
    fImagem("fImagem"), fpdf("fpdf"),
    fConsultaOpFechamento("fConsultaOpFechamento"),
    fExecutaOpFechamento("fExecutaOpFechamento"),
    fImprimirOpFechamento("fImprimirOpFechamento"),
    fFechamentoCaixa("fFechamentoCaixa"),
    fImpressoras("fImpressoras"),
    fConfiguracaoLIO("fConfiguracaoLIO"),
    fImprimirConta("fImprimirConta"),
    fNFCeGetContaPedido("fNFCeGetContaPedido"),
    fNFCeSetContaPedido("fNFCeSetContaPedido"),
    fNFCeQrCode("fNFCeQrCode"), fGrupos("fGrupos"),
    fGradeVendas("fGradeVendas"),
    fGetVendaVista("fGetVendaVista"),
    fSetVendaVista("fSetVendaVista"),
    fMenuPrincipal("fMenuPrincipal"),
    fImpressoraRemotaPedido("fImpressoraRemotaPedido"),
    fImpressoraRemotaVenda("fImpressoraRemotaVenda"),
    fConsultaTerminal("fConsultaTerminal"),
    fAtualizaTerminal("fAtualizaTerminal");

    public String valor;
    Link(String v) {
        valor = v;
    }
}
