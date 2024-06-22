package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.OpcaoMenuPrincipal;
import com.anequimplus.utilitarios.Configuracao;

import java.util.ArrayList;
import java.util.List;

public class OpcoesPrincipalADO {

    private Context ctx  ;
    private static List<OpcaoMenuPrincipal> listOpcoesPricipais ;

    public OpcoesPrincipalADO(Context ctx) {
        this.ctx = ctx ;
        listOpcoesPricipais = new ArrayList<OpcaoMenuPrincipal>() ;
  //      listOpcoesPricipais.add(new OpcaoMenuPrincipal(4,R.mipmap.menu_vendavista, "Venda a Vista","Venda a vista de produtos.")) ;
    }

    public List<OpcaoMenuPrincipal> getList(){
        listOpcoesPricipais.clear();
//        listOpcoesPricipais.add(new OpcaoMenuPrincipal(1, R.mipmap.ic_menu_conta , "Desconto em Folha","Consume de produtos para desconto em folha.")) ;
        listOpcoesPricipais.add(new OpcaoMenuPrincipal(2, R.mipmap.ic_menu_pedido , "Pedido","Envia pedidos para mesas ou comandas.")) ;
        listOpcoesPricipais.add(new OpcaoMenuPrincipal(3,R.mipmap.menu_caixa, "Caixa / Movimento", "Abertura e fechamento do movimento.")) ;
        listOpcoesPricipais.add(new OpcaoMenuPrincipal(4, R.drawable.ic_baseline_print_24, "Suprimento / Sangria", "Surimento e sangra do caixa.")) ;

        if (!Configuracao.getPedidoCompartilhado(ctx))
            listOpcoesPricipais.add(new OpcaoMenuPrincipal(5, R.drawable.ic_baseline_refresh_white_24, "Exportar Movimento", "Exporta movimento para o Gerezim.")) ;
        return listOpcoesPricipais ;
    }


}
