package com.mercadopago.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mercadopago.demo.App;
import com.mercadopago.demo.R;
import com.mercadopago.demo.adapter.CardIssuersAdapter;
import com.mercadopago.demo.model.CardIssuer;
import com.mercadopago.demo.model.PaymentMethod;
import com.mercadopago.demo.utils.Constants;
import com.mercadopago.demo.utils.FormatUtils;
import com.mercadopago.demo.utils.PhoneStateUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardIssuerActivity extends BaseActivity {

    static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
    static final String EXTRA_PAYMENTMETHOD = "EXTRA_PAYMENTMETHOD";

    private CardIssuersAdapter adapter;
    private List<CardIssuer> cardIssuers = new ArrayList<>();

    private String selectedAmount;
    private PaymentMethod selectedPaymentMethod;

    // Se utilizan para continuar automáticamente a selección de cuotas cuando no existen emisores para la tarjeta seleccionada
    private Handler handler = new Handler();
    private Runnable continueToInstallments = new Runnable() {
        @Override
        public void run() {
            showProgressBar(false);
            goToInstallmentsSelection(selectedAmount, selectedPaymentMethod, null);
            finish();
        }
    };

    private static final int DELAY_MILLIS = 1000 * 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarTitle(getString(R.string.cardissuer_title));
        setSubheaderTitle(getString(R.string.cardissuer_action));

        getExtras();
        setUpHeader();
        setUpRecyclerView();

        if (PhoneStateUtils.isNetworkAvailable(this))
            requestCardIssuers();
        else
            showErrorIcon(true);
    }

    @Override
    int getLayoutId() {
        return R.layout.activity_card_issuer;
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

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CardIssuersAdapter(cardIssuers);
        adapter.setOnItemClickListener(new CardIssuersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                CardIssuer cardIssuer = cardIssuers.get(position);
                goToInstallmentsSelection(selectedAmount, selectedPaymentMethod, cardIssuer.getId());
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void requestCardIssuers() {

        showProgressBar(true);
        showErrorIcon(false);

        App.mercadoPagoApi.getCardIssuers(Constants.MERCADOPAGO_API_KEY, selectedPaymentMethod.getId()).enqueue(new Callback<List<CardIssuer>>() {
            @Override
            public void onResponse(@NonNull Call<List<CardIssuer>> call, @NonNull Response<List<CardIssuer>> response) {

                showProgressBar(false);
                List<CardIssuer> cardIssuers = response.body();

                if (!response.isSuccessful() || cardIssuers == null) {
                    showErrorIcon(true);
                    return;
                }

                if (!cardIssuers.isEmpty())
                    updateCardIssuersList(cardIssuers);
                else
                    goToInstallmentsSelectionNoIssuer();

            }

            @Override
            public void onFailure(@NonNull Call<List<CardIssuer>> call, @NonNull Throwable t) {
                showProgressBar(false);
                showErrorIcon(true);
            }
        });

    }

    private void updateCardIssuersList(List<CardIssuer> obtainedCardIssuers) {
        cardIssuers.clear();
        cardIssuers.addAll(obtainedCardIssuers);

        adapter.notifyDataSetChanged();
    }

    private void goToInstallmentsSelectionNoIssuer() {
        handler.postDelayed(continueToInstallments, DELAY_MILLIS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Se cancela el callback para que no se invoque a la Activity de elección de cuotas después de transcurrido el tiempo
        handler.removeCallbacks(continueToInstallments);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToInstallmentsSelection(String amount, PaymentMethod paymentMethod, String cardIssuerId) {
        Intent intent = new Intent(this, InstallmentsActivity.class);
        intent.putExtra(InstallmentsActivity.EXTRA_AMOUNT, amount);
        intent.putExtra(InstallmentsActivity.EXTRA_PAYMENTMETHOD, paymentMethod);
        intent.putExtra(InstallmentsActivity.EXTRA_CARDISSUER_ID, cardIssuerId);
        startActivity(intent);
    }

}
