package com.anequimplus.impressao;

import android.content.Context;
import android.util.Log;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.entity.ConfiguracaoImpressora;
import com.anequimplus.entity.ContaPedido;
import com.anequimplus.entity.Impressora;
import com.anequimplus.relatorios.IImpressaoContaPedido;
import com.anequimplus.relatorios.ImpressaoContaPedidoI9;
import com.anequimplus.utilitarios.RowImpressao;
import com.elgin.e1.Impressora.Termica;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ContaPedidoI9 implements ControleImpressora {

    private Context ctx ;
    private ListenerImpressao listenerImpressao ;
    private IImpressaoContaPedido impressaoContaPedido ;
    private ConfiguracaoImpressora config = null ;

    public ContaPedidoI9(Context ctx, Impressora impressora) {
        this.ctx = ctx;
        config = DaoDbTabela.getConfiguracaoImpressoraADO(ctx).get(impressora.getId()) ;
    }

    protected String getModelo(){
        return "i9" ;
    }

    @Override
    public void open() {
        close() ;
        if (config != null) {
            try {
                JSONObject conf = new JSONObject(config.getConfig()) ;
                if (!conf.isNull("ENDERECO_IP") && (!conf.isNull("PORTA"))) {
                    String enderecoIP = conf.getString("ENDERECO_IP") ;//"10.1.1.51";
                    int porta = conf.getInt("PORTA");// 9100 ;
                    int retorno = Termica.AbreConexaoImpressora(3, getModelo(), enderecoIP, porta);
                   // listenerImpressao.onError(retorno, enderecoIP +" porta "+porta+" retorno "+retorno );
                    Log.i("open_imp_1", "retorno " + retorno);
                    if (retorno != 0){
                        retorno = Termica.FechaConexaoImpressora() ;
                        Log.i("open_imp_2", "retorno " + retorno);
                        retorno = Termica.AbreConexaoImpressora(3, getModelo(), enderecoIP, porta);
                        Log.i("open_imp_3", "retorno " + retorno);
                    }
                } else listenerImpressao.onError(0, "Erro na Configuração da Impressora!");
            } catch (JSONException e) {
                e.printStackTrace();
                listenerImpressao.onError(0, e.getMessage());
            }
        } else listenerImpressao.onError(0, "Impressora Sem Configuração!");
    }

    @Override
    public void close() {
      //  if Termica.

       int retorno = Termica.FechaConexaoImpressora();
       Log.i("open_imp_close", "retorno " + retorno);

    }

    @Override
    public void setListenerImpressao(ListenerImpressao listenerImpressao) {
     this.listenerImpressao = listenerImpressao ;
    }

    @Override
    public void imprimeConta(ContaPedido conta) {
        if (config != null) {
            impressaoContaPedido = new ImpressaoContaPedidoI9(48);
            impressaoContaPedido.setContaPedido(conta);
            List<ContaPedidoI9.LinhaImpressao> list = impressaoContaPedido.getListLinhas();
            int retorno = 0;
            for (ContaPedidoI9.LinhaImpressao l : list) {
                retorno = Termica.ImpressaoTexto(l.getLinha(), l.getAling().valor, l.getEst().valor, l.getTam().valor);
                // retorno = Termica.ImpressaoTexto(l.getLinha(), 0, 1 , 2);
                Termica.AvancaPapel(1);
                Log.i("retorno", "retorno " + retorno + " " + l.getLinha());
            }
            retorno = Termica.CorteTotal(10);
            if (retorno != 0)
                listenerImpressao.onError(retorno, "Erro na Impressão COD: " + retorno);
            listenerImpressao.onImpressao(retorno);
        } else listenerImpressao.onError(0, "Impressora Sem Configuração!");
    }

    @Override
    public void imprimirXML(String txt) {
       if (config != null) {
           int retorno = Termica.ImprimeXMLNFCe(txt, 0, "", 10);
           Termica.CorteTotal(1);
           if (retorno == 0) listenerImpressao.onImpressao(retorno);
           else listenerImpressao.onError(retorno, "Erro ao Imprimir XML");
       } else listenerImpressao.onError(0,"Impressora sem Configuração!");
    }

    @Override
    public void impressaoLivre(List<RowImpressao> l) {
        int retorno = 0 ;
        EstiloM8_M10 est =  EstiloM8_M10.FonteA;
        TamanhoM8_M10 tam = TamanhoM8_M10.A1 ;
        AlinhamentoM8_M10 aling = AlinhamentoM8_M10.Left ;;
        for (RowImpressao r : l){
            switch (r.getTipoAlinhamento()){
                case ptCenter: aling = AlinhamentoM8_M10.Center ;
                    break;
                case ptRight: aling = AlinhamentoM8_M10.Right ;
                    break;
                default: aling = AlinhamentoM8_M10.Left ;
                    break;
            }
            switch (r.getTipoEstilo()){
                case ptsBold: est = EstiloM8_M10.Negrito ;
                    break;
                case ptsReverso: est = EstiloM8_M10.Reverso;
                    break;
                default: est = EstiloM8_M10.FonteB ;
                    break;
            }
            tam = TamanhoM8_M10.A1;
            if (r.getTamFont() < 5 ) {
                tam = TamanhoM8_M10.A1;
            } else if (r.getTamFont() < 11) {
                tam = TamanhoM8_M10.A2;
            } else if (r.getTamFont() < 100) {
                tam = TamanhoM8_M10.L1;
            }
            retorno = Termica.ImpressaoTexto(r.getLinha(), aling.valor, est.valor, tam.valor);
            Termica.AvancaPapel(1);
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
        impressaoContaPedido = new ImpressaoContaPedidoI9(48);
        impressaoContaPedido.setContaPedido(conta);
        List<ContaPedidoI9.LinhaImpressao> list = impressaoContaPedido.getRecibo() ;
        int retorno = 0 ;
        for (LinhaImpressao l : list){
            retorno = Termica.ImpressaoTexto(l.getLinha(), l.getAling().valor, l.getEst().valor ,  l.getTam().valor);
            Termica.AvancaPapel(1);
        }
        retorno = Termica.CorteTotal(10) ;
        if (retorno != 0) listenerImpressao.onError(retorno,"Erro na Impressão COD: "+retorno) ;
        listenerImpressao.onImpressao(retorno);

//        listenerImpressao.onError(0, "Erro ao Imprimir Recibo");  ;

    }

    public static class LinhaImpressao {
        private String linha ;
        private ContaPedidoI9.EstiloM8_M10 est ;
        private ContaPedidoI9.TamanhoM8_M10 tam ;
        private ContaPedidoI9.AlinhamentoM8_M10 aling ;

        public LinhaImpressao(String linha, ContaPedidoI9.EstiloM8_M10 est, ContaPedidoI9.TamanhoM8_M10 tam, ContaPedidoI9.AlinhamentoM8_M10 aling) {
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

        public ContaPedidoI9.EstiloM8_M10 getEst() {
            return est;
        }

        public void setEst(ContaPedidoI9.EstiloM8_M10 est) {
            this.est = est;
        }

        public ContaPedidoI9.TamanhoM8_M10 getTam() {
            return tam;
        }

        public void setTam(ContaPedidoI9.TamanhoM8_M10 tam) {
            this.tam = tam;
        }

        public ContaPedidoI9.AlinhamentoM8_M10 getAling() {
            return aling;
        }

        public void setAling(ContaPedidoI9.AlinhamentoM8_M10 aling) {
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

}
