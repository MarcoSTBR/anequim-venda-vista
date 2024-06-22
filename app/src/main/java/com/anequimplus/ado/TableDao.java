package com.anequimplus.ado;

import android.util.Log;

import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;

import java.util.List;

public class TableDao {

   public String getWhere(FilterTables filters, String order){
      String where ="" ;
      List<FilterTable>  fter = filters.getList();
      if (fter.size() > 0 ){
         for (FilterTable f  : fter) {
            String vl = "" ;
            Log.i("getWhere", "indexof IN "+f.getOperador().toUpperCase().indexOf("IN")) ;
            if (f.getOperador().toUpperCase().indexOf("IN") < 0) {
               vl = '"' + f.getVariavel() + '"';
            } else {
               vl = f.getVariavel() ;
            }
            if (where.length() == 0)
               where = "("+f.getCampo()+" "+f.getOperador()+" "+vl+")" ;
            else
               where = where + " AND ("+f.getCampo()+" "+f.getOperador()+" "+vl+")" ;
         }
         where = " WHERE "+ where ;
      }
      if (order.length() > 0) {
         where = where + " ORDER BY "+order ;
      }

      return where ;

   }
}
