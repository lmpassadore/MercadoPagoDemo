package com.mercadopago.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PaymentMethod implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("payment_type_id")
    private String paymentTypeId;
    @SerializedName("status")
    private String status;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("min_allowed_amount")
    private Float minAllowedAmount;
    @SerializedName("max_allowed_amount")
    private Float maxAllowedAmount;
    @SerializedName("accreditation_time")
    private Integer accreditationTime;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public String getStatus() {
        return status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Float getMinAllowedAmount() {
        return minAllowedAmount;
    }

    public Float getMaxAllowedAmount() {
        return maxAllowedAmount;
    }

    public Integer getAccreditationTime() {
        return accreditationTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.paymentTypeId);
        dest.writeString(this.status);
        dest.writeString(this.thumbnail);
        dest.writeValue(this.minAllowedAmount);
        dest.writeValue(this.maxAllowedAmount);
        dest.writeValue(this.accreditationTime);
    }

    public PaymentMethod() {

    }

    protected PaymentMethod(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.paymentTypeId = in.readString();
        this.status = in.readString();
        this.thumbnail = in.readString();
        this.minAllowedAmount = (Float) in.readValue(Float.class.getClassLoader());
        this.maxAllowedAmount = (Float) in.readValue(Float.class.getClassLoader());
        this.accreditationTime = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<PaymentMethod> CREATOR = new Parcelable.Creator<PaymentMethod>() {
        @Override
        public PaymentMethod createFromParcel(Parcel source) {
            return new PaymentMethod(source);
        }

        @Override
        public PaymentMethod[] newArray(int size) {
            return new PaymentMethod[size];
        }
    };

}
