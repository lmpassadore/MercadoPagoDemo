package com.mercadopago.demo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadopago.demo.R;
import com.mercadopago.demo.model.PaymentMethod;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.PaymentMethodViewHolder> {

    private List<PaymentMethod> paymentMethods;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class PaymentMethodViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewLogo;
        TextView textViewName;

        PaymentMethodViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageViewLogo = itemView.findViewById(R.id.thumbnailitem_imageview_logo);
            textViewName = itemView.findViewById(R.id.thumbnailitem_textview_name);

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

    public PaymentMethodsAdapter(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    @NonNull
    @Override
    public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View paymentMethodView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_thumbnailitem, viewGroup, false);

        return new PaymentMethodViewHolder(paymentMethodView);

    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodViewHolder paymentMethodViewHolder, int i) {
        PaymentMethod paymentMethod = paymentMethods.get(i);
        String name = paymentMethod.getName();
        String logoUrl = paymentMethod.getThumbnail();

        if (!TextUtils.isEmpty(logoUrl))
            Picasso.get()
                    .load(logoUrl)
                    .resize(50, 20)
                    .centerInside()
                    .error(R.drawable.broken_image_black_18dp)
                    .into(paymentMethodViewHolder.imageViewLogo);

        if (!TextUtils.isEmpty(name))
            paymentMethodViewHolder.textViewName.setText(paymentMethod.getName());
    }

    @Override
    public int getItemCount() {
        return paymentMethods != null ? paymentMethods.size() : 0;
    }

}
