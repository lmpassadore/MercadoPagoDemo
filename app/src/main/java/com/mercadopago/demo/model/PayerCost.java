package com.mercadopago.demo.model;

import com.google.gson.annotations.SerializedName;

public class PayerCost {

    @SerializedName("installments")
    private Integer installments;
    @SerializedName("installment_rate")
    private Float installmentRate;
    @SerializedName("discount_rate")
    private Float discountRate;
    @SerializedName("recommended_message")
    private String recommendedMessage;
    @SerializedName("installment_amount")
    private Float installmentAmount;
    @SerializedName("total_amount")
    private Float totalAmount;

    public Integer getInstallments() {
        return installments;
    }

    public Float getInstallmentRate() {
        return installmentRate;
    }

    public Float getDiscountRate() {
        return discountRate;
    }

    public String getRecommendedMessage() {
        return recommendedMessage;
    }

    public Float getInstallmentAmount() {
        return installmentAmount;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

}
