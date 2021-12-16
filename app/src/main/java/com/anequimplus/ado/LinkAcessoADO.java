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
import com.anequimplus.utilitarios.UtilSet;

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
           // URL serv = new URL("https://pampofood.com.br/anequimfood/rest.php") ;
            //URL serv = new URL("https://anequim.gerezimbi.com.br/template/rest.php") ;
            //URL serv = new URL("http://192.168.0.17/design/rest.php") ;
            //URL serv = new URL("http://127.0.0.1/design/rest.php") ;
            //https://www.pampofood.com.br/gerezim/

            //String serv = "http://10.0.0.105/design/rest.php" ;
            //String serv = "http://45.232.4.238/design/rest.php";
            String serv = UtilSet.getServidorMaster(ctx);

            String link = "" ;
            URL url = new URL(serv + link) ;
            int i = 0 ;
            if (getList().size() == 0) {
                delete();
                inserir(new LinkAcesso(i++,Link.fAutenticacao,url));
                inserir(new LinkAcesso(i++,Link.fLogar,url));
                inserir(new LinkAcesso(i++,Link.fAtualizarCaixa,url));
                inserir(new LinkAcesso(i++,Link.fLinkAcesso,url));
                inserir(new LinkAcesso(i++,Link.fCancelarPedidoItem,url));
                inserir(new LinkAcesso(i++,Link.fConsultaProduto,url));
                inserir(new LinkAcesso(i++,Link.fIncluirPedido,url));
                inserir(new LinkAcesso(i++,Link.fConsultaPedido,url));
                inserir(new LinkAcesso(i++,Link.fPagamentoPedido,url));
                inserir(new LinkAcesso(i++,Link.fTesteConexao,url));
                inserir(new LinkAcesso(i++,Link.fConsultaModalidade,url));
                inserir(new LinkAcesso(i++,Link.fImagem,url));
                inserir(new LinkAcesso(i++,Link.fpdf,url));
                inserir(new LinkAcesso(i++,Link.fConsultaOpFechamento,url));
                inserir(new LinkAcesso(i++,Link.fExecutaOpFechamento,url));
                inserir(new LinkAcesso(i++,Link.fImprimirOpFechamento,url));
                inserir(new LinkAcesso(i++,Link.fFechamentoCaixa,url));
                inserir(new LinkAcesso(i++,Link.fImpressoras,url));
                inserir(new LinkAcesso(i++,Link.fConfiguracaoLIO,url));
                inserir(new LinkAcesso(i++,Link.fImprimirConta,url));
                inserir(new LinkAcesso(i++,Link.fNFCeGetContaPedido,url));
                inserir(new LinkAcesso(i++,Link.fNFCeSetContaPedido,url));
                inserir(new LinkAcesso(i++,Link.fConsultaProduto,url));
                inserir(new LinkAcesso(i++,Link.fGrupos,url));
                inserir(new LinkAcesso(i++,Link.fGradeVendas,url));
                inserir(new LinkAcesso(i++,Link.fMenuPrincipal,url));
                inserir(new LinkAcesso(i++,Link.fImpressoraRemotaPedido,url));
                inserir(new LinkAcesso(i++,Link.fImpressoraRemotaVenda,url));
                inserir(new LinkAcesso(i++,Link.fGetVendaVista,url));
                inserir(new LinkAcesso(i++,Link.fSetVendaVista,url));
                inserir(new LinkAcesso(i++,Link.fAtualizaTerminal,url));
                inserir(new LinkAcesso(i++,Link.fConsultaTerminal,url));

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
