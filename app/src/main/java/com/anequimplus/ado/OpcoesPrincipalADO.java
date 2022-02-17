package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.OpcaoMenuPrincipal;

import java.util.ArrayList;
import java.util.List;

public class OpcoesPrincipalADO {

    private Context ctx  ;
    private static List<OpcaoMenuPrincipal> listOpcoesPricipais ;

    public OpcoesPrincipalADO(Context ctx) {
        this.ctx = ctx ;
        listOpcoesPricipais = new ArrayList<OpcaoMenuPrincipal>() ;
        listOpcoesPricipais.add(new OpcaoMenuPrincipal(1,R.mipmap.ic_menu_pedido , "Pedido","Envia pedidos para mesas ou comandas.")) ;
        listOpcoesPricipais.add(new OpcaoMenuPrincipal(2,R.mipmap.ic_menu_conta, "Conta","Emitir contas para pagamento")) ;
        listOpcoesPricipais.add(new OpcaoMenuPrincipal(3,R.mipmap.menu_caixa, "Caixa / Movimento", "Abertura e fechamento do movimento.")) ;
  //      listOpcoesPricipais.add(new OpcaoMenuPrincipal(4,R.mipmap.menu_vendavista, "Venda a Vista","Venda a vista de produtos.")) ;
    }

    public List<OpcaoMenuPrincipal> getList(){
        return listOpcoesPricipais ;
    }


}
