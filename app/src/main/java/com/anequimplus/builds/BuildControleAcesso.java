package com.anequimplus.builds;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.listeners.ListenerControleAcesso;
import com.anequimplus.utilitarios.UtilSet;

public class BuildControleAcesso {

    private Context ctx ;
    private int acesso_id ;
    private ListenerControleAcesso listenerControleAcesso ;

    public BuildControleAcesso(Context ctx, int acesso_id, ListenerControleAcesso listenerControleAcesso) {
        this.ctx = ctx;
        this.acesso_id = acesso_id;
        this.listenerControleAcesso = listenerControleAcesso;
    }

    public void executar(){
        int idUsuario = UtilSet.getUsuarioId(ctx) ;
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("USUARIO_ID", "=", String.valueOf(idUsuario)));
        filters.add(new FilterTable("ACESSO_ID", "=", String.valueOf(acesso_id)));
        if (DaoDbTabela.getUsuarioAcessoADO(ctx).getList(filters, "").size() > 0){
            listenerControleAcesso.ok(idUsuario);
        } else {
            listenerControleAcesso.erro("Acesso Negado!");
        }
    }
/*

            1	1.1.1.00001	Abertura de caixa	1	1
            2	1.1.1.00002	Suprimento	1	1
            3	1.1.1.00003	Retirada	1	1

            4	1.1.1.00004	ConfiguraÃ§Ã£o SAT/NFCE	1	1
            5	1.1.1.00005	Venda Vista	1	1
            6	1.1.1.00006	Desconto no Item	1	1
            7	1.1.1.00007	Acrescimo no Item	1	1
            8	1.1.1.00008	Desconto RodapÃ©	1	1
            9	1.1.1.00009	Cancelamento Nota	1	1
            10	1.1.1.00010	Cancelamento Item da Nota	1	1

            11	1.1.1.00011	Re-ImpressÃ£o de Nota	1	1

            12	1.1.1.00012	Fechamento Caixa	1	1
            13	1.1.1.00013	ImpressÃ£o do BorderÃ´	1	1
            14	1.1.1.00014	PrÃ©-ImpressÃ£o do Fechamento	1	1

            15	1.1.1.00015	Conta Cliente	1	1

            16	1.1.1.00016	TransferÃªncia de Mesa/Pedidos	1	1

            17	1.1.1.00017	Emitir Conta Sem Taxa de ServiÃ§o	1	1

            18	1.1.1.00018	Cancelamento de Item do Pedido	1	1

            19	1.1.1.00019	ReemissÃ£o de Conta	1	1
            20	1.1.1.00020	Cancelar Pagamento do Pedido	1	1
            21	1.1.1.00021	Emitir Conta Com Desconto	1	1

            22	1.1.1.00022	RelaÃ§Ã£o de Pedidos Fechados	1	1
*/

}
