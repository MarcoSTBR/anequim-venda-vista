package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.entity.Produto;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProdutoADO {
    private Context ctx;
    private SQLiteDatabase db = null;
    private final String DB_TABLE = "PRODUTO";

    public ProdutoADO(Context ctx) {
        this.ctx = ctx;
        db = DBHelper.getDB(ctx).getWritableDatabase();
    }

    public void setJSON(JSONArray jr) throws JSONException {
        List<Produto> ls = getTodos() ;
        Produto p = null ;
        Produto resp = null ;
        for (int i = 0 ; i < jr.length() ; i++) {
            p = new Produto(jr.getJSONObject(i)) ;
            resp = seekProduto(ls, p) ;
            if (resp == null){
                Log.i("ProdutosI", p.toString()) ;
                Log.i("ProdutosI", jr.getJSONObject(i).toString()) ;
               inserir(p);
            } else {
                Log.i("ProdutosA", p.toString()) ;
                if (!p.toString().equals(resp.toString())) {
                    alterar(p);
                    Log.i("ProdutosAA", p.toJson().toString()) ;
                }
            }
        }
    }

    public Produto seekProduto(List<Produto> ll, Produto p){
        Produto resp = null ;
        for (Produto pp : ll){
            if (pp.getId() == p.getId()) resp = pp ;
        }
        return resp ;
    }

    public List<Produto> getTodos() {
        List<Produto> l = new ArrayList<Produto>();
        Cursor res = db.rawQuery("SELECT ID, CODIGO, UNIDADE, DESCRICAO, STATUS, IMAGEM, PRECO, COMISSAO " +
                "FROM PRODUTO ORDER BY DESCRICAO ", null);
        res.moveToFirst();
        while(res.isAfterLast()==false) {
                l.add(new Produto(res.getInt(res.getColumnIndexOrThrow("ID")),
                        res.getString(res.getColumnIndexOrThrow("CODIGO")),
                        res.getString(res.getColumnIndexOrThrow("UNIDADE")),
                        res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                        res.getString(res.getColumnIndexOrThrow("IMAGEM")),
                        res.getInt(res.getColumnIndexOrThrow("STATUS")),
                        res.getDouble(res.getColumnIndexOrThrow("PRECO")),
                        res.getDouble(res.getColumnIndexOrThrow("COMISSAO"))));
                Log.i("ProdutosC", l.get(l.size()-1).toString());
                res.moveToNext();
        }
        return l;
    }

    public List<Produto> getFiltro(List<Produto> listProdutos, String txt) {
        if (txt.equals("")) {
            return listProdutos ;
        }
        List<Produto> l = new ArrayList<Produto>() ;
        for (Produto p : listProdutos) {
            if ((ifEncontrou(p.getDescricao(), txt) || (ifEncontrou(txt, p.getCodBarra())))) {
                l.add(p);
            }
        }
        return l ;
    }

     private boolean ifEncontrou (String p1, String p2){
       boolean b  = false;
       if (p1.toUpperCase().contains(p2.toUpperCase())) b = true ;
       return b ;
     }

    public Produto getProdutoId(@NonNull int nId){
        Produto produto = null ;
        Cursor res =  db.rawQuery( "SELECT ID, CODIGO, UNIDADE, DESCRICAO, STATUS, IMAGEM, PRECO, COMISSAO " +
                "FROM PRODUTO WHERE ID = ?", new String[]{String.valueOf(nId)});
        res.moveToFirst();
        if (res.isAfterLast() == false){
            produto = new Produto(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("CODIGO")),
                    res.getString(res.getColumnIndexOrThrow("UNIDADE")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getString(res.getColumnIndexOrThrow("IMAGEM")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getDouble(res.getColumnIndexOrThrow("PRECO")),
                    res.getDouble(res.getColumnIndexOrThrow("COMISSAO")));
           // res.moveToNext();
        }
        return produto ;
    }

    public Produto getCodigo(String codigo) {
        Produto produto = null ;
        Cursor res =  db.rawQuery( "SELECT ID, CODIGO, UNIDADE, DESCRICAO, STATUS, IMAGEM, PRECO, COMISSAO " +
                "FROM PRODUTO WHERE CODIGO = ?", new String[]{codigo});
        res.moveToFirst();
        if (res.isAfterLast() == false){
            produto = new Produto(res.getInt(res.getColumnIndexOrThrow("ID")),
                    res.getString(res.getColumnIndexOrThrow("CODIGO")),
                    res.getString(res.getColumnIndexOrThrow("UNIDADE")),
                    res.getString(res.getColumnIndexOrThrow("DESCRICAO")),
                    res.getString(res.getColumnIndexOrThrow("IMAGEM")),
                    res.getInt(res.getColumnIndexOrThrow("STATUS")),
                    res.getDouble(res.getColumnIndexOrThrow("PRECO")),
                    res.getDouble(res.getColumnIndexOrThrow("COMISSAO")));
            res.moveToNext();
        }
        return produto ;
    }


    public void inserir(Produto produto){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", produto.getId());
        contentValues.put("CODIGO", produto.getCodBarra());
        contentValues.put("UNIDADE", produto.getUnidade());
        contentValues.put("DESCRICAO", produto.getDescricao());
        contentValues.put("IMAGEM", produto.getImagem());
        contentValues.put("STATUS", produto.getStatus());
        contentValues.put("PRECO", produto.getPreco());
        contentValues.put("COMISSAO", produto.getComissao());
        db.insert(DB_TABLE, null, contentValues) ;
    }

    public void alterar(Produto produto){
        ContentValues contentValues = new ContentValues();
        //contentValues.put("ID", produto.getId());
        contentValues.put("CODIGO", produto.getCodBarra());
        contentValues.put("UNIDADE", produto.getUnidade());
        contentValues.put("DESCRICAO", produto.getDescricao());
        contentValues.put("IMAGEM", produto.getImagem());
        contentValues.put("STATUS", produto.getStatus());
        contentValues.put("PRECO", produto.getPreco());
        contentValues.put("COMISSAO", produto.getComissao());
        db.update(DB_TABLE, contentValues, " ID = ? ", new String[] {String.valueOf(produto.getId())});
    }

    public void excluir(){
        db.delete(DB_TABLE, null,null);
    }

    public void excluir(Produto produto) {
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(produto.getId())});
    }

}