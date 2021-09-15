package com.anequimplus.tipos;

public enum Link {

    fAutenticacao("fAutenticacao"), fLojas("fLojas"), fLinkAcesso("fLinkAcesso"), fLogar("fLogar"),  fConsultaProduto("fConsultaProduto"),
    fIncluirPedido("fIncluirPedido"), fConsultaPedido("fConsultaPedido"), fPagamentoPedido("fPagamentoPedido"),
    fTesteConexao("fTesteConexao"),  fConsultaModalidade("fConsultaModalidade"),  fImagem("fImagem"), fpdf("fpdf"),
    fAberturaCaixa("fAberturaCaixa"), fConsultaCaixa("fConsultaCaixa"), fConsultaOpFechamento("fConsultaOpFechamento"),
    fExecutaOpFechamento("fExecutaOpFechamento"), fImprimirOpFechamento("fImprimirOpFechamento"),
    fFechamentoCaixa("fFechamentoCaixa"),  fImpressoras("fImpressoras"), fConfiguracaoLIO("fConfiguracaoLIO"),
    fImprimirConta("fImprimirConta"), fNFCeGetContaPedido("fNFCeGetContaPedido"),
    fNFCeSetContaPedido("fNFCeSetContaPedido"), fNFCeQrCode("fNFCeQrCode"), fGrupos("fGrupos"),
    fGradeVendas("fGradeVendas"),  fGetVendaVista("fGetVendaVista"), fSetVendaVista("fSetVendaVista"),
    fMenuPrincipal("fMenuPrincipal"),  fImpressoraRemotaPedido("fImpressoraRemotaPedido"),
    fImpressoraRemotaVenda("fImpressoraRemotaVenda"), fGetTerminal("fGetTerminal"), fSetTerminal("fSetTerminal");

    public String valor;
    Link(String v) {
        valor = v;
    }
}
