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
/*

    private static ContaPedidoItemADO contaPedidoItemADO = null ;
    public static ContaPedidoItemADO getContaPedidoItemADO(Context ctx){
        if (contaPedidoItemADO == null){
            contaPedidoItemADO = new ContaPedidoItemADO(ctx) ;
            return contaPedidoItemADO ;
        } else return contaPedidoItemADO ;
    }
*/

    private static ContaPedidoPagamentoADO contaPedidoPagamentoADO = null ;
    public static ContaPedidoPagamentoADO getContaPedidoPagamentoADO(Context ctx){
        if (contaPedidoPagamentoADO == null){
            contaPedidoPagamentoADO = new ContaPedidoPagamentoADO(ctx) ;
            return contaPedidoPagamentoADO;
        } else return contaPedidoPagamentoADO;
    }

    private static ModalidadeADO modalidadeADO = null ;
    public static ModalidadeADO getModalidadeADO(Context ctx){
        if (modalidadeADO == null){
            modalidadeADO = new ModalidadeADO(ctx) ;
            return modalidadeADO ;
        } else return modalidadeADO ;
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

    private static ContaPedidoDAO contaPedidoDAO = null ;
    public static ContaPedidoDAO getContaPedidoInternoDAO(Context ctx){
        if (contaPedidoDAO == null){
            contaPedidoDAO = new ContaPedidoDAO(ctx) ;
            return contaPedidoDAO;
        } else return contaPedidoDAO;
    }

    private static ContaPedidoItemDAO contaPedidoItemDAO = null ;
    public static ContaPedidoItemDAO getContaPedidoItemInternoDAO(Context ctx){
        if (contaPedidoItemDAO == null){
            contaPedidoItemDAO = new ContaPedidoItemDAO(ctx) ;
            return contaPedidoItemDAO;
        } else return contaPedidoItemDAO;
    }

    private static ContaPedidoItemCancelamentoDAO contaPedidoItemCancelamentoDAO = null ;
    public static ContaPedidoItemCancelamentoDAO getContaPedidoItemCancelamentoDAO(Context ctx){
        if (contaPedidoItemCancelamentoDAO == null){
            contaPedidoItemCancelamentoDAO = new ContaPedidoItemCancelamentoDAO(ctx) ;
            return contaPedidoItemCancelamentoDAO ;
        } else return contaPedidoItemCancelamentoDAO ;
    }

    private static ContaPedidoTransferenciaADO contaPedidoTransferenciaADO = null ;
    public static ContaPedidoTransferenciaADO getTransferenciaDAO(Context ctx){
        if (contaPedidoTransferenciaADO == null){
            contaPedidoTransferenciaADO = new ContaPedidoTransferenciaADO(ctx) ;
            return contaPedidoTransferenciaADO;
        } else return contaPedidoTransferenciaADO;
    }



}
