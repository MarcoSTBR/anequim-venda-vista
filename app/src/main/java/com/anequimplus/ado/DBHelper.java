package com.anequimplus.ado;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private Context ctx ;
    private static DBHelper dbhelper = null ;

    public DBHelper(Context ctx) {
        super(ctx, "BbAnequimP", null, 8);
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
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if ((i < 8) && (i1 == 8)) {
            excluirTabelas(db) ;
            criarTabelas(db) ;
        }
        /*
        if (i == 3) {
            if (i1 == 4){
                excluirTabelas(db) ;
                criarTabelas(db) ;
            }
        }
        if (i < 5) {
            if (i1 == 5){
                excluirTabelas(db) ;
                criarTabelas(db) ;
            }
        }
        if (i < 6) {
            if (i1 == 6){
                excluirTabelas(db) ;
                criarTabelas(db) ;
            }
        }
        if (i < 7) {
            if (i1 == 7){
                excluirTabelas(db) ;
                criarTabelas(db) ;
            }
        }

         */
    }

    public void excluirTabelas(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS CAIXA") ;
        db.execSQL("DROP TABLE IF EXISTS GRADE_VENDAS") ;
        db.execSQL("DROP TABLE IF EXISTS GRADE_VENDAS_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS CAIXA") ;
        db.execSQL("DROP TABLE IF EXISTS LINKACESSO ") ;
        db.execSQL("DROP TABLE IF EXISTS GRUPO ") ;
        db.execSQL("DROP TABLE IF EXISTS PRODUTO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM ") ;
        db.execSQL("DROP TABLE IF EXISTS FUNCIONARIO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_FUNCIONARIO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_FUNC_ITEM ") ;
        db.execSQL("DROP TABLE IF EXISTS EMPRESA ") ;
        db.execSQL("DROP TABLE IF EXISTS CENTROCUSTO ") ;
        db.execSQL("DROP TABLE IF EXISTS FUNCIONARIO_TIPO ") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA_PAGAMENTO") ;
        db.execSQL("DROP TABLE IF EXISTS MODALIDADE") ;
        db.execSQL("DROP TABLE IF EXISTS LOJA") ;
    }

    public static void criarTabelas(SQLiteDatabase db){
        //Datetime	TEXT	aaaa-MM-DD HH: mm: SS. FFFFFFF

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

        db.execSQL("CREATE TABLE IF NOT EXISTS LINKACESSO ( "
                + "ID INTEGER , "
                + "LINK TEXT, "
                + "URL TEXT)");
/*
        db.execSQL("CREATE TABLE IF NOT EXISTS GRUPO ( "
                + "ID INTEGER , "
                + "DESCRICAO TEXT,"
                + "STATUS INTEGER)");
*/
        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUTO ( "
                + "ID INTEGER , "
                + "CODIGO TEXT , "
                + "UNIDADE TEXT , "
                + "DESCRICAO TEXT , "
                + "STATUS INTEGER,"
                + "IMAGEM TEXT)");

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
                + "VALOR DOUBLE, "
                + "OBS TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS FUNCIONARIO ( "
                + "ID INTEGER, "
                + "EMPRESA_ID INTEGER, "
                + "CENTRO_CUSTO_ID INTEGER, "
                + "TIPO_ID INTEGER, "
                + "NOME TEXT, "
                + "STATUS INTEGER) ");
/*
        db.execSQL("CREATE TABLE IF NOT EXISTS EMPRESA ( "
                + "ID INTEGER, "
                + "DESCRICAO TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS CENTROCUSTO ( "
                + "ID INTEGER, "
                + "DESCRICAO TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS FUNCIONARIO_TIPO ( "
                + "ID INTEGER, "
                + "DESCRICAO TEXT)");


        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_FUNCIONARIO ( "
                + "ID INTEGER, "
                + "FUNCIONARIO_ID INTEGER, "
                + "DATA DATETIME,"
                + "STATUS INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_FUNC_ITEM ( "
                + "ID INTEGER, "
                + "PEDIDO_FUNC_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "VALOR DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "STATUS INTEGER, "
                + "OBS TEXT) ");

 */


        db.execSQL("CREATE TABLE IF NOT EXISTS VENDA_VISTA ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CAIXA_ID INTEGER, "
                + "CODIGO TEXT, "
                + "STATUS INTEGER,"
                + "DATA DATETIME)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS VENDA_VISTA_ITEM ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "VENDA_VISTA_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "VALOR DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "STATUS INTEGER,"
                + "OBS TEXT)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS VENDA_VISTA_PAGAMENTO ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "VENDA_VISTA_ID INTEGER, "
                + "MODALIDADE_ID INTEGER, "
                + "STATUS INTEGER, "
                + "VALOR DOUBLE)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS MODALIDADE ( "
                + "ID INTEGER, "
                + "CODIGO TEXT, "
                + "DESCRICAO TEXT, "
                + "TIPOMODALIDADE TEXT, "
                + "COD_RECEBIMENTO INTEGER, "
                + "URL TEXT, "
                + "PARAM TEXT,"
                + "STATUS INTEGER)") ;
    }

}
