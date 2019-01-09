package com.mercadopago.demo.utils;

import android.content.Context;

import com.mercadopago.demo.R;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    private static final String LANGUAGE = "es";
    private static final String COUNTRY = "AR";

    public static String getAmountInLocalCurrency(String amount) {
        float amountNumber = Float.valueOf(amount);
        return getAmountInLocalCurrency(amountNumber);
    }

    public static String getAmountInLocalCurrency(float amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY));

        return format.format(amount);
    }

    public static String getAccreditationTime(Context context, Integer minutes) {
        int hours = minutes / 60;
        return hours > 0 ? String.format(context.getString(R.string.all_accreditationtime_placeholder), hours) : context.getString(R.string.all_accreditationtime_inmediate);
    }

}
