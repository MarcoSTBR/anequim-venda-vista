package com.anequimplus.ado;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anequimplus.DaoClass.DBHelper;
import com.anequimplus.DaoClass.Dao;
import com.anequimplus.DaoClass.DaoCampoTipo;
import com.anequimplus.DaoClass.DaoCampos;
import com.anequimplus.tipos.DaoPrimaryKeyTipo;

import java.util.ArrayList;
import java.util.List;

public class UsuarioADO extends Dao {

    public UsuarioADO(Context ctx) {
        super(ctx);
        SQLiteDatabase db = DBHelper.getDB(ctx).getWritableDatabase() ;
        db.execSQL("CREATE TABLE IF NOT EXISTS USUARIO ( "
                + "ID INTEGER PRIMARY KEY, "
                + "LOGIN TEXT, "
                + "NOME TEXT, "
                + "SENHA TEXT, "
                + "AUTENTICACAO TEXT, "
                + "STATUS INTEGER) ");

    }

    @Override
    public String getTabela() {
        return "USUARIO";
    }

    @Override
    public DaoPrimaryKeyTipo getTipoPrimaryKey() {
        return DaoPrimaryKeyTipo.tpkSerial;
    }

    @Override
    public String getID() {
        return "ID";
    }

    @Override
    public List<DaoCampos> getCampos() {
        List<DaoCampos> l = new ArrayList<DaoCampos>() ;
        l.add(new DaoCampos("LOGIN", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("NOME", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("SENHA", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("AUTENTICACAO", DaoCampoTipo.dbString, false)) ;
        l.add(new DaoCampos("STATUS", DaoCampoTipo.dbInt, true)) ;
        return l;
    }

}
