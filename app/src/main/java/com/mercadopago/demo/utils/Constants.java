package com.mercadopago.demo.utils;

public class Constants {

    public static final String MERCADOPAGO_API_BASEURL = "https://api.mercadopago.com";
    public static final String MERCADOPAGO_API_KEY = "444a9ef5-8a6b-429f-abdf-587639155d88";

    public static final class PaymentMethodType {
        public static final String TICKET = "ticket";
        public static final String ATM = "atm";
        public static final String CREDIT_CARD = "credit_card";
        public static final String DEBIT_CARD = "debit_card";
        public static final String PREPAID_CARD = "prepaid_card";
    }

    public static final class PaymentMethodStatus {
        public static final String ACTIVE = "active";
        public static final String DEACTIVE = "deactive";
        public static final String TEMPORALLY_DEACTIVE = "temporally_deactive";
    }

}
