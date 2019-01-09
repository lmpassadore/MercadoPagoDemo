package com.mercadopago.demo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mercadopago.demo.App;
import com.mercadopago.demo.R;
import com.mercadopago.demo.adapter.InstallmentsAdapter;
import com.mercadopago.demo.model.Installment;
import com.mercadopago.demo.model.PayerCost;
import com.mercadopago.demo.model.PaymentMethod;
import com.mercadopago.demo.utils.Constants;
import com.mercadopago.demo.utils.FormatUtils;
import com.mercadopago.demo.utils.PhoneStateUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstallmentsActivity extends BaseActivity {

    static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
    static final String EXTRA_PAYMENTMETHOD = "EXTRA_PAYMENTMETHOD";
    static final String EXTRA_CARDISSUER_ID = "EXTRA_CARDISSUER_ID";

    private InstallmentsAdapter adapter;
    private List<PayerCost> payerCosts = new ArrayList<>();

    private Installment obtainedInstallment;
    private String selectedInstallments;

    private String selectedAmount;
    private PaymentMethod selectedPaymentMethod;
    private String cardIssuerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarTitle(getString(R.string.installments_title));
        setSubheaderTitle(getString(R.string.installments_action));

        getExtras();
        setUpHeader();
        setUpRecyclerView();

        if (PhoneStateUtils.isNetworkAvailable(this))
            requestInstallments();
        else
            showErrorIcon(true);
    }

    @Override
    int getLayoutId() {
        return R.layout.activity_installments;
    }

    private void getExtras() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras == null)
            return;

        if (intent.hasExtra(EXTRA_AMOUNT))
            selectedAmount = extras.getString(EXTRA_AMOUNT);

        if (intent.hasExtra(EXTRA_PAYMENTMETHOD))
            selectedPaymentMethod = extras.getParcelable(EXTRA_PAYMENTMETHOD);

        if (intent.hasExtra(EXTRA_CARDISSUER_ID))
            cardIssuerId = extras.getString(EXTRA_CARDISSUER_ID);

    }

    private void setUpHeader() {
        TextView textViewAmount = findViewById(R.id.baseview_textview_amount);
        textViewAmount.setText(FormatUtils.getAmountInLocalCurrency(selectedAmount));

        TextView textViewPaymentMethod = findViewById(R.id.baseview_textview_paymentmethod);
        textViewPaymentMethod.setText(selectedPaymentMethod.getName());

        int accreditationTime = selectedPaymentMethod.getAccreditationTime();
        TextView textViewAccreditationTime = findViewById(R.id.baseview_textview_accreditationtime);
        textViewAccreditationTime.setText(FormatUtils.getAccreditationTime(this, accreditationTime));
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.baseview_recyclerview_selection);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new InstallmentsAdapter(this, payerCosts);
        adapter.setOnItemClickListener(new InstallmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                PayerCost selectedPayerCost = payerCosts.get(position);
                selectedInstallments = String.valueOf(selectedPayerCost.getInstallments());
                showConfirmationDialog();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void requestInstallments() {

        showProgressBar(true);

        App.mercadoPagoApi.getInstallments(
                Constants.MERCADOPAGO_API_KEY,
                selectedAmount,
                selectedPaymentMethod.getId(),
                cardIssuerId
        ).enqueue(new Callback<List<Installment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Installment>> call, @NonNull Response<List<Installment>> response) {

                showProgressBar(false);
                List<Installment> installments = response.body();

                if (!response.isSuccessful() || installments == null) {
                    showErrorIcon(true);
                    return;
                }

                obtainedInstallment = installments.get(0);
                updateInstallmentsList(obtainedInstallment.getPayerCosts());

            }

            @Override
            public void onFailure(@NonNull Call<List<Installment>> call, @NonNull Throwable t) {
                showProgressBar(false);
            }
        });
    }

    private void updateInstallmentsList(List<PayerCost> obtainedInstallments) {
        payerCosts.clear();
        payerCosts.addAll(obtainedInstallments);

        adapter.notifyDataSetChanged();
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.installments_payment))
                .setMessage(getString(R.string.installments_payment_confirmation))
                .setPositiveButton(getString(R.string.installments_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndGoBack(
                                selectedAmount,
                                selectedPaymentMethod.getName(),
                                obtainedInstallment.getIssuer().getName(),
                                selectedInstallments
                        );
                    }
                })
                .setNegativeButton(getString(R.string.installments_cancel), null)
                .show();
    }

    private void finishAndGoBack(String amount, String paymentMethodName, String cardIssuerName, String installments) {
        Intent intent = new Intent(this, AmountActivity.class);

        intent.putExtra(AmountActivity.EXTRA_AMOUNT, amount);
        intent.putExtra(AmountActivity.EXTRA_PAYMENTMETHOD_NAME, paymentMethodName);
        intent.putExtra(AmountActivity.EXTRA_CARDISSUER_NAME, cardIssuerName);
        intent.putExtra(AmountActivity.EXTRA_INSTALLMENTS, installments);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
