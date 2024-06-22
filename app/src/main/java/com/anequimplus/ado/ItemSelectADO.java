package com.anequimplus.ado;

import com.anequimplus.entity.PedidoItem;

import java.util.ArrayList;
import java.util.List;

public class ItemSelectADO {

    private static List<PedidoItem> list = null;

    public ItemSelectADO(){
        list = new ArrayList<PedidoItem>() ;
    }

    public List<PedidoItem> getList() {
        return list;
    }

    public void setList(List<PedidoItem> l) {
        list = l;
    }

    public PedidoItem get(int id) {
        PedidoItem it = null ;
        for (PedidoItem i : list){
            if (i.getId() == id) it = i ;
        }
        return it;
    }

}
