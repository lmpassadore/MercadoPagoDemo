package com.mercadopago.demo;

import android.app.Application;

import com.mercadopago.demo.endpoint.MercadoPagoApiClient;
import com.mercadopago.demo.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    public static MercadoPagoApiClient mercadoPagoApi;

    @Override
    public void onCreate() {
        super.onCreate();

        configureEndpoint();
    }

    private void configureEndpoint() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MERCADOPAGO_API_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mercadoPagoApi = retrofit.create(MercadoPagoApiClient.class);
    }

}
