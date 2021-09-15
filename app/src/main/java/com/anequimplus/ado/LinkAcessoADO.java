package com.anequimplus.ado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anequimplus.entity.LinkAcesso;
import com.anequimplus.tipos.Link;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinkAcessoADO {

    private Context ctx ;
    private static List<LinkAcesso> list ;
    private SQLiteDatabase db = null ;

    public LinkAcessoADO(Context ctx)  {
        this.ctx = ctx ;
        list = new ArrayList<LinkAcesso>() ;
        db = DBHelper.getDB(ctx).getWritableDatabase() ;
        delete();
        iniciarPadrao() ;
    }

    private void iniciarPadrao()  {
        try {
            //URL serv = new URL("http://10.0.0.103/design/rest.php") ;
            URL serv = new URL("https://pampofood.com.br/anequimfood/rest.php") ;
            //URL serv = new URL("https://anequim.gerezimbi.com.br/template/rest.php") ;
            //URL serv = new URL("http://192.168.0.17/design/rest.php") ;
            //URL serv = new URL("http://127.0.0.1/design/rest.php") ;
            if (getList().size() == 0) {
                delete();
                inserir(new LinkAcesso(1,Link.fAutenticacao,serv));
                inserir(new LinkAcesso(2,Link.fLinkAcesso,serv));
                inserir(new LinkAcesso(3,Link.fLojas,serv));
                inserir(new LinkAcesso(4,Link.fLogar,serv));
                inserir(new LinkAcesso(5,Link.fConsultaProduto,serv));
                inserir(new LinkAcesso(6,Link.fIncluirPedido,serv));
                inserir(new LinkAcesso(7,Link.fConsultaPedido,serv));
                inserir(new LinkAcesso(8,Link.fPagamentoPedido,serv));
                inserir(new LinkAcesso(9,Link.fTesteConexao,serv));
                inserir(new LinkAcesso(10,Link.fConsultaModalidade,serv));
                inserir(new LinkAcesso(11,Link.fImagem,serv));
                inserir(new LinkAcesso(12,Link.fpdf,serv));
                inserir(new LinkAcesso(13,Link.fAberturaCaixa,serv));
                inserir(new LinkAcesso(14,Link.fConsultaCaixa,serv));
                inserir(new LinkAcesso(15,Link.fConsultaOpFechamento,serv));
                inserir(new LinkAcesso(16,Link.fExecutaOpFechamento,serv));
                inserir(new LinkAcesso(17,Link.fImprimirOpFechamento,serv));
                inserir(new LinkAcesso(18,Link.fFechamentoCaixa,serv));
                inserir(new LinkAcesso(19,Link.fImpressoras,serv));
                inserir(new LinkAcesso(20,Link.fConfiguracaoLIO,serv));
                inserir(new LinkAcesso(21,Link.fImprimirConta,serv));
                inserir(new LinkAcesso(22,Link.fNFCeGetContaPedido,serv));
                inserir(new LinkAcesso(23,Link.fNFCeSetContaPedido,serv));
                inserir(new LinkAcesso(24,Link.fConsultaProduto,serv));
                inserir(new LinkAcesso(25,Link.fGrupos,serv));
                inserir(new LinkAcesso(26,Link.fGradeVendas,serv));
                inserir(new LinkAcesso(27,Link.fMenuPrincipal,serv));
                inserir(new LinkAcesso(28,Link.fImpressoraRemotaPedido,serv));
                inserir(new LinkAcesso(29,Link.fImpressoraRemotaVenda,serv));
                inserir(new LinkAcesso(30,Link.fGetVendaVista,serv));
                inserir(new LinkAcesso(31,Link.fSetVendaVista,serv));
                inserir(new LinkAcesso(32,Link.fGetTerminal,serv));
                inserir(new LinkAcesso(33,Link.fSetTerminal,serv));

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public LinkAcesso getLinkAcesso(Link link) throws ExceptionLinkNaoEncontrado, MalformedURLException {
       // atualizaList() ;
        List<LinkAcesso> ls = getList() ;
        for (LinkAcesso l : ls){
            Log.i("LINKS", "link "+link.valor) ;
            Log.i("LINKS", "l "+l.getLink().valor) ;
            if (link.valor.equals(l.getLink().valor)){
                return  l ;
            }
        }
        throw new ExceptionLinkNaoEncontrado(link);
    }

    public static class ExceptionLinkNaoEncontrado extends Exception{

        Link link ;
        public ExceptionLinkNaoEncontrado(Link link) {
            this.link = link ;
        }

        @NonNull
        @Override
        public String toString() {
            return "Link para "+link.valor+" Não Encontrado!";
        }

        @Nullable
        @Override
        public String getMessage() {
            return "Link para "+link.valor+" Não Encontrado!";
        }
    }

    public void setLinkAcesso(JSONArray jr) throws JSONException, MalformedURLException {
       /*
        list.clear();
        Log.e("onPosExecute_l","record "+jr.length()) ;
        if (jr.length() > 0) delete() ;
        for (int i = 0 ; i < jr.length() ; i++){
            JSONObject j = jr.getJSONObject(i) ;
            Log.e("onPosExecute_l",j.getString("url")) ;
            Log.e("onPosExecute_l",j.getString("link")) ;
            Link link = Link.valueOf(j.getString("link")) ;
            URL url = new URL(j.getString("url")) ;
            inserir(new LinkAcesso(j.getInt("id"), link, url) );
        }

        */
    }

    private void inserir(LinkAcesso linkAcesso){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", linkAcesso.getId());
        contentValues.put("LINK", linkAcesso.getLink().valor);
        contentValues.put("URL", linkAcesso.getUrl().toString());
        if (db.insert("LINKACESSO", null, contentValues) > -1)
            list.add(linkAcesso) ;
    }

    private void alterar(LinkAcesso linkAcesso){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", linkAcesso.getId());
        contentValues.put("LINK", linkAcesso.getLink().valor);
        contentValues.put("URL", linkAcesso.getUrl().toString());
        db.update("LINKACESSO", contentValues, "LINK = ?", new String[] {linkAcesso.getLink().valor});

    }


    public List<LinkAcesso> getList() throws MalformedURLException {
        list.clear();
        Cursor res =  db.rawQuery( "SELECT ID, LINK, URL FROM LINKACESSO ", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Log.i("LINKS_res", res.getString(res.getColumnIndex("LINK")));
            Link link= Link.valueOf(res.getString(res.getColumnIndex("LINK"))) ;
            URL url = new URL(res.getString(res.getColumnIndex("URL"))) ;
            list.add(new LinkAcesso(res.getInt(res.getColumnIndex("ID")),
                    link,
                    url)) ;
            res.moveToNext();
        }
        return list ;

    }

    public void delete(){
        db.delete("LINKACESSO",null, null) ;
    }

}
