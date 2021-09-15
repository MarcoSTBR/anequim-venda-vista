package com.anequimplus.conexoes;

import android.content.Context;
import android.util.Log;

import com.anequimplus.entity.Modalidade;
import com.anequimplus.tipos.PagamentoStatus;

import java.text.DecimalFormat;

import cielo.orders.domain.CheckoutRequest;
import cielo.orders.domain.Credentials;
import cielo.orders.domain.Order;
import cielo.sdk.info.InfoManager;
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.order.payment.PaymentCode;
import cielo.sdk.order.payment.PaymentError;
import cielo.sdk.order.payment.PaymentListener;

public abstract class ConexaoPagamentoLio {

    private Context ctx ;
    private Modalidade modalidade ;
    private String codPed ;
    private Order order;
    private long valorApagar ;
    private double valor ;
    private OrderManager orderManager;
    private InfoManager infoManager;
    private boolean orderManagerServiceBinded ;
    private String clientID ;
    private String accessToken ;


    public ConexaoPagamentoLio(Context ctx, String codPed, String clientID, String accessToken) {
        this.ctx = ctx;
        this.codPed = codPed ;
        this.clientID = clientID ;
        this.accessToken = accessToken ;
    }

    private void configSDK(){
        //String clientID = "kfV6NAzc6nPSF9RT30WGLwQCDim6kA7Viapjgk47U7KO50R9n9" ;
        //String accessToken = "Ymk4JyovfZW5XUMrgfcWMtgabiyKH2MdGNP1tO7aR1WY498mCd" ;
        infoManager = new InfoManager();
        infoManager.getSettings(ctx);
        Credentials credentials = new Credentials( clientID, accessToken);
        orderManager = new OrderManager(credentials, ctx);
        orderManager.bind(ctx, new ServiceBindListener() {

            @Override
            public void onServiceBoundError(Throwable throwable) {
                orderManagerServiceBinded = false;
                String m = "Erro fazendo bind do serviço de ordem -> "+
                throwable.getMessage() ;
                returnoPag(modalidade, valor, m, PagamentoStatus.ERRO) ;
                /*
                Toast.makeText(ctx,
                        String.format("Erro fazendo bind do serviço de ordem -> %s",
                                throwable.getMessage()), Toast.LENGTH_LONG).show();

                 */
            }

            @Override
            public void onServiceBound() {
                orderManagerServiceBinded = true;
                order = orderManager.createDraftOrder(codPed);
                Log.i("Bind","onServiceBound") ;
                addItem() ;
            }

            @Override
            public void onServiceUnbound() {
                orderManagerServiceBinded = false;
                returnoPag(modalidade, valor, "Sem serviço Cielo!", PagamentoStatus.ERRO) ;

            }
        });

    }


    public void execute(Modalidade modalidade, double v) {
        this.modalidade = modalidade;
        valor = v ;
        String vl = new DecimalFormat("#0.00").format(v) ;
        vl = vl.replace(",","") ;
        valorApagar = Long.parseLong(vl) ;
        //infoManager = new InfoManager();
        configSDK() ;

    }

    private void addItem(){
        //infoManager.getSettings(ctx).getLogicNumber() ;
        String ec = infoManager.getSettings(ctx).getMerchantCode() ;
        Log.i("EC Lio",ec) ;
        order.addItem(codPed, "Pagamento "+codPed, valorApagar,1 , "EACH") ;
        orderManager.placeOrder(order) ;
        CheckoutRequest.Builder requestBuilder = new CheckoutRequest.Builder()
                .orderId(order.getId()) /* Obrigatório */
                .amount(valorApagar) /* Opcional */
                //.ec(ec) /* Opcional (precisa estar habilitado na LIO) */
                //.installments(1) /* Opcional */
                //.email("teste@email.com") /* Opcional */
                .paymentCode(getPagamentoCode(modalidade.getCod_recebimento())) ;/* Opcional */

        CheckoutRequest request = requestBuilder.build() ;

        orderManager.checkoutOrder(request, new PaymentListener() {
            @Override
            public void onStart() {
                Log.d("checkoutOrder", "ON START");
            }

            @Override
            public void onPayment(Order paidOrder) {
                Log.d("checkoutOrder", "ON onPayment");
                order = paidOrder;

                order.markAsPaid();
                orderManager.updateOrder(order);
/*
                Payment pgTO = order.getPayments().get(0);
                bandeira = getBandeira(pgTO.getPaymentFields().get("brand"));
                transacao = pgTO.getPaymentFields().get("authCode");

                pagamento.setBandeira(bandeira);
                pagamento.setAutorizacao(transacao);
*/
                resetOrderManger() ;
                returnoPag(modalidade, valor, "OK", PagamentoStatus.SUCESSO) ;

            }

            @Override
            public void onCancel() {
                resetOrderManger() ;
                returnoPag(modalidade, valor, "Cancelado", PagamentoStatus.CANCELADO) ;
                Log.d("checkoutOrder", "ON CANCELAMENTO");
            }

            @Override
            public void onError(PaymentError paymentError) {
                resetOrderManger() ;
                returnoPag(modalidade, valor, paymentError.getDescription(), PagamentoStatus.ERRO) ;
                Log.d("checkoutOrder", "ON ERROR "+paymentError.getDescription());

            }
        });

    }

    protected abstract void returnoPag(Modalidade modalidade, double valor, String msg, PagamentoStatus status) ;


    private void resetOrderManger(){
        orderManager.unbind();
    }



    private PaymentCode getPagamentoCode(int cod_recebimento) {
        switch (cod_recebimento){
            case 0  : return PaymentCode.DEBITO_AVISTA ;
            case 1  : return PaymentCode.DEBITO_PREDATADO ;
            case 2  : return PaymentCode.CREDITO_AVISTA ;
            case 3  : return PaymentCode.CREDITO_PARCELADO_LOJA ;
            case 4  : return PaymentCode.CREDITO_PARCELADO_ADM ;
            case 5  : return PaymentCode.PRE_AUTORIZACAO ;
            case 6  : return PaymentCode.VOUCHER_ALIMENTACAO ;
            case 7  : return PaymentCode.VOUCHER_REFEICAO ;
            case 8  : return PaymentCode.CARTAO_LOJA_AVISTA;
            case 9  : return PaymentCode.CARTAO_LOJA_PARCELADO_LOJA;
            case 10 : return PaymentCode.CARTAO_LOJA_PARCELADO;
            default: return PaymentCode.DEBITO_AVISTA ;
        }
    }

}
