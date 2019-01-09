package com.mercadopago.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mercadopago.demo.R;
import com.mercadopago.demo.dialog.PaymentSummaryDialogFragment;
import com.mercadopago.demo.utils.FormatUtils;

public class AmountActivity extends AppCompatActivity {

    static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
    static final String EXTRA_PAYMENTMETHOD_NAME = "EXTRA_PAYMENTMETHOD_NAME";
    static final String EXTRA_CARDISSUER_NAME = "EXTRA_CARDISSUER_NAME";
    static final String EXTRA_INSTALLMENTS = "EXTRA_INSTALLMENTS";

    EditText editTextAmount;
    Button buttonContinue;

    private String current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);

        editTextAmount = findViewById(R.id.amount_edittext_amount);
        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(current))
                    return;

                editTextAmount.removeTextChangedListener(this);

                String cleanString = s.toString().replaceAll("[$.,\\s]", "");

                float parsed = Float.parseFloat(cleanString);
                String formatted = FormatUtils.getAmountInLocalCurrency((parsed / 100));

                current = formatted;
                editTextAmount.setText(formatted);
                editTextAmount.setSelection(formatted.length());

                editTextAmount.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonContinue = findViewById(R.id.amount_button_continue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAmountSelected();
            }
        });

    }

    private void onAmountSelected() {

        String amount = editTextAmount.getText().toString();
        amount = amount.replaceAll("[$.\\s]", "");
        amount = amount.replace(",", ".");

        if (TextUtils.isEmpty(amount) || Float.parseFloat(amount) == 0) {
            Toast.makeText(this, "Debe ingresar un monto a pagar", Toast.LENGTH_SHORT).show();
            return;
        }

        goToPaymentMethodSelection(amount);

    }

    private void goToPaymentMethodSelection(String amount) {
        Intent intent = new Intent(this, PaymentMethodActivity.class);
        intent.putExtra(PaymentMethodActivity.EXTRA_AMOUNT, amount);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String amount = "";
        String paymentMethod = "";
        String cardIssuer = "";
        String installments = "";

        Bundle extras = intent.getExtras();

        if (extras == null)
            return;

        if (intent.hasExtra(EXTRA_AMOUNT))
            amount = intent.getExtras().getString(EXTRA_AMOUNT);

        if (intent.hasExtra(EXTRA_PAYMENTMETHOD_NAME))
            paymentMethod = intent.getExtras().getString(EXTRA_PAYMENTMETHOD_NAME);

        if (intent.hasExtra(EXTRA_CARDISSUER_NAME))
            cardIssuer = intent.getExtras().getString(EXTRA_CARDISSUER_NAME);

        if (intent.hasExtra(EXTRA_INSTALLMENTS))
            installments = intent.getExtras().getString(EXTRA_INSTALLMENTS);

        showPaymentSummaryDialog(amount, paymentMethod, cardIssuer, installments);

    }

    private void showPaymentSummaryDialog(String amount, String paymentMethod, String cardIssuer, String installments) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PaymentSummaryDialogFragment dialogFragment = PaymentSummaryDialogFragment.newInstance(amount, paymentMethod, cardIssuer, installments);
        dialogFragment.show(fragmentManager, "dialog_paymentsummary");
    }

}
