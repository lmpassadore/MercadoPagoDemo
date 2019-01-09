package com.mercadopago.demo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Installment {

    @SerializedName("payment_method_id")
    private String paymentMethodId;
    @SerializedName("payment_type_id")
    private String paymentTypeId;
    @SerializedName("issuer")
    private CardIssuer issuer;
    @SerializedName("payer_costs")
    private ArrayList<PayerCost> payerCosts;

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public CardIssuer getIssuer() {
        return issuer;
    }

    public ArrayList<PayerCost> getPayerCosts() {
        return payerCosts;
    }

}
