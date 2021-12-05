package com.anequimplus.ado;

import android.content.Context;

public class Dao {

    private static GrupoADO grupoDAO = null ;
    public static GrupoADO getGrupoDAO(Context ctx){
        if (grupoDAO == null){
            grupoDAO = new GrupoADO(ctx) ;
            return grupoDAO ;
        } else return grupoDAO ;
    }

    private static ProdutoADO produtoADO = null ;
    public static ProdutoADO getProdutoADO(Context ctx){
        if (produtoADO == null){
            produtoADO = new ProdutoADO(ctx) ;
            return produtoADO ;
        } else return produtoADO ;
    }

    private static PedidoADO pedidoADO = null ;
    public static PedidoADO getPedidoADO(Context ctx){
        if (pedidoADO == null){
            pedidoADO = new PedidoADO(ctx) ;
            return pedidoADO ;
        } else return pedidoADO ;
    }

    private static PedidoItemADO pedidoItemADO = null ;
    public static PedidoItemADO getPedidoItemADO(Context ctx){
        if (pedidoItemADO == null){
            pedidoItemADO = new PedidoItemADO(ctx) ;
            return pedidoItemADO ;
        } else return pedidoItemADO ;
    }

    private static ContaPedidoADO contaPedidoADO = null ;
    public static ContaPedidoADO getContaPedidoADO(Context ctx){
        if (contaPedidoADO == null){
            contaPedidoADO = new ContaPedidoADO(ctx) ;
            return contaPedidoADO ;
        } else return contaPedidoADO ;
    }

    private static ContaPedidoItemADO contaPedidoItemADO = null ;
    public static ContaPedidoItemADO getContaPedidoItemADO(Context ctx){
        if (contaPedidoItemADO == null){
            contaPedidoItemADO = new ContaPedidoItemADO(ctx) ;
            return contaPedidoItemADO ;
        } else return contaPedidoItemADO ;
    }

    private static PagamentoContaADO pagamentoContaADO = null ;
    public static PagamentoContaADO getPagamentoContaADO(Context ctx){
        if (pagamentoContaADO == null){
            pagamentoContaADO = new PagamentoContaADO(ctx) ;
            return pagamentoContaADO ;
        } else return pagamentoContaADO ;
    }

    private static ModalidadeADO modalidadeADO = null ;
    public static ModalidadeADO getModalidadeADO(Context ctx){
        if (modalidadeADO == null){
            modalidadeADO = new ModalidadeADO(ctx) ;
            return modalidadeADO ;
        } else return modalidadeADO ;
    }

    private static ItenSelectADO itemSelectADO = null ;
    public static ItenSelectADO getItemSelectADO(Context ctx){
        if (itemSelectADO == null){
            itemSelectADO = new ItenSelectADO(ctx) ;
            return itemSelectADO ;
        } else return itemSelectADO ;
    }

    private static OpcoesFechamentoADO opcoesFechamentoADO = null ;
    public static OpcoesFechamentoADO getOpcoesFechamentoADO(Context ctx){
        if (opcoesFechamentoADO == null){
            opcoesFechamentoADO = new OpcoesFechamentoADO(ctx) ;
            return opcoesFechamentoADO ;
        } else return opcoesFechamentoADO ;
    }

    private static ImpressoraADO impressoraADO = null ;
    public static ImpressoraADO getImpressoraADO(Context ctx){
        if (impressoraADO == null){
            impressoraADO = new ImpressoraADO(ctx) ;
            return impressoraADO ;
        } else return impressoraADO ;
    }

    private static LinkAcessoADO linkAcessoADO = null ;
    public static LinkAcessoADO getLinkAcessoADO(Context ctx){
        if (linkAcessoADO == null){
            linkAcessoADO = new LinkAcessoADO(ctx) ;
            return linkAcessoADO ;
        } else return linkAcessoADO ;
    }

    private static CaixaADO caixaADO = null ;
    public static CaixaADO getCaixaADO(Context ctx){
        if (caixaADO == null){
            caixaADO = new CaixaADO(ctx) ;
            return caixaADO ;
        } else return caixaADO ;
    }

    private static GradeVendasADO gradeVendasADO = null ;
    public static GradeVendasADO getGradeVendasADO(Context ctx){
        if (gradeVendasADO == null){
            gradeVendasADO = new GradeVendasADO(ctx) ;
            return gradeVendasADO ;
        } else return gradeVendasADO ;
    }

    private static GradeVendasItemDAO gradeVendasItemADO = null ;
    public static GradeVendasItemDAO getGradeVendasItemADO(Context ctx){
        if (gradeVendasItemADO == null){
            gradeVendasItemADO = new GradeVendasItemDAO(ctx) ;
            return gradeVendasItemADO ;
        } else return gradeVendasItemADO ;
    }


    private static OpcoesPrincipalADO opcoesPrincipalADO = null ;
    public static OpcoesPrincipalADO getOpcoesPrincipalADO(Context ctx){
        if (opcoesPrincipalADO == null){
            opcoesPrincipalADO = new OpcoesPrincipalADO(ctx) ;
            return opcoesPrincipalADO ;
        } else return opcoesPrincipalADO ;
    }

    private static VendaVistaADO vendaVistaADO = null ;
    public static VendaVistaADO getVendaVistaADO(Context ctx){
        if (vendaVistaADO == null){
            vendaVistaADO = new VendaVistaADO(ctx) ;
            return vendaVistaADO ;
        } else return vendaVistaADO ;
    }

    private static VendaVistaItemADO vendaVistaItemADO = null ;
    public static VendaVistaItemADO getVendaVistaItemADO(Context ctx){
        if (vendaVistaItemADO == null){
            vendaVistaItemADO = new VendaVistaItemADO(ctx) ;
            return vendaVistaItemADO ;
        } else return vendaVistaItemADO ;
    }

    private static VendaVistaPagamentoADO vendaVistaPagamentoADO = null ;
    public static VendaVistaPagamentoADO getVendaVistaPagamentoADO(Context ctx){
        if (vendaVistaPagamentoADO == null){
            vendaVistaPagamentoADO = new VendaVistaPagamentoADO(ctx) ;
            return vendaVistaPagamentoADO ;
        } else return vendaVistaPagamentoADO ;
    }


}
