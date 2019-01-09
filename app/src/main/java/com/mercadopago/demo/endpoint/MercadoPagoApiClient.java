package com.mercadopago.demo.endpoint;

import com.mercadopago.demo.model.CardIssuer;
import com.mercadopago.demo.model.Installment;
import com.mercadopago.demo.model.PaymentMethod;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MercadoPagoApiClient {

    String API_VERSION = "v1";

    @GET("/" + API_VERSION + "/payment_methods")
    Call<List<PaymentMethod>> getPaymentMethods(
            @Query("public_key") String publicKey
    );

    @GET("/" + API_VERSION + "/payment_methods/card_issuers")
    Call<List<CardIssuer>> getCardIssuers(
            @Query("public_key") String publicKey,
            @Query("payment_method_id") String paymentMethodId
    );

    @GET("/" + API_VERSION + "/payment_methods/installments")
    Call<List<Installment>> getInstallments(
            @Query("public_key") String publicKey,
            @Query("amount") String amount,
            @Query("payment_method_id") String paymentMethodId,
            @Query("issuer.id") String issuerId
    );

}
