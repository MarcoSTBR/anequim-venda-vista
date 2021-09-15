package com.anequimplus.ado;

import android.content.Context;

import com.anequimplus.entity.ItenSelect;

import java.util.ArrayList;
import java.util.List;

public class ItenSelectADO {

    private Context ctx ;
    private static List<ItenSelect> list = null ;

    public ItenSelectADO(Context ctx) {
        this.ctx = ctx;
    }

    public List<ItenSelect> getList(){
        if (list == null){
            list = new ArrayList<ItenSelect>() ;
        }
        return list ;
    }

    public ItenSelect getItemSelectId(int id) {
        for (ItenSelect i : list){
            if (i.getId() == id){
                return i ;
            }
        }
        return null;
    }
}
