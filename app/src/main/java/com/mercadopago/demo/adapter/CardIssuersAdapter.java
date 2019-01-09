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
import com.mercadopago.demo.model.CardIssuer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardIssuersAdapter extends RecyclerView.Adapter<CardIssuersAdapter.CardIssuerViewHolder> {

    private List<CardIssuer> cardIssuers;

    private CardIssuersAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(CardIssuersAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    class CardIssuerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewLogo;
        TextView textViewName;

        CardIssuerViewHolder(@NonNull final View itemView) {
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

    public CardIssuersAdapter(List<CardIssuer> cardIssuers) {
        this.cardIssuers = cardIssuers;
    }

    @NonNull
    @Override
    public CardIssuersAdapter.CardIssuerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View cardIssuerView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_thumbnailitem, viewGroup, false);

        return new CardIssuersAdapter.CardIssuerViewHolder(cardIssuerView);

    }

    @Override
    public void onBindViewHolder(@NonNull CardIssuersAdapter.CardIssuerViewHolder cardIssuerViewHolder, int i) {
        CardIssuer cardIssuer = cardIssuers.get(i);
        String name = cardIssuer.getName();
        String logoUrl = cardIssuer.getThumbnail();

        if (!TextUtils.isEmpty(logoUrl))
            Picasso.get()
                    .load(logoUrl)
                    .resize(50, 20)
                    .centerInside()
                    .error(R.drawable.broken_image_black_18dp)
                    .into(cardIssuerViewHolder.imageViewLogo);

        if (!TextUtils.isEmpty(name))
            cardIssuerViewHolder.textViewName.setText(cardIssuer.getName());
    }

    @Override
    public int getItemCount() {
        return cardIssuers != null ? cardIssuers.size() : 0;
    }
}
