package com.anequimplus.controler;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.Acompanhamento_produto;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.Produto;
import com.anequimplus.listeners.ListenerAcompanhamentoSelect;

import java.util.List;

public class ControleAcompanhamentoSelecao {

    private Context ctx ;
    private PedidoItem pedidoItem ;
    private ListenerAcompanhamentoSelect listenerAcompanhamentoSelect;
    private List<Acompanhamento_produto> acompanhamento_produtos ;



    public ControleAcompanhamentoSelecao(Context ctx, PedidoItem pedidoItem, ListenerAcompanhamentoSelect listenerAcompanhamentoSelect) {
        this.ctx = ctx;
        this.listenerAcompanhamentoSelect = listenerAcompanhamentoSelect;
        this.pedidoItem = pedidoItem;
    }


    public void mais(){
        if (pedidoItem.getItemSelect().getQuantidade() == 0) {
            //pedidoItem.getItemSelect().setQuantidade(pedidoItem.getItemSelect().getQuantidade() + q);
            if (getAcompanhamentos(pedidoItem.getItemSelect().getProduto()).size() > 0){
                listenerAcompanhamentoSelect.setAcompanhamento(pedidoItem);
            } else  {
                pedidoItem.getItemSelect().setQuantidade(pedidoItem.getItemSelect().getQuantidade() + 1);
                setValores();
            }
        } else {
            pedidoItem.getItemSelect().setQuantidade(pedidoItem.getItemSelect().getQuantidade() + 1);
            setValores();
        }
    }

    public void menos(){
        if ((pedidoItem.getItemSelect().getQuantidade() <= 0) || ((pedidoItem.getItemSelect().getQuantidade() - 1) < 0))
            listenerAcompanhamentoSelect.erro("Quantidade Inválida!");
        else {
            pedidoItem.getItemSelect().setQuantidade(pedidoItem.getItemSelect().getQuantidade() - 1) ;
            setValores();
        }
    }

    private void setValores(){
        pedidoItem.getItemSelect().setValor(pedidoItem.getItemSelect().getQuantidade() * pedidoItem.getItemSelect().getPreco() - pedidoItem.getItemSelect().getDesconto());
        pedidoItem.getItemSelect().setComissao(pedidoItem.getItemSelect().getValor() * (pedidoItem.getItemSelect().getProduto().getComissao() / 100));
        listenerAcompanhamentoSelect.ok(pedidoItem);
    }

    private List<Acompanhamento_produto>  getAcompanhamentos(Produto p) {
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("AFOOD_PRODUTO_ID", "=", String.valueOf(p.getId())));
        return DaoDbTabela.getAcompanhamanto_ProdutoADO(ctx).getList(filters, "");
    }


}
