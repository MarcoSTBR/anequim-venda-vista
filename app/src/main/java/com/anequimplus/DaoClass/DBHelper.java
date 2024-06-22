package com.anequimplus.DaoClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private Context ctx ;
    private static DBHelper dbhelper = null ;

    public DBHelper(Context ctx) {
        super(ctx, "BbAnequimP", null, 13);
        this.ctx = ctx ;
    }

    public static DBHelper getDB(Context ctx){
        if (dbhelper == null) {
            dbhelper = new DBHelper(ctx) ;
        }
        return  dbhelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
       // excluirTabelas(db) ;
        criarTabelas(db) ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldv, int newv) {
        Log.i("onUpgrade", "i= "+oldv+" i1= "+newv) ;
        if ((newv == 13) && (oldv == 12)) {
            db.execSQL("DROP TABLE IF EXISTS CONTA_PEDIDO_NFCE") ;
            db.execSQL("CREATE TABLE IF NOT EXISTS CONTA_PEDIDO_NFCE ( "
                    + "UUID TEXT PRIMARY KEY, "
                    + "CONTA_PEDIDO_ID INTEGER, "
                    + "DATA DATETIME, "
                    + "CHAVE TEXT, "
                    + "XMOTIVO TEXT, "
                    + "PROTOCOLO TEXT, "
                    + "STACT INTEGER,"
                    + "STATUS_CONTINGENCIA INTEGER,"
                    + "STATUS_CANCELAMENTO INTEGER,"
                    + "STATUS INTEGER,"
                    + "TIPO INTEGER)");
        }

        if ((newv == 12) && (oldv <= 11)) {
            if (oldv == 11) {
                db.execSQL("DROP TABLE IF EXISTS MODALIDADE") ;
                db.execSQL("CREATE TABLE IF NOT EXISTS MODALIDADE ( "
                        + "ID INTEGER  PRIMARY KEY, "
                        + "CODIGO TEXT, "
                        + "DESCRICAO TEXT, "
                        + "TIPOMODALIDADE TEXT, "
                        + "COD_RECEBIMENTO TEXT, "
                        + "URL TEXT, "
                        + "PARAM TEXT,"
                        + "STATUS INTEGER)") ;
                db.execSQL("CREATE TABLE IF NOT EXISTS CONTA_PEDIDO_NFCE ( "
                        + "UUID TEXT PRIMARY KEY, "
                        + "CONTA_PEDIDO_ID INTEGER, "
                        + "DATA DATETIME, "
                        + "CHAVE TEXT, "
                        + "XMOTIVO TEXT, "
                        + "PROTOCOLO TEXT, "
                        + "STACT INTEGER,"
                        + "STATUS_CONTINGENCIA INTEGER,"
                        + "STATUS_CANCELAMENTO INTEGER,"
                        + "TIPO INTEGER,"
                        + "STATUS INTEGER)");
                db.execSQL("CREATE TABLE IF NOT EXISTS CONTA_PEDIDO_DEST ( "
                        + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "UUID TEXT, "
                        + "CONTA_PEDIDO_ID INTEGER, "
                        + "CPFCNPJ TEXT, "
                        + "NOME TEXT, "
                        + "STATUS INTEGER)");

            } else {
                excluirTabelas(db);
                criarTabelas(db);
            }
        }
    }

    public void excluirTabelas(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS CAIXA") ;
        db.execSQL("DROP TABLE IF EXISTS GRADE_VENDAS") ;
        db.execSQL("DROP TABLE IF EXISTS GRADE_VENDAS_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS PRODUTO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_I ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM_I ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_PG_I ") ;
        db.execSQL("DROP TABLE IF EXISTS MODALIDADE") ;
        db.execSQL("DROP TABLE IF EXISTS TRANSFERENCIA") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM_CANCEL") ;
        db.execSQL("DROP TABLE IF EXISTS IMPRESSORA") ;
        db.execSQL("DROP TABLE IF EXISTS SUPRIMENTO") ;
        db.execSQL("DROP TABLE IF EXISTS SANGRIA") ;
        db.execSQL("DROP TABLE IF EXISTS CONTA_PEDIDO_NFCE") ;
        db.execSQL("DROP TABLE IF EXISTS CONTA_PEDIDO_DEST") ;
        db.execSQL("DROP TABLE IF EXISTS USUARIO_ACESSO") ;

        db.execSQL("DROP TABLE IF EXISTS FUNCIONARIO") ;
        db.execSQL("DROP TABLE IF EXISTS EMPRESA") ;
        db.execSQL("DROP TABLE IF EXISTS CENTROCUSTO") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_FUNCIONARIO") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_FUNC_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS FUNCIONARIO_TIPO") ;

        db.execSQL("DROP TABLE IF EXISTS EMPRESA_DF") ;
        db.execSQL("DROP TABLE IF EXISTS CENTROCUSTO_DF") ;
        db.execSQL("DROP TABLE IF EXISTS FUNCIONARIO_TIPO_DF") ;
        db.execSQL("DROP TABLE IF EXISTS FUNCIONARIO_DF") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_DF") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM_DF");
      //  db.execSQL("DROP TABLE IF EXISTS CONFIG_IMP");



        db.execSQL("DROP TABLE IF EXISTS ACOMP_PROD") ;
        db.execSQL("DROP TABLE IF EXISTS ACOMP") ;
        db.execSQL("DROP TABLE IF EXISTS ACOMP_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM_ACOMP") ;

    }


    public static void criarTabelas(SQLiteDatabase db){

        db.execSQL("CREATE TABLE IF NOT EXISTS USUARIO ( "
                + "ID INTEGER PRIMARY KEY, "
                + "LOGIN TEXT, "
                + "NOME TEXT, "
                + "SENHA TEXT, "
                + "AUTENTICACAO TEXT, "
                + "STATUS INTEGER) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS USUARIO_ACESSO ( "
                + "ID INTEGER  PRIMARY KEY, "
                + "USUARIO_ID INTEGER, "
                + "ACESSO_ID INTEGER, "
                + "DESCRICAO TEXT)"
        ) ;
        db.execSQL("CREATE TABLE IF NOT EXISTS CONTA_PEDIDO_DEST ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "CONTA_PEDIDO_ID INTEGER, "
                + "CPFCNPJ TEXT, "
                + "NOME TEXT, "
                + "STATUS INTEGER)"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS CONTA_PEDIDO_NFCE ( "
                + "UUID TEXT PRIMARY KEY, "
                + "CONTA_PEDIDO_ID INTEGER, "
                + "DATA DATETIME, "
                + "CHAVE TEXT, "
                + "XMOTIVO TEXT, "
                + "PROTOCOLO TEXT, "
                + "STACT INTEGER,"
                + "STATUS_CONTINGENCIA INTEGER,"
                + "STATUS_CANCELAMENTO INTEGER,"
                + "TIPO INTEGER,"
                + "STATUS INTEGER)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS SUPRIMENTO ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DESCRICAO TEXT, "
                + "DATA DATETIME, "
                + "CAIXA_ID INTEGER, "
                + "MODALIDADE_ID INTEGER, "
                + "VALOR DOUBLE, "
                + "UUID TEXT, "
                + "STATUS INTEGER)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS SANGRIA ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DESCRICAO TEXT, "
                + "DATA DATETIME, "
                + "CAIXA_ID INTEGER, "
                + "MODALIDADE_ID INTEGER, "
                + "VALOR DOUBLE, "
                + "UUID TEXT, "
                + "STATUS INTEGER)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS IMPRESSORA ( "
                + "ID INTEGER PRIMARY KEY, "
                + "DESCRICAO TEXT, "
                + "TAMCOLUNA INTEGER, "
                + "TIPOIMPRESSORA TEXT, "
                + "STATUS INTEGER)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS CAIXA ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "USUARIO_ID INTEGER, "
                + "DATA DATETIME, "
                + "DATA_FINAL DATETIME, "
                + "VALOR DOUBLE,"
                + "STATUS INTEGER,"
                + "GEREZIM_ID INTEGER)"
                );

        db.execSQL("CREATE TABLE IF NOT EXISTS GRADE_VENDAS ( "
                + "ID INTEGER , "
                + "DESCRICAO TEXT, "
                + "IMAGEM TEXT, "
                + "STATUS INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS GRADE_VENDAS_ITEM ( "
                + "ID INTEGER , "
                + "GRADE_VENDAS_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "PRECO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "STATUS INTEGER)");


        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUTO ( "
                + "ID INTEGER , "
                + "CODIGO TEXT , "
                + "UNIDADE TEXT , "
                + "DESCRICAO TEXT , "
                + "STATUS INTEGER,"
                + "IMAGEM TEXT,"
                + "COMISSAO DOUBLE,"
                + "PRECO DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PEDIDO TEXT , "
                + "DATA DATETIME) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PEDIDO_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "VALOR DOUBLE, "
                + "STATUS INTEGER, "
                + "OBS TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM_ACOMP ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PEDIDO_ITEM_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "VALOR DOUBLE, "
                + "STATUS INTEGER, "
                + "OBS TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_I ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PEDIDO TEXT , "
                + "UUID TEXT, "
                + "STATUS INTEGER, "
                + "DESCONTO DOUBLE, "
                + "SYSTEM_USER_ID INTEGER, "
                + "DATA DATETIME, "
                + "STATUS_COMISSAO INTEGER, "
                + "NUM_IMPRESSAO INTEGER, "
                + "DATA_FECHAMENTO DATETIME, "
                + "NUM_PESSOAS INTEGER) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM_I ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "DATA DATETIME, "
                + "PEDIDO_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "VALOR DOUBLE, "
                + "STATUS INTEGER, "
                + "USUARIO_ID INTEGER, "
                + "OBS TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_PG_I ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "DATA DATETIME, "
                + "PEDIDO_ID INTEGER, "
                + "CAIXA_ID INTEGER, "
                + "MODALIDADE_ID INTEGER, "
                + "VALOR DOUBLE, "
                + "STATUS INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_OPG_I ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "DATA DATETIME, "
                + "MODALIDADE_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "VALOR DOUBLE, "
                + "STATUS INTEGER) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS MODALIDADE ( "
                + "ID INTEGER, "
                + "CODIGO TEXT, "
                + "DESCRICAO TEXT, "
                + "TIPOMODALIDADE TEXT, "
                + "COD_RECEBIMENTO TEXT, "
                + "URL TEXT, "
                + "PARAM TEXT,"
                + "STATUS INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS TRANSFERENCIA ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "CONTA_PEDIDO_ITEM_ID INTEGER, "
                + "CONTA_PEDIDO_ORIGEM_ID INTEGER, "
                + "CONTA_PEDIDO_DESTINO_ID INTEGER, "
                + "DATA DATETIME, "
                + "USUARIO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "CAIXA_ID INTEGER, "
                + "STATUS INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM_CANCEL ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "UUID TEXT, "
                + "CONTA_PEDIDO_ITEM_ID INTEGER, "
                + "DATA DATETIME, "
                + "USUARIO_ID INTEGER, "
                + "CAIXA_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "STATUS INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS ACOMP_PROD ( "
                + "ID INTEGER PRIMARY KEY, "
                + "AFOOD_PRODUTO_ID INTEGER, "
                + "AFOOD_ACOMP_ID INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS ACOMP ( "
                + "ID INTEGER PRIMARY KEY, "
                + "DESCRICAO TEXT, "
                + "OBS TEXT, "
                + "MIN INTEGER, "
                + "MAX INTEGER, "
                + "PRECO DOUBLE)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS ACOMP_ITEM ( "
                + "ID INTEGER PRIMARY KEY, "
                + "AFOOD_ACOMP_ID INTEGER, "
                + "AFOOD_PRODUTO_ID INTEGER, "
                + "OBS TEXT, "
                + "MIN INTEGER, "
                + "MAX INTEGER, "
                + "PRECO DOUBLE)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS CONFIG_IMP ( "
                + "ID INTEGER PRIMARY KEY, "
                + "CONFIG TEXT) ");


    }

    public void limparTudo(Context ctx) {
        SQLiteDatabase db = getDB(ctx).getWritableDatabase() ;
        excluirTabelas(db);
        //excluirTodasTabelas(db) ;
        criarTabelas(db);

    }

    public void excluirTodasTabelas(SQLiteDatabase db){
//        SQLiteDatabase db = getDB(ctx).getWritableDatabase() ;
        Cursor res =  db.rawQuery( "SELECT name FROM sqlite_master", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            String tabela = res.getString(res.getColumnIndexOrThrow("name")) ;
            Log.i("tabelas", tabela) ;
            if (!((tabela.equals("android_metadata")) || (tabela.equals("sqlite_sequence"))))
              db.execSQL("DROP TABLE IF EXISTS "+tabela) ;
            res.moveToNext();
        }
    }

    public void listarTabelas(Context ctx){
        SQLiteDatabase db = getDB(ctx).getWritableDatabase() ;
        Cursor res =  db.rawQuery( "SELECT name FROM sqlite_master", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Log.i("tabelas", res.getString(res.getColumnIndexOrThrow("name"))) ;
            res.moveToNext();
        }
    }


}
