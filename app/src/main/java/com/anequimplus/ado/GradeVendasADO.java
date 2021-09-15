package com.anequimplus.ado;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.Grupo;
import com.anequimplus.entity.Produto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GradeVendasADO {
    private Context ctx ;
    private List<Grupo> lsGrupo = null ;
    private List<Produto> lsProduto = null ;

    public GradeVendasADO(Context ctx) {
        this.ctx = ctx;
    }

    public void setJSON(JSONArray jr) throws JSONException {
        lsGrupo = Dao.getGrupoDAO(ctx).getList();
        lsProduto = Dao.getProdutoADO(ctx).getList() ;
        for (int i = 0 ; i < jr.length() ; i++){
            JSONObject j = jr.getJSONObject(i) ;
            Grupo g = new Grupo(j) ;
            if (getGrupoList(g) == null) {
                Dao.getGrupoDAO(ctx).inserir(g);
                lsGrupo.add(g) ;
            } else Dao.getGrupoDAO(ctx).alterar(g);
            Produto p = new Produto(g, j) ;
            if (getProdutoList(p) == null) {
                Dao.getProdutoADO(ctx).inserir(p) ;
                lsProduto.add(p) ;
            } else Dao.getProdutoADO(ctx).alterar(p);
        }
    }

    public void setGrupoProduto(JSONObject j) throws JSONException {

        Dao.getGrupoDAO(ctx).setJSON(j.getJSONArray("GRUPOS")) ;
        Dao.getProdutoADO(ctx).setJSON(j.getJSONArray("PRODUTOS")) ;

        Dao.getGrupoDAO(ctx).delete() ;


        lsGrupo = Dao.getGrupoDAO(ctx).getList();
        lsProduto = Dao.getProdutoADO(ctx).getList() ;
        JSONArray jrGrupo = j.getJSONArray("GRUPOS") ;
        for (int i = 0 ; i < jrGrupo.length() ; i++){
            Log.i("setGrupos", jrGrupo.getJSONObject(i).toString()) ;
            Grupo grupo = new Grupo(jrGrupo.getJSONObject(i)) ;
            if (getGrupoList(grupo) == null) {
                Dao.getGrupoDAO(ctx).inserir(grupo);
                lsGrupo.add(grupo) ;
            } else {
                Dao.getGrupoDAO(ctx).alterar(grupo);
            }
        }

        JSONArray jrProduto = j.getJSONArray("PRODUTOS") ;
        for (int i = 0 ; i < jrProduto.length() ; i++){
            Log.i("setProdutos", jrProduto.getJSONObject(i).toString()) ;
            Grupo grupo = getGrupoList(new Grupo(jrProduto.getJSONObject(i).getInt("GRUPO_ID"),"", 1)) ;
            Produto produto = new Produto(jrProduto.getJSONObject(i).getInt("ID"),
                    grupo,
                    jrProduto.getJSONObject(i).getString("CODBARRA"),
                    jrProduto.getJSONObject(i).getString("DESCRICAO"),
                    jrProduto.getJSONObject(i).getString("UNIDADE"),
                    jrProduto.getJSONObject(i).getDouble("PRECO"),
                    jrProduto.getJSONObject(i).getInt("STATUS")) ;
            if (getProdutoList(produto) == null) {
                Dao.getProdutoADO(ctx).inserir(produto) ;
                lsProduto.add(produto) ;
            } else Dao.getProdutoADO(ctx).alterar(produto);
        }
    }

    private Grupo getGrupoList(Grupo tmp){
        Grupo g = null ;
        for (Grupo gr : lsGrupo){
            if (gr.getId() == tmp.getId()){
                g = gr ;
            }
        }
        return g ;
    }

    private Produto getProdutoList(Produto tmp){
        Produto p = null ;
        for (Produto pd : lsProduto){
            if (pd.getId() == tmp.getId()){
                p = pd ;
            }
        }
        return p ;
    }

}
