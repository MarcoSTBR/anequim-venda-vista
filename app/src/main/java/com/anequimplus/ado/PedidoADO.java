package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.anequimplus.entity.Pedido;
import com.anequimplus.entity.PedidoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoADO {

    private Context ctx ;
    private SQLiteDatabase db = null ;

    public PedidoADO(Context ctx) {
        this.ctx = ctx ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
    }

    public List<Pedido> getList(){
        List<Pedido> list = new ArrayList<Pedido>() ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, DATA FROM PEDIDO ORDER BY ID", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Date d = null;
            try {
                d = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                Pedido pedido = new Pedido(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("PEDIDO")),
                        d,
                        null);
                pedido.setListPedidoItem(Dao.getPedidoItemADO(ctx).getListPedidoItem(pedido));
                list.add(pedido) ;
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            res.moveToNext();
        }
        return list ;
    }

    public JSONArray getListJSON(List<Pedido> l) throws JSONException {
        JSONArray jarr = new JSONArray();
        for (Pedido pedido: l) {
            jarr.put(pedido.getJSon()) ;
        }
        return jarr ;
    }

    public Pedido getPedido(String p) {
        Pedido pd = filtrar(p) ;
        if (pd == null) {
            pd = new Pedido(-1, p, new Date(), new ArrayList<PedidoItem>()) ;
            inserir(pd);
        }
        return pd ;
    }

    public Pedido getId(int pedido_id) {
        for (Pedido p : getList()){
            if (p.getId() == pedido_id){
                return  p ;
            }
        }
        return null ;
    }


    public void limparVazio(){
        List<Pedido> ls = getList();
        for (Pedido p : ls){
            if (p.getListPedidoItem().size() == 0){
                Dao.getPedidoItemADO(ctx).delete(p.getId());
                delete(p);
               // getList().remove(p) ;
            }
        }
    }


    public JSONArray getPedidos() throws JSONException {
        JSONArray jarr = new JSONArray() ;
        JSONObject j = new JSONObject() ;
        for (Pedido p : getList()){
            jarr.put(p.getJSon()) ;
        }
        return  jarr ;
    }

    public int itemCount() {
        int cont = 0 ;
        for (Pedido p : getList()){
            cont = cont + p.getListPedidoItem().size();
        }
        return cont ;
    }

    private Pedido filtrar(String p){
        Pedido pedido = null ;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor res =  db.rawQuery( "SELECT ID, PEDIDO, DATA FROM PEDIDO WHERE PEDIDO = ?", new String[] {p} );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Date d = null;
            try {
                d = (Date) df.parse(res.getString(res.getColumnIndex("DATA")));
                pedido = new Pedido(res.getInt(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("PEDIDO")),
                        d,
                        null);
                pedido.setListPedidoItem(Dao.getPedidoItemADO(ctx).getListPedidoItem(pedido));
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            res.moveToNext();
        }
        return pedido ;
    }

    private void inserir(Pedido pedido){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO", pedido.getPedido());
        contentValues.put("DATA", df.format(pedido.getData()));
        pedido.setId((int) db.insert("PEDIDO", null, contentValues)) ;
    }

    private void alterar(Pedido pedido){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put("PEDIDO", pedido.getPedido());
        contentValues.put("DATA", df.format(pedido.getData()));
        db.update("PEDIDO", contentValues, "ID = ?", new String[] {String.valueOf(pedido.getId())});
    }

    public void delete(){
        db.delete("PEDIDO",null, null) ;
    }

    public void delete(Pedido pedido){
        Dao.getPedidoItemADO(ctx).delete(pedido);
        db.delete("PEDIDO","ID = ?",new String[]{ String.valueOf(pedido.getId()) }) ;
    }


}


