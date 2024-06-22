package com.anequimplus.DaoClass;

import android.content.Context;

import com.anequimplus.ado.AcompanhamentoADO;
import com.anequimplus.ado.Acompanhamento_ItemADO;
import com.anequimplus.ado.Acompanhamento_ProdutoADO;
import com.anequimplus.ado.CaixaADO;
import com.anequimplus.ado.ConfiguracaoImpressoraADO;
import com.anequimplus.ado.ContaPedidoDAO;
import com.anequimplus.ado.ContaPedidoDestADO;
import com.anequimplus.ado.ContaPedidoItemCancelamentoDAO;
import com.anequimplus.ado.ContaPedidoItemDAO;
import com.anequimplus.ado.ContaPedidoNFceADO;
import com.anequimplus.ado.ContaPedidoPagamentoADO;
import com.anequimplus.ado.ContaPedidoTransferenciaADO;
import com.anequimplus.ado.ExportacaoADO;
import com.anequimplus.ado.GradeVendasADO;
import com.anequimplus.ado.GradeVendasItemDAO;
import com.anequimplus.ado.ImpressoraADO;
import com.anequimplus.ado.ItemSelectADO;
import com.anequimplus.ado.ModalidadeADO;
import com.anequimplus.ado.OpcoesFechamentoADO;
import com.anequimplus.ado.OpcoesPrincipalADO;
import com.anequimplus.ado.PedidoADO;
import com.anequimplus.ado.PedidoItemADO;
import com.anequimplus.ado.PedidoItemAcompADO;
import com.anequimplus.ado.ProdutoADO;
import com.anequimplus.ado.SangriaADO;
import com.anequimplus.ado.SuprimentoADO;
import com.anequimplus.ado.UsuarioADO;
import com.anequimplus.ado.UsuarioAcessoADO;

public class DaoDbTabela {


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

    private static ExportacaoADO exportacaoADO = null ;
    public static ExportacaoADO getExportacaoADO(Context ctx){
        if (exportacaoADO == null){
            exportacaoADO = new ExportacaoADO(ctx) ;
            return exportacaoADO;
        } else return exportacaoADO;
    }

    private static SuprimentoADO suprimentoADO = null ;
    public static SuprimentoADO getSuprimentoADO(Context ctx){
        if (suprimentoADO == null){
            suprimentoADO = new SuprimentoADO(ctx) ;
            return suprimentoADO;
        } else return suprimentoADO;
    }

    private static SangriaADO sangriaADO = null ;
    public static SangriaADO getSangriaADO(Context ctx){
        if (sangriaADO == null){
            sangriaADO = new SangriaADO(ctx) ;
            return sangriaADO;
        } else return sangriaADO;
    }

    private static ContaPedidoNFceADO contaPedidoNFceADO = null ;
    public static ContaPedidoNFceADO getContaPedidoNFceADO(Context ctx){
        if (contaPedidoNFceADO == null){
            contaPedidoNFceADO = new ContaPedidoNFceADO(ctx) ;
            return contaPedidoNFceADO;
        } else return contaPedidoNFceADO;
    }

    private static ConfiguracaoImpressoraADO configuracaoImpressoraADO = null ;
    public static ConfiguracaoImpressoraADO getConfiguracaoImpressoraADO(Context ctx){
        if (configuracaoImpressoraADO == null){
            configuracaoImpressoraADO = new ConfiguracaoImpressoraADO(ctx) ;
            return configuracaoImpressoraADO;
        } else return configuracaoImpressoraADO;
    }


    private static ContaPedidoDestADO contaPedidoDestADO = null ;
    public static ContaPedidoDestADO getContaPedidoDestADO(Context ctx){
        if (contaPedidoDestADO == null){
            contaPedidoDestADO = new ContaPedidoDestADO(ctx) ;
            return contaPedidoDestADO;
        } else return contaPedidoDestADO;
    }


    private static UsuarioADO usuarioADO = null ;
    public static UsuarioADO getUsuarioADO(Context ctx){
        if (usuarioADO == null){
            usuarioADO = new UsuarioADO(ctx) ;
            return usuarioADO;
        } else return usuarioADO;
    }

    private static UsuarioAcessoADO usuarioAcessoADO = null ;
    public static UsuarioAcessoADO getUsuarioAcessoADO(Context ctx){
        if (usuarioAcessoADO == null){
            usuarioAcessoADO = new UsuarioAcessoADO(ctx) ;
            return usuarioAcessoADO;
        } else return usuarioAcessoADO;
    }

    private static Acompanhamento_ProdutoADO acompanhamento_ProdutoADO = null ;
    public static Acompanhamento_ProdutoADO getAcompanhamanto_ProdutoADO(Context ctx){
        if (acompanhamento_ProdutoADO == null){
            acompanhamento_ProdutoADO = new Acompanhamento_ProdutoADO(ctx) ;
            return acompanhamento_ProdutoADO;
        } else return acompanhamento_ProdutoADO;
    }

    private static AcompanhamentoADO acompanhamentoADO = null ;
    public static AcompanhamentoADO getAcompanhamentoADO(Context ctx){
        if (acompanhamentoADO == null){
            acompanhamentoADO = new AcompanhamentoADO(ctx) ;
            return acompanhamentoADO;
        } else return acompanhamentoADO;
    }

    private static Acompanhamento_ItemADO acompanhamento_ItemADO = null ;
    public static Acompanhamento_ItemADO getAcompanhamento_ItemADO(Context ctx){
        if (acompanhamento_ItemADO == null){
            acompanhamento_ItemADO = new Acompanhamento_ItemADO(ctx) ;
            return acompanhamento_ItemADO;
        } else return acompanhamento_ItemADO;
    }

    private static ItemSelectADO itemSelectADO = null ;
    public static ItemSelectADO getItemSelectADO(){
        if (itemSelectADO == null){
            itemSelectADO = new ItemSelectADO() ;
            return itemSelectADO;
        } else return itemSelectADO;
    }

    private static PedidoItemAcompADO pedidoItemAcompADO = null ;
    public static PedidoItemAcompADO getPedidoItemAcompADO(Context ctx){
        if (pedidoItemAcompADO == null){
            pedidoItemAcompADO = new PedidoItemAcompADO(ctx) ;
            return pedidoItemAcompADO;
        } else return pedidoItemAcompADO;
    }



}
