package com.anequimplus.impressao;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.ContaPedido;
import com.anequimplus.relatorios.IImpressaoContaPedido;
import com.anequimplus.relatorios.ImpressaoContaPedidoM10;
import com.anequimplus.utilitarios.RowImpressao;
import com.elgin.e1.Impressora.Termica;

import java.util.List;

public class ContaPedidoM10 implements ControleImpressora {

    private Context ctx ;
    private ListenerImpressao listenerImpressao ;
    private IImpressaoContaPedido impressaoContaPedido ;

    public ContaPedidoM10(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void open() {
        Termica.setContext(ctx);
        int retorno = Termica.AbreConexaoImpressora(6, "M8", "", 0);
        if (retorno != 0){
            Termica.FechaConexaoImpressora() ;
            retorno = Termica.AbreConexaoImpressora(6, "M8", "", 0);
        }
    }

    @Override
    public void close() {
        int retorno = Termica.FechaConexaoImpressora();
    }

    @Override
    public void setListenerImpressao(ListenerImpressao listenerImpressao) {
        this.listenerImpressao = listenerImpressao ;
    }

    @Override
    public void impressaoLivre(List<RowImpressao> l) {
        int retorno = 0 ;
        EstiloM8_M10 est = EstiloM8_M10.FonteA;
        TamanhoM8_M10 tam = TamanhoM8_M10.A1 ;
        AlinhamentoM8_M10 aling = AlinhamentoM8_M10.Left ;;
        for (RowImpressao r : l){
            switch (r.getTipoAlinhamento()){
                case ptCenter: aling = AlinhamentoM8_M10.Center ;
                break;
                case ptLeft: aling = AlinhamentoM8_M10.Left ;
                break;
                case ptRight: aling = AlinhamentoM8_M10.Right ;
                break;
            }
            switch (r.getTipoEstilo()){
                case ptsNormal: est = EstiloM8_M10.FonteB ;
                    break;
                case ptsBold: est = EstiloM8_M10.Negrito ;
                    break;
                case ptsReverso: est = EstiloM8_M10.Reverso;
                    break;
            }
            if (r.getTamFont() < 5 ) {
                tam = TamanhoM8_M10.A1;
            } else if (r.getTamFont() < 11) {
                tam = TamanhoM8_M10.A2;
            } else if (r.getTamFont() < 100) {
                tam = TamanhoM8_M10.L1;
            }
            retorno = Termica.ImpressaoTexto(r.getLinha(), aling.valor, est.valor, tam.valor);
            Log.e("Termica", "retorno "+retorno+" "+r.getLinha()) ;
          //  if (retorno != 0) break ;
        }
        if (true /*retorno == 0*/) {
            retorno = Termica.Corte(10);
            listenerImpressao.onImpressao(retorno);
        } else {
           listenerImpressao.onError(retorno,"Erro na Impressão COD: "+retorno) ;
        }
    }

    @Override
    public void imprimirRecibo(ContaPedido conta) {
        impressaoContaPedido = new ImpressaoContaPedidoM10(40);
        impressaoContaPedido.setContaPedido(conta);
        List<LinhaImpressao> list = impressaoContaPedido.getRecibo() ;
        int retorno = 0 ;
        for (LinhaImpressao l : list){
            retorno = Termica.ImpressaoTexto(l.getLinha(), l.getAling().valor, l.getEst().valor ,  l.getTam().valor);
        }
        retorno = Termica.CorteTotal(10) ;
        if (retorno != 0) listenerImpressao.onError(retorno,"Erro na Impressão COD: "+retorno) ;
        listenerImpressao.onImpressao(retorno);
    }

    @Override
    public void imprimeConta(ContaPedido conta) {
        impressaoContaPedido = new ImpressaoContaPedidoM10(40);
        impressaoContaPedido.setContaPedido(conta);
        List<LinhaImpressao> list = impressaoContaPedido.getListLinhas() ;
        int retorno = 0 ;
        for (LinhaImpressao l : list){
            retorno = Termica.ImpressaoTexto(l.getLinha(), l.getAling().valor, l.getEst().valor ,  l.getTam().valor);
        }
        retorno = Termica.Corte(10) ;
        if (retorno != 0) listenerImpressao.onError(retorno,"Erro na Impressão COD: "+retorno) ;
        listenerImpressao.onImpressao(retorno);
    }

    @Override
    public void imprimirXML(String txt) {
        int retorno = Termica.ImprimeXMLNFCe(txt, 0, "", 10) ;
        Termica.CorteTotal(1) ;
        if (retorno != 0) listenerImpressao.onImpressao(retorno);
            else  listenerImpressao.onError(retorno, "Erro ao Imprimir XML");  ;
    }

    public static class LinhaImpressao {
        private String linha ;
        private EstiloM8_M10 est ;
        private TamanhoM8_M10 tam ;
        private AlinhamentoM8_M10 aling ;

        public LinhaImpressao(String linha, EstiloM8_M10 est, TamanhoM8_M10 tam, AlinhamentoM8_M10 aling) {
            this.linha = linha;
            this.est = est;
            this.tam = tam;
            this.aling = aling;
        }

        public String getLinha() {
            return linha;
        }

        public void setLinha(String linha) {
            this.linha = linha;
        }

        public EstiloM8_M10 getEst() {
            return est;
        }

        public void setEst(EstiloM8_M10 est) {
            this.est = est;
        }

        public TamanhoM8_M10 getTam() {
            return tam;
        }

        public void setTam(TamanhoM8_M10 tam) {
            this.tam = tam;
        }

        public AlinhamentoM8_M10 getAling() {
            return aling;
        }

        public void setAling(AlinhamentoM8_M10 aling) {
            this.aling = aling;
        }
    }

    public enum EstiloM8_M10{
        FonteA(0), FonteB(1),  Subli(2), Reverso(4), Negrito(8);

        public int valor;
        EstiloM8_M10(int v) {
            valor = v;
        }
    }

    public enum AlinhamentoM8_M10 {
        Left(0), Center(1), Right(2) ;

        public int valor ;
        AlinhamentoM8_M10(int v){
            valor = v ;
        };

    }

    public enum TamanhoM8_M10{
        A1(0), A2(1), A3(2), L1(16); //, L2(32), L3(48);

        public int valor ;
        TamanhoM8_M10(int v){
            valor = v;
        }
    }

    private void testeimpressao(){
        for (EstiloM8_M10 e : EstiloM8_M10.values()) {
            for (TamanhoM8_M10 t : TamanhoM8_M10.values()){
                String txt = e.name()+" "+t.name() ;
                Termica.ImpressaoTexto(e.name()+" "+t.name(), 0, e.valor, t.valor) ;
                Log.i("tipoimp",txt) ;
            }

        }
        int retorno = Termica.Corte(10) ;
    }

/*
            - Parâmetro numérico que altera o estilo do texto impresso.
            O texto pode ser alterado entre os seguintes estilos:
            Valor	Descrição
            0	Fonte A
            1	Fonte B
            2	Sublinhado
            4	Modo reverso
            8	Negrito

tamanho	- Define o tamanho do texto a ser impresso.
O texto pode ser aumentado em até 6 vezes na sua largura e altura. Os valores definidos são:
Valor	Descrição
0	1 x na altura e largura
1	2 x na altura
2	3 x na altura
3	4 x na altura
4	5 x na altura
5	6 x na altura
6	7 x na altura
7	8 x na altura
16	2 x na largura
32	3 x na largura
48	4 x na largura
64	5 x na largura
80	6 x na largura
96	7 x na largura
112	8 x na largura

*/

/*        testeimpressao() ;


        impressaoContaPedido.setContaPedido(conta);
        List<RowImpressao> l = impressaoContaPedido.getListLinhas() ;
        int retorno = 0 ;
        int alinhamento = 0 ;
        int tamanho = 0 ;
        int  estilo = 0 ;
        for (RowImpressao row : l){
            switch (row.getTipoAlinhamento()){
                case ptLeft: alinhamento = 0 ;
                break;
                case ptCenter: alinhamento = 1 ;
                break;
                case ptRight: alinhamento = 2 ;
                break;
            }
            if (row.getTamFont() < 8){
                tamanho = row.getTamFont() ;
            } else
            if ((row.getTamFont() > 8) && (row.getTamFont() < 16)) {
                tamanho = 16 ;
            } else tamanho = 32 ;


            switch (row.getTipoEstilo()){
                case ptsBold: estilo = 8;
                break;
                case ptsNormal: estilo = 0 ;
                break;
                case ptsReverso: estilo = 4 ;
                break;
                default: estilo = 0 ;

            }

          retorno = Termica.ImpressaoTexto(row.getLinha(),alinhamento, estilo , tamanho);
          Log.i("imprimindo", "Ret "+retorno+" "+ row.getLinha()) ;
        }
        retorno = Termica.Corte(10) ;
        //int retorno = Termica.ImprimeXMLSAT(XML_SAT, 0);
*/


}
