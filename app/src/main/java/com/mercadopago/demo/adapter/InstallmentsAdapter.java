package com.mercadopago.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mercadopago.demo.R;
import com.mercadopago.demo.model.PayerCost;

import java.util.List;

public class InstallmentsAdapter extends RecyclerView.Adapter<InstallmentsAdapter.PayerCostViewHolder> {

    private Context context;
    private List<PayerCost> payerCosts;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class PayerCostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuantity;
        TextView textViewRecommendedMessage;

        PayerCostViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewQuantity = itemView.findViewById(R.id.installment_textview_quantity);
            textViewRecommendedMessage = itemView.findViewById(R.id.installment_textview_recommendedmessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null)
                        return;

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                    }
                }
            });

        }
    }

    public InstallmentsAdapter(Context context, List<PayerCost> payerCosts) {
        this.context = context;
        this.payerCosts = payerCosts;
    }

    @NonNull
    @Override
    public PayerCostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View payerCostView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_payercost, viewGroup, false);

        return new PayerCostViewHolder(payerCostView);
    }

    @Override
    public void onBindViewHolder(@NonNull PayerCostViewHolder payerCostViewHolder, int i) {

        PayerCost payerCost = payerCosts.get(i);

        Integer installments = payerCost.getInstallments();
        String recommendedMessage = payerCost.getRecommendedMessage();

        if (installments != null)
            payerCostViewHolder.textViewQuantity.setText(String.format(context.getString(R.string.installments_quantity_placeholder), installments));

        if (!TextUtils.isEmpty(recommendedMessage))
            payerCostViewHolder.textViewRecommendedMessage.setText(recommendedMessage);

    }

    @Override
    public int getItemCount() {
        return payerCosts != null ? payerCosts.size() : 0;
    }
}
