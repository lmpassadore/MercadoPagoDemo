package com.mercadopago.demo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mercadopago.demo.App;
import com.mercadopago.demo.R;
import com.mercadopago.demo.adapter.PaymentMethodsAdapter;
import com.mercadopago.demo.model.PaymentMethod;
import com.mercadopago.demo.utils.Constants;
import com.mercadopago.demo.utils.FormatUtils;
import com.mercadopago.demo.utils.PhoneStateUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodActivity extends BaseActivity {

    static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";

    private PaymentMethodsAdapter adapter;
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    private String selectedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarTitle(getString(R.string.paymentmethod_title));
        setSubheaderTitle(getString(R.string.paymentmethod_action));

        getExtras();
        setUpHeader();
        setUpRecyclerView();

        if (PhoneStateUtils.isNetworkAvailable(this))
            requestPaymentMethods();
        else
            showErrorIcon(true);

    }

    @Override
    int getLayoutId() {
        return R.layout.activity_payment_method;
    }

    private void getExtras() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras == null || !intent.hasExtra(EXTRA_AMOUNT))
            return;

        String amount = extras.getString(EXTRA_AMOUNT);
        if (!TextUtils.isEmpty(amount))
            selectedAmount = amount;

    }

    private void setUpHeader() {
        TextView textViewAmount = findViewById(R.id.baseview_textview_amount);
        textViewAmount.setText(FormatUtils.getAmountInLocalCurrency(selectedAmount));

        hidePaymentDetailLabels();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.baseview_recyclerview_selection);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PaymentMethodsAdapter(paymentMethods);
        adapter.setOnItemClickListener(new PaymentMethodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                PaymentMethod paymentMethod = paymentMethods.get(position);

                Float amount = Float.valueOf(selectedAmount);
                Float minAmount = paymentMethod.getMinAllowedAmount();
                Float maxAmount = paymentMethod.getMaxAllowedAmount();

                if (amount < minAmount || amount > maxAmount) {
                    showInvalidAmountDialog(
                            FormatUtils.getAmountInLocalCurrency(String.valueOf(minAmount)),
                            FormatUtils.getAmountInLocalCurrency(String.valueOf(maxAmount))
                    );

                    return;
                }

                goToBankSelection(selectedAmount, paymentMethod);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void requestPaymentMethods() {

        showProgressBar(true);
        showErrorIcon(false);

        App.mercadoPagoApi.getPaymentMethods(Constants.MERCADOPAGO_API_KEY).enqueue(new Callback<List<PaymentMethod>>() {
            @Override
            public void onResponse(@NonNull Call<List<PaymentMethod>> call, @NonNull Response<List<PaymentMethod>> response) {

                showProgressBar(false);
                List<PaymentMethod> paymentMethods = response.body();

                if (!response.isSuccessful() || paymentMethods == null) {
                    showErrorIcon(true);
                    return;
                }

                updatePaymentMethodsList(paymentMethods);

            }

            @Override
            public void onFailure(@NonNull Call<List<PaymentMethod>> call, @NonNull Throwable t) {
                showProgressBar(false);
                showErrorIcon(true);
            }
        });
    }

    private void updatePaymentMethodsList(List<PaymentMethod> obtainedPaymentMethods) {
        List<PaymentMethod> filteredList = filterPaymentMethodsByType(obtainedPaymentMethods, Constants.PaymentMethodType.CREDIT_CARD, true);
        paymentMethods.clear();
        paymentMethods.addAll(filteredList);

        adapter.notifyDataSetChanged();
    }

    private List<PaymentMethod> filterPaymentMethodsByType(List<PaymentMethod> paymentMethods, String typeId, boolean activeOnly) {
        List<PaymentMethod> filteredList = new ArrayList<>();

        for (PaymentMethod paymentMethod : paymentMethods) {
            if (activeOnly) {
                String status = paymentMethod.getStatus();
                if (TextUtils.isEmpty(status) || !status.equals(Constants.PaymentMethodStatus.ACTIVE))
                    continue;
            }

            String methodTypeId = paymentMethod.getPaymentTypeId();
            if (TextUtils.isEmpty(methodTypeId) || !methodTypeId.equals(typeId))
                continue;

            filteredList.add(paymentMethod);
        }

        return filteredList;
    }

    private void showInvalidAmountDialog(String minAmount, String maxAmount) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.paymentmethod_alert))
                .setMessage(String.format(getString(R.string.paymentmethod_invalidamount_placeholder), minAmount, maxAmount))
                .setPositiveButton(getString(R.string.paymentmethod_chooseagain), null)
                .setNegativeButton(getString(R.string.paymentmethod_correctamount), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void goToBankSelection(String amount, PaymentMethod paymentMethod) {
        Intent intent = new Intent(this, CardIssuerActivity.class);
        intent.putExtra(CardIssuerActivity.EXTRA_AMOUNT, amount);
        intent.putExtra(CardIssuerActivity.EXTRA_PAYMENTMETHOD, paymentMethod);
        startActivity(intent);
    }

    private void hidePaymentDetailLabels() {
        TextView textViewPaymentMethodLabel = findViewById(R.id.baseview_textview_paymentmethodlabel);
        TextView textViewPaymentMethod = findViewById(R.id.baseview_textview_paymentmethod);
        TextView textViewAccreditationTimeLabel = findViewById(R.id.baseview_textview_accreditationtimelabel);
        TextView textViewAccreditationTime = findViewById(R.id.baseview_textview_accreditationtime);

        textViewPaymentMethodLabel.setVisibility(View.GONE);
        textViewPaymentMethod.setVisibility(View.GONE);
        textViewAccreditationTimeLabel.setVisibility(View.GONE);
        textViewAccreditationTime.setVisibility(View.GONE);
    }

}
