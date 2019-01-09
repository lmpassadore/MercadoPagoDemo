package com.mercadopago.demo.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mercadopago.demo.R;
import com.mercadopago.demo.utils.FormatUtils;

public class PaymentSummaryDialogFragment extends DialogFragment {

    static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
    static final String EXTRA_PAYMENTMETHOD_NAME = "EXTRA_PAYMENTMETHOD_NAME";
    static final String EXTRA_CARDISSUER_NAME = "EXTRA_CARDISSUER_NAME";
    static final String EXTRA_INSTALLMENTS = "EXTRA_INSTALLMENTS";

    public PaymentSummaryDialogFragment() {

    }

    public static PaymentSummaryDialogFragment newInstance(String amount, String paymentMethod, String cardIssuer, String installments) {
        PaymentSummaryDialogFragment fragment = new PaymentSummaryDialogFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_AMOUNT, amount);
        args.putString(EXTRA_PAYMENTMETHOD_NAME, paymentMethod);
        args.putString(EXTRA_CARDISSUER_NAME, cardIssuer);
        args.putString(EXTRA_INSTALLMENTS, installments);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paymentsummary, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textViewAmount = view.findViewById(R.id.paymentsummary_textview_amount);
        TextView textViewPaymentMethod = view.findViewById(R.id.paymentsummary_textview_paymentmethod);
        TextView textViewCardIssuer = view.findViewById(R.id.paymentsummary_textview_cardissuer);
        TextView textViewInstallments = view.findViewById(R.id.paymentsummary_textview_installments);

        Bundle args = getArguments();

        if (args == null)
            return;

        String amount = args.getString(EXTRA_AMOUNT);
        textViewAmount.setText(FormatUtils.getAmountInLocalCurrency(amount));

        String paymentMethod = args.getString(EXTRA_PAYMENTMETHOD_NAME);
        textViewPaymentMethod.setText(paymentMethod);

        String cardIssuer = args.getString(EXTRA_CARDISSUER_NAME);
        textViewCardIssuer.setText(cardIssuer);

        String installments = args.getString(EXTRA_INSTALLMENTS);
        textViewInstallments.setText(installments);

    }

}
