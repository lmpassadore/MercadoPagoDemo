package com.mercadopago.demo.model;

import com.google.gson.annotations.SerializedName;

public class CardIssuer {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("thumbnail")
    private String thumbnail;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

}
