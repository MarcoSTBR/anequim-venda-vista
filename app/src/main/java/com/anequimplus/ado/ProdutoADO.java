package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.entity.Grupo;
import com.anequimplus.entity.Produto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        for (int i = 0 ; i < jr.length() ; i++) {
            JSONObject j = jr.getJSONObject(i);

        }

    }

    public List<Produto> getTodos() {
        List<Produto> l = new ArrayList<Produto>();
        Cursor res = db.rawQuery("SELECT ID, GRUPO_ID, CODIGO, UNIDADE, DESCRICAO, PRECO, STATUS " +
                "FROM PRODUTO P ", null);
        res.moveToFirst();
        while(res.isAfterLast()==false) {
                l.add(new Produto(res.getInt(res.getColumnIndex("ID")),
                        Dao.getGrupoDAO(ctx).getGrupoID(res.getInt(res.getColumnIndex("GRUPO_ID"))),
                        res.getString(res.getColumnIndex("CODIGO")),
                        res.getString(res.getColumnIndex("DESCRICAO")),
                        res.getString(res.getColumnIndex("UNIDADE")),
                        res.getDouble(res.getColumnIndex("PRECO")),
                        res.getInt(res.getColumnIndex("STATUS"))));
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

    public Produto getProdtoId(int nId){
        Produto produto = null ;
        Cursor res =  db.rawQuery( "SELECT ID, GRUPO_ID, CODIGO, UNIDADE, DESCRICAO, PRECO, " +
                "STATUS FROM PRODUTO WHERE ID = ?", new String[]{String.valueOf(nId)});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Grupo grupo = Dao.getGrupoDAO(ctx).getGrupoID(res.getInt(res.getColumnIndex("GRUPO_ID"))) ;
            if (grupo == null) grupo = new Grupo(0,"Temp", 1) ;
            produto = new Produto(res.getInt(res.getColumnIndex("ID")),
                      grupo,
                      res.getString(res.getColumnIndex("CODIGO")),
                      res.getString(res.getColumnIndex("DESCRICAO")),
                      res.getString(res.getColumnIndex("UNIDADE")),
                      res.getDouble(res.getColumnIndex("PRECO")),
                      res.getInt(res.getColumnIndex("STATUS"))) ;
            res.moveToNext();
        }
        return produto ;
    }

    public List<Produto> getList(){
        List<Produto> list = new ArrayList<Produto>();
        List<Grupo> lsGrupo = new ArrayList<Grupo>();
        Cursor res =  db.rawQuery( "SELECT P.ID, P.GRUPO_ID, P.CODIGO, P.UNIDADE, P.DESCRICAO, P.PRECO, P.STATUS, " +
                "G.DESCRICAO DESC_GRUPO, G.STATUS GRUPO_STATUS FROM PRODUTO P INNER JOIN GRUPO G ", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Grupo grupo = getGrupoList(lsGrupo, new Grupo(res.getInt(res.getColumnIndex("GRUPO_ID")),
                    res.getString(res.getColumnIndex("DESC_GRUPO")),
                    res.getInt(res.getColumnIndex("GRUPO_STATUS")))) ;
            list.add(new Produto(res.getInt(res.getColumnIndex("ID")),
                    grupo,
                    res.getString(res.getColumnIndex("CODIGO")),
                    res.getString(res.getColumnIndex("DESCRICAO")),
                    res.getString(res.getColumnIndex("UNIDADE")),
                    res.getDouble(res.getColumnIndex("PRECO")),
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

    private Grupo getGrupoList(List<Grupo> list, Grupo tmp){
        Grupo resp = null ;
        for (Grupo g : list){
            if (g.getId() == tmp.getId()){
                resp = g ;
            }
        }
        if (resp == null){
            resp = tmp ;
            list.add(resp) ;
        }
        return resp ;
    }

    public List<Produto> getList(Grupo grupo){
        List<Produto> list = new ArrayList<Produto>();
        Cursor res =  db.rawQuery( "SELECT ID, GRUPO_ID, CODIGO, UNIDADE, DESCRICAO, PRECO, STATUS " +
                " FROM PRODUTO WHERE (GRUPO_ID = ?) AND (STATUS = 1)  " +
                "ORDER BY ID ", new String[]{String.valueOf(grupo.getId())});
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(
                    new Produto(res.getInt(res.getColumnIndex("ID")),
                    grupo,
                    res.getString(res.getColumnIndex("CODIGO")),
                    res.getString(res.getColumnIndex("DESCRICAO")),
                    res.getString(res.getColumnIndex("UNIDADE")),
                    res.getDouble(res.getColumnIndex("PRECO")),
                    res.getInt(res.getColumnIndex("STATUS")))) ;
            res.moveToNext();
        }
        return list ;
    }

    public void inserir(Produto produto){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", produto.getId());
        contentValues.put("GRUPO_ID", produto.getGrupo().getId());
        contentValues.put("CODIGO", produto.getCodBarra());
        contentValues.put("UNIDADE", produto.getUnidade());
        contentValues.put("DESCRICAO", produto.getDescricao());
        contentValues.put("PRECO", produto.getPreco());
        contentValues.put("STATUS", produto.getStatus());
        db.insert(DB_TABLE, null, contentValues) ;
    }

    public void alterar(Produto produto){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", produto.getId());
        contentValues.put("GRUPO_ID", produto.getGrupo().getId());
        contentValues.put("CODIGO", produto.getCodBarra());
        contentValues.put("UNIDADE", produto.getUnidade());
        contentValues.put("DESCRICAO", produto.getDescricao());
        contentValues.put("PRECO", produto.getPreco());
        contentValues.put("STATUS", produto.getStatus());
        db.update(DB_TABLE, contentValues, " ID = ? ", new String[] {String.valueOf(produto.getId())});
    }

    public void excluir(){
        db.delete(DB_TABLE, null,null);
    }

    public void excluir(Produto produto) {
        db.delete(DB_TABLE, "ID = ?", new String[]{String.valueOf(produto.getId())});
    }

}