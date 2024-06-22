package com.anequimplus.controler;

import android.content.Context;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.DaoClass.DaoFilter;
import com.anequimplus.DaoClass.DaoFilterTabelas;
import com.anequimplus.DaoClass.DaoFilters;
import com.anequimplus.entity.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class ControleLogin extends DaoFilterTabelas<Usuario> {

    private Context ctx ;

    public ControleLogin(Context ctx) {
        this.ctx = ctx;
    }

    public void login(String login, String senha){
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("LOGIN", "=", login));
        try {
            List<Usuario> l = getAll(DaoDbTabela.getUsuarioADO(ctx).findAll(f, "LOGIN")) ;
            if (l.size() >0 ) {
                Usuario u = l.get(0);
                if (senha.equals(u.getSenha())){
                    ok(u);
                } else erro("Senha Inválida!");
            } else erro("Usuário Não Encontrado!");
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
       }

    }

    public Usuario getNew(JSONObject j) throws JSONException {
            return new Usuario(j) ;
    }



    public abstract void ok(Usuario u) ;
    public abstract void erro(String msg) ;
}
